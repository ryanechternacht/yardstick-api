(ns yardstick-api.routes.layout
  (:require [compojure.core :refer [GET]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.layout :as d-layout]))

(def GET-layout
  (GET "/v0.1/students/layout" [:as {db :db}]
    (response (d-layout/get-layout db))))
