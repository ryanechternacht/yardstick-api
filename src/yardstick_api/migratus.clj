(ns my-migrations
 (:require [migratus.core :as migratus]
           [yardstick-api.state :as state]))

(def config {:store                :database
             :migration-dir        "migrations/"
             :migration-table-name "migratus"
             :db state/pg-db})

(comment
  (migratus/create config "example")
  (migratus/migrate config)
  (migratus/rollback config)
  ;
  )