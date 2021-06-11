(ns yardstick-api.middlewares.user
  (:require [yardstick-api.data.users :as d-users]))

(defn- wrap-user-impl [handler {:keys [session db] :as request}]
  (let [user (d-users/get-user-by-id db (:user-id session))]
    (handler (assoc request :user user))))

; This form has the advantage that changes to wrap-debug-impl are
; automatically reflected in the handler (due to the lookup in `wrap-db`)
(defn wrap-user [h] (partial #'wrap-user-impl h))
