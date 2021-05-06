(ns yardstick-api.routes.supports
  (:require [compojure.core :refer [GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.supports :as d-supports]))

(def GET-supports
  (GET "/v0.1/student/:student-id/supports" [student-id :<< as-int :as {db :db}]
    (response (d-supports/get-by-student-id db student-id))))
