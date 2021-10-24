(ns yardstick-api.routes.jobs
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [yardstick-api.routes.helpers.coercions :as coerce]
            [compojure.core :refer [GET POST]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.jobs :as d-jobs]
            [yardstick-api.data.jobs.star :as star]
            [yardstick-api.routes.helpers.auth :as auth]))

(def ^:private handler-lookup
  {3 #'star/upload-star
   4 #'star/upload-star})

(def POST-assessment-upload
  (POST "/v0.1/admin/assessment/upload"
    [assessment_id :<< coerce/as-int-req year :<< coerce/as-int-req
     period_id :<< coerce/as-int-req school_id :<< coerce/as-int-req
     file :as {:keys [db user]}]
    (if-not (auth/has-school-access? db user school_id :admin)
      auth/unauthorized-response
    ;; TODO upload to s3 out here
      (let [instance-id (d-jobs/upsert-school-assessment-instance
                         db year period_id school_id)]
        (with-open [file-data (-> file
                                  :tempfile
                                  io/reader)]
          (let [handler (handler-lookup assessment_id)]
            (response (handler db instance-id (csv/read-csv file-data)))))))))