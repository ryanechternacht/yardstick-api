(ns yardstick-api.routes.student
  (:require [compojure.core :refer [GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.student :as d-student]))

(def GET-student 
  (GET "/v0.1/student/:student-id" [student-id :<< as-int :as {db :db}]
    (response (d-student/get-student-by-id db student-id))))

;; TODO shouldn't just return 1, but return all of a user's students
(def GET-students
  (GET "/v0.1/students" [:as {db :db}]
    (response [(d-student/get-student-by-id db 1)])))