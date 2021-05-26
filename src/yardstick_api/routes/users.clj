(ns yardstick-api.routes.users
  (:require [compojure.core :refer [GET POST]]
            [ring.util.response :refer [response redirect]]
            [yardstick-api.data.users :as d-users]))

(def fail-401
  {:status  401
   :headers {}})

(defn login-successful [user-id]
  (-> (redirect "http://localhost:4000/" :see-other)
      (assoc :session {:user-id user-id :recreate true})))

(def POST-login
  (POST "/v0.1/users/login" [:as {{:keys [username password]} :body db :db}]
    (println username password)
    (let [user-id (d-users/validate-login db username password)]
      (if user-id
        (login-successful user-id)
        fail-401))))

(def GET-me
  (GET "/v0.1/users/me" [:as {db :db {user-id :user-id} :session}]
    (let [user (d-users/get-user db user-id)]
      (if user
        (response user)
        fail-401))))
