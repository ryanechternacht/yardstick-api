(ns yardstick-api.routes.jobs
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [compojure.coercions :as coerce]
            [compojure.core :refer [POST]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.jobs :as d-jobs]
            [yardstick-api.data.jobs.star :as star]
            [yardstick-api.routes.helpers.auth :as auth]))

(def ^:private handler-lookup
  {4 #'star/upload-star})

(def POST-assessment-upload
  (POST "/v0.1/admin/assessment/upload"
    [assessment_id :<< coerce/as-int year :<< coerce/as-int
     period_id :<< coerce/as-int school_id :<< coerce/as-int
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
