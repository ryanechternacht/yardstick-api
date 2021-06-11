(ns yardstick-api.routes.helpers.auth
  (:require [honeysql.helpers :refer [select from merge-join merge-where]]
            [yardstick-api.db :as db]))

(def unauthorized
  "Returns a 401 'Unauthorized' response."
  {:status 401
   :headers {}
   :body nil})

(defn has-student-access?
  "Ensures the user has access to see the student's data. If a grant exists,
   the user id is returned; if not, nil is returned"
  ;; TODO perhaps we should put all grants in the user object?
  [db user student-id permission]
  (->> (-> (select :yardstick_user.id)
           (from :yardstick_user)
           (merge-join :yardstick_grant [:= :yardstick_user.id :yardstick_grant.user_id])
           (merge-join :student [:= :yardstick_grant.target_id :student.id])
           (merge-where [:= :yardstick_user.id (:id user)])
           (merge-where [:= :yardstick_grant.target_type "student"])
           (merge-where [:= :yardstick_grant.permission (name permission)])
           (merge-where [:= :student.id student-id]))
       (db/execute db)
       first))