(ns yardstick-api.data.users
  (:require [honeysql.helpers :refer [select from merge-where]]
            [yardstick-api.db :as db]))

(defn validate-login
  "Returns the user id if the login was valid. Nil otherwise"
  [db username password_plaintext]
  (->> (-> (select :id)
           (from :yardstick_user)
           (merge-where [:and
                         [:= :username username]
                         [:= :password_plaintext password_plaintext]]))
       (db/execute db)
       (map :id)
       first))

(defn get-user
  [db user-id]
  (->> (-> (select :id :username :first_name :last_name)
           (from :yardstick_user)
           (merge-where [:= :id user-id]))
       (db/execute db)
       first))
