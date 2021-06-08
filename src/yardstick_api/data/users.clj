(ns yardstick-api.data.users
  (:require [honeysql.helpers :refer [select from merge-where]]
            [yardstick-api.db :as db]))

(defn get-user-by-id
  [db user-id]
  (->> (-> (select :id :first_name :last_name)
           (from :yardstick_user)
           (merge-where [:= :id user-id]))
       (db/execute db)
       first))

(defn get-user-by-email
  [db email]
  (->> (-> (select :id :first_name :last_name)
           (from :yardstick_user)
           (merge-where [:= :email email]))
       (db/execute db)
       first))
