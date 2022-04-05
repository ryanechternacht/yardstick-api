(ns yardstick-api.data.google-sheets.api
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [yardstick-api.db :as db]
            [clojure.string :as s]
            [honey.sql.helpers :as h]))

(defn determine-col-letter [num]
  ;; TODO this won't support > 26
  (char (+ (dec num) (int \A))))

;; cells = Sheet1!A1:H6
;; body {:range Sheet1!A1:H6
;;       :majorDimension "ROWS"
;;       :values [[...]] }
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
