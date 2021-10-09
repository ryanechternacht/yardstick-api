(ns yardstick-api.data.assessments.star-v1
  (:require [honeysql.helpers :refer [select from merge-join merge-where order-by]]
            [yardstick-api.middlewares.config :refer [last-year]]
            [yardstick-api.db :as db]))

;; TODO pull this from db
(def reference-lookup [626 627 628 630 631 632 633 634
                       630 632 634 636 635 632 634 635])

(defn- render-star-results [rows assessment-id]
  (let [most-recent (last rows)]
    {:id assessment-id
     :name (:assessment_name most-recent)
     :shortName (:assessment_short_name most-recent)
     :scale (:scale most-recent)
     :subject {:name (:subject most-recent)}
     :type (:type most-recent)
     :latestTerm {:fullName (str (:period_name most-recent) " " (:year_short_name most-recent))
                  ;; TODO star doesn't have this
                  :gradeLevelAverage 0
                  :domains []}
     :achievement (:percentile_rank most-recent)
     :growth (:current_sgp most-recent)
     :recentResults (map-indexed (fn [i r]
                                   (let [score (:current_sgp r)
                                         reference (nth reference-lookup i)]
                                     {:year (:year_id r)
                                      :yearShortName (:year_short_name r)
                                      :grade (:grade r)
                                      :term (:period_name r)
                                      :score score
                                      :norm reference
                                      :growthGoal 6 ;; TODO
                                      :metGoal (>= score reference)
                                      :percentile (:percentile_rank r)
                                      :growthPercentile (:current_sgp r)
                                      :proficiencyLevels [{:study "Screening Category"
                                                           :level (:screening_category r)}
                                                          {:study "State Benchmark"
                                                           :level   (:state_benchmark r)}]
                                      :testDuration (:test_duration r)}))
                                 rows)}))

;; TODO honeysql preserve case of keywords
;; TODO there is a common bit here that can be pulled out
(defn get-star-results [db assessment-id student-id]
  (-> (select [:assessment.name :assessment_name] :assessment.subject
              :assessment.type [:assessment.short_name :assessment_short_name] :assessment.scale
              [:academic_year.short_name :year_short_name] [:assessment_period.name :period_name]
              [:assessment_period.id :assessment_period_id] [:academic_year.id :year_id]
              [:grade.ordinal :grade]
              :assessment_star_v1.scaled_score :assessment_star_v1.test_duration
              :assessment_star_v1.percentile_rank :assessment_star_v1.screening_category
              :assessment_star_v1.state_benchmark :assessment_star_v1.current_sgp
              :assessment_star_v1.literacy_classification :assessment_star_v1.irl
              :assessment_star_v1.lower_zpd :assessment_star_v1.upper_zpd)
      (from :student_assessment)
      (merge-join :school_assessment_instance [:=
                                               :student_assessment.school_assessment_instance_id
                                               :school_assessment_instance.id])
      (merge-join :assessment_period [:= :school_assessment_instance.assessment_period_id
                                      :assessment_period.id])
      (merge-join :assessment [:= :assessment_period.assessment_id :assessment.id])
      (merge-join :academic_year [:= :school_assessment_instance.academic_year_id :academic_year.id])
      (merge-join :assessment_star_v1 [:= :student_assessment.assessment_table_id
                                       :assessment_star_v1.id])
      (merge-join :grade [:= :student_assessment.grade_id :grade.id])
      (merge-where [:and
                    [:= :student_assessment.student_id student-id]
                    [:>= :school_assessment_instance.academic_year_id last-year]
                    [:= :assessment.id assessment-id]])
      (order-by :school_assessment_instance.academic_year_id :assessment_period.ordering)
      (db/->execute db)
      (render-star-results assessment-id)))
