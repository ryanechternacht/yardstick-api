(ns yardstick-api.data.assessments
  (:require [honeysql.helpers :refer [select from merge-join merge-where group order-by]]
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

;; TODO pull this from db
(def ^:private reference-lookup
  {226 222
   228 220
   229 222
   230 224
   227 222
   232 224})

(defn- render-map-results [assessment-id rows]
  (let [most-recent (last rows)]
    {:id assessment-id
     :name (:assessment_name most-recent)
     :shortName (:short_name most-recent)
     :scale (:scale most-recent)
     :subject {:name (:type most-recent)}
     :latestTerm {:fullName (str (:assessment_term most-recent) (:short_name most-recent))
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
     :achievement 72 ;; TODO norm data
     :growth (:testpercentile most-recent)
     :recentResults (map (fn [r]
                           (let [score (:testritscore r)
                                 reference (reference-lookup score)]
                             {:label (str (:assessment_term r) (:short_name r))
                              :student score
                              :reference reference
                              :hitGoal (>= score reference)}))
                         rows)
     :recentTerms (map (fn [r]
                         (let [score (:testritscore r)
                               reference (reference-lookup score)]
                           {:id (:assessment_term_id r)
                            :year (:year_id r)
                            :grade 8 ;; TODO do we need this?
                            :term (:term_name r)
                            :ritScore score
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
;; TODO where are we deciding these are map results
(defn get-results-by-assessment [db assessment-id student-id]
  (->> (-> (select [:assessment.name :assessment_name] :assessment.type
                   :assessment.short_name :assessment.scale
                   :academic_year.short_name [:assessment_term.name :term_name]
                   [:assessment_term.id :assessment_term_id] [:academic_year.id :year_id]
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
                   :assessment_map_v1.TestDurationMinutes)
           (from :student_assessment)
           (merge-join :assessment_instance [:= :student_assessment.assessment_instance_id
                                             :assessment_instance.id])
           (merge-join :assessment_term [:= :assessment_instance.assessment_term_id
                                         :assessment_term.id])
           (merge-join :assessment [:= :assessment_term.assessment_id :assessment.id])
           (merge-join :academic_year [:= :assessment_instance.academic_year_id :academic_year.id])
           (merge-join :assessment_map_v1 [:= :student_assessment.id
                                           :assessment_map_v1.student_assessment_id])
           (merge-where [:and
                         [:= :student_assessment.student_id student-id]
                         [:>= :assessment_instance.academic_year_id last-year]
                         [:= :assessment.id assessment-id]])
           (order-by :assessment_instance.academic_year_id :assessment_term.ordering))
       (db/execute db)
       (render-map-results assessment-id)))
