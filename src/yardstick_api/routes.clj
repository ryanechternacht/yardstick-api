(ns yardstick-api.routes
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [honeysql.core :as sql]
            [honeysql.helpers :as h]
            [next.jdbc :as jdbc]
            [yardstick-api.routes.student :as s]))

(def ^:private GET-sample
  (GET "/sample" [:as {db :db}]
    (let [value (jdbc/execute! db
                               (sql/format {:select [:id]
                                            :from [:sample]}))]
      (response {:text "sample"
                 :a value
                 :db db}))))

; TODO this should be loaded based off of the session
; Should this really just live in local storage? 
(def GET-settings
  (GET "/v0.1/settings" []
    (response {:currentStudent 1})))

; TODO Maybe this should become 2 different routes?
(def GET-supports-by-student
  (GET "/v0.1/student/:student-id/supports" [student-id]
    (response [{:id 1
                :overview {:title "Help ${student.name.first} Comprehend What ${student.pronouns.nominativeUpper} Is Reading By Having Gist Conversations"
                           :tags [{:text "Suggested by School"}
                                  {:text "Daily"}
                                  {:text "Computer not Required"}]
                           :description "\"Gist\" is a great tool for helping readers develop a reoccurring habit of self monitoring. The goal of this action plan is for ${student.name.first} to not get stuck in the weeds with every vocabulary word but instead nudge ${student.pronouns.accusative} to be focusing on the main idea of what ${student.pronouns.nominative} has just read. By having quick, frequent conversations with ${student.name.first} you can help ${student.pronouns.accusative} develop this habit in just a few weeks."
                           :action "Dive into the Gist action plan"}
                :details {:title "Gist Action Plan"
                          :subtitle "Sometimes getting the gist is enough."
                          :description "It is not always necessary for readers to understand every word.  Sometimes struggling readers slow down comprehension by grappling with each word.  This can be discouraging and stall momentum so that comprehension breaks down further.  Work with your child determine the gist or essential idea of a passage."
                          :steps [{:title "Do This"
                                   :text "Read with ${student.name.first} for at least 20 minutes and ask ${student.pronouns.accusative} to share the “Gist” of what ${student.pronouns.nominative} read with you"}
                                  {:title "Using This"
                                   :text "Model stating the gist using this <a href='https://nysrti.org/intervention-tools/reading-tools/tool:getthegist/'>Reseach supported one-pager</a> that outlines an easy way to explain Gist."}
                                  {:title "This Often"
                                   :text "Read with ${student.name.first} and have ${student.pronouns.accusative} write or say the Gist 3-4 times a week"}
                                  {:title "For This Long"
                                   :text "Keep this up for 4 weeks or until you start to see ${student.name.possessive} ability to accurately share the Gist of what ${student.pronouns.nominative} read improve"}]}}
               {:id 2
                :overview {:title "Assist ${student.name.first} in Selecting Fiction Books That Match ${student.pronouns.possessiveUpper} Developmental Level"
                           :tags [{:text "Suggested by Teacher"}
                                  {:text "Ongoing"}
                                  {:text "Computer Based"}]
                           :description "For ${student.pronouns.possessive} independent reading to have a positive impact on ${student.name.possessive} comprehension ${student.pronouns.nominative} needs to be engaging with books at the right level. Too easy, and ${student.pronouns.nominative} won’t be pushed to stretch ${student.pronouns.possessive} abilities. Too hard and it is likely to be a frustrating experience. Using online tools you can help ${student.pronouns.accusative} select books in ${student.pronouns.possessive} development sweetspot."
                           :action "View the Selecting Books action plan"}
                :details {:title "Gist Action Plan"
                          :subtitle "Sometimes getting the gist is enough."
                          :description "It is not always necessary for readers to understand every word.  Sometimes struggling readers slow down comprehension by grappling with each word.  This can be discouraging and stall momentum so that comprehension breaks down further.  Work with your child determine the gist or essential idea of a passage."
                          :steps [{:title "Do This"
                                   :text "Read with ${student.name.first} for at least 20 minutes and ask ${student.pronouns.accusative} to share the “Gist” of what ${student.pronouns.nominative} read with you"}
                                  {:title "Using This"
                                   :text "Model stating the gist using this <a href='https://nysrti.org/intervention-tools/reading-tools/tool:getthegist/'>Reseach supported one-pager</a> that outlines an easy way to explain Gist."}
                                  {:title "This Often"
                                   :text "Read with ${student.name.first} and have ${student.pronouns.accusative} write or say the Gist 3-4 times a week"}
                                  {:title "For This Long"
                                   :text "Keep this up for 4 weeks or until you start to see ${student.name.possessive} ability to accurately share the Gist of what ${student.pronouns.nominative} read improve"}]}}
               {:id 3
                :overview {:title "Work With ${student.name.first} to Master More Grade Level Vocabulary"
                           :tags [{:text "Suggested by School"}
                                  {:text "Daily"}
                                  {:text "Computer not Required"}]
                           :description "Knowing ${student.name.first} needs increased support to build out ${student.pronouns.possessive} understanding of grade level appropriate vocabulary helping ${student.name.accusative} work through a set of flashcards is a quick way to help ${student.pronouns.accusative} feel more comfortable when tackling challenging texts."
                           :action "Review the Grade Level Vocab action plan"}
                :details {:title "Gist Action Plan"
                          :subtitle "Sometimes getting the gist is enough."
                          :description "It is not always necessary for readers to understand every word.  Sometimes struggling readers slow down comprehension by grappling with each word.  This can be discouraging and stall momentum so that comprehension breaks down further.  Work with your child determine the gist or essential idea of a passage."
                          :steps [{:title "Do This"
                                   :text "Read with ${student.name.first} for at least 20 minutes and ask ${student.pronouns.accusative} to share the “Gist” of what ${student.pronouns.nominative} read with you"}
                                  {:title "Using This"
                                   :text "Model stating the gist using this <a href='https://nysrti.org/intervention-tools/reading-tools/tool:getthegist/'>Reseach supported one-pager</a> that outlines an easy way to explain Gist."}
                                  {:title "This Often"
                                   :text "Read with ${student.name.first} and have ${student.pronouns.accusative} write or say the Gist 3-4 times a week"}
                                  {:title "For This Long"
                                   :text "Keep this up for 4 weeks or until you start to see ${student.name.possessive} ability to accurately share the Gist of what ${student.pronouns.nominative} read improve"}]}}])))

