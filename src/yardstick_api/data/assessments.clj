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
                         rows)}))

;; TODO honeysql preserve case of keywords
;; TODO where are we deciding these are map results
(defn get-results-by-assessment [db assessment-id student-id]
  (->> (-> (select [:assessment.name :assessment_name] :assessment.type
                   [:assessment_term.name :term_name] :assessment.short_name
                   :assessment.scale :academic_year.short_name
                   :assessment_map_v1.Goal1Name :assessment_map_v1.Goal1RitScore
                   :assessment_map_v1.Goal2Name :assessment_map_v1.Goal2RitScore
                   :assessment_map_v1.Goal3Name :assessment_map_v1.Goal3RitScore
                   :assessment_map_v1.Goal4Name :assessment_map_v1.Goal4RitScore
                   :assessment_map_v1.Goal5Name :assessment_map_v1.Goal5RitScore
                   :assessment_map_v1.Goal6Name :assessment_map_v1.Goal6RitScore
                   :assessment_map_v1.Goal7Name :assessment_map_v1.Goal7RitScore
                   :assessment_map_v1.Goal8Name :assessment_map_v1.Goal8RitScore
                   :assessment_map_v1.TestPercentile :assessment_map_v1.TestRITScore)
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

  ; (case assessment-id
  ;   1 {:id 1
  ;      :name "PreACT 8/9"
  ;      :shortName "PreACT"
  ;      :scale "RIT Score"
  ;      :subject {:name ""}
  ;      :latestTerm {:fullName "Winter 2021"
  ;                   :gradeLevelAverage 280
  ;                   :domains [{:name "ELA"
  ;                              :score 278}
  ;                             {:name "Science"
  ;                              :score 282}
  ;                             {:name "Social Studies"
  ;                              :score 283}
  ;                             {:name "Mathematics"
  ;                              :score 284}]}
  ;      :achievement 72
  ;      :growth 98
  ;      :recentResults [{:label "Spring '18"
  ;                       :student 226
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Fall '19"
  ;                       :student 228
  ;                       :reference 220
  ;                       :hitGoal false}
  ;                      {:label "Winter '19"
  ;                       :student 229
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Spring '20"
  ;                       :student 230
  ;                       :reference 224
  ;                       :hitGoal true}
  ;                      {:label "Fall '20"
  ;                       :student 227
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Winter '20"
  ;                       :student 232
  ;                       :reference 224
  ;                       :hitGoal true}]
  ;      :recentTerms [{:id 4
  ;                     :year 2021
  ;                     :grade 8
  ;                     :term "Fall"
  ;                     :ritScore 232
  ;                     :norm 224.9
  ;                     :growthGoal nil
  ;                     :metGoal nil
  ;                     :percentile 76
  ;                     :growthPercentile 52
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 37}
  ;                    {:id 5
  ;                     :year 2021
  ;                     :grade 8
  ;                     :term "Winter"
  ;                     :ritScore 239
  ;                     :norm 228.1
  ;                     :growthGoal 6
  ;                     :metGoal true
  ;                     :percentile 70
  ;                     :growthPercentile 98
  ;                     :actCollegeReadiness "On Track for a 24"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 84}
  ;                    {:id 6
  ;                     :year "-"
  ;                     :grade "-"
  ;                     :term "Spring"
  ;                     :ritScore "-"
  ;                     :norm "-"
  ;                     :growthGoal "-"
  ;                     :metGoal "-"
  ;                     :percentile "-"
  ;                     :growthPercentile "-"
  ;                     :actCollegeReadiness "-"
  ;                     :forwardProjection "-"
  ;                     :testDuration "-"}
  ;                    {:id 1
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Fall"
  ;                     :ritScore 233
  ;                     :norm 220.2
  ;                     :growthGoal nil
  ;                     :metGoal nil
  ;                     :percentile 75
  ;                     :growthPercentile 65
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 68}
  ;                    {:id 2
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Winter"
  ;                     :ritScore 235
  ;                     :norm 224.1
  ;                     :growthGoal 5
  ;                     :metGoal nil
  ;                     :percentile 74
  ;                     :growthPercentile 82
  ;                     :actCollegeReadiness "On Track for a 22"
  ;                     :forwardProjection "Advanced"
  ;                     :testDuration 71}
  ;                    {:id 3
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Spring"
  ;                     :ritScore 236
  ;                     :norm 226.7
  ;                     :growthGoal 4
  ;                     :metGoal true
  ;                     :percentile 66
  ;                     :growthPercentile 51
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 62}]}
  ;   2 {:id 2
  ;      :name "Forward"
  ;      :shortName "Forward"
  ;      :scale "RIT Score"
  ;      :subject {:name "Mathematics"}
  ;      :latestTerm {:fullName "Winter 2021"
  ;                   :gradeLevelAverage 280
  ;                   :domains [{:name "Geometry"
  ;                              :score 278}
  ;                             {:name "Statistics and Probability"
  ;                              :score 282}
  ;                             {:name "Operations and Algebraic Thinking"
  ;                              :score 283}
  ;                             {:name "The Real and Complex Number System"
  ;                              :score 284}]}
  ;      :achievement 72
  ;      :growth 98
  ;      :recentResults [{:label "Spring '18"
  ;                       :student 226
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Fall '19"
  ;                       :student 228
  ;                       :reference 220
  ;                       :hitGoal false}
  ;                      {:label "Winter '19"
  ;                       :student 229
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Spring '20"
  ;                       :student 230
  ;                       :reference 224
  ;                       :hitGoal true}
  ;                      {:label "Fall '20"
  ;                       :student 227
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Winter '20"
  ;                       :student 232
  ;                       :reference 224
  ;                       :hitGoal true}]
  ;      :recentTerms [{:id 4
  ;                     :year 2021
  ;                     :grade 8
  ;                     :term "Fall"
  ;                     :ritScore 232
  ;                     :norm 224.9
  ;                     :growthGoal nil
  ;                     :metGoal nil
  ;                     :percentile 76
  ;                     :growthPercentile 52
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 37}
  ;                    {:id 5
  ;                     :year 2021
  ;                     :grade 8
  ;                     :term "Winter"
  ;                     :ritScore 239
  ;                     :norm 228.1
  ;                     :growthGoal 6
  ;                     :metGoal true
  ;                     :percentile 70
  ;                     :growthPercentile 98
  ;                     :actCollegeReadiness "On Track for a 24"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 84}
  ;                    {:id 6
  ;                     :year "-"
  ;                     :grade "-"
  ;                     :term "Spring"
  ;                     :ritScore "-"
  ;                     :norm "-"
  ;                     :growthGoal "-"
  ;                     :metGoal "-"
  ;                     :percentile "-"
  ;                     :growthPercentile "-"
  ;                     :actCollegeReadiness "-"
  ;                     :forwardProjection "-"
  ;                     :testDuration "-"}
  ;                    {:id 1
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Fall"
  ;                     :ritScore 233
  ;                     :norm 220.2
  ;                     :growthGoal nil
  ;                     :metGoal nil
  ;                     :percentile 75
  ;                     :growthPercentile 65
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 68}
  ;                    {:id 2
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Winter"
  ;                     :ritScore 235
  ;                     :norm 224.1
  ;                     :growthGoal 5
  ;                     :metGoal nil
  ;                     :percentile 74
  ;                     :growthPercentile 82
  ;                     :actCollegeReadiness "On Track for a 22"
  ;                     :forwardProjection "Advanced"
  ;                     :testDuration 71}
  ;                    {:id 3
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Spring"
  ;                     :ritScore 236
  ;                     :norm 226.7
  ;                     :growthGoal 4
  ;                     :metGoal true
  ;                     :percentile 66
  ;                     :growthPercentile 51
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 62}]}
  ;   3 {:id 3
  ;      :name "NWEA MAP"
  ;      :shortName "MAP"
  ;      :scale "RIT Score"
  ;      :subject {:name "Mathematics"}
  ;      :latestTerm {:fullName "Winter 2021"
  ;                   :gradeLevelAverage 280
  ;                   :domains [{:name "Geometry"
  ;                              :score 278}
  ;                             {:name "Statistics and Probability"
  ;                              :score 282}
  ;                             {:name "Operations and Algebraic Thinking"
  ;                              :score 283}
  ;                             {:name "The Real and Complex Number System"
  ;                              :score 284}]}
  ;      :achievement 72
  ;      :growth 98
  ;      :recentResults [{:label "Spring '18"
  ;                       :student 226
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Fall '19"
  ;                       :student 228
  ;                       :reference 220
  ;                       :hitGoal false}
  ;                      {:label "Winter '19"
  ;                       :student 229
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Spring '20"
  ;                       :student 230
  ;                       :reference 224
  ;                       :hitGoal true}
  ;                      {:label "Fall '20"
  ;                       :student 227
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Winter '20"
  ;                       :student 232
  ;                       :reference 224
  ;                       :hitGoal true}]
  ;      :recentTerms [{:id 4
  ;                     :year 2021
  ;                     :grade 8
  ;                     :term "Fall"
  ;                     :ritScore 232
  ;                     :norm 224.9
  ;                     :growthGoal nil
  ;                     :metGoal nil
  ;                     :percentile 76
  ;                     :growthPercentile 52
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 37}
  ;                    {:id 5
  ;                     :year 2021
  ;                     :grade 8
  ;                     :term "Winter"
  ;                     :ritScore 239
  ;                     :norm 228.1
  ;                     :growthGoal 6
  ;                     :metGoal true
  ;                     :percentile 70
  ;                     :growthPercentile 98
  ;                     :actCollegeReadiness "On Track for a 24"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 84}
  ;                    {:id 6
  ;                     :year "-"
  ;                     :grade "-"
  ;                     :term "Spring"
  ;                     :ritScore "-"
  ;                     :norm "-"
  ;                     :growthGoal "-"
  ;                     :metGoal "-"
  ;                     :percentile "-"
  ;                     :growthPercentile "-"
  ;                     :actCollegeReadiness "-"
  ;                     :forwardProjection "-"
  ;                     :testDuration "-"}
  ;                    {:id 1
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Fall"
  ;                     :ritScore 233
  ;                     :norm 220.2
  ;                     :growthGoal nil
  ;                     :metGoal nil
  ;                     :percentile 75
  ;                     :growthPercentile 65
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 68}
  ;                    {:id 2
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Winter"
  ;                     :ritScore 235
  ;                     :norm 224.1
  ;                     :growthGoal 5
  ;                     :metGoal nil
  ;                     :percentile 74
  ;                     :growthPercentile 82
  ;                     :actCollegeReadiness "On Track for a 22"
  ;                     :forwardProjection "Advanced"
  ;                     :testDuration 71}
  ;                    {:id 3
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Spring"
  ;                     :ritScore 236
  ;                     :norm 226.7
  ;                     :growthGoal 4
  ;                     :metGoal true
  ;                     :percentile 66
  ;                     :growthPercentile 51
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 62}]}
  ;   4 {:id 4
  ;      :name "Forward"
  ;      :shortName "Forward"
  ;      :scale "RIT Score"
  ;      :subject {:name "ELA"}
  ;      :latestTerm {:fullName "Winter 2021"
  ;                   :gradeLevelAverage 280
  ;                   :domains [{:name "Reading Comprehension"
  ;                              :score 278}
  ;                             {:name "Essay Writing"
  ;                              :score 282}
  ;                             {:name "American Literature"
  ;                              :score 283}
  ;                             {:name "Creative Writing"
  ;                              :score 284}]}
  ;      :achievement 72
  ;      :growth 98
  ;      :recentResults [{:label "Spring '18"
  ;                       :student 226
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Fall '19"
  ;                       :student 228
  ;                       :reference 220
  ;                       :hitGoal false}
  ;                      {:label "Winter '19"
  ;                       :student 229
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Spring '20"
  ;                       :student 230
  ;                       :reference 224
  ;                       :hitGoal true}
  ;                      {:label "Fall '20"
  ;                       :student 227
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Winter '20"
  ;                       :student 232
  ;                       :reference 224
  ;                       :hitGoal true}]
  ;      :recentTerms [{:id 4
  ;                     :year 2021
  ;                     :grade 8
  ;                     :term "Fall"
  ;                     :ritScore 232
  ;                     :norm 224.9
  ;                     :growthGoal nil
  ;                     :metGoal nil
  ;                     :percentile 76
  ;                     :growthPercentile 52
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 37}
  ;                    {:id 5
  ;                     :year 2021
  ;                     :grade 8
  ;                     :term "Winter"
  ;                     :ritScore 239
  ;                     :norm 228.1
  ;                     :growthGoal 6
  ;                     :metGoal true
  ;                     :percentile 70
  ;                     :growthPercentile 98
  ;                     :actCollegeReadiness "On Track for a 24"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 84}
  ;                    {:id 6
  ;                     :year "-"
  ;                     :grade "-"
  ;                     :term "Spring"
  ;                     :ritScore "-"
  ;                     :norm "-"
  ;                     :growthGoal "-"
  ;                     :metGoal "-"
  ;                     :percentile "-"
  ;                     :growthPercentile "-"
  ;                     :actCollegeReadiness "-"
  ;                     :forwardProjection "-"
  ;                     :testDuration "-"}
  ;                    {:id 1
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Fall"
  ;                     :ritScore 233
  ;                     :norm 220.2
  ;                     :growthGoal nil
  ;                     :metGoal nil
  ;                     :percentile 75
  ;                     :growthPercentile 65
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 68}
  ;                    {:id 2
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Winter"
  ;                     :ritScore 235
  ;                     :norm 224.1
  ;                     :growthGoal 5
  ;                     :metGoal nil
  ;                     :percentile 74
  ;                     :growthPercentile 82
  ;                     :actCollegeReadiness "On Track for a 22"
  ;                     :forwardProjection "Advanced"
  ;                     :testDuration 71}
  ;                    {:id 3
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Spring"
  ;                     :ritScore 236
  ;                     :norm 226.7
  ;                     :growthGoal 4
  ;                     :metGoal true
  ;                     :percentile 66
  ;                     :growthPercentile 51
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 62}]}
  ;   5 {:id 5
  ;      :name "NWEA MAP"
  ;      :shortName "MAP"
  ;      :scale "RIT Score"
  ;      :subject {:name "ELA"}
  ;      :latestTerm {:fullName "Winter 2021"
  ;                   :gradeLevelAverage 280
  ;                   :domains [{:name "Reading Comprehension"
  ;                              :score 278}
  ;                             {:name "Essay Writing"
  ;                              :score 282}
  ;                             {:name "American Literature"
  ;                              :score 283}
  ;                             {:name "Creative Writing"
  ;                              :score 284}]}
  ;      :achievement 72
  ;      :growth 98
  ;      :recentResults [{:label "Spring '18"
  ;                       :student 226
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Fall '19"
  ;                       :student 228
  ;                       :reference 220
  ;                       :hitGoal false}
  ;                      {:label "Winter '19"
  ;                       :student 229
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Spring '20"
  ;                       :student 230
  ;                       :reference 224
  ;                       :hitGoal true}
  ;                      {:label "Fall '20"
  ;                       :student 227
  ;                       :reference 222
  ;                       :hitGoal true}
  ;                      {:label "Winter '20"
  ;                       :student 232
  ;                       :reference 224
  ;                       :hitGoal true}]
  ;      :recentTerms [{:id 4
  ;                     :year 2021
  ;                     :grade 8
  ;                     :term "Fall"
  ;                     :ritScore 232
  ;                     :norm 224.9
  ;                     :growthGoal nil
  ;                     :metGoal nil
  ;                     :percentile 76
  ;                     :growthPercentile 52
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 37}
  ;                    {:id 5
  ;                     :year 2021
  ;                     :grade 8
  ;                     :term "Winter"
  ;                     :ritScore 239
  ;                     :norm 228.1
  ;                     :growthGoal 6
  ;                     :metGoal true
  ;                     :percentile 70
  ;                     :growthPercentile 98
  ;                     :actCollegeReadiness "On Track for a 24"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 84}
  ;                    {:id 6
  ;                     :year "-"
  ;                     :grade "-"
  ;                     :term "Spring"
  ;                     :ritScore "-"
  ;                     :norm "-"
  ;                     :growthGoal "-"
  ;                     :metGoal "-"
  ;                     :percentile "-"
  ;                     :growthPercentile "-"
  ;                     :actCollegeReadiness "-"
  ;                     :forwardProjection "-"
  ;                     :testDuration "-"}
  ;                    {:id 1
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Fall"
  ;                     :ritScore 233
  ;                     :norm 220.2
  ;                     :growthGoal nil
  ;                     :metGoal nil
  ;                     :percentile 75
  ;                     :growthPercentile 65
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 68}
  ;                    {:id 2
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Winter"
  ;                     :ritScore 235
  ;                     :norm 224.1
  ;                     :growthGoal 5
  ;                     :metGoal nil
  ;                     :percentile 74
  ;                     :growthPercentile 82
  ;                     :actCollegeReadiness "On Track for a 22"
  ;                     :forwardProjection "Advanced"
  ;                     :testDuration 71}
  ;                    {:id 3
  ;                     :year 2020
  ;                     :grade 7
  ;                     :term "Spring"
  ;                     :ritScore 236
  ;                     :norm 226.7
  ;                     :growthGoal 4
  ;                     :metGoal true
  ;                     :percentile 66
  ;                     :growthPercentile 51
  ;                     :actCollegeReadiness "Not on Track"
  ;                     :forwardProjection "Proficient"
  ;                     :testDuration 62}]}))