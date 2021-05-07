(ns yardstick-api.data.language
  (:require [clojure.walk :as w]
            [honeysql.helpers :refer [select from merge-join merge-where]]
            [yardstick-api.db :as db]))

(def ^:private keyword-suffix "_lang")

(defn should-be-replaced? [k]
  (-> k name (clojure.string/ends-with? keyword-suffix)))

(defn get-keys [f obj]
  (reduce (fn [acc [k v]]
            (cond
              (map? v) (into acc (get-keys f v))
              (f k) (conj acc v)
              :else acc))
          []
          obj))

(defn get-langs [db lang fields]
  (let [lang_keyword (keyword (str "lang_" lang))]
    (->> (-> (select :id lang_keyword)
             (from :language_lookup)
             (merge-where [:in :id fields]))
         (db/execute db)
         (map (fn [row] [(:id row) (lang_keyword row)]))
         (into {}))))

(defn- remove-keyword-suffix [k]
  (if (clojure.string/ends-with? (name k) keyword-suffix)
    (->> k
         name
         (drop-last (count keyword-suffix))
         (apply str)
         keyword)
    k))

(defn replace-fields [obj replacements]
  (w/walk (fn ([[k v]]
               (cond
                 (map? v) (replace-fields v replacements)
                 (replacements v) [k (replacements v)]
                 :else [k v])))
          identity
          obj))

; TODO make this handle arrays too
(defn render-language [db lang obj]
  (println obj)
  (->> obj
       (get-keys should-be-replaced?)
       (get-langs db lang)
       (replace-fields obj)))
