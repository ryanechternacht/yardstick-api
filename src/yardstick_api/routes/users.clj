(ns yardstick-api.routes.users
  (:require [clj-http.client :as http]
            [compojure.core :refer [GET POST]]
            [ring.util.response :refer [response redirect]]
            [yardstick-api.data.users :as d-users]))

(def fail-401
  {:status  401
   :headers {}})

(defn set-session-response [user-id]
  {:status 200
   :session {:user-id user-id
             :recreate true}})

(def POST-login
  (POST "/v0.1/users/login" [:as {{:keys [username password]} :body db :db}]
    (let [user-id (d-users/validate-login db username password)]
      (if user-id
        (set-session-response user-id)
        fail-401))))

(def GET-me
  (GET "/v0.1/users/me" [:as {db :db {user-id :user-id} :session}]
    (let [user (d-users/get-user db user-id)]
      (if user
        (response user)
        fail-401))))

(defn- get-token [code]
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

(defn- get-user [token]
  (-> (http/get (str "https://dev-zimmr.us.auth0.com/userinfo?access_token=" token)
                {:accept :json
                 :as :json})
      :body))

(def GET-auth0-callback
  (GET "/v0.1/auth0/callback" [code :as {body :body :as req}]
    (println "code")
    (println code)
    (let [token (get-token code)]
      (println "token")
      (println token)
      (let [user (get-user token)]
        (println "user")
        (println user)))
    (redirect "http://localhost:4000")))
