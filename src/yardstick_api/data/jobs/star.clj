(ns yardstick-api.data.jobs.star
  (:require [honey.sql.helpers
             :refer [insert-into select from values columns
                     on-conflict on-constraint do-update-set returning]]
            [java-time :as jt]
            [yardstick-api.db :as db]
            [yardstick-api.utils :refer [parse-int parse-double]]))

(def ^:private partition-size 100)

(defn- parse-date [dt]
  (jt/local-date-time "yyyy-MM-dd HH:mm:ss" dt))

(defn- format-row
  [instance-id
   [studentid studentid2 currentgrade assessment_subject student_first_name student_last_name
    teacher_last_name assessment_date scaled_score test_duration literacy_classification irl
    lower_zpd upper_zpd percentile_rank screening_category state_benchmark current_sgp]]
  ;; returns
  [instance-id studentid studentid2 currentgrade assessment_subject student_first_name student_last_name
   teacher_last_name (parse-date assessment_date) scaled_score (parse-int test_duration)
   literacy_classification irl (parse-double lower_zpd) (parse-double upper_zpd)
   (parse-int percentile_rank) screening_category state_benchmark (parse-int current_sgp)])

(defn- upsert-chunk [db instance-id chunk]
  (-> (insert-into :assessment_star_v1)
      (columns :school_assessment_instance_id :studentid :studentid2 :currentgrade :assessment_subject
               :student_first_name :student_last_name :teacher_last_name :assessment_date
               :scaled_score :test_duration :literacy_classification :irl :lower_zpd :upper_zpd
               :percentile_rank :screening_category :state_benchmark :current_sgp)
      (values (map #(format-row instance-id %) chunk))
      on-conflict
      (on-constraint :assessment_star_v1_school_assessment_student_unique)
      (do-update-set :currentgrade :assessment_subject :student_first_name :student_last_name
                     :teacher_last_name :assessment_date :scaled_score :test_duration
                     :literacy_classification :irl :lower_zpd :upper_zpd :percentile_rank
                     :screening_category :state_benchmark :current_sgp
                          ;; TODO add updated_at here
                     )
      (db/->execute db)))

;; This might be generalizable?
(defn- link-rows-and-students
  "Through an SQL query, finds the most recent attempt for each student
   (from the uploaded data) and compares these to the existing `student_assessment`
   records we have. Upserts any new (or newer) attempts to `student_assessment`.
   This could be because: 
     a) This is the first time we're seeing assessment rows for a student 
        in this assessment instance
     b) This is a newer attempt of this assessment
     c) We had seen this data before, but we have had new students uploaded,
        and we can now link the data.
     d) Both a and c (the student records and assessment data are new)"
  [db instance-id]
  (-> (insert-into :student_assessment
                   [:school_assessment_instance_id :student_id :grade_id
                    :local_student_id :state_student_id :assessment_table
                    :assessment_table_id :date_taken :attempts])
      on-conflict
      (on-constraint :student_assessment_unique_student_instance)
      (do-update-set {:attempts "EXCLUDED.attempts"
                      :date_taken "EXCLUDED.date_taken"
                      :assessment_table_id "EXCLUDED.assessment_table_id"
                      :yardstick_performance_rating 0
                      :updated_at [:now]})
      (db/->format db)))

(comment
  (link-rows-and-students nil 37)
  ;;
  )

;; STAR Math
(defn upload-star [db instance-id csv]
  (let [csv-no-header (drop 1 csv)]
    (doseq [chunk (partition partition-size partition-size nil csv-no-header)]
      (upsert-chunk db instance-id chunk))
    "i'm a return value"))
