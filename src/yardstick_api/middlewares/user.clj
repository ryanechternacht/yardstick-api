(ns yardstick-api.middlewares.user
  (:require [yardstick-api.data.users :as d-users]))

(defn- wrap-user-impl [handler {:keys [session db] :as request}]
  (let [user (d-users/get-user db (:user-id session))]
    (handler (assoc request :user user))))

(defn wrap-user [h] (partial #'wrap-user-impl h))
