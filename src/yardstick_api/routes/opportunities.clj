(ns yardstick-api.routes.opportunities
  (:require [compojure.core :refer [GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.opportunities :as d-opportunities]))

(def GET-opportunities
  (GET "/v0.1/student/:student-id/opportunities" [student-id :<< as-int :as {db :db}]
    (response (d-opportunities/get-by-student-id db student-id))))
