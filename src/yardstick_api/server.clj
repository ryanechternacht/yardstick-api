(ns yardstick-api.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.params :refer [wrap-params]]
            [yardstick-api.routes :as r]
            [yardstick-api.middlewares.db :refer [wrap-db]]
            [yardstick-api.middlewares.user :refer [wrap-user]])
  (:gen-class))

; TODO add a 404 wrapper
(def handler
  (-> r/routes
      (wrap-json-body {:keywords? true})
      wrap-user
      wrap-db
      wrap-session
      wrap-params
      wrap-json-response
      (wrap-cors :access-control-allow-origin #".*"
                 :access-control-allow-methods [:get :put :post :delete])))

(defn -main
  [& _]
  (run-jetty #'handler {:port 3001
                        :join? false}))

#_(-main)
