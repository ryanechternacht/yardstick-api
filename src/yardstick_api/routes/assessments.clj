(ns yardstick-api.routes.assessments
  (:require [compojure.core :refer [GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.assessments :as d-ass]
            [yardstick-api.routes.helpers.auth :as auth]))

(def GET-assessment-overviews-by-student
  (GET "/v0.1/student/:student-id/assessments"
    [student-id :<< as-int :as {:keys [db user]}]
    (if (auth/has-student-access? db user student-id :read)
      (response (d-ass/get-all-by-student-id db student-id))
      auth/unauthorized-response)))


(def GET-assessment-results-by-student-and-assessment
  (GET "/v0.1/student/:student-id/assessment/:assessment-id"
    [student-id :<< as-int assessment-id :<< as-int :as {:keys [db user]}]
    (if (auth/has-student-access? db user student-id :read)
      (response (d-ass/get-results-by-assessment db assessment-id student-id))
      auth/unauthorized-response)))

(def GET-assessment-explanations-assessment
  (GET "/v0.1/assessment/:assessment-id/explanation"
    [assessment-id :<< as-int :as {:keys [db]}]
    (response (d-ass/get-explanations-by-assessment db assessment-id))))
