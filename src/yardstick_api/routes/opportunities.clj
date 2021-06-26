(ns yardstick-api.routes.opportunities
  (:require [compojure.core :refer [GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.opportunities :as d-opportunities]
            [yardstick-api.routes.helpers.auth :refer [unauthorized has-student-access?]]))

(def GET-opportunities
  (GET "/v0.1/student/:student-id/opportunities"
    [student-id :<< as-int :as {:keys [db user language]}]
    (if (has-student-access? db user student-id :read)
      (response (d-opportunities/get-by-student-id db language student-id))
      unauthorized)))
