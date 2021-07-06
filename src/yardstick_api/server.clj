(ns yardstick-api.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.params :refer [wrap-params]]
            [yardstick-api.middlewares.config :refer [wrap-config]]
            [yardstick-api.middlewares.db :refer [wrap-db]]
            [yardstick-api.middlewares.language :refer [wrap-language]]
            [yardstick-api.middlewares.user :refer [wrap-user]]
            [yardstick-api.routes :as r])
  (:gen-class))

; TODO add a 404 wrapper
(def handler
  (-> r/routes
      (wrap-json-body {:keywords? true})
      wrap-config
      wrap-language
      wrap-user
      wrap-db
      wrap-session
      wrap-params
      wrap-json-response
      (wrap-cors :access-control-allow-origin #".*"
                 :access-control-allow-methods [:get :put :post :delete])))

;; TODO we chnaged the port here
(defn -main
  [& _]
  (run-jetty #'handler {:port 80
                        :join? false}))

#_(-main)
