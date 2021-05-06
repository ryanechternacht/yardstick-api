(ns yardstick-api.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [yardstick-api.routes :as r]
            [yardstick-api.state :refer [wrap-db]])
  (:gen-class))

; TODO add a 404 wrapper
(def handler
  (-> r/routes
      wrap-params
      wrap-json-response
      (wrap-cors :access-control-allow-origin #".*"
                 :access-control-allow-methods [:get :put :post :delete])
      wrap-db))

(defn -main
  "I don't do a whole lot ... yet."
  [& _]
  (run-jetty #'handler {:port 3001
                        :join? false}))

#_(-main)