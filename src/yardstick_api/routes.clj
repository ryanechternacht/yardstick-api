(ns yardstick-api.routes
  (:require [compojure.core :refer [GET]]
            [ring.util.response :refer [response]]))

(def GET-students
  (GET "/v0.1/students" []
    (response [{:name {:first "Darryl"
                       :last "Hurt"
                       :full "Darryl Hurt"
                       :possessive "Darryl's"}
                :pronouns {:nominative "he"
                           :nominativeUpper "He"
                           :possessive "his"
                           :possessiveUpper "His"
                           :accusative "him"
                           :accusativeUpper "Him"}
                :grade {:ordinal "8th"
                        :cardinal 8}
                :id 1}])))

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
                                   :text "Model stating the gist using this Reseach supported one-pager that outlines an easy way to explain Gist."}
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
                                   :text "Model stating the gist using this Reseach supported one-pager that outlines an easy way to explain Gist."}
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
                                   :text "Model stating the gist using this Reseach supported one-pager that outlines an easy way to explain Gist."}
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