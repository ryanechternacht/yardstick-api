(ns yardstick-api.server
  (:require [compojure.core :refer [defroutes GET]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [response]]
            [yardstick-api.other-file :as of])
  (:gen-class))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") (of/add-text "!"))))

(def ^:private GET-sample
  (GET "/sample" [a]
    (response {:text "sample"
               :a a})))

(defroutes routes #'GET-sample)

(def handler
  (-> routes
      wrap-params
      wrap-json-response
      (wrap-cors :access-control-allow-origin #".*"
                 :access-control-allow-methods [:get :put :post :delete])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (run-jetty #'handler {:port 3001
                      :join? false}))

#_(-main)