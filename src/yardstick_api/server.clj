(ns yardstick-api.server
  (:require [jdbc-ring-session.core :refer [jdbc-store]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.params :refer [wrap-params]]
            [yardstick-api.middlewares.config :refer [wrap-config config]]
            [yardstick-api.middlewares.db :refer [wrap-db]]
            [yardstick-api.middlewares.language :refer [wrap-language]]
            [yardstick-api.middlewares.user :refer [wrap-user]]
            [yardstick-api.routes :as r]))

(def session-store (jdbc-store (:pg-db config)))

(def handler
  (-> r/routes
      (wrap-json-body {:keywords? true})
      wrap-language
      wrap-user
      wrap-db
      wrap-config
      (wrap-session {:store session-store :cookie-attrs (:cookie-attrs config)})
      wrap-params
      wrap-multipart-params
      wrap-json-response
      (wrap-cors :access-control-allow-origin #".*"
                 :access-control-allow-methods [:get :put :post :delete])))

(defn -main
  [& _]
  (run-jetty #'handler {:port 3001
                        :join? false}))

#_(-main)
