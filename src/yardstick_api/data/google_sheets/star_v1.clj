(ns yardstick-api.data.google-sheets.star-v1
  (:require [yardstick-api.db :as db]
            [honey.sql.helpers :as h]))

;; TODO don't call it state student id, but just student id 2
;; periods - [{:year :period-id}]
;; metrics - [{:column}]
(defn load-star-v1 [pg-db school-id metrics periods]
  (let [cols (concat [:school_assessment_instance.academic_year_id
                      :school_assessment_instance.assessment_period_id
                      :school_assessment_instance.school_id]
                     [:assessment_star_v1.student_first_name
                      :assessment_star_v1.student_last_name
                      :assessment_star_v1.studentid
                      :assessment_star_v1.studentid2]
                     (->> metrics (map :column) (map keyword)))
        years (->> periods (map :year) (distinct))
        periods (->> periods (map :period-id) distinct)]
    (-> (apply h/select cols)
        (h/from :assessment_star_v1)
        (h/join :school_assessment_instance
                [:=
                 :assessment_star_v1.school_assessment_instance_id
                 :school_assessment_instance.id])
        (h/where [:and
                  [:in :school_assessment_instance.academic_year_id years]
                  [:in :school_assessment_instance.assessment_period_id periods]
                  [:= :school_assessment_instance.school_id school-id]])
        (h/where [:or
                  [:<> :assessment_star_v1.studentid nil]
                  [:<> :assessment_star_v1.studentid2 nil]])
        (h/order-by [:assessment_star_v1.assessment_date :asc])
        (db/->execute pg-db))))
