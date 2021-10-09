(ns yardstick-api.data.obstacles
  (:require [honey.sql.helpers :refer [select from join where order-by]]
            [yardstick-api.db :as db]
            [yardstick-api.data.language :as lang]))

(def ^:private base-obstacles-query
  (-> (select :obstacle.id :obstacle.type :obstacle.question_lang
              :obstacle.answer_lang :student_obstacle.ordering
              :student_obstacle.additional_fields)
      (from :obstacle)
      (join :student_obstacle [:= :obstacle.id :student_obstacle.obstacle_id])
      (order-by :student_obstacle.ordering)))

(defn- row->obj [{:keys [id question_lang answer_lang]}]
  {:id id
   :question question_lang
   :answer answer_lang})

(defn get-by-student-id [db lang student-id]
  (->> (where base-obstacles-query
              [:= :student_obstacle.student_id student-id])
       (db/execute db)
       (lang/render-language db lang)
       (map row->obj)))
