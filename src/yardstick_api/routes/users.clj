(ns yardstick-api.routes.users
  (:require [clj-http.client :as http]
            [compojure.core :refer [GET POST]]
            [ring.util.response :refer [response redirect]]
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

(defn- get-auth0-token [code]
  ; TODO refactor these
  (-> (http/post "https://dev-zimmr.us.auth0.com/oauth/token"
                 {:form-params {:grant_type "authorization_code"
                                :client_id "26th5lB0f97TdLXdCFPSYV0kqI9FXh5z"
                                :client_secret "65JhapG9xp4cfStFSj6jKZggvOFg_VdyIRKLClQ8BeG8u_fLaRY5vbcCfQA73vVj"
                                :redirect_uri "http://localhost:3001/v0.1/auth0/callback"
                                :code code}
                  :content-type :json
                  :accept :json
                  :as :json})
      :body
      :access_token))

(defn- get-auth0-user [token]
  (-> (http/get (str "https://dev-zimmr.us.auth0.com/userinfo?access_token=" token)
                {:accept :json
                 :as :json})
      :body))

(def GET-auth0-callback
  (GET "/v0.1/auth0/callback" [code :as {db :db}]
    (let [token (get-auth0-token code)
          auth0-user (get-auth0-user token)
          user (d-users/get-or-create-user db auth0-user)]
      (set-session (redirect "http://localhost:4000") (:id user)))))

(def GET-login
  (GET "/v0.1/login" []
    (redirect "https://dev-zimmr.us.auth0.com/authorize?client_id=26th5lB0f97TdLXdCFPSYV0kqI9FXh5z&response_type=code&connection=google-oauth2&redirect_uri=http://localhost:3001/v0.1/auth0/callback&state=asdfasdfasdf&scope=profile email openid")))
