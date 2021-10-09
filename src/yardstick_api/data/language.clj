(ns yardstick-api.data.language
  (:require [clojure.walk :as w]
            [honey.sql.helpers :refer [select from where]]
            [yardstick-api.db :as db]))

(def ^:private keyword-suffix "_lang")

(defn- should-be-replaced? [k]
  (-> k name (clojure.string/ends-with? keyword-suffix)))

(defn- get-keys
  "gets all keys from maps contained in obj. obj must be a map
   or a collection of maps"
  [f obj]
  (reduce (fn [acc node]
            (cond
              (map? node)
              (into acc (get-keys f node))
              (or (seq? node) (vector? node))
              (let [[k v] node]
                (if (keyword? k)
                  ; if 1st item is keywor, treat like a kvp
                  (cond
                    (map? v)
                    (into acc (get-keys f v))
                    (or (seq? v) (vector? v))
                    (into acc (get-keys f node))
                    (f k)
                    (conj acc v)
                    :else acc)
                  ; else treat it as a seq and step through each
                  (into acc (get-keys f node))))
              :else acc))
          #{}
          obj))

(defn- get-langs [db lang fields]
  (let [lang_keyword (keyword (str "lang_" lang))]
    (->> (-> (select :id lang_keyword)
             (from :language_lookup)
             (where [:in :id fields]))
         (db/execute db)
         (map (fn [row] [(:id row) (lang_keyword row)]))
         (into {}))))

(defn- replace-fields
  "replaces all values from maps contained in obj with the values provided
   in the replacements map. obj must be a map or a collection of maps"
  [obj replacements]
  (w/walk (fn ([node]
               (cond
                 (map? node)
                 (replace-fields node replacements)
                 (or (seq? node) (vector? node))
                 (let [[k v] node]
                   (if (keyword? k)
                     ; if 1st item is keyword, treat like a kvp
                     (cond
                       (map? v) (replace-fields v replacements)
                       (or (seq? v) (vector? v)) (map #(replace-fields % replacements) v)
                       (replacements v) [k (replacements v)]
                       :else node)
                     ; else treat is as a seq and step through each
                     (map #(replace-fields % replacements) node))))))
          identity
          obj))

(defn render-language [db lang obj]
  (->> obj
       (get-keys should-be-replaced?)
       (get-langs db lang)
       (replace-fields obj)))
