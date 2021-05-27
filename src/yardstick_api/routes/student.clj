(ns yardstick-api.routes.student
  (:require [compojure.core :refer [GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.student :as d-student]
            [yardstick-api.routes.helpers.auth :refer [unauthorized has-student-access?]]))

(def GET-student 
  (GET "/v0.1/student/:student-id" 
    [student-id :<< as-int :as {db :db user :user}]
    (if (has-student-access? db user student-id :read)
      (response (d-student/get-student-by-id db student-id))
      unauthorized)))

;; TODO shouldn't just return 1, but return all of a user's students
(def GET-students
  (GET "/v0.1/students" [:as {db :db user :user}]
    (if user
      (response [(d-student/get-student-by-id db 1)])
      unauthorized)))
