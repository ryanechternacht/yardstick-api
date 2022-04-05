(ns yardstick-api.data.google-sheets.shared
  (:require [clojure.string :as s]
            [yardstick-api.data.google-sheets.shared :as shared]))

;; TODO rename this

(defn trim-table-name [kw]
  (-> kw
      name
      (s/split #"\.")
      second
      keyword))

(defn- build-student-data [{:keys [studentid state_studentid studentfirstname studentlastname]}]
  ;; TODO these aren't the right keys
  {:sid studentid
   :ssid state_studentid
   :first studentfirstname
   :last studentlastname})

;; metrics - [{:column}]
(defn- get-metrics-from-row [metrics {:keys [academic_year_id assessment_period_id] :as row}]
  (map (fn [{:keys [column]}]
         [[academic_year_id assessment_period_id (shared/trim-table-name column)] ((shared/trim-table-name column) row)])
       metrics))


;; result {:data [{[year period-id :table.column] metric}]
;;         :student-id-lookup {student-id row-num}
;;         :state--id-lookup {state-student-id row-num}}
(defn- add-existing-student [metrics result index row]
  (assoc-in result [:data index] (reduce (fn [m [k v]]
                                           (assoc m k v))
                                         (get-in result [:data index])
                                         (get-metrics-from-row metrics row))))


(defn- add-new-student [metrics
                        {:keys [data student-id-lookup state-id-lookup] :as result}
                        {:keys [studentid state_studentid] :as row}]
  ;; TODO this should do some kind of "is not null or empty" check on
  ;; both studentid and state_studentid
  ;; TODO this shouldn't add if we have neither a studentid or a state_studentid
  (let [index (count data)]
    (cond-> result
      studentid (assoc-in [:student-id-lookup studentid] index)
      state_studentid (assoc-in [:state-id-lookup state_studentid] index)
      :always (update :data conj (reduce (fn [m [k v]]
                                           (assoc m k v))
                                         (build-student-data row)
                                         (get-metrics-from-row metrics row))))))

;; starting-result {:data [{[year period-id :table.column] metric
;;                          :sid v}]
;;                  :student-id-lookup {student-id row-num}
;;                  :state--id-lookup {state-student-id row-num}}
;; rows [{:academic_year_id :assessment_period_id :school_id 
;;        :studentfirstname :studentlastname 
;;        :studentid :student_stateid
;;        ... (other columns)}]
(defn add-metric-data [metrics starting-result rows]
  (reduce (fn [{:keys [student-id-lookup state-id-lookup] :as acc-result}
               {:keys [studentid student_stateid] :as row}]
            (let [existing-index (or (student-id-lookup studentid)
                                     (state-id-lookup student_stateid))]
              (if existing-index
                (add-existing-student metrics acc-result existing-index row)
                (add-new-student metrics acc-result row))))
          starting-result
          rows))

(defn- make-header-row [metrics periods]
  (concat ["student id" "state student id" "first name" "last name"]
          (for [{:keys [year period-display]} periods
                {:keys [display]} metrics]
            (str year " " period-display " " display))))

(defn- make-cols-fn [metrics periods]
  (apply juxt (concat [:sid :ssid :first :last]
                      (for [{:keys [year period-id]} periods
                            {:keys [column]} metrics]
                        #(get % [year period-id (shared/trim-table-name column)])))))

;; metrics - [{:column}]
(defn make-table [metrics periods {:keys [data]}]
  (let [cols-fn (make-cols-fn metrics periods)]
    {:header (make-header-row metrics periods)
     :data-rows (reduce (fn [acc r]
                          (conj acc (cols-fn r)))
                        []
                        data)}))