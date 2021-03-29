(ns yardstick-api.yardstick-backend
  (:require [yardstick-api.other-file :as of]
            [compojure.core :refer [defroutes GET]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response :refer [response]])
  (:gen-class))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") (of/add-text "!"))))

(def ^:private GET-sample
  (GET "/sample" []
    (response "sample")))

(defroutes routes #'GET-sample)

(def handler
  (-> routes))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "starting server")
  (run-jetty handler {:port 3001
                      :join? false})
  (println "ready"))
