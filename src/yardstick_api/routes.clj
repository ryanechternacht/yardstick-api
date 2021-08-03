(ns yardstick-api.routes
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.routes.assessments :as ass]
            [honeysql.core :as sql]
            [honeysql.helpers :refer [select from merge-join merge-where order-by]]
            [ring.util.response :refer [response]]
            [yardstick-api.db :as db]
            [yardstick-api.routes.obstacles :as obs]
            [yardstick-api.routes.opportunities :as opp]
            [yardstick-api.routes.student :as s]
            [yardstick-api.routes.supports :as supp]
            [yardstick-api.routes.users :as users]))

(def GET-healthz
  (GET "/v0.1/healthz" [:as {:keys [db]}]
    (let [user-count (->> (-> (select :%count.*)
                              (from :yardstick_user))
                          (db/execute db))]
      (response user-count))))

;; I'd prefer to do the health check on a different route, but I can't
;; figure out how to do that in AWS AppRunner
(def GET-root-healthz
  (GET "/" []
    (response "I'm here")))

; TODO this should be loaded based off of the session
; Should this really just live in local storage? 
(def GET-settings
  (GET "/v0.1/settings" []
    (response {:currentStudent 1})))

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

(def GET-healthz
  (GET "/v0.1/healthz" []
    (response "I'm here")))

(defroutes routes
  <<<<<<< HEAD
  =======
  #'GET-root-healthz
  >>>>>>> main
  #'GET-healthz
  #'s/GET-student
  #'s/GET-students
  #'GET-settings
  #'supp/GET-supports
  #'opp/GET-opportunities
  #'obs/GET-obstacles
  #'ass/GET-assessment-overviews-by-student
  #'ass/GET-assessment-results-by-student-and-assessment
  #'GET-assessment-explanations-by-student-and-assessment
  #'users/GET-me
  #'users/GET-auth0-callback
  #'users/GET-login)
