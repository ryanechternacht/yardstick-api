(ns yardstick-api.routes.helpers.auth
  (:require [honey.sql.helpers :refer [select from join where]]
            [yardstick-api.db :as db]))

(def unauthorized-response
  "Returns a 401 'Unauthorized' response."
  {:status 401
   :headers {}
   :body nil})

(defn has-student-access?
  "Ensures the user has access to see the student's data. If a grant 
   allowing access exists, the user id is returned; if not, nil is returned"
  ;; TODO perhaps we should put all grants in the user object?
  [db user student-id permission]
  (-> (select :yardstick_user.id)
      (from :yardstick_user)
      (join :yardstick_grant [:= :yardstick_user.id :yardstick_grant.user_id])
      (join :student [:= :student.id student-id])
      (where [:= :yardstick_user.id (:id user)]
             [:= :yardstick_grant.permission (name permission)]
             [:or
              [:and
               [:= :yardstick_grant.target_type "student"]
               [:= :student.id :yardstick_grant.target_id]]
              [:and
               [:= :yardstick_grant.target_type "school"]
               [:= :student.school_id :yardstick_grant.target_id]]])
      (db/->execute db)
      first))

(defn has-school-access?
  "Ensures the user has admin access for the school in question.  If a grant 
   allowing access exists, the user id is returned; if not, nil is returned"
  [db user school-id permission]
  (-> (select :yardstick_user.id)
      (from :yardstick_user)
      (join :yardstick_grant [:= :yardstick_user.id :yardstick_grant.user_id])
      (join :school [:= :school.id school-id])
      (where [:= :yardstick_user.id (:id user)]
             [:= :yardstick_grant.permission (name permission)]
             [:= :yardstick_grant.target_type "school"]
             [:= :school.id :yardstick_grant.target_id])
      (db/->execute db)
      first))
