(ns yardstick-api.data.jobs.star
  (:require [honeysql.helpers
             :refer [insert-into merge-where select from values columns]]
            [honeysql-postgres.helpers
             :refer [upsert on-conflict-constraint do-update-set returning]]
            [yardstick-api.db :as db]
            [yardstick-api.utils :refer [parse-int parse-double]]))

(def ^:private partition-size 100)

(defn- format-row
  [instance-id
   [studentid studentid2 currentgrade assessment_subject student_first_name student_last_name
    teacher_last_name assessment_date scaled_score test_duration literacy_classification irl
    lower_zpd upper_zpd percentile_rank screening_category state_benchmark current_sgp]]
  ;; returns
  [instance-id studentid studentid2 currentgrade assessment_subject student_first_name student_last_name
   teacher_last_name assessment_date scaled_score (parse-int test_duration)
   literacy_classification irl (parse-double lower_zpd) (parse-double upper_zpd)
   (parse-int percentile_rank) screening_category state_benchmark (parse-int current_sgp)])

;; reformat to a CTE with the raw data in values, then join it to student get the correct student ids
;; OR change tables to not enforce student id syncing at this time (and look it up later?)
(defn- upsert-chunk [db instance-id chunk]
  (-> (insert-into :assessment_star_v1)
      (columns :school_assessment_instance_id :studentid :studentid2 :currentgrade :assessment_subject
               :student_first_name :student_last_name :teacher_last_name :assessment_date
               :scaled_score :test_duration :literacy_classification :irl :lower_zpd :upper_zpd
               :percentile_rank :screening_category :state_benchmark :current_sgp)
      (values (map #(format-row instance-id %) chunk))
      (upsert
       (-> (on-conflict-constraint :assessment_star_v1_school_assessment_student_unique)
           (do-update-set :currentgrade :assessment_subject :student_first_name :student_last_name
                          :teacher_last_name :assessment_date :scaled_score :test_duration
                          :literacy_classification :irl :lower_zpd :upper_zpd :percentile_rank
                          :screening_category :state_benchmark :current_sgp)))
      (db/->execute db)))

;; studentid               | text    |           |          |
;; studentid2              | text    |           |          |
;; currentgrade            | text    |           |          |
;; assessment_subject      | text    |           |          |
;; student_first_name      | text    |           |          |
;; student_last_name       | text    |           |          |
;; teacher_last_name       | text    |           |          |
;; assessment_date         | text    |           |          |
;; scaled_score            | text    |           |          |
;; test_duration           | integer |           |          |
;; literacy_classification | text    |           |          |
;; irl                     | text    |           |          |
;; lower_zpd               | numeric |           |          |
;; upper_zpd               | numeric |           |          |
;; percentile_rank         | integer |           |          |
;; screening_category      | text    |           |          |
;; state_benchmark         | text    |           |          |
;; current_sgp             | integer |           |          |

;; STAR Math
(defn upload-star [db instance-id csv]
  (let [csv-no-header (drop 1 csv)]
    (doseq [chunk (partition partition-size partition-size nil csv-no-header)]
      (upsert-chunk db instance-id chunk))
    "i'm a return value"))
