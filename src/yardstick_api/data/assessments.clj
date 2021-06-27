(ns yardstick-api.data.assessments
  (:require [honeysql.helpers :refer [select from merge-join merge-where group]]
            [yardstick-api.db :as db]
            [honeysql.core :as sql]))

(def ^:private this-year 2021)
(def ^:private last-year 2020)

(defn get-all-by-student-id [db student-id]
  (->> (-> (select :assessment.id
                   :assessment.name :assessment.name
                   :assessment.type
                   :student_assessment.yardstick_performance_rating)
           (from [(-> (select :assessment_term.assessment_id
                              [:%max.assessment_term.ordering :ordering])
                      (from :student_assessment)
                      (merge-join :assessment_instance [:=
                                                        :student_assessment.assessment_instance_id
                                                        :assessment_instance.id])
                      (merge-join :assessment_term [:=
                                                    :assessment_instance.assessment_term_id
                                                    :assessment_term.id])
                      (merge-where [:and
                                    [:= :student_assessment.student_id student-id]
                                    [:>= :assessment_instance.academic_year_id last-year]])
                      (group :assessment_term.assessment_id))
                  :recent_term])
           (merge-join :assessment_term [:and
                                         [:= :recent_term.assessment_id :assessment_term.assessment_id]
                                         [:= :recent_term.ordering :assessment_term.ordering]])
           (merge-join :assessment [:= :assessment_term.assessment_id :assessment.id])
           (merge-join :assessment_instance [:and
                                             [:= :assessment_term.id :assessment_instance.assessment_term_id]
                                             [:= :assessment_instance.academic_year_id this-year]])
           (merge-join :student_assessment [:and
                                            [:= :assessment_instance.id :student_assessment.assessment_instance_id]
                                            [:= :student_assessment.student_id student-id]]))
       (db/execute db)))

; [
;     {
;         "id": 1,
;         "name": "PreACT 8/9",
;         "rating": 3,
;         "type": "general"
;     },
;     {
;         "id": 2,
;         "name": "Forward - Mathematics",
;         "rating": 4,
;         "type": "math"
;     },
;     {
;         "id": 3,
;         "name": "NWEA MAP - Mathematics",
;         "rating": 5,
;         "type": "math"
;     },
;     {
;         "id": 4,
;         "name": "Forward - ELA",
;         "rating": 2,
;         "type": "ela"
;     },
;     {
;         "id": 5,
;         "name": "NWEA MAP - Reading",
;         "rating": 1,
;         "type": "ela"
;     }
; ]