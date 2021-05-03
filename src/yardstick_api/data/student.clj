(ns yardstick-api.data.student
  (:require [honeysql.core :as sql]
            [honeysql.helpers :refer [select from merge-join merge-where]]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

;; TODO this should be able to take multiple ids
(defn get-student-by-id [db student-id]
  (let [query (-> (select :student.id :student.first_name :student.last_name :grade.ordinal
                          :grade.cardinal :pronouns.nominative :pronouns.nominative_upper
                          :pronouns.possessive :pronouns.possessive_upper :pronouns.accusative
                          :pronouns.accusative_upper)
                  (from :student)
                  (merge-join :grade [:= :student.grade_id :grade.id])
                  (merge-join :pronouns [:= :student.pronouns_id :pronouns.id])
                  (merge-where [:= :student.id student-id]))
        {:keys [id first_name last_name ordinal cardinal nominative nominative_upper
                accusative accusative_upper possessive possessive_upper] :as result}
        (first (jdbc/execute! db (sql/format query)
                              {:builder-fn rs/as-unqualified-lower-maps}))]
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
                :possessiveUpper possessive_upper}}))
