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
(defn- render-students [db ids]
  (->> (merge-where base-student-query
                    [:in :student.id ids])
       (db/execute db)
      ; TODO lang should come from the route
       (lang/render-language db "es")
       (map row->obj)))

(defn get-students-by-id [db student-ids]
  (render-students db student-ids))

(defn get-student-by-id [db student-id]
  (first (get-students-by-id db [student-id])))
