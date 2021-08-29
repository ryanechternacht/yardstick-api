(ns yardstick-api.routes
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response not-found]]
            [yardstick-api.routes.assessments :as ass]
            [honeysql.core :as sql]
            [honeysql.helpers :refer [select from merge-join merge-where order-by]]
            [ring.util.response :refer [response]]
            [yardstick-api.db :as db]
            [yardstick-api.routes.jobs :as jobs]
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
    (response {:currentStudent 1
               :this_academic_year {:name "2020 - 2021"
                                    :short_name "'20-'21"
                                    :start_year 2020
                                    :end_year 2021}
               :last_academic_year {:name "2019 - 2020"
                                    :short_name "'19-'20"
                                    :start_year 2019
                                    :end_year 2020}})))

(def handle-404
  (GET "*" []
    (not-found nil)))

(defroutes routes
  #'GET-root-healthz
  #'GET-healthz
  #'s/GET-student
  #'s/GET-students
  #'GET-settings
  #'supp/GET-supports
  #'opp/GET-opportunities
  #'obs/GET-obstacles
  #'ass/GET-assessment-overviews-by-student
  #'ass/GET-assessment-results-by-student-and-assessment
  #'ass/GET-assessment-explanations-assessment
  #'users/GET-me
  #'users/GET-auth0-callback
  #'users/GET-login
  #'jobs/POST-assessment-upload)
