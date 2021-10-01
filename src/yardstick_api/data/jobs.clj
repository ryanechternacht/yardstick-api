(ns yardstick-api.data.jobs
  (:require [honeysql.helpers
             :refer [insert-into merge-where select from values columns]]
            [honeysql-postgres.helpers
             :refer [upsert on-conflict-constraint do-update-set returning]]
            [yardstick-api.db :as db]))

(defn upsert-school-assessment-instance
  "upserts the info necessary to get a school_assessment_instance
   and returns the associated id"
  [db academic_year period_id school_id]
  ;; TODO this form may not be optimal. see:
  ;; https://stackoverflow.com/questions/34708509/how-to-use-returning-with-on-conflict-in-postgresql
  (-> (insert-into :school_assessment_instance)
      (columns :academic_year_id :assessment_period_id :school_id)
      (values [[academic_year period_id school_id]])
      (upsert
       (-> (on-conflict-constraint :unique_year_period_school)
           (do-update-set :updated_at)))
      (returning :id)
      (db/->execute db)
      first
      :id))
