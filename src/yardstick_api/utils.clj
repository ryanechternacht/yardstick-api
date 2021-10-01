(ns yardstick-api.utils)

;; is there a better way to do thi?
(defn parse-int
  "Tries to parse a string as an int. Returns nil if it can't."
  [s]
  (try
    (Integer/parseInt s)
    (catch Exception _
      nil)))

;; is there a better way to do thi?
(defn parse-double
  "Tries to parse a string as an double. Returns nil if it can't."
  [s]
  (try
    (Double/parseDouble s)
    (catch Exception _
      nil)))
