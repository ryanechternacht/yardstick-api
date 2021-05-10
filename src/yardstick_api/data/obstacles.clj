(ns yardstick-api.data.obstacles
  (:require [honeysql.helpers :refer [select from merge-join merge-where order-by]]
            [yardstick-api.db :as db]
            [yardstick-api.data.language :as lang]))

(def ^:private base-obstacles-query
  (-> (select :obstacle.id :obstacle.type :obstacle.question_lang
              :obstacle.answer_lang :student_obstacle.ordering
              :student_obstacle.additional_fields)
      (from :obstacle)
      (merge-join :student_obstacle [:= :obstacle.id :student_obstacle.obstacle_id])
      (order-by :student_obstacle.ordering)))

(defn- row->obj [{:keys [id question_lang answer_lang]}]
  {:id id
   :question question_lang
   :answer answer_lang})

(defn get-by-student-id [db student-id]
  (->> (merge-where base-obstacles-query
                    [:= :student_obstacle.student_id student-id])
       (db/execute db)
       (lang/render-language db "es")
       (map row->obj)))
