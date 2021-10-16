(ns yardstick-api.routes.supports
  (:require [compojure.core :refer [GET]]
            [compojure.coercions :refer [as-int]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.supports :as d-supports]
            [yardstick-api.routes.helpers.auth :as auth]))

(def GET-supports
  (GET "/v0.1/student/:student-id/supports"
    [student-id :<< as-int :as {:keys [db user language]}]
    (if (auth/has-student-access? db user student-id :read)
      (response (d-supports/get-by-student-id db language student-id))
      auth/unauthorized-response)))
