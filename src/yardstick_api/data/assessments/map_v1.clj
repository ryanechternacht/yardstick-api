(ns yardstick-api.data.assessments.map-v1
  (:require [honeysql.helpers :refer [select from merge-join merge-where order-by]]
            [yardstick-api.middlewares.config :refer [last-year]]
            [yardstick-api.db :as db]))

;; TODO pull this from db
(def reference-lookup [222 220 222 224 222 224])

(defn- render-map-results [rows assessment-id]
  (let [most-recent (last rows)]
    {:id assessment-id
     :name (:assessment_name most-recent)
     :shortName (:assessment_short_name most-recent)
     :scale (:scale most-recent)
     :subject {:name (:subject most-recent)}
     :type (:type most-recent)
     :latestTerm {:fullName (str (:period_name most-recent) " " (:year_short_name most-recent))
                  :gradeLevelAverage 280 ;; TODO norm data
                  :domains (->> [(:goal1name most-recent) (:goal1ritscore most-recent)
                                 (:goal2name most-recent) (:goal2ritscore most-recent)
                                 (:goal3name most-recent) (:goal3ritscore most-recent)
                                 (:goal4name most-recent) (:goal4ritscore most-recent)
                                 (:goal5name most-recent) (:goal5ritscore most-recent)
                                 (:goal6name most-recent) (:goal6ritscore most-recent)
                                 (:goal7name most-recent) (:goal7ritscore most-recent)
                                 (:goal8name most-recent) (:goal8ritscore most-recent)]
                                (partition 2)
                                (filter second)
                                (map (fn [[name score]]
                                       {:name name :score score})))}
     :achievement 72 ;; TODO what field is this? or how do we calculate this?
     :growth (:testpercentile most-recent)
     :recentResults (map-indexed (fn [i r]
                                   (let [score (:testritscore r)
                                         reference (nth reference-lookup i)]
                                     {:year (:year_id r)
                                      :yearShortName (:year_short_name r)
                                      :grade (:grade r)
                                      :term (:period_name r)
                                      :score score
                                      :norm reference
                                      :growthGoal 6 ;; TODO
                                      :metGoal (>= score reference)
                                      :percentile 76 ;; TODO
                                      :growthPercentile (:testpercentile r)
                                      :proficiencyLevels (->> [(:projectedproficiencystudy1 r) (:projectedproficiencylevel1 r)
                                                               (:projectedproficiencystudy2 r) (:projectedproficiencylevel2 r)
                                                               (:projectedproficiencystudy3 r) (:projectedproficiencylevel3 r)
                                                               (:projectedproficiencystudy4 r) (:projectedproficiencylevel4 r)
                                                               (:projectedproficiencystudy5 r) (:projectedproficiencylevel5 r)
                                                               (:projectedproficiencystudy6 r) (:projectedproficiencylevel6 r)
                                                               (:projectedproficiencystudy7 r) (:projectedproficiencylevel7 r)
                                                               (:projectedproficiencystudy8 r) (:projectedproficiencylevel8 r)
                                                               (:projectedproficiencystudy9 r) (:projectedproficiencylevel9 r)
                                                               (:projectedproficiencystudy10 r) (:projectedproficiencylevel10 r)]
                                                              (partition 2)
                                                              (filter second)
                                                    ;; TODO Need a more generalized name
                                                              (map (fn [[study level]]
                                                                     {:study study :level level})))
                                      :testDuration (:testdurationminutes r)}))
                                 rows)}))

;; TODO honeysql preserve case of keywords
(defn get-map-results [db assessment-id student-id]
  (-> (select [:assessment.name :assessment_name] :assessment.subject
              :assessment.type [:assessment.short_name :assessment_short_name] :assessment.scale
              [:academic_year.short_name :year_short_name] [:assessment_period.name :period_name]
              [:assessment_period.id :assessment_period_id] [:academic_year.id :year_id]
              :assessment_map_v1.Goal1Name :assessment_map_v1.Goal1RitScore
              :assessment_map_v1.Goal2Name :assessment_map_v1.Goal2RitScore
              :assessment_map_v1.Goal3Name :assessment_map_v1.Goal3RitScore
              :assessment_map_v1.Goal4Name :assessment_map_v1.Goal4RitScore
              :assessment_map_v1.Goal5Name :assessment_map_v1.Goal5RitScore
              :assessment_map_v1.Goal6Name :assessment_map_v1.Goal6RitScore
              :assessment_map_v1.Goal7Name :assessment_map_v1.Goal7RitScore
              :assessment_map_v1.Goal8Name :assessment_map_v1.Goal8RitScore
              :assessment_map_v1.ProjectedProficiencyStudy1 :assessment_map_v1.ProjectedProficiencyLevel1
              :assessment_map_v1.ProjectedProficiencyStudy2 :assessment_map_v1.ProjectedProficiencyLevel2
              :assessment_map_v1.ProjectedProficiencyStudy3 :assessment_map_v1.ProjectedProficiencyLevel3
              :assessment_map_v1.ProjectedProficiencyStudy4 :assessment_map_v1.ProjectedProficiencyLevel4
              :assessment_map_v1.ProjectedProficiencyStudy5 :assessment_map_v1.ProjectedProficiencyLevel5
              :assessment_map_v1.ProjectedProficiencyStudy6 :assessment_map_v1.ProjectedProficiencyLevel6
              :assessment_map_v1.ProjectedProficiencyStudy7 :assessment_map_v1.ProjectedProficiencyLevel7
              :assessment_map_v1.ProjectedProficiencyStudy8 :assessment_map_v1.ProjectedProficiencyLevel8
              :assessment_map_v1.ProjectedProficiencyStudy9 :assessment_map_v1.ProjectedProficiencyLevel9
              :assessment_map_v1.ProjectedProficiencyStudy10 :assessment_map_v1.ProjectedProficiencyLevel10
              :assessment_map_v1.TestPercentile :assessment_map_v1.TestRITScore
              :assessment_map_v1.TestDurationMinutes
              [:grade.ordinal :grade])
      (from :student_assessment)
      (merge-join :school_assessment_instance [:=
                                               :student_assessment.school_assessment_instance_id
                                               :school_assessment_instance.id])
      (merge-join :assessment_period [:= :school_assessment_instance.assessment_period_id
                                      :assessment_period.id])
      (merge-join :assessment [:= :assessment_period.assessment_id :assessment.id])
      (merge-join :academic_year [:= :school_assessment_instance.academic_year_id :academic_year.id])
      (merge-join :assessment_map_v1 [:= :student_assessment.id
                                      :assessment_map_v1.student_assessment_id])
      (merge-join :grade [:= :student_assessment.grade_id :grade.id])
      (merge-where [:and
                    [:= :student_assessment.student_id student-id]
                    [:>= :school_assessment_instance.academic_year_id last-year]
                    [:= :assessment.id assessment-id]])
      (order-by :school_assessment_instance.academic_year_id :assessment_period.ordering)
      (db/->execute db)
      (render-map-results assessment-id)))
