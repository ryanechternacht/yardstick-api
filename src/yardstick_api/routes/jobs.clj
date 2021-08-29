(ns yardstick-api.routes.jobs
  (:require [compojure.core :refer [POST]]))

(def POST-assessment-upload
  (POST "/v0.1/admin/assessment/upload" [file]
    (let [txt (slurp (:tempfile file))]
      txt)))