(def GET-opportunities-by-student
  (GET "/v0.1/student/:student-id/opportunities" [student-id]
    (response [{:id 1
                :title "Shooting Star"
                :image "/images/opportunities-star.png"
                :description "Holy Moly! ${student.name.first} grew more than <span class=\"font-bold\">88% of ${student.pronouns.possessive} peers</span> on the most recent ${assessment.name} Assessment"}
               {:id 2
                :title "Top of Class"
                :image "/images/opportunities-award.png"
                :description "${student.name.first} has the highest ${assessment.name} score in <span class=\"font-bold\">The Real and Complex Number System</span> in ${student.pronouns.possessive} class. Is ice cream in order?"}
               {:id 3
                :title "Math Wizard"
                :image "/images/opportunities-wizard.png"
                :description "${student.name.first} might have some magic up ${student.pronouns.possessive} sleeves as ${student.pronouns.nominative} is <span class=\"font-bold\">Proficient</span> or <span class=\"font-bold\">On Track</span> across all of ${student.pronouns.possessive} math assessments"}])))

(def GET-obstacles-by-student
  (GET "/v0.1/student/:student-id/obstacles" [student-id]
    (response [{:id 1
                :question "Where do ${student.name.possessive} academic skills have the greatest obstacles to overcome for ${student.pronouns.accusative} to be college and career ready by graduation?"
                :answer "Based on ${student.name.possessive} scores on ${student.name.possessive} NWEA MAP and Forward Exam, we see ${student.pronouns.possessive} greatest obstacles to overcome as ${student.pronouns.possessive} performance with <span class=\"underline\">Fiction Comprehension</span>."
                :type "SimpleObstacle"
                :order 1}
               {:id 2
                :question "How does ${student.name.first} compare to other ${student.grade.ordinal} graders?"
                :answer "Based on ${student.pronouns.possessive} assessments, ${student.name.possessive} performance is more than one grade level behind on related Fiction Comprehension skills"
                :type "SimpleObstacle"
                :order 2}
               {:id 3
                :question "Why is overcoming these osbstacles important for ${student.name.first} in the next few years?"
                :answer "The instruction ${student.name.first} will receive in ${student.pronouns.possessive} High School English Language Arts courses will likely not focus a great deal on comprehension. <br><br> Often instruction at the high school level is focused on more deeply analyzing a text, with teachers and the curriculum assuming that students can understand the material at a basic level."
                :type "SimpleObstacle"
                :order 3
                :cta {:position "below"}}
               {:id 4
                :question "Can you show me what ${student.name.possessive} performance looks like?"
                :answer "The passage on the left represents an approximate text ${student.name.first} could read and comprehend. On the right is a text of what an On Track ${student.grade.ordinal} grader reader could comprehend."
                :type "ReadingPassageObstacle"
                :studentLevel "We tiptoed down the hall to the second classroom on the right. The heavy wooden door opened easily and we stepped in. There is an eerie, expectant feeling to a school room in the summer. The normal classroom items were there: desks, chalkboards, a set of encyclopedias. The American flag with accompanying pictures of Presidents Washington and Lincoln. But without students occupying those desks and their homework tacked on the wall, that empty summer classroom seemed laden with the memory of past students and past learning that took place within those walls. I strained to listen, as if I might hear the whisperings and stirrings of the past."
                :targetLevel "Lydia was a stout, well-grown girl of fifteen, with a fine complexion and good-humoured countenance; a favourite with her mother, whose affection had brought her into public at an early age. She had high animal spirits, and a sort of natural self-consequence, which the attentions of the officers, to whom her uncle’s good dinners and her own easy manners recommended her, had increased into assurance. She was very equal therefore to address Mr. Bingley on the subject of the ball, and abruptly reminded him of his promise; adding, that it would be the most shameful thing in the world if he did not keep it. His answer to this sudden attack was delightful to their mother’s ear."
                :order 4
                :cta {:position "above"}}
               {:id 5
                :question "Why is this important in the long run?"
                :answer "Research has shown a strong connection between literacy skills (fiction and nonfiction comprehension) and success in highly sought-ofter jobs. <br><br> Meaning the stronger ${student.name.possessive} literacy skills are, the more opportunities will be on the table when ${student.pronouns.nominative} picks out ${student.pronouns.possessive} future career."
                :type "SimpleObstacle"
                :order 5
                :cta {:position "above"}}])))

(def GET-assessment-overviews-by-student
  (GET "/v0.1/student/:student-id/assessments" [student-id]
    (response [{:id 1
                :name "PreACT 8/9"
                :rating 3
                :type "general"}
               {:id 2
                :name "Forward - Mathematics"
                :rating 4
                :type "math"}
               {:id 3
                :name "NWEA MAP - Mathematics"
                :rating 5
                :type "math"}
               {:id 4
                :name "Forward - ELA"
                :rating 2
                :type "ela"}
               {:id 5
                :name "NWEA MAP - Reading"
                :rating 1
                :type "ela"}])))

(def GET-assessment-results-by-student-and-assessment
  (GET "/v0.1/student/:student-id/assessment/:assessment-id" [student-id assessment-id :<< as-int]
    (response (case assessment-id
                1 {:id 1
                   :name "PreACT 8/9"
                   :shortName "PreACT"
                   :scale "RIT Score"
                   :subject {:name ""}
                   :latestTerm {:fullName "Winter 2021"
                                :gradeLevelAverage 280
                                :domains [{:name "ELA"
                                           :score 278}
                                          {:name "Science"
                                           :score 282}
                                          {:name "Social Studies"
                                           :score 283}
                                          {:name "Mathematics"
                                           :score 284}]}
                   :achievement 72
                   :growth 98
                   :recentResults [{:label "Spring '18"
                                    :student 226
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Fall '19"
                                    :student 228
                                    :reference 220
                                    :hitGoal false}
                                   {:label "Winter '19"
                                    :student 229
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Spring '20"
                                    :student 230
                                    :reference 224
                                    :hitGoal true}
                                   {:label "Fall '20"
                                    :student 227
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Winter '20"
                                    :student 232
                                    :reference 224
                                    :hitGoal true}]
                   :recentTerms [{:id 4
                                  :year 2021
                                  :grade 8
                                  :term "Fall"
                                  :ritScore 232
                                  :norm 224.9
                                  :growthGoal nil
                                  :metGoal nil
                                  :percentile 76
                                  :growthPercentile 52
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 37}
                                 {:id 5
                                  :year 2021
                                  :grade 8
                                  :term "Winter"
                                  :ritScore 239
                                  :norm 228.1
                                  :growthGoal 6
                                  :metGoal true
                                  :percentile 70
                                  :growthPercentile 98
                                  :actCollegeReadiness "On Track for a 24"
                                  :forwardProjection "Proficient"
                                  :testDuration 84}
                                 {:id 6
                                  :year "-"
                                  :grade "-"
                                  :term "Spring"
                                  :ritScore "-"
                                  :norm "-"
                                  :growthGoal "-"
                                  :metGoal "-"
                                  :percentile "-"
                                  :growthPercentile "-"
                                  :actCollegeReadiness "-"
                                  :forwardProjection "-"
                                  :testDuration "-"}
                                 {:id 1
                                  :year 2020
                                  :grade 7
                                  :term "Fall"
                                  :ritScore 233
                                  :norm 220.2
                                  :growthGoal nil
                                  :metGoal nil
                                  :percentile 75
                                  :growthPercentile 65
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 68}
                                 {:id 2
                                  :year 2020
                                  :grade 7
                                  :term "Winter"
                                  :ritScore 235
                                  :norm 224.1
                                  :growthGoal 5
                                  :metGoal nil
                                  :percentile 74
                                  :growthPercentile 82
                                  :actCollegeReadiness "On Track for a 22"
                                  :forwardProjection "Advanced"
                                  :testDuration 71}
                                 {:id 3
                                  :year 2020
                                  :grade 7
                                  :term "Spring"
                                  :ritScore 236
                                  :norm 226.7
                                  :growthGoal 4
                                  :metGoal true
                                  :percentile 66
                                  :growthPercentile 51
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 62}]}
                2 {:id 2
                   :name "Forward"
                   :shortName "Forward"
                   :scale "RIT Score"
                   :subject {:name "Mathematics"}
                   :latestTerm {:fullName "Winter 2021"
                                :gradeLevelAverage 280
                                :domains [{:name "Geometry"
                                           :score 278}
                                          {:name "Statistics and Probability"
                                           :score 282}
                                          {:name "Operations and Algebraic Thinking"
                                           :score 283}
                                          {:name "The Real and Complex Number System"
                                           :score 284}]}
                   :achievement 72
                   :growth 98
                   :recentResults [{:label "Spring '18"
                                    :student 226
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Fall '19"
                                    :student 228
                                    :reference 220
                                    :hitGoal false}
                                   {:label "Winter '19"
                                    :student 229
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Spring '20"
                                    :student 230
                                    :reference 224
                                    :hitGoal true}
                                   {:label "Fall '20"
                                    :student 227
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Winter '20"
                                    :student 232
                                    :reference 224
                                    :hitGoal true}]
                   :recentTerms [{:id 4
                                  :year 2021
                                  :grade 8
                                  :term "Fall"
                                  :ritScore 232
                                  :norm 224.9
                                  :growthGoal nil
                                  :metGoal nil
                                  :percentile 76
                                  :growthPercentile 52
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 37}
                                 {:id 5
                                  :year 2021
                                  :grade 8
                                  :term "Winter"
                                  :ritScore 239
                                  :norm 228.1
                                  :growthGoal 6
                                  :metGoal true
                                  :percentile 70
                                  :growthPercentile 98
                                  :actCollegeReadiness "On Track for a 24"
                                  :forwardProjection "Proficient"
                                  :testDuration 84}
                                 {:id 6
                                  :year "-"
                                  :grade "-"
                                  :term "Spring"
                                  :ritScore "-"
                                  :norm "-"
                                  :growthGoal "-"
                                  :metGoal "-"
                                  :percentile "-"
                                  :growthPercentile "-"
                                  :actCollegeReadiness "-"
                                  :forwardProjection "-"
                                  :testDuration "-"}
                                 {:id 1
                                  :year 2020
                                  :grade 7
                                  :term "Fall"
                                  :ritScore 233
                                  :norm 220.2
                                  :growthGoal nil
                                  :metGoal nil
                                  :percentile 75
                                  :growthPercentile 65
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 68}
                                 {:id 2
                                  :year 2020
                                  :grade 7
                                  :term "Winter"
                                  :ritScore 235
                                  :norm 224.1
                                  :growthGoal 5
                                  :metGoal nil
                                  :percentile 74
                                  :growthPercentile 82
                                  :actCollegeReadiness "On Track for a 22"
                                  :forwardProjection "Advanced"
                                  :testDuration 71}
                                 {:id 3
                                  :year 2020
                                  :grade 7
                                  :term "Spring"
                                  :ritScore 236
                                  :norm 226.7
                                  :growthGoal 4
                                  :metGoal true
                                  :percentile 66
                                  :growthPercentile 51
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 62}]}
                3 {:id 3
                   :name "NWEA MAP"
                   :shortName "MAP"
                   :scale "RIT Score"
                   :subject {:name "Mathematics"}
                   :latestTerm {:fullName "Winter 2021"
                                :gradeLevelAverage 280
                                :domains [{:name "Geometry"
                                           :score 278}
                                          {:name "Statistics and Probability"
                                           :score 282}
                                          {:name "Operations and Algebraic Thinking"
                                           :score 283}
                                          {:name "The Real and Complex Number System"
                                           :score 284}]}
                   :achievement 72
                   :growth 98
                   :recentResults [{:label "Spring '18"
                                    :student 226
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Fall '19"
                                    :student 228
                                    :reference 220
                                    :hitGoal false}
                                   {:label "Winter '19"
                                    :student 229
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Spring '20"
                                    :student 230
                                    :reference 224
                                    :hitGoal true}
                                   {:label "Fall '20"
                                    :student 227
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Winter '20"
                                    :student 232
                                    :reference 224
                                    :hitGoal true}]
                   :recentTerms [{:id 4
                                  :year 2021
                                  :grade 8
                                  :term "Fall"
                                  :ritScore 232
                                  :norm 224.9
                                  :growthGoal nil
                                  :metGoal nil
                                  :percentile 76
                                  :growthPercentile 52
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 37}
                                 {:id 5
                                  :year 2021
                                  :grade 8
                                  :term "Winter"
                                  :ritScore 239
                                  :norm 228.1
                                  :growthGoal 6
                                  :metGoal true
                                  :percentile 70
                                  :growthPercentile 98
                                  :actCollegeReadiness "On Track for a 24"
                                  :forwardProjection "Proficient"
                                  :testDuration 84}
                                 {:id 6
                                  :year "-"
                                  :grade "-"
                                  :term "Spring"
                                  :ritScore "-"
                                  :norm "-"
                                  :growthGoal "-"
                                  :metGoal "-"
                                  :percentile "-"
                                  :growthPercentile "-"
                                  :actCollegeReadiness "-"
                                  :forwardProjection "-"
                                  :testDuration "-"}
                                 {:id 1
                                  :year 2020
                                  :grade 7
                                  :term "Fall"
                                  :ritScore 233
                                  :norm 220.2
                                  :growthGoal nil
                                  :metGoal nil
                                  :percentile 75
                                  :growthPercentile 65
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 68}
                                 {:id 2
                                  :year 2020
                                  :grade 7
                                  :term "Winter"
                                  :ritScore 235
                                  :norm 224.1
                                  :growthGoal 5
                                  :metGoal nil
                                  :percentile 74
                                  :growthPercentile 82
                                  :actCollegeReadiness "On Track for a 22"
                                  :forwardProjection "Advanced"
                                  :testDuration 71}
                                 {:id 3
                                  :year 2020
                                  :grade 7
                                  :term "Spring"
                                  :ritScore 236
                                  :norm 226.7
                                  :growthGoal 4
                                  :metGoal true
                                  :percentile 66
                                  :growthPercentile 51
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 62}]}
                4 {:id 4
                   :name "Forward"
                   :shortName "Forward"
                   :scale "RIT Score"
                   :subject {:name "ELA"}
                   :latestTerm {:fullName "Winter 2021"
                                :gradeLevelAverage 280
                                :domains [{:name "Reading Comprehension"
                                           :score 278}
                                          {:name "Essay Writing"
                                           :score 282}
                                          {:name "American Literature"
                                           :score 283}
                                          {:name "Creative Writing"
                                           :score 284}]}
                   :achievement 72
                   :growth 98
                   :recentResults [{:label "Spring '18"
                                    :student 226
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Fall '19"
                                    :student 228
                                    :reference 220
                                    :hitGoal false}
                                   {:label "Winter '19"
                                    :student 229
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Spring '20"
                                    :student 230
                                    :reference 224
                                    :hitGoal true}
                                   {:label "Fall '20"
                                    :student 227
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Winter '20"
                                    :student 232
                                    :reference 224
                                    :hitGoal true}]
                   :recentTerms [{:id 4
                                  :year 2021
                                  :grade 8
                                  :term "Fall"
                                  :ritScore 232
                                  :norm 224.9
                                  :growthGoal nil
                                  :metGoal nil
                                  :percentile 76
                                  :growthPercentile 52
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 37}
                                 {:id 5
                                  :year 2021
                                  :grade 8
                                  :term "Winter"
                                  :ritScore 239
                                  :norm 228.1
                                  :growthGoal 6
                                  :metGoal true
                                  :percentile 70
                                  :growthPercentile 98
                                  :actCollegeReadiness "On Track for a 24"
                                  :forwardProjection "Proficient"
                                  :testDuration 84}
                                 {:id 6
                                  :year "-"
                                  :grade "-"
                                  :term "Spring"
                                  :ritScore "-"
                                  :norm "-"
                                  :growthGoal "-"
                                  :metGoal "-"
                                  :percentile "-"
                                  :growthPercentile "-"
                                  :actCollegeReadiness "-"
                                  :forwardProjection "-"
                                  :testDuration "-"}
                                 {:id 1
                                  :year 2020
                                  :grade 7
                                  :term "Fall"
                                  :ritScore 233
                                  :norm 220.2
                                  :growthGoal nil
                                  :metGoal nil
                                  :percentile 75
                                  :growthPercentile 65
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 68}
                                 {:id 2
                                  :year 2020
                                  :grade 7
                                  :term "Winter"
                                  :ritScore 235
                                  :norm 224.1
                                  :growthGoal 5
                                  :metGoal nil
                                  :percentile 74
                                  :growthPercentile 82
                                  :actCollegeReadiness "On Track for a 22"
                                  :forwardProjection "Advanced"
                                  :testDuration 71}
                                 {:id 3
                                  :year 2020
                                  :grade 7
                                  :term "Spring"
                                  :ritScore 236
                                  :norm 226.7
                                  :growthGoal 4
                                  :metGoal true
                                  :percentile 66
                                  :growthPercentile 51
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 62}]}
                5 {:id 5
                   :name "NWEA MAP"
                   :shortName "MAP"
                   :scale "RIT Score"
                   :subject {:name "ELA"}
                   :latestTerm {:fullName "Winter 2021"
                                :gradeLevelAverage 280
                                :domains [{:name "Reading Comprehension"
                                           :score 278}
                                          {:name "Essay Writing"
                                           :score 282}
                                          {:name "American Literature"
                                           :score 283}
                                          {:name "Creative Writing"
                                           :score 284}]}
                   :achievement 72
                   :growth 98
                   :recentResults [{:label "Spring '18"
                                    :student 226
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Fall '19"
                                    :student 228
                                    :reference 220
                                    :hitGoal false}
                                   {:label "Winter '19"
                                    :student 229
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Spring '20"
                                    :student 230
                                    :reference 224
                                    :hitGoal true}
                                   {:label "Fall '20"
                                    :student 227
                                    :reference 222
                                    :hitGoal true}
                                   {:label "Winter '20"
                                    :student 232
                                    :reference 224
                                    :hitGoal true}]
                   :recentTerms [{:id 4
                                  :year 2021
                                  :grade 8
                                  :term "Fall"
                                  :ritScore 232
                                  :norm 224.9
                                  :growthGoal nil
                                  :metGoal nil
                                  :percentile 76
                                  :growthPercentile 52
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 37}
                                 {:id 5
                                  :year 2021
                                  :grade 8
                                  :term "Winter"
                                  :ritScore 239
                                  :norm 228.1
                                  :growthGoal 6
                                  :metGoal true
                                  :percentile 70
                                  :growthPercentile 98
                                  :actCollegeReadiness "On Track for a 24"
                                  :forwardProjection "Proficient"
                                  :testDuration 84}
                                 {:id 6
                                  :year "-"
                                  :grade "-"
                                  :term "Spring"
                                  :ritScore "-"
                                  :norm "-"
                                  :growthGoal "-"
                                  :metGoal "-"
                                  :percentile "-"
                                  :growthPercentile "-"
                                  :actCollegeReadiness "-"
                                  :forwardProjection "-"
                                  :testDuration "-"}
                                 {:id 1
                                  :year 2020
                                  :grade 7
                                  :term "Fall"
                                  :ritScore 233
                                  :norm 220.2
                                  :growthGoal nil
                                  :metGoal nil
                                  :percentile 75
                                  :growthPercentile 65
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 68}
                                 {:id 2
                                  :year 2020
                                  :grade 7
                                  :term "Winter"
                                  :ritScore 235
                                  :norm 224.1
                                  :growthGoal 5
                                  :metGoal nil
                                  :percentile 74
                                  :growthPercentile 82
                                  :actCollegeReadiness "On Track for a 22"
                                  :forwardProjection "Advanced"
                                  :testDuration 71}
                                 {:id 3
                                  :year 2020
                                  :grade 7
                                  :term "Spring"
                                  :ritScore 236
                                  :norm 226.7
                                  :growthGoal 4
                                  :metGoal true
                                  :percentile 66
                                  :growthPercentile 51
                                  :actCollegeReadiness "Not on Track"
                                  :forwardProjection "Proficient"
                                  :testDuration 62}]}))))

(def GET-assessment-explanations-by-student-and-assessment
  (GET "/v0.1/student/:student-id/assessment/:assessment-id/explanation" [student-id assessment-id :<< as-int]
    (response
     {:assessmentId assessment-id
      :overview {:title "Why is the ${assessment.name} important?"
                 :p1 "Your school uses the ${assessment.name} as it’s primary tool for determing student academic growth"
                 :p2 "${student.name.possessive} scores at the end of the year maybe be a factor in determining what Math and ELA classes are available to ${student.pronouns.accusative} in high school"}
      :type {:title "What Type of Assessment is ${assessment.name}?"
             :p1 "${assessment.name} is considered a growth assessment"
             :p2 "This means ${assessment.shortName} can measure ${student.name.possessive} growth from the start to end of a school year, as well as ${student.pronouns.possessive} growth over time."
             :p3 "<sup>*</sup>Growth Assessments similar to ${assessment.name} are very common in K-12 Education"}
      :traits {:title "How does the ${assessment.name} Work?"
               :traits [{:title "Adaptive"
                         :description "${assessment.name} responds to how your student is performing, getting harder or easier to gain a better understanding of their abilities."
                         :icon "/images/adaptive-icon.svg"}
                        {:title "Subject Based"
                         :description "${student.name.first} takes one test per subject area. ${student.pronouns.possessiveUpper} most recent testing was in Reading and Math."
                         :icon "/images/subject-based-icon.svg"}
                        {:title "Reoccurring"
                         :description "${student.name.first} takes the ${assessment.name} assessment 3-4 times per year. <br><br> Normally once in the in the fall, winter and spring."
                         :icon "/images/reoccuring-icon.svg"}
                        {:title "Normative"
                         :description "${student.name.possessive} ${assessment.name} Scores can be easily compared to students in ${student.pronouns.possessive} grade level all across the country."
                         :icon "/images/normative-icon.svg"}]}})))

(defroutes routes
  #'GET-sample
  #'s/GET-student
  #'s/GET-students
  #'GET-settings
  #'GET-supports-by-student
  #'GET-opportunities-by-student
  #'GET-obstacles-by-student
  #'GET-assessment-overviews-by-student
  #'GET-assessment-results-by-student-and-assessment
  #'GET-assessment-explanations-by-student-and-assessment)