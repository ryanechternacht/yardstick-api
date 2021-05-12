(ns yardstick-api.data.opportunities
  (:require [honeysql.helpers :refer [select from merge-join merge-where]]
            [yardstick-api.db :as db]
            [yardstick-api.data.language :as lang]))

(def ^:private base-opportunities-query
  (-> (select :opportunity.id :opportunity.title_lang :opportunity.image :opportunity.description_lang)
      (from :opportunity)
      (merge-join :student_opportunity [:= :opportunity.id :student_opportunity.opportunity_id])))

(defn- row->obj [{:keys [id title_lang image description_lang]}]
  {:id id
   :title title_lang
   :image image
   :description description_lang})

(defn get-by-student-id [db student-id]
  (->> (merge-where base-opportunities-query
                    [:= :student_opportunity.student_id student-id])
       (db/execute db)
       (lang/render-language db db/global-language)
       (map row->obj)))
