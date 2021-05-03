(ns yardstick-api.server
  (:require [compojure.core :refer [defroutes GET]]
            [honeysql.core :as sql]
            [honeysql.helpers :as h]
            [next.jdbc :as jdbc]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [response]]
            [yardstick-api.routes :as r]
            [yardstick-api.state :refer [wrap-db pg-db]])
  (:gen-class))

(def ^:private GET-sample
  (GET "/sample" [:as {db :db}]
    (let [value (jdbc/execute! pg-db
                               (sql/format {:select [:id]
                                            :from [:sample]}))]
      (response {:text "sample"
                 :a value
                 :db db}))))

(defroutes routes
  #'GET-sample
  #'r/GET-students
  #'r/GET-settings
  #'r/GET-supports-by-student
  #'r/GET-opportunities-by-student
  #'r/GET-obstacles-by-student
  #'r/GET-assessment-overviews-by-student
  #'r/GET-assessment-results-by-student-and-assessment
  #'r/GET-assessment-explanations-by-student-and-assessment)

; TODO add a 404 wrapper
(def handler
  (-> routes
      wrap-params
      wrap-json-response
      (wrap-cors :access-control-allow-origin #".*"
                 :access-control-allow-methods [:get :put :post :delete])
      wrap-db))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (run-jetty #'handler {:port 3001
                        :join? false}))

#_(-main)