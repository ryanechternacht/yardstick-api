(ns yardstick-api.data.users
  (:require [honeysql.helpers :refer
             [select from merge-where insert-into columns values delete-from]]
            [honeysql-postgres.helpers :refer [returning]]
            [yardstick-api.db :as db]))

(def ^:private base-select
  (-> (select :id :first_name :last_name :picture :language)
      (from :yardstick_user)))

(defn get-user-by-id
  [db user-id]
  (->> (-> base-select
           (merge-where [:= :id user-id]))
       (db/execute db)
       first))

(defn- get-user-by-email
  [db email]
  (->> (-> base-select
           (merge-where [:= :email email]))
       (db/execute db)
       first))

(defn- insert-user
  ;; TODO this will need to differentiate between different social logins;
  ;;      This iteration is built specifically for google
  [db {:keys [given_name family_name email picture]}]
  (->> (-> (insert-into :yardstick_user)
           (values [{:first_name given_name
                     :last_name family_name
                     :email email
                     :picture picture}])
           (returning :first_name :last_name :id :email))
       (db/execute db)
       first))

(defn- get-and-remove-pending-grants [db email]
  (->> (-> (delete-from :pending_grant)
           (merge-where [:= :email email])
           (returning :permission :target_type :target_id))
       (db/execute db)))

(defn- add-grants [db user grants]
  (let [formatted-grants (->> grants
                              (map (juxt :permission :target_type :target_id))
                              (map #(conj % (:id user))))]
    (->> (-> (insert-into :yardstick_grant)
             (columns :permission :target_type :target_id :user_id)
             (values formatted-grants))
         (db/execute db))))

(defn- create-user
  "Creates a new user. This will attach any `pending_grants` to the newly
   created user record"
  ;; TODO this should be wrapped in a transaction
  [db auth0-user]
  (let [user (insert-user db auth0-user)
        grants (get-and-remove-pending-grants db (:email user))]
    (when (not-empty grants) (add-grants db user grants))
    user))

(defn get-or-create-user
  "Gets or creates a user. This is typically used as part of the auth0
   callback flow, since when a SSO login happens, auth0 doesn't distinguish
   between a new or returning user"
  [db auth0-user]
  (if-let [existing-user (get-user-by-email db (:email auth0-user))]
    existing-user
    (create-user db auth0-user)))
