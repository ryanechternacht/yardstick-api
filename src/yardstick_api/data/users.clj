(ns yardstick-api.data.users
  (:require [honeysql.helpers :refer [select from merge-where]]
            [yardstick-api.db :as db]))

(defn validate-login
  "Returns the user object if the login was valid. Nil otherwise"
  [db username password_plaintext]
  (->> (-> (select :id :username)
           (from :users)
           (merge-where [:and
                         [:= :username username]
                         [:= :password_plaintext password_plaintext]]))
       (db/execute db)
       first))
