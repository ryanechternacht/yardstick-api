(ns yardstick-api.data.jobs
  (:require [honey.sql.helpers :as h]
            [yardstick-api.db :as db]))

(defn upsert-school-assessment-instance
  "upserts the info necessary to get a school_assessment_instance
   and returns the associated id"
  [db academic_year period_id school_id]
  ;; TODO this form may not be optimal. see:
  ;; https://stackoverflow.com/questions/34708509/how-to-use-returning-with-on-conflict-in-postgresql
  (-> (h/insert-into :school_assessment_instance)
      (h/columns :academic_year_id :assessment_period_id :school_id)
      (h/values [[academic_year period_id school_id]])
      h/on-conflict
      (h/on-constraint :school_assessment_instance_unique_year_period_school)
      (h/do-update-set :updated_at)
      (h/returning :id)
      (db/->execute db)
      first
      :id))
