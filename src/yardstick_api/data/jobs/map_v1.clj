(ns yardstick-api.data.jobs.map-v1
  (:require [honey.sql.helpers :as h]
            [yardstick-api.db :as db]
            [yardstick-api.utils :as util]))

(def ^:private partition-size 100)

(defn- format-row
  [instance-id
   [TermName DistrictName District_StateID SchoolName School_StateID StudentLastName
    StudentFirstName StudentMI StudentID Student_StateID StudentDateOfBirth
    StudentEthnicGroup NWEAStandard_EthnicGroup StudentGender Grade NWEAStandard_Grade
    Subject Course NormsReferenceData
    WISelectedAYFall WISelectedAYWinter WISelectedAYSpring
    WIPreviousAYFall WIPreviousAYWinter WIPreviousAYSpring
    TestType TestName TestStartDate TestStartTime TestDurationMinutes TestRITScore
    TestStandardError TestPercentile AchievementQuintile PercentCorrect RapidGuessingPercentage
    FallToFallProjectedGrowth FallToFallObservedGrowth
    FallToFallObservedGrowthSE FallToFallMetProjectedGrowth
    FallToFallConditionalGrowthIndex FallToFallConditionalGrowthPercentile
    FallToFallGrowthQuintile FallToWinterProjectedGrowth
    FallToWinterObservedGrowth FallToWinterObservedGrowthSE
    FallToWinterMetProjectedGrowth FallToWinterConditionalGrowthIndex
    FallToWinterConditionalGrowthPercentile FallToWinterGrowthQuintile
    FallToSpringProjectedGrowth FallToSpringObservedGrowth
    FallToSpringObservedGrowthSE FallToSpringMetProjectedGrowth
    FallToSpringConditionalGrowthIndex FallToSpringConditionalGrowthPercentile
    FallToSpringGrowthQuintile
    WinterToWinterProjectedGrowth WinterToWinterObservedGrowth
    WinterToWinterObservedGrowthSE WinterToWinterMetProjectedGrowth
    WinterToWinterConditionalGrowthIndex WinterToWinterConditionalGrowthPercentile
    WinterToWinterGrowthQuintile WinterToSpringProjectedGrowth
    WinterToSpringObservedGrowth WinterToSpringObservedGrowthSE
    WinterToSpringMetProjectedGrowth WinterToSpringConditionalGrowthIndex
    WinterToSpringConditionalGrowthPercentile WinterToSpringGrowthQuintile
    SpringToSpringProjectedGrowth SpringToSpringObservedGrowth
    SpringToSpringObservedGrowthSE SpringToSpringMetProjectedGrowth
    SpringToSpringConditionalGrowthIndex SpringToSpringConditionalGrowthPercentile
    SpringToSpringGrowthQuintile
    LexileScore LexileMin LexileMax QuantileScore QuantileMin QuantileMax
    Goal1Name Goal1RitScore Goal1StdErr Goal1Range Goal1Adjective
    Goal2Name Goal2RitScore Goal2StdErr Goal2Range Goal2Adjective
    Goal3Name Goal3RitScore Goal3StdErr Goal3Range Goal3Adjective
    Goal4Name Goal4RitScore Goal4StdErr Goal4Range Goal4Adjective
    Goal5Name Goal5RitScore Goal5StdErr Goal5Range Goal5Adjective
    Goal6Name Goal6RitScore Goal6StdErr Goal6Range Goal6Adjective
    Goal7Name Goal7RitScore Goal7StdErr Goal7Range Goal7Adjective
    Goal8Name Goal8RitScore Goal8StdErr Goal8Range Goal8Adjective
    AccommodationCategories Accommodations
    TypicalFallToFallGrowth TypicalFallToWinterGrowth
    TypicalFallToSpringGrowth TypicalWintertoWinterGrowth
    TypicalWintertoSpringGrowth TypicalSpringToSpringGrowth
    ProjectedProficiencyStudy1 ProjectedProficiencyLevel1
    ProjectedProficiencyStudy2 ProjectedProficiencyLevel2
    ProjectedProficiencyStudy3 ProjectedProficiencyLevel3
    ProjectedProficiencyStudy4 ProjectedProficiencyLevel4
    ProjectedProficiencyStudy5 ProjectedProficiencyLevel5
    ProjectedProficiencyStudy6 ProjectedProficiencyLevel6
    ProjectedProficiencyStudy7 ProjectedProficiencyLevel7
    ProjectedProficiencyStudy8 ProjectedProficiencyLevel8
    ProjectedProficiencyStudy9 ProjectedProficiencyLevel9
    ProjectedProficiencyStudy10 ProjectedProficiencyLevel10]]
  [instance-id TermName DistrictName District_StateID SchoolName School_StateID StudentLastName
   StudentFirstName StudentMI StudentID Student_StateID StudentDateOfBirth
   StudentEthnicGroup NWEAStandard_EthnicGroup StudentGender Grade NWEAStandard_Grade
   Subject Course (util/parse-int NormsReferenceData)
   (util/parse-int WISelectedAYFall) (util/parse-int WISelectedAYWinter)
   (util/parse-int WISelectedAYSpring) (util/parse-int WIPreviousAYFall)
   (util/parse-int WIPreviousAYWinter) (util/parse-int WIPreviousAYSpring)
   TestType TestName TestStartDate TestStartTime (util/parse-int TestDurationMinutes)
   (util/parse-int TestRITScore) (util/parse-double TestStandardError)
   (util/parse-int TestPercentile) AchievementQuintile (util/parse-int PercentCorrect)
   (util/parse-double RapidGuessingPercentage)
   (util/parse-int FallToFallProjectedGrowth) (util/parse-int FallToFallObservedGrowth)
   (util/parse-double FallToFallObservedGrowthSE) FallToFallMetProjectedGrowth
   (util/parse-double FallToFallConditionalGrowthIndex) (util/parse-double FallToFallConditionalGrowthPercentile)
   FallToFallGrowthQuintile (util/parse-int FallToWinterProjectedGrowth)
   (util/parse-int FallToWinterObservedGrowth) (util/parse-double FallToWinterObservedGrowthSE)
   FallToWinterMetProjectedGrowth (util/parse-double FallToWinterConditionalGrowthIndex)
   (util/parse-double FallToWinterConditionalGrowthPercentile) FallToWinterGrowthQuintile
   (util/parse-int FallToSpringProjectedGrowth) (util/parse-int FallToSpringObservedGrowth)
   (util/parse-double FallToSpringObservedGrowthSE) FallToSpringMetProjectedGrowth
   (util/parse-double FallToSpringConditionalGrowthIndex) (util/parse-double FallToSpringConditionalGrowthPercentile)
   FallToSpringGrowthQuintile
   (util/parse-int WinterToWinterProjectedGrowth) (util/parse-int WinterToWinterObservedGrowth)
   (util/parse-double WinterToWinterObservedGrowthSE) WinterToWinterMetProjectedGrowth
   (util/parse-double WinterToWinterConditionalGrowthIndex) (util/parse-double WinterToWinterConditionalGrowthPercentile)
   WinterToWinterGrowthQuintile (util/parse-int WinterToSpringProjectedGrowth)
   (util/parse-int WinterToSpringObservedGrowth) (util/parse-double WinterToSpringObservedGrowthSE)
   WinterToSpringMetProjectedGrowth (util/parse-double WinterToSpringConditionalGrowthIndex)
   (util/parse-double WinterToSpringConditionalGrowthPercentile) WinterToSpringGrowthQuintile
   (util/parse-int SpringToSpringProjectedGrowth) (util/parse-int SpringToSpringObservedGrowth)
   (util/parse-double SpringToSpringObservedGrowthSE) SpringToSpringMetProjectedGrowth
   (util/parse-double SpringToSpringConditionalGrowthIndex) (util/parse-double SpringToSpringConditionalGrowthPercentile)
   SpringToSpringGrowthQuintile
   LexileScore LexileMin LexileMax QuantileScore QuantileMin QuantileMax
   Goal1Name (util/parse-double Goal1RitScore) (util/parse-double Goal1StdErr) Goal1Range Goal1Adjective
   Goal2Name (util/parse-double Goal2RitScore) (util/parse-double Goal2StdErr) Goal2Range Goal2Adjective
   Goal3Name (util/parse-double Goal3RitScore) (util/parse-double Goal3StdErr) Goal3Range Goal3Adjective
   Goal4Name (util/parse-double Goal4RitScore) (util/parse-double Goal4StdErr) Goal4Range Goal4Adjective
   Goal5Name (util/parse-double Goal5RitScore) (util/parse-double Goal5StdErr) Goal5Range Goal5Adjective
   Goal6Name (util/parse-double Goal6RitScore) (util/parse-double Goal6StdErr) Goal6Range Goal6Adjective
   Goal7Name (util/parse-double Goal7RitScore) (util/parse-double Goal7StdErr) Goal7Range Goal7Adjective
   Goal8Name (util/parse-double Goal8RitScore) (util/parse-double Goal8StdErr) Goal8Range Goal8Adjective
   AccommodationCategories Accommodations
   (util/parse-int TypicalFallToFallGrowth) (util/parse-int TypicalFallToWinterGrowth)
   (util/parse-int TypicalFallToSpringGrowth) (util/parse-int TypicalWintertoWinterGrowth)
   (util/parse-int TypicalWintertoSpringGrowth) (util/parse-int TypicalSpringToSpringGrowth)
   ProjectedProficiencyStudy1 ProjectedProficiencyLevel1
   ProjectedProficiencyStudy2 ProjectedProficiencyLevel2
   ProjectedProficiencyStudy3 ProjectedProficiencyLevel3
   ProjectedProficiencyStudy4 ProjectedProficiencyLevel4
   ProjectedProficiencyStudy5 ProjectedProficiencyLevel5
   ProjectedProficiencyStudy6 ProjectedProficiencyLevel6
   ProjectedProficiencyStudy7 ProjectedProficiencyLevel7
   ProjectedProficiencyStudy8 ProjectedProficiencyLevel8
   ProjectedProficiencyStudy9 ProjectedProficiencyLevel9
   ProjectedProficiencyStudy10 ProjectedProficiencyLevel10])

(defn- upsert-chunk [db instance-id chunk]
  (-> (h/insert-into :assessment_map_v1)
      (h/columns :school_assessment_instance_id :TermName :DistrictName
                 :District_StateID :SchoolName :School_StateID
                 :StudentLastName :StudentFirstName :StudentMI
                 :StudentID :Student_StateID :StudentDateOfBirth
                 :StudentEthnicGroup :NWEAStandard_EthnicGroup :StudentGender
                 :Grade :NWEAStandard_Grade :Subject :Course
                 :NormsReferenceData :WISelectedAYFall :WISelectedAYWinter
                 :WISelectedAYSpring :WIPreviousAYFall :WIPreviousAYWinter
                 :WIPreviousAYSpring :TestType :TestName :TestStartDate
                 :TestStartTime :TestDurationMinutes :TestRITScore
                 :TestStandardError :TestPercentile :AchievementQuintile
                 :PercentCorrect :RapidGuessingPercentage
                 :FallToFallProjectedGrowth :FallToFallObservedGrowth
                 :FallToFallObservedGrowthSE :FallToFallMetProjectedGrowth
                 :FallToFallConditionalGrowthIndex :FallToFallConditionalGrowthPercentile
                 :FallToFallGrowthQuintile :FallToWinterProjectedGrowth
                 :FallToWinterObservedGrowth :FallToWinterObservedGrowthSE
                 :FallToWinterMetProjectedGrowth :FallToWinterConditionalGrowthIndex
                 :FallToWinterConditionalGrowthPercentile :FallToWinterGrowthQuintile
                 :FallToSpringProjectedGrowth :FallToSpringObservedGrowth
                 :FallToSpringObservedGrowthSE :FallToSpringMetProjectedGrowth
                 :FallToSpringConditionalGrowthIndex :FallToSpringConditionalGrowthPercentile
                 :FallToSpringGrowthQuintile
                 :WinterToWinterProjectedGrowth :WinterToWinterObservedGrowth
                 :WinterToWinterObservedGrowthSE :WinterToWinterMetProjectedGrowth
                 :WinterToWinterConditionalGrowthIndex :WinterToWinterConditionalGrowthPercentile
                 :WinterToWinterGrowthQuintile :WinterToSpringProjectedGrowth
                 :WinterToSpringObservedGrowth :WinterToSpringObservedGrowthSE
                 :WinterToSpringMetProjectedGrowth :WinterToSpringConditionalGrowthIndex
                 :WinterToSpringConditionalGrowthPercentile :WinterToSpringGrowthQuintile
                 :SpringToSpringProjectedGrowth :SpringToSpringObservedGrowth
                 :SpringToSpringObservedGrowthSE :SpringToSpringMetProjectedGrowth
                 :SpringToSpringConditionalGrowthIndex :SpringToSpringConditionalGrowthPercentile
                 :SpringToSpringGrowthQuintile
                 :LexileScore :LexileMin :LexileMax
                 :QuantileScore :QuantileMin :QuantileMax
                 :Goal1Name :Goal1RitScore :Goal1StdErr :Goal1Range :Goal1Adjective
                 :Goal2Name :Goal2RitScore :Goal2StdErr :Goal2Range :Goal2Adjective
                 :Goal3Name :Goal3RitScore :Goal3StdErr :Goal3Range :Goal3Adjective
                 :Goal4Name :Goal4RitScore :Goal4StdErr :Goal4Range :Goal4Adjective
                 :Goal5Name :Goal5RitScore :Goal5StdErr :Goal5Range :Goal5Adjective
                 :Goal6Name :Goal6RitScore :Goal6StdErr :Goal6Range :Goal6Adjective
                 :Goal7Name :Goal7RitScore :Goal7StdErr :Goal7Range :Goal7Adjective
                 :Goal8Name :Goal8RitScore :Goal8StdErr :Goal8Range :Goal8Adjective
                 :AccommodationCategories :Accommodations
                 :TypicalFallToFallGrowth :TypicalFallToWinterGrowth
                 :TypicalFallToSpringGrowth :TypicalWintertoWinterGrowth
                 :TypicalWintertoSpringGrowth :TypicalSpringToSpringGrowth
                 :ProjectedProficiencyStudy1 :ProjectedProficiencyLevel1
                 :ProjectedProficiencyStudy2 :ProjectedProficiencyLevel2
                 :ProjectedProficiencyStudy3 :ProjectedProficiencyLevel3
                 :ProjectedProficiencyStudy4 :ProjectedProficiencyLevel4
                 :ProjectedProficiencyStudy5 :ProjectedProficiencyLevel5
                 :ProjectedProficiencyStudy6 :ProjectedProficiencyLevel6
                 :ProjectedProficiencyStudy7 :ProjectedProficiencyLevel7
                 :ProjectedProficiencyStudy8 :ProjectedProficiencyLevel8
                 :ProjectedProficiencyStudy9 :ProjectedProficiencyLevel9
                 :ProjectedProficiencyStudy10 :ProjectedProficiencyLevel10)
      (h/values (map #(format-row instance-id %) chunk))
      h/on-conflict
      (h/on-constraint :assessment_map_v1_school_assessment_student_unique)
      (h/do-update-set :TermName :DistrictName :District_StateID :SchoolName
                       :School_StateID :StudentLastName :StudentFirstName
                       :StudentMI :StudentDateOfBirth :StudentEthnicGroup
                       :NWEAStandard_EthnicGroup :StudentGender
                       :Grade :NWEAStandard_Grade :Subject :Course
                       :NormsReferenceData :WISelectedAYFall :WISelectedAYWinter
                       :WISelectedAYSpring :WIPreviousAYFall :WIPreviousAYWinter
                       :WIPreviousAYSpring :TestType :TestName :TestStartDate
                       :TestStartTime :TestDurationMinutes :TestRITScore
                       :TestStandardError :TestPercentile :AchievementQuintile
                       :PercentCorrect :RapidGuessingPercentage
                       :FallToFallProjectedGrowth :FallToFallObservedGrowth
                       :FallToFallObservedGrowthSE :FallToFallMetProjectedGrowth
                       :FallToFallConditionalGrowthIndex :FallToFallConditionalGrowthPercentile
                       :FallToFallGrowthQuintile :FallToWinterProjectedGrowth
                       :FallToWinterObservedGrowth :FallToWinterObservedGrowthSE
                       :FallToWinterMetProjectedGrowth :FallToWinterConditionalGrowthIndex
                       :FallToWinterConditionalGrowthPercentile :FallToWinterGrowthQuintile
                       :FallToSpringProjectedGrowth :FallToSpringObservedGrowth
                       :FallToSpringObservedGrowthSE :FallToSpringMetProjectedGrowth
                       :FallToSpringConditionalGrowthIndex :FallToSpringConditionalGrowthPercentile
                       :FallToSpringGrowthQuintile
                       :WinterToWinterProjectedGrowth :WinterToWinterObservedGrowth
                       :WinterToWinterObservedGrowthSE :WinterToWinterMetProjectedGrowth
                       :WinterToWinterConditionalGrowthIndex :WinterToWinterConditionalGrowthPercentile
                       :WinterToWinterGrowthQuintile :WinterToSpringProjectedGrowth
                       :WinterToSpringObservedGrowth :WinterToSpringObservedGrowthSE
                       :WinterToSpringMetProjectedGrowth :WinterToSpringConditionalGrowthIndex
                       :WinterToSpringConditionalGrowthPercentile :WinterToSpringGrowthQuintile
                       :SpringToSpringProjectedGrowth :SpringToSpringObservedGrowth
                       :SpringToSpringObservedGrowthSE :SpringToSpringMetProjectedGrowth
                       :SpringToSpringConditionalGrowthIndex :SpringToSpringConditionalGrowthPercentile
                       :SpringToSpringGrowthQuintile
                       :LexileScore :LexileMin :LexileMax
                       :QuantileScore :QuantileMin :QuantileMax
                       :Goal1Name :Goal1RitScore :Goal1StdErr :Goal1Range :Goal1Adjective
                       :Goal2Name :Goal2RitScore :Goal2StdErr :Goal2Range :Goal2Adjective
                       :Goal3Name :Goal3RitScore :Goal3StdErr :Goal3Range :Goal3Adjective
                       :Goal4Name :Goal4RitScore :Goal4StdErr :Goal4Range :Goal4Adjective
                       :Goal5Name :Goal5RitScore :Goal5StdErr :Goal5Range :Goal5Adjective
                       :Goal6Name :Goal6RitScore :Goal6StdErr :Goal6Range :Goal6Adjective
                       :Goal7Name :Goal7RitScore :Goal7StdErr :Goal7Range :Goal7Adjective
                       :Goal8Name :Goal8RitScore :Goal8StdErr :Goal8Range :Goal8Adjective
                       :AccommodationCategories :Accommodations
                       :TypicalFallToFallGrowth :TypicalFallToWinterGrowth
                       :TypicalFallToSpringGrowth :TypicalWintertoWinterGrowth
                       :TypicalWintertoSpringGrowth :TypicalSpringToSpringGrowth
                       :ProjectedProficiencyStudy1 :ProjectedProficiencyLevel1
                       :ProjectedProficiencyStudy2 :ProjectedProficiencyLevel2
                       :ProjectedProficiencyStudy3 :ProjectedProficiencyLevel3
                       :ProjectedProficiencyStudy4 :ProjectedProficiencyLevel4
                       :ProjectedProficiencyStudy5 :ProjectedProficiencyLevel5
                       :ProjectedProficiencyStudy6 :ProjectedProficiencyLevel6
                       :ProjectedProficiencyStudy7 :ProjectedProficiencyLevel7
                       :ProjectedProficiencyStudy8 :ProjectedProficiencyLevel8
                       :ProjectedProficiencyStudy9 :ProjectedProficiencyLevel9
                       :ProjectedProficiencyStudy10 :ProjectedProficiencyLevel10)
      (db/->execute db)))

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
  ;; TODO this assumes that map doesn't allow retakes (or just overwrites existing
  ;; data with retakes)
  ;; TODO (^ related) there is a TestStartDate and a TestStartTime that we are reading in
  ;; as 2 text fields -- we could read them in as a date and a time, or combined into a
  ;; datetime
  [db instance-id]
  (-> (h/insert-into :student_assessment
                     [:school_assessment_instance_id :student_id :grade_id
                      :local_student_id :state_student_id :assessment_table
                      :assessment_table_id :date_taken :attempts]
                     (-> (h/select :assessment_map_v1.school_assessment_instance_id
                                   :student.id :student.grade_id :student.student_id
                                   :student.student_state_id "assessment_map_v1"
                                   :assessment_map_v1.id :assessment_map_v1.TestStartDate 1)
                         (h/from :assessment_map_v1)
                         (h/left-join :student_assessment
                                      [:and
                                       [:= :student_assessment.assessment_table "assessment_map_v1"]
                                       [:= :assessment_map_v1.id :student_assessment.assessment_table_id]])
                         (h/join :school_assessment_instance
                                 [:=
                                  :assessment_map_v1.school_assessment_instance_id
                                  :school_assessment_instance.id])
                         (h/join :student
                                 [:and
                                  [:= :school_assessment_instance.school_id :student.school_id]
                                  [:or
                                   [:= :assessment_map_v1.StudentID :student.student_id]
                                   [:= :assessment_map_v1.StudentID :student.student_state_id]
                                   [:= :assessment_map_v1.Student_StateID :student.student_id]
                                   [:= :assessment_map_v1.Student_StateID :student.student_state_id]]])
                         (h/where [:and
                                   [:is :student_assessment.id nil]
                                   [:= :assessment_map_v1.school_assessment_instance_id instance-id]])))
      h/on-conflict
      (h/on-constraint :student_assessment_unique_student_instance)
      (h/do-update-set  {:attempts :excluded.attempts
                         :date_taken :excluded.date_taken
                         :assessment_table_id :excluded.assessment_table_id
                         :yardstick_performance_rating nil
                         :updated_at [:now]})
      (db/->execute db)))

(defn upload-map
  "Uploads map data in chunks, then links this data to students"
  [db instance-id csv]
  (let [csv-no-header (drop 1 csv)]
    (doseq [chunk (partition partition-size partition-size nil csv-no-header)]
      (upsert-chunk db instance-id chunk))
    (link-rows-and-students db instance-id)
    ;; (calculate-new-yprs db instance-id)
    ;; TODO what to return?
    "i'm a return value"))
