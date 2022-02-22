(ns yardstick-api.data.google-sheets
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(def access-token "ya29.A0ARrdaM82hpIwJXdigx6TrlNQXrvdlx_B1WIAQu6L67Y3vhYMT40XpoHlNI9WbXM_W2eKkTHqhBhDVCuyXRgGqO0iEiENaiHqAcSC4BxuRFf6EvJiX83SVp6n3JKxfXrIPb7dFhlhxo2dlOK0pt5UPwBd2WGM")
(def sheet-id "1wu_WRRqBd9sdV8um8VKXLV1XiqS50Xp6uUc1SjUbshI")

(defn get-cells [sheet-id cells]
  (http/get (format "https://sheets.googleapis.com/v4/spreadsheets/%s/values/%s"
                    sheet-id cells)
            {:headers {:Authorization (str "Bearer " access-token)
                       :accept :json}
             :as :json}))

(defn put-cells [sheet-id cells body]
  (http/put (format "https://sheets.googleapis.com/v4/spreadsheets/%s/values/%s"
                    sheet-id cells)
            {:headers {:Authorization (str "Bearer " access-token)
                       :accept :json}
             :query-params {:valueInputOption "USER_ENTERED"}
             :as :json
             :body (json/encode body)}))

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
  ;
  )