(ns yardstick-api.routes.assessments
  (:require [compojure.core :refer [GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.assessments :as d-ass]
            [yardstick-api.routes.helpers.auth :refer [unauthorized has-student-access?]]))

(def GET-assessment-overviews-by-student
  (GET "/v0.1/student/:student-id/assessments"
    [student-id :<< as-int :as {:keys [db user]}]
    (if (has-student-access? db user student-id :read)
      (response (d-ass/get-all-by-student-id db student-id))
      unauthorized)))
`