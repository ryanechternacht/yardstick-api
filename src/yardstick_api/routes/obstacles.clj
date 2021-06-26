(ns yardstick-api.routes.obstacles
  (:require [compojure.core :refer [GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.obstacles :as d-obstacles]
            [yardstick-api.routes.helpers.auth :refer [unauthorized has-student-access?]]))

(def GET-obstacles
  (GET "/v0.1/student/:student-id/obstacles"
    [student-id :<< as-int :as {:keys [user db language]}]
    (if (has-student-access? db user student-id :read)
      (response (d-obstacles/get-by-student-id db language student-id))
      unauthorized)))
