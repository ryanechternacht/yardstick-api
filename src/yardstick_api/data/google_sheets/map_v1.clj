(ns yardstick-api.data.google-sheets.map-v1
  (:require [yardstick-api.db :as db]
            [honey.sql.helpers :as h]))

;; ignore paging for now

;; periods - [{:year :period-id}]
;; metrics - [{:column}]
(defn load-map-v1 [pg-db school-id metrics periods]
  (let [cols (concat [:school_assessment_instance.academic_year_id
                      :school_assessment_instance.assessment_period_id
                      :school_assessment_instance.school_id]
                     [:assessment_map_v1.studentfirstname
                      :assessment_map_v1.studentlastname
                      :assessment_map_v1.studentid
                      :assessment_map_v1.student_stateid]
                     (->> metrics (map :column) (map keyword)))
        years (->> periods (map :year) (distinct))
        periods (->> periods (map :period-id) distinct)]
    (-> (apply h/select cols)
        (h/from :assessment_map_v1)
        (h/join :school_assessment_instance
                [:=
                 :assessment_map_v1.school_assessment_instance_id
                 :school_assessment_instance.id])
        (h/where [:and
                  [:in :school_assessment_instance.academic_year_id years]
                  [:in :school_assessment_instance.assessment_period_id periods]
                  [:= :school_assessment_instance.school_id school-id]])
        (db/->execute pg-db))))
