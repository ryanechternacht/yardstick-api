(ns yardstick-api.data.opportunities
  (:require [honeysql.helpers :refer [select from merge-join merge-where]]
            [yardstick-api.db :as db]))

(def ^:private base-opportunities-query
  (-> (select :opportunity.id :opportunity.title :opportunity.image :opportunity.description)
      (from :opportunity)
      (merge-join :student_opportunity [:= :opportunity.id :student_opportunity.opportunity_id])))

(defn get-by-student-id [db student-id]
  (->> (merge-where base-opportunities-query
                    [:= :student_opportunity.student_id student-id])
       (db/execute db)))
