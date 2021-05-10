(ns yardstick-api.routes.obstacles
  (:require [compojure.core :refer [GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.obstacles :as d-obstacles]))

(def GET-obstacles
  (GET "/v0.1/student/:student-id/obstacles" [student-id :<< as-int :as {db :db}]
    (response (d-obstacles/get-by-student-id db student-id))))
