(ns yardstick-api.data.google-sheets.core
  (:require [yardstick-api.data.google-sheets.api :as api]
            [yardstick-api.data.google-sheets.shared :as shared]
            [yardstick-api.data.google-sheets.map-v1 :as map-v1]
            [yardstick-api.data.google-sheets.star-v1 :as star-v1]))

;; TODO rename this

(def access-token "ya29.A0ARrdaM-7Wh_f31u0xvaU4EC2LWb6W6dI2dDULuqgoy4kUuh_BLHMxfG2Ndb6ekpTccHxgtt6EF5W-hlEOZU6LHOHR527vNZiP4XRsNtetuc9DMj1k52aWOEO-2e_8QJS5q5eBSjPmyPBNsRiIvHTNimZSX-0")
(def sheet-id "1wu_WRRqBd9sdV8um8VKXLV1XiqS50Xp6uUc1SjUbshI")
(def pg-db {:dbtype "postgresql"
            :dbname "yardstick"
            :host "127.0.0.1"
            :user "ryan"
            :password nil
            :ssl false})

(defn upload-table [access-token sheet-id {:keys [header data-rows]}]
  (let [range (format "Sheet1!A1:%s%s"
                      (api/determine-col-letter (count header))
                      (inc (count data-rows)))]
    (api/put-cells access-token
                   sheet-id
                   range
                   {:range range
                    :majorDimension "ROWS"
                    :values (concat [header] data-rows)})))

(comment
  (let [metrics [{:display "RIT Score"
                  :column "assessment_map_v1.testritscore"}
                 {:display "Test Duration"
                  :column "assessment_map_v1.testdurationminutes"}
                 {:display "Rapid Guessing %"
                  :column "assessment_map_v1.rapidguessingpercentage"}]
        periods [{:year 2020
                  :period-id 1
                  :period-display "Fall"}
                 {:year 2020
                  :period-id 2
                  :period-display "Winter"}
                 {:year 2020
                  :period-id 3
                  :period-display "Spring"}
                 {:year 2021
                  :period-id 1
                  :period-display "Fall"}
                 {:year 2021
                  :period-id 2
                  :period-display "Winter"}
                 {:year 2021
                  :period-id 3
                  :period-display "Spring"}]]
    (upload-table access-token
                  sheet-id
                  (shared/make-table metrics
                                     periods
                                     (shared/add-metric-data metrics
                                                             {:data []
                                                              :student-id-lookup {"a" -1}
                                                              :state-id-lookup {"b" -2}}
                                                             (map-v1/load-map-v1 pg-db 1 metrics periods)))))

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
                  (shared/make-table metrics periods
                                     (shared/add-metric-data metrics
                                                             {:data []
                                                              :student-id-lookup {"a" -1}
                                                              :state-id-lookup {"b" -2}}
                                                             (star-v1/load-star-v1 pg-db 1 metrics periods)))))
  ;;
  )
