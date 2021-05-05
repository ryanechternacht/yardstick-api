(ns my-migrations
 (:require [migratus.core :as migratus]
           [yardstick-api.state :as state]))

(def schema-config {:store                :database
                    :migration-dir        "migrations/schema/"
                    :migration-table-name "migratus"
                    :db state/pg-db})

(def data-config {:store                :database
                  :migration-dir        "migrations/data/"
                  :migration-table-name "migratus"
                  :db state/pg-db})

(comment
  (migratus/create schema-config "example")
  (migratus/migrate schema-config)
  (migratus/rollback schema-config)
  ;
  )

(comment
  (migratus/create data-config "example")
  (migratus/migrate data-config)
  (migratus/rollback data-config)
  ;
  )