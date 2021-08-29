(ns yardstick-api.data.assessments
  (:require [honeysql.helpers :refer [select from merge-join merge-where group order-by]]
            [yardstick-api.middlewares.config :refer [last-year this-year]]
            [yardstick-api.data.assessments.map-v1 :as map-v1]
            [yardstick-api.data.assessments.star-v1 :as star-v1]
            [yardstick-api.db :as db]))

(defn get-all-by-student-id [db student-id]
  (-> (select :assessment.id
              :assessment.name
              [:assessment.short_name :shortName]
              :assessment.type
              :assessment.subject
              :student_assessment.yardstick_performance_rating)
      (from [(-> (select :assessment_period.assessment_id
                         [:%max.assessment_period.ordering :ordering])
                 (from :student_assessment)
                 (merge-join :school_assessment_instance [:=
                                                          :student_assessment.school_assessment_instance_id
                                                          :school_assessment_instance.id])
                 (merge-join :assessment_period [:=
                                                 :school_assessment_instance.assessment_period_id
                                                 :assessment_period.id])
                 (merge-where [:and
                               [:= :student_assessment.student_id student-id]
                               [:>= :school_assessment_instance.academic_year_id last-year]])
                 (group :assessment_period.assessment_id))
             :recent_period])
      (merge-join :assessment_period [:and
                                      [:= :recent_period.assessment_id :assessment_period.assessment_id]
                                      [:= :recent_period.ordering :assessment_period.ordering]])
      (merge-join :assessment [:= :assessment_period.assessment_id :assessment.id])
      (merge-join :school_assessment_instance [:and
                                               [:= :assessment_period.id :school_assessment_instance.assessment_period_id]
                                               [:= :school_assessment_instance.academic_year_id this-year]])
      (merge-join :student_assessment [:and
                                       [:= :school_assessment_instance.id :student_assessment.school_assessment_instance_id]
                                       [:= :student_assessment.student_id student-id]])
      (db/->execute db)))

(defn get-results-by-assessment [db assessment-id student-id]
  ;; TODO read out subject and pass it down
  (let [table (-> (select :assessment_table)
                  (from :assessment)
                  (merge-where [:= :assessment.id assessment-id])
                  (db/->execute db)
                  first
                  :assessment_table)]
    (case table
      "assessment_map_v1" (map-v1/get-map-results db assessment-id student-id)
      "assessment_star_v1" (star-v1/get-star-results db assessment-id student-id))))

(defn get-explanations-by-assessment [db assessment-id]
  (let [assessment-type (-> (select :type)
                            (from :assessment)
                            (merge-where [:= :assessment.id assessment-id])
                            (db/->execute db)
                            first
                            :type)
        traits (-> (select :assessment_trait.title :assessment_trait.description
                           :assessment_trait.icon)
                   (from :assessment_assessment_trait)
                   (merge-join :assessment_trait [:=
                                                  :assessment_assessment_trait.assessment_trait_id
                                                  :assessment_trait.id])
                   (merge-where [:= :assessment_assessment_trait.assessment_id assessment-id])
                   (db/->execute db))]
    {:assessmentType assessment-type
     :traits traits}))
