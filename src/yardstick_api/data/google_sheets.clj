(ns yardstick-api.data.google-sheets
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [yardstick-api.db :as db]
            [honey.sql.helpers :as h]))

(def access-token "ya29.A0ARrdaM9yQOjKgaLQuba85K9qYcF056RGWO8_gHYcYSvOMtXqLoc1XZ9GbqjNliESR2Jj0AOHxBeHHbowsmU51Wu3vuIQ1U5PlFj2PruiOcCQbkWWyj_utQLPb8jCoqj2nuvNa2-bkMlNjswdWlZihljgk4py")
(def sheet-id "1wu_WRRqBd9sdV8um8VKXLV1XiqS50Xp6uUc1SjUbshI")
(def pg-db {:dbtype "postgresql"
            :dbname "yardstick"
            :host "127.0.0.1"
            :user "ryan"
            :password nil
            :ssl false})

(defn get-cells [sheet-id cells]
  (http/get (format "https://sheets.googleapis.com/v4/spreadsheets/%s/values/%s"
                    sheet-id cells)
            {:headers {:Authorization (str "Bearer " access-token)
                       :accept :json}
             :as :json}))

(defn put-cells [sheet-id cells body]
  (try
    (http/put (format "https://sheets.googleapis.com/v4/spreadsheets/%s/values/%s"
                      sheet-id cells)
              {:headers {:Authorization (str "Bearer " access-token)
                         :accept :json}
               :query-params {:valueInputOption "USER_ENTERED"}
               :as :json
               :body (json/encode body)})
    (catch Exception e (println e))))

(defn get-assessments [assessment-ids years]
  (-> (h/select :assessment.id
                [:assessment.name :assessment_name]
                :assessment.assessment_table
                :assessment_period.ordering
                [:assessment_period.name :assessment_period_name]
                :school_assessment_instance.academic_year_id)
      (h/from :assessment)
      (h/join :assessment_period [:= :assessment.id :assessment_period.assessment_id])
      (h/join :school_assessment_instance [:=
                                           :assessment_period.id
                                           :school_assessment_instance.assessment_period_id])
      (h/where [:and
                [:in :assessment.id assessment-ids]
                [:in :school_assessment_instance.academic_year_id years]])
      (h/order-by :assessment.id
                  :school_assessment_instance.academic_year_id
                  :assessment_period.ordering)
      (db/->execute pg-db)))

(defn convert-assessment-to-sheet-header [assessments]
  (let [cols (->> assessments
                  (map (fn [{:keys [assessment_period_name academic_year_id]}]
                         (str academic_year_id " " assessment_period_name))))]
    {:row-1 (concat (list (-> assessments first :assessment_name))
                    (repeat (dec (count cols)) ""))
     :row-2 cols}))

(defn convert-assessments-to-sheet-header [assessments]
  (->> assessments
       (map (fn [[_ v]] (convert-assessment-to-sheet-header v)))))

(def student-header {:row-1 ["Student" "" "" ""]
                     :row-2 ["Student ID" "Student State ID" "First Name" "Last Name"]})

(defn combine-cols [student-header assessment-cols]
  {:row-1 (into (:row-1 student-header) (mapcat identity (map :row-1 assessment-cols)))
   :row-2 (into (:row-2 student-header) (mapcat identity (map :row-2 assessment-cols)))})

(defn determine-col-letter [num]
  ;; TODO this won't support > 26
  (char (+ (dec num) (int \A))))

(defn get-header-rows [assessments]
  (let [assessment-cols (convert-assessments-to-sheet-header assessments)
        {:keys [row-1 row-2]} (combine-cols student-header assessment-cols)
        range (format "Sheet1!A1:%s2" (determine-col-letter (count row-1)))]
    {:url-cells range
     :body-cells {:range range
                  :majorDimension "ROWS"
                  :values [row-1 row-2]}}))

(defn get-assessment-records [school-id assessment-id years]
  ;; TODO this is only for map now
  (-> (h/select :assessment_map_v1.studentid
                :assessment_map_v1.student_stateid
                :assessment_map_v1.studentfirstname
                :assessment_map_v1.studentlastname
                :assessment_map_v1.testritscore
                :assessment_period.ordering
                :school_assessment_instance.academic_year_id
                :assessment_period.assessment_id)
      (h/from :assessment_period)
      (h/join :school_assessment_instance [:=
                                           :assessment_period.id
                                           :school_assessment_instance.assessment_period_id])
      (h/join :assessment_map_v1 [:=
                                  :school_assessment_instance.id
                                  :assessment_map_v1.school_assessment_instance_id])
      (h/where [:and
                [:= :assessment_period.assessment_id assessment-id]
                [:= :school_assessment_instance.school_id school-id]
                [:in :school_assessment_instance.academic_year_id years]])
      (h/order-by :school_assessment_instance.academic_year_id :assessment_period.ordering)
      (db/->execute pg-db)))

