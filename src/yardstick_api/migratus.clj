(ns migratus
  (:require [migratus.core :as migratus]
            [yardstick-api.middlewares.config :as config]))

;; TODO how do I roll these out to AWS?
;; Ideally I'd port into an AWS repl. In reality I'll probably just
;; fire up a repl with config set to the right AWS environment?
(def db (:pg-db config/config))

(def schema-config {:store                :database
                    :migration-dir        "migrations/schema/"
                    :migration-table-name "migratus"
                    :db db})

(def data-config {:store                :database
                  :migration-dir        "migrations/data/"
                  :migration-table-name "migratus"
                  :db db})

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