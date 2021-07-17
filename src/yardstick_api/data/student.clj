(ns yardstick-api.data.student
  (:require [honeysql.helpers :refer [select from merge-join merge-where]]
            [yardstick-api.db :as db]
            [yardstick-api.data.language :as lang]))

(def ^:private base-student-query
  (-> (select :student.id :student.first_name :student.last_name :grade.ordinal
              :grade.cardinal :pronouns.nominative_lang :pronouns.nominative_upper_lang
              :pronouns.possessive_lang :pronouns.possessive_upper_lang
              :pronouns.accusative_lang :pronouns.accusative_upper_lang)
      (from :student)
      (merge-join :grade [:= :student.grade_id :grade.id])
      (merge-join :pronouns [:= :student.pronouns_id :pronouns.id])))

(defn- row->obj [{:keys [id first_name last_name ordinal cardinal
                         nominative_lang nominative_upper_lang accusative_lang
                         accusative_upper_lang possessive_lang possessive_upper_lang]}]
  {:id id
   :name {:first first_name
          :last last_name
            ; TODO this doesn't fit the internationalization approach
          :full (str first_name " " last_name)
            ; TODO this doesn't fit the internationalization approach
          :possessive (str first_name "'s")}
   :grade {:cardinal cardinal
           :ordinal ordinal}
   :pronouns {:nominative nominative_lang
              :nominativeUpper nominative_upper_lang
              :accusative accusative_lang
              :accusativeUpper accusative_upper_lang
              :possessive possessive_lang
              :possessiveUpper possessive_upper_lang}})

; TODO not sure this is quite what I want...
(defn- render-students [db lang ids]
  (->> (merge-where base-student-query
                    [:in :student.id ids])
       (db/execute db)
       (lang/render-language db lang)
       (map row->obj)))

(defn get-students-by-id [db lang student-ids]
  (render-students db lang student-ids))

(defn get-student-by-id [db lang student-id]
  (first (get-students-by-id db lang [student-id])))

(defn- get-my-student-ids [db user-id]
  (->> (-> (select :target_id)
           (from :yardstick_grant)
           (merge-where [:= :user_id user-id]
                        [:= :target_type "student"]
                        [:= :permission "read"]))
       (db/execute db)
       (map :target_id)))

(defn get-my-students [db lang user-id]
  (render-students db lang (get-my-student-ids db user-id)))
