(ns my-migrations
 (:require [migratus.core :as migratus]
           [yardstick-api.middlewares.db :as db]))

(def schema-config {:store                :database
                    :migration-dir        "migrations/schema/"
                    :migration-table-name "migratus"
                    :db db/pg-db})

(def data-config {:store                :database
                  :migration-dir        "migrations/data/"
                  :migration-table-name "migratus"
                  :db db/pg-db})

(comment
  (migratus/create schema-config "schema-example")
  (migratus/migrate schema-config)
  (migratus/rollback schema-config)
  ;
  )

(comment
  (migratus/create data-config "data-example")
  (migratus/migrate data-config)
  (migratus/rollback data-config)
  ;
  )