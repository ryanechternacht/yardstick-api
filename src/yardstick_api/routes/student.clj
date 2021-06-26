(ns yardstick-api.routes.student
  (:require [compojure.core :refer [GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.student :as d-student]
            [yardstick-api.routes.helpers.auth :refer [unauthorized has-student-access?]]))

(def GET-student
  (GET "/v0.1/student/:student-id" 
    [student-id :<< as-int :as {:keys [db user language]}]
    (if (has-student-access? db user student-id :read)
      (response (d-student/get-student-by-id db language student-id))
      unauthorized)))

(def GET-students
  (GET "/v0.1/students" [:as {:keys [db user language]}]
    (if user
      (response (d-student/get-my-students db language (:id user)))
      unauthorized)))
