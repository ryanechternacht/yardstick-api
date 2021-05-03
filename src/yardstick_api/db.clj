(ns yardstick-api.db
  (:require [honeysql.core :as sql]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

(defn execute [db query]
  (jdbc/execute! db (sql/format query)
                 {:builder-fn rs/as-unqualified-lower-maps}))