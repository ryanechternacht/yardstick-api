(ns yardstick-api.server
  (:require [compojure.core :refer [defroutes GET]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [response]]
            [yardstick-api.routes :as r]
            [yardstick-api.state :refer [wrap-db]]
            [yardstick-api.other-file :as of])
  (:gen-class))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") (of/add-text "!"))))

(def ^:private GET-sample
  (GET "/sample" [a :as {db :db}]
    (response {:text "sample"
               :a a
               :db db})))

(defroutes routes #'GET-sample #'r/GET-students #'r/GET-settings)

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