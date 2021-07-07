(ns yardstick-api.server
  (:require [jdbc-ring-session.core :refer [jdbc-store]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.params :refer [wrap-params]]
            [yardstick-api.middlewares.config :refer [wrap-config]]
            [yardstick-api.middlewares.db :refer [wrap-db pg-db]]
            [yardstick-api.middlewares.language :refer [wrap-language]]
            [yardstick-api.middlewares.user :refer [wrap-user]]
            [yardstick-api.routes :as r]))

(def session-store (jdbc-store pg-db))

; TODO add a 404 wrapper
(def handler
  (-> r/routes
      (wrap-json-body {:keywords? true})
      wrap-config
      wrap-language
      wrap-user
      wrap-db
      ;; I think setting the domain this broadly is HELLA bad
      ;; I think the solution here will be to pass in a query param that the frontend
      ;; uses to set the cookie itself
      ;; or own a domain and host these on the same domain and set it there
      ;; or... something better?
      (wrap-session {:store session-store :cookie-attrs {:domain "awsapprunner.com"}})
      wrap-params
      wrap-json-response
      (wrap-cors :access-control-allow-origin #".*"
                 :access-control-allow-methods [:get :put :post :delete])))

(defn -main
  [& _]
  (run-jetty #'handler {:port 3001
                        :join? false}))

#_(-main)
