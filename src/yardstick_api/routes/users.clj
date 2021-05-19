(ns yardstick-api.routes.users
  (:require [compojure.core :refer [POST]]
            [ring.util.response :refer [response ]]
            [yardstick-api.data.users :as d-users]))

(def fail-401
  {:status  401
   :headers {}})

(def POST-login
  (POST "/v0.1/login" [:as {{:keys [username password] :as body} :body db :db}]
    (println body)
    (println db)
    (let [user (d-users/validate-login db username password)]
      (if user
        (response user)
        fail-401))))
