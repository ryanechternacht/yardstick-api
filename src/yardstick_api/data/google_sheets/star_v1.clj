(ns yardstick-api.data.google-sheets.star-v1
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [yardstick-api.db :as db]
            [clojure.string :as s]
            [honey.sql.helpers :as h]))

;; TODO don't call it state student id, but just student id 2
;; TODO alias sid, sid2, first, last and it's largely shared code (unique is just the db pull)\

(def access-token "ya29.A0ARrdaM_m-2_Ewx-dAkL5qTD1hR96mgZC4Hx_BDRiOApUnESo7Vn1ggxxhhpxLWuo5t4tD4bmFkxjmFHW6s9lHNSmwyut36JJ-f3mrd61uCyaIrc2cOu3kUimCDCBAPI4CErwo_ftRZzmqcHGqbSXpxjkNG1x")
(def sheet-id "1wu_WRRqBd9sdV8um8VKXLV1XiqS50Xp6uUc1SjUbshI")
(def pg-db {:dbtype "postgresql"
            :dbname "yardstick"
            :host "127.0.0.1"
            :user "ryan"
            :password nil
            :ssl false})

(defn put-cells [access-token sheet-id cells body]
  (try
    (http/put (format "https://sheets.googleapis.com/v4/spreadsheets/%s/values/%s"
                      sheet-id cells)
              {:headers {:Authorization (str "Bearer " access-token)
                         :accept :json}
               :query-params {:valueInputOption "USER_ENTERED"}
               :as :json
               :body (json/encode body)})
    (catch Exception e (println e))))

(defn determine-col-letter [num]
  ;; TODO this won't support > 26
  (char (+ (dec num) (int \A))))

;; periods - [{:year :period-id}]
;; metrics - [{:column}]
(defn load-star-v1 [pg-db school-id metrics periods]
  (let [cols (concat [:school_assessment_instance.academic_year_id
                      :school_assessment_instance.assessment_period_id
                      :school_assessment_instance.school_id]
                     [:assessment_star_v1.student_first_name
                      :assessment_star_v1.student_last_name
                      :assessment_star_v1.studentid
                      :assessment_star_v1.studentid2]
                     (->> metrics (map :column) (map keyword)))
        years (->> periods (map :year) (distinct))
        periods (->> periods (map :period-id) distinct)]
    (-> (apply h/select cols)
        (h/from :assessment_star_v1)
        (h/join :school_assessment_instance
                [:=
                 :assessment_star_v1.school_assessment_instance_id
                 :school_assessment_instance.id])
        (h/where [:and
                  [:in :school_assessment_instance.academic_year_id years]
                  [:in :school_assessment_instance.assessment_period_id periods]
                  [:= :school_assessment_instance.school_id school-id]])
        (h/where [:or
                  [:<> :assessment_star_v1.studentid nil]
                  [:<> :assessment_star_v1.studentid2 nil]])
        (h/order-by [:assessment_star_v1.assessment_date :asc])
        (db/->execute pg-db))))

(defn- trim-table-name [kw]
  (-> kw
      name
      (s/split #"\.")
      second
      keyword))

;; metrics - [{:column}]
(defn get-metrics-from-row [metrics {:keys [academic_year_id assessment_period_id] :as row}]
  (map (fn [{:keys [column]}]
         [[academic_year_id assessment_period_id (trim-table-name column)] ((trim-table-name column) row)])
       metrics))

(defn- build-student-data [{:keys [studentid studentid2 student_first_name student_last_name]}]
  ;; TODO these aren't the right keys
  {:sid studentid
   :ssid studentid2
   :first student_first_name
   :last student_last_name})

;; result {:data [{[year period-id :table.column] metric}]
;;         :student-id-lookup {student-id row-num}
;;         :state--id-lookup {state-student-id row-num}}
(defn add-existing-student [metrics result index row]
  (assoc-in result [:data index] (reduce (fn [m [k v]]
                                           (assoc m k v))
                                         (get-in result [:data index])
                                         (get-metrics-from-row metrics row))))

(defn add-new-student [metrics
                       {:keys [data] :as result}
                       {:keys [studentid studentid2] :as row}]
  ;; TODO this should do some kind of "is not null or empty" check on
  ;; both studentid and studentid2
  ;; TODO this shouldn't add if we have neither a studentid or a studentid2
  (let [index (count data)]
    (cond-> result
      studentid (assoc-in [:student-id-lookup studentid] index)
      studentid2 (assoc-in [:state-id-lookup studentid2] index)
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
(defn add-star-v1-data [metrics starting-result rows]
  (reduce (fn [{:keys [student-id-lookup state-id-lookup] :as acc-result}
               {:keys [studentid student_stateid] :as row}]
            (let [existing-index (or (student-id-lookup studentid)
                                     (state-id-lookup student_stateid))]
              (if existing-index
                (add-existing-student metrics acc-result existing-index row)
                (add-new-student metrics acc-result row))))
          starting-result
          rows))

(defn make-header-row [metrics periods]
  (concat ["student id" "state student id" "first name" "last name"]
          (for [{:keys [year period-display]} periods
                {:keys [display]} metrics]
            (str year " " period-display " " display))))

(defn- make-cols-fn [metrics periods]
  (apply juxt (concat [:sid :ssid :first :last]
                      (for [{:keys [year period-id]} periods
                            {:keys [column]} metrics]
                        #(get % [year period-id (trim-table-name column)])))))

;; TODO this should probably exist elsewhere
;; metrics - [{:column}]
(defn make-table [metrics periods {:keys [data]}]
  (let [cols-fn (make-cols-fn metrics periods)]
    {:header (make-header-row metrics periods)
     :data-rows (reduce (fn [acc r]
                          (conj acc (cols-fn r)))
                        []
                        data)}))

(defn upload-table [access-token sheet-id {:keys [header data-rows]}]
  (let [range (format "Sheet1!A1:%s%s"
                      (determine-col-letter (count header))
                      (inc (count data-rows)))]
    (put-cells access-token
               sheet-id
               range
               {:range range
                :majorDimension "ROWS"
                :values (concat [header] data-rows)})))

(comment
  (let [metrics [{:display "Screening Cateogry"
                  :column "assessment_star_v1.screening_category"}
                 {:display "Scaled Score"
                  :column "assessment_star_v1.scaled_score"}
                 {:display "Percentile"
                  :column "assessment_star_v1.percentile_rank"}]
        periods [{:year 2021
                  :period-id 19
                  :period-display "January"}
                 {:year 2021
                  :period-id 20
                  :period-display "February"}
                 {:year 2021
                  :period-id 21
                  :period-display "March"}
                 {:year 2021
                  :period-id 22
                  :period-display "April"}]]
    (upload-table access-token
                  sheet-id
                  (make-table metrics periods
                              (add-star-v1-data metrics
                                                {:data []
                                                 :student-id-lookup {"a" -1}
                                                 :state-id-lookup {"b" -2}}
                                                (load-star-v1 pg-db 1 metrics periods)))))
  ;;
  )