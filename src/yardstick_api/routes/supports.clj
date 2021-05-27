(ns yardstick-api.routes.supports
  (:require [compojure.core :refer [GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.supports :as d-supports]
            [yardstick-api.routes.helpers.auth :refer [unauthorized has-student-access?]]))

(def GET-supports
  (GET "/v0.1/student/:student-id/supports"
    [student-id :<< as-int :as {db :db user :user}]
    (if (has-student-access? db user student-id :read)
      (response (d-supports/get-by-student-id db student-id))
      unauthorized)))
