(ns yardstick-api.apis.auth0
  (:require [cemerick.url :refer [url]]
            [clj-http.client :as http]))

(defn- auth0-uri
  ([base-url path]
   (auth0-uri base-url path {}))
  ([base-url path params]
   (str (assoc (url "https:")
               :host base-url
               :path path
               :query params))))

(defn- get-auth0-redirect-url [redirect-url-base service]
  (condp = service
    ;; TODO handle via a path library?
    "google" (str redirect-url-base "/google")
    "facebook" (str redirect-url-base "/facebook")))

(defn- fetch-auth0-token [{:keys [base-url client-id client-secret redirect-url-base]} service code]
  (-> (http/post (auth0-uri base-url "oauth/token")
                 {:form-params {:grant_type "authorization_code"
                                :client_id client-id
                                :client_secret client-secret
                                :redirect_uri (get-auth0-redirect-url redirect-url-base service)
                                :code code}
                  :content-type :json
                  :accept :json
                  :as :json})
      :body
      :access_token))

(defn- fetch-auth0-user [{:keys [base-url]} token]
  (-> (http/get (auth0-uri base-url "userinfo" {:access_token token})
                {:accept :json
                 :as :json})
      :body))

(defn get-auth0-user [auth0-config service code]
  (->> code
       (fetch-auth0-token auth0-config service)
       (fetch-auth0-user auth0-config)))

(defn get-auth0-login-page [{:keys [base-url client-id redirect-url-base]} service]
  (auth0-uri base-url "authorize" {:client_id client-id
                                   :response_type "code"
                                   :connection "google-oauth2"
                                   :redirect_uri (get-auth0-redirect-url redirect-url-base service)
                                   :scope "profile email openid"}))