(defn build-up-student-results [students new-records]
  (reduce (fn [acc {:keys [studentid student_stateid studentfirstname studentlastname
                           testritscore ordering academic_year_id assessment_id]}]
            (cond-> acc
              (nil? (acc studentid)) (assoc studentid {:student-id studentid
                                                       :student-state-id student_stateid
                                                       :first-name studentfirstname
                                                       :last-name studentlastname
                                                       :assessments {}})
              :always (update-in [studentid :assessments assessment_id] conj {:score testritscore
                                                                              :ordering ordering
                                                                              :year academic_year_id})))
          students
          new-records))

(defn build-assessment-results-row [assessment-overviews {:keys [student-id student-state-id first-name last-name assessments]}]
  (let [row [student-id student-state-id first-name last-name]]
    (reduce (fn [acc {:keys [id ordering academic_year_id]}]
              (conj acc (some->> assessments
                                 (#(get % id))
                                 (filter #(and (= ordering (:ordering %))
                                               (= academic_year_id (:year %))))
                                 first
                                 :score)))
            row
            assessment-overviews)))

                ;;  {:id 2
                ;;   :assessment_name "NWEA MAP - ELA"
                ;;   :assessment_table "assessment_map_v1"
                ;;   :ordering 1
                ;;   :assessment_period_name "Fall"
                ;;   :academic_year_id 2021}
                  ;; {:student-id "113789"
                  ;;  :student-state-id ""
                  ;;  :first-name "SARAH"
                  ;;  :last-name "RIDDELL"
                  ;;  :assessments {1 ({:score 197, :ordering 1, :year 2021})}}

(defn get-result-rows [rows]
  (let [range (format "Sheet1!A3:%s%s"
                      (determine-col-letter (count (first rows)))
                      (+ 2 (count rows)))]
    {:url-cells range
     :body-cells {:range range
                  :majorDimension "ROWS"
                  :values rows}}))

(defn put-sheet [sheet-id school-id assessment-ids years]
  (let [assessments (get-assessments assessment-ids years)
        grouped-assessments (group-by :id assessments)
        {url-cells-header :url-cells body-cells-header :body-cells}
        (get-header-rows grouped-assessments)
        assessment-results-by-student
        (reduce (fn [acc a-id]
                  (build-up-student-results acc (get-assessment-records school-id a-id years)))
                {}
                (keys grouped-assessments))
        assessment-rows
        (map (fn [[_ v]] (build-assessment-results-row assessments v)) assessment-results-by-student)
        {url-cells-results :url-cells body-cells-results :body-cells}
        (get-result-rows assessment-rows)]
    (put-cells sheet-id url-cells-header body-cells-header)
    (put-cells sheet-id url-cells-results body-cells-results)))

(comment
  (get-cells sheet-id "Sheet1!A1:A1")
  (put-cells sheet-id "Sheet1!A1:D5"
             {:range "Sheet1!A1:D5"
              :majorDimension "ROWS"
              :values [["Item", "Cost", "Stocked", "Ship Date"]
                       ["Wheel", "$20.50", "4", "3/1/2016"]
                       ["Door", "$15", "2", "3/15/2016"]
                       ["Engine", "$100", "1", "3/20/2016"]
                       ["Totals", "=SUM(B2:B4)", "=SUM(C2:C4)", "=MAX(D2:D4)"]]})
  (get-assessments [1 2] [2020 2021])
  (convert-assessments-to-sheet-header (get-assessments [1 2] [2020 2021]))
  (get-header-rows (group-by :id (get-assessments [1 2] [2020 2021])))
  (let [{:keys [url-cells body-cells]} (get-header-rows (group-by :id (get-assessments [1 2] [2020 2021])))]
    (put-cells sheet-id url-cells body-cells))

  (build-up-student-results {"113162" {:student-id "113162"
                                       :student-state-id ""
                                       :first-name "SARAH"
                                       :last-name "RIDDELL"
                                       :assessments [{:score 14, :ordering 1, :year 2021}]}}
                            (get-assessment-records 1 1 [2020 2021]))

  (put-sheet sheet-id 1 [1 2] [2020 2021])
  ;
  )
