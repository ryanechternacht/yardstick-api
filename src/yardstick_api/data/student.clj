(ns yardstick-api.data.student
  (:require [honeysql.helpers :refer [select from merge-join merge-where]]
            [yardstick-api.db :as db]))

(def ^:private base-student-query
  (-> (select :student.id :student.first_name :student.last_name :grade.ordinal
              :grade.cardinal :pronouns.nominative :pronouns.nominative_upper
              :pronouns.possessive :pronouns.possessive_upper :pronouns.accusative
              :pronouns.accusative_upper)
      (from :student)
      (merge-join :grade [:= :student.grade_id :grade.id])
      (merge-join :pronouns [:= :student.pronouns_id :pronouns.id])))

(defn- row->obj [{:keys [id first_name last_name ordinal cardinal nominative nominative_upper
                         accusative accusative_upper possessive possessive_upper]}]
  {:id id
     :name {:first first_name
            :last last_name
            :full (str first_name " " last_name)
            :possessive (str first_name "'s")}
     :grade {:cardinal cardinal
             :ordinal ordinal}
     :pronouns {:nominative nominative
                :nominativeUpper nominative_upper
                :accusative accusative
                :accusativeUpper accusative_upper
                :possessive possessive
                :possessiveUpper possessive_upper}})

; TODO not sure this is quite what I want...
(defn- render-students [db ids]
  (->> (-> base-student-query
           (merge-where [:in :student.id ids]))
       (db/execute db)
       (map row->obj)))

(defn get-students-by-id [db student-ids]
  (render-students db student-ids))

(defn get-student-by-id [db student-id]
  (first (get-students-by-id db [student-id])))
