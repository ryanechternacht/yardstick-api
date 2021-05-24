(ns yardstick-api.routes.users
  (:require [compojure.core :refer [GET POST]]
            [ring.util.response :refer [response]]
            [yardstick-api.data.users :as d-users]))

(def fail-401
  {:status  401
   :headers {}})

(defn cookies-200 [value]
  {:status 200
   :cookies {:world value}})

(defn session-200 [user-id]
  {:status 200
   :session {:user-id user-id}})

(def POST-login
  (POST "/v0.1/users/login" [:as {{:keys [username password]} :body db :db cookies :cookies}]
    (println cookies)
    (let [user (d-users/validate-login db "ryan@echternacht.org" "123")]
      (if user
        (cookies-200 "1234")
        fail-401))))

(def GET-me
  (GET "/v0.1/users/me" [:as {db :db session :session}]
    (println "me")
    (println session)
    (session-200 1)))
