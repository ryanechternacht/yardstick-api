(ns yardstick-api.state
  (:require [honeysql.core :as hsql]
            [honeysql.helpers :refer [select from merge-where order-by limit]]
            [next.jdbc.sql :as sql]))

;; TODO probaby use component or mount for this
; (def db-conn (atom ))

(def pg-db {:dbtype "postgresql"
            :dbname "ryan"
            :host "127.0.0.1"
            :user "ryan"
            :password nil
            :ssl false})

(->> (-> (select :*)
         (from :test))
     (hsql/format)
     (#(sql/query pg-db %)))