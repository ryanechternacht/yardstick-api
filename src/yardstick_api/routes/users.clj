(ns yardstick-api.routes.users
  (:require [compojure.core :refer [GET]]
            [ring.util.response :refer [response redirect]]
            [yardstick-api.apis.auth0 :as auth0]
            [yardstick-api.data.users :as d-users]))

(def fail-401
  {:status 401
   :headers {}})

(defn set-session [response user-id]
  (assoc response :session {:user-id user-id
                            :recreate true}))

(def GET-me
  (GET "/v0.1/users/me" [:as {db :db {user-id :user-id} :session}]
    (let [user (d-users/get-user-by-id db user-id)]
      (if user
        (response user)
        fail-401))))

(def GET-auth0-callback
  (GET "/v0.1/auth0/callback" [code :as {{auth0-config :auth0} :config db :db}]
    (let [auth0-user (auth0/get-auth0-user auth0-config code)
          user (d-users/get-or-create-user db auth0-user)]
      (set-session (redirect "http://localhost:4000") (:id user)))))

(def GET-login
  (GET "/v0.1/login" [:as {{auth0-config :auth0} :config}]
    (redirect (auth0/get-auth0-login-page auth0-config))))
    ; (redirect "https://dev-zimmr.us.auth0.com/authorize?client_id=26th5lB0f97TdLXdCFPSYV0kqI9FXh5z&response_type=code&connection=google-oauth2&redirect_uri=http://localhost:3001/v0.1/auth0/callback&state=asdfasdfasdf&scope=profile email openid")))
                 https://dev-zimmr.us.auth0.com/authorize/?client_id=26th5lB0f97TdLXdCFPSYV0kqI9FXh5z&connection=google-oauth2&redirect_uri=http%253A%252F%252Flocalhost%253A3001%252Fv0.1%252Fauth0%252Fcallback&response_type=code&scope=profile%2520email%2520openid