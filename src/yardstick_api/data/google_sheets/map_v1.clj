(ns yardstick-api.data.google-sheets.map-v1
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [yardstick-api.db :as db]
            [honey.sql.helpers :as h]))

;; ignore paging for now

;; periods - [{:year :period-id}]
;; metrics - [{:column}]
(defn load-map-v1 [pg-db school-id metrics periods]
  (let [cols (concat [:school_assessment_instance.academic_year_id
                      :school_assessment_instance.assessment_period_id
                      :school_assessment_instance.school_id]
                     [:assessment_map_v1.studentfirstname
                      :assessment_map_v1.studentlastname
                      :assessment_map_v1.studentid
                      :assessment_map_v1.student_stateid]
                     (->> metrics (map :column) (map keyword)))
        years (->> periods (map :year) (distinct))
        periods (->> periods (map :period-id) distinct)]
    (-> (apply h/select cols)
        (h/from :assessment_map_v1)
        (h/join :school_assessment_instance
                [:=
                 :assessment_map_v1.school_assessment_instance_id
                 :school_assessment_instance.id])
        (h/where [:and
                  [:in :school_assessment_instance.academic_year_id years]
                  [:in :school_assessment_instance.assessment_period_id periods]
                  [:= :school_assessment_instance.school_id school-id]])
        ;; TODO remove this filter
        (h/where [:= :assessment_map_v1.studentid "abc123"])
        (db/->execute pg-db))))

(defn add-existing-student [{:keys [data] :as result} index row]
  (println "add existing")
  result)

(defn- build-student-data [{:keys [studentid state_studentid studentfirstname studentlastname]}]
  ;; TODO these aren't the right keys
  {:sid studentid
   :ssid state_studentid
   :first studentfirstname
   :last studentlastname})

(defn add-new-student [{:keys [data student-id-lookup state-id-lookup] :as result}
                       {:keys [studentid state_studentid] :as row}]
  (println "add new" studentid state_studentid)
  ;; TODO this should do some kind of "is not null or empty" check on
  ;; both studentid and state_studentid
  ;; TODO this shouldn't add if we have neither a studentid or a state_studentid
  (let [index (count data)]
    (cond-> result
      studentid (assoc-in [:student-id-lookup studentid] index)
      state_studentid (assoc-in [:state-id-lookup state_studentid] index)
      :always (update-in [:data] conj (build-student-data row)))))

;; results {:data [{[year period-id :table.column] metric}]
;;          :student-id-lookup {student-id row-num}
;;          :state--id-lookup {state-student-id row-num}}
;; rows [{:academic_year_id :assessment_period_id :school_id 
;;        :studentfirstname :studentlastname 
;;        :studentid :student_stateid
;;        ... (other columns)}]
(defn add-map-v1-data [results rows]
  (reduce (fn [{:keys [data student-id-lookup state-id-lookup] :as result}
               {:keys [studentid student_stateid] :as row}]
            (let [existing-index (or (student-id-lookup studentid)
                                     (state-id-lookup student_stateid))]
              (if existing-index
                (add-existing-student result existing-index row)
                (add-new-student result row))))
          results
          rows))

(comment
  (let [pg-db {:dbtype "postgresql"
               :dbname "yardstick"
               :host "127.0.0.1"
               :user "ryan"
               :password nil
               :ssl false}
        metrics [{:display "RIT Score"
                  :column "assessment_map_v1.testritscore"}
                 {:display "Test Duration"
                  :column "assessment_map_v1.testdurationminutes"}
                 {:display "Rapid Guessing %"
                  :column "assessment_map_v1.rapidguessingpercentage"}]
        periods [{:year 2020
                  :period-id 1}
                 {:year 2020
                  :period-id 2}
                 {:year 2020
                  :period-id 3}
                 {:year 2021
                  :period-id 1}
                 {:year 2021
                  :period-id 2}
                 {:year 2021
                  :period-id 3}]]
    (add-map-v1-data {:data []
                      :student-id-lookup {"a" 1}
                      :state-id-lookup {"b" 2}}
                     (load-map-v1 pg-db 1 metrics periods)))
  ;;
  )