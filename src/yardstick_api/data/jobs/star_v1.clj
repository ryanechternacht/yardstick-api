(ns yardstick-api.data.jobs.star-v1
  (:require [honey.sql.helpers :as h]
            [java-time :as jt]
            [yardstick-api.db :as db]
            [yardstick-api.utils :as util]))

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
   teacher_last_name (parse-date assessment_date) (util/parse-int scaled_score) (util/parse-int test_duration)
   literacy_classification irl (util/parse-double lower_zpd) (util/parse-double upper_zpd)
   (util/parse-int percentile_rank) screening_category state_benchmark (util/parse-int current_sgp)])

(defn- upsert-chunk [db instance-id chunk]
  (-> (h/insert-into :assessment_star_v1)
      (h/columns :school_assessment_instance_id :studentid :studentid2 :currentgrade :assessment_subject
                 :student_first_name :student_last_name :teacher_last_name :assessment_date
                 :scaled_score :test_duration :literacy_classification :irl :lower_zpd :upper_zpd
                 :percentile_rank :screening_category :state_benchmark :current_sgp)
      (h/values (map #(format-row instance-id %) chunk))
      h/on-conflict
      (h/on-constraint :assessment_star_v1_school_assessment_student_unique)
      (h/do-update-set :currentgrade :assessment_subject :student_first_name :student_last_name
                       :teacher_last_name :assessment_date :scaled_score :test_duration
                       :literacy_classification :irl :lower_zpd :upper_zpd :percentile_rank
                       :screening_category :state_benchmark :current_sgp)
                          ;; TODO add updated_at here

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
  (-> (h/insert-into :student_assessment
                     [:school_assessment_instance_id :student_id :grade_id
                      :local_student_id :state_student_id :assessment_table
                      :assessment_table_id :date_taken :attempts]
                     (-> (h/select :assessment_star_v1.school_assessment_instance_id
                                   :student.id :student.grade_id :student.student_id
                                   :student.student_state_id "assessment_star_v1"
                                   :assessment_star_v1.id :most_recent.assessment_date
                                   :most_recent.attempts)
                         (h/from [(-> (h/select :school_assessment_instance_id :studentID
                                                :studentID2 [:%max.assessment_date :assessment_date]
                                                [:%count.* :attempts])
                                      (h/from :assessment_star_v1)
                                      (h/where [:= :school_assessment_instance_id instance-id])
                                      (h/group-by :school_assessment_instance_id :studentID :studentID2)) :most_recent])
                         (h/join :assessment_star_v1
                                 [:and
                                  [:=
                                   :most_recent.school_assessment_instance_id
                                   :assessment_star_v1.school_assessment_instance_id]
                                  [:=
                                   :most_recent.studentID
                                   :assessment_star_v1.studentID]
                                  [:=
                                   :most_recent.studentID2
                                   :assessment_star_v1.studentID2]
                                  [:=
                                   :most_recent.assessment_date
                                   :assessment_star_v1.assessment_date]])
                         (h/left-join :student_assessment
                                      [:and
                                       [:= :student_assessment.assessment_table "assessment_star_v1"]
                                       [:= :assessment_star_v1.id :student_assessment.assessment_table_id]])
                         (h/join :school_assessment_instance
                                 [:=
                                  :assessment_star_v1.school_assessment_instance_id
                                  :school_assessment_instance.id])
                         (h/join :student
                                 [:and
                                  [:= :school_assessment_instance.school_id :student.school_id]
                                  [:or
                                   [:= :assessment_star_v1.studentID :student.student_id]
                                   [:= :assessment_star_v1.studentID :student.student_state_id]
                                   [:= :assessment_star_v1.studentID2 :student.student_id]
                                   [:= :assessment_star_v1.studentID2 :student.student_state_id]]])
                         (h/where [:is :student_assessment.id nil])))
      h/on-conflict
      (h/on-constraint :student_assessment_unique_student_instance)
      (h/do-update-set {:attempts :excluded.attempts
                        :date_taken :excluded.date_taken
                        :assessment_table_id :excluded.assessment_table_id
                        :yardstick_performance_rating nil
                        :updated_at [:now]})
      (db/->execute db)))

(defn- get-records-without-yprs
  [db instance-id]
  (-> (h/select :id :student_id)
      (h/from :student_assessment)
      (h/where [:and
                [:= :yardstick_performance_rating nil]
                [:= :school_assessment_instance_id instance-id]])
      (db/->execute db)))

(defn- calculate-ypr
  "Calculates the YPR for the STAR assessment for a specific student"
  [db instance-id student-id]
  100)

(defn- update-record-with-ypr
  ""
  [db record-id ypr]
  (-> (h/update :student_assessment)
      (h/set {:yardstick_performance_rating ypr})
      (h/where [:= :id record-id])
      (db/->execute db)))

(defn- calculate-new-yprs
  "Calculates the Yardstick Performance Ratings for STAR `student_assessment`
  records without them. This happens for any new data that gets upserted"
  [db instance-id]
  (let [records (get-records-without-yprs db instance-id)]
    (doseq [{:keys [student-id id]} records]
      (let [ypr (calculate-ypr db instance-id student-id)]
        (update-record-with-ypr db id ypr)))))

(comment
  (calculate-new-yprs {:dbtype "postgresql"
                       :dbname "yardstick"
                       :host "127.0.0.1"
                       :user "ryan"
                       :password nil
                       :ssl false}
                      37))

;; STAR Math
(defn upload-star
  "Uploads star data in chunks, then links this data to students"
  [db instance-id csv]
  (let [csv-no-header (drop 1 csv)]
    (doseq [chunk (partition partition-size partition-size nil csv-no-header)]
      (upsert-chunk db instance-id chunk))
    (link-rows-and-students db instance-id)
    (calculate-new-yprs db instance-id)
    ;; TODO what to return?
    "i'm a return value"))
