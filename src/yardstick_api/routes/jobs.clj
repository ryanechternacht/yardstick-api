(ns yardstick-api.routes.jobs
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [compojure.core :refer [POST]]
            [yardstick-api.data.jobs :as d-jobs]))

(def ^:private handler-lookup
  {"STAR - Mathematics" #'d-jobs/upload-star})

(def POST-assessment-upload
  (POST "/v0.1/admin/assessment/upload" [assessment file]
    ;; TODO upload to s3 out here
    (with-open [file-data (-> file
                              :tempfile
                              io/reader)]
      (let [handler (handler-lookup assessment)]
        (handler (csv/read-csv file-data))))))