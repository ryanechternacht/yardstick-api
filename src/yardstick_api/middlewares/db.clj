(ns yardstick-api.middlewares.db)

;; TODO probaby use component or mount for this
;; TODO probably setup a connection pool for this (c3p0?)
(def pg-db {:dbtype "postgresql"
            :dbname "yardstick"
            :host "127.0.0.1"
            :user "ryan"
            :password nil
            :ssl false})

; This form has the advantage that changes to wrap-debug-impl are
; automatically reflected in the handler (due to the lookup in `wrap-db`)
(defn- wrap-debug-impl [handler request]
  (handler (assoc request :db pg-db)))

(defn wrap-db [h] (partial #'wrap-debug-impl h))
