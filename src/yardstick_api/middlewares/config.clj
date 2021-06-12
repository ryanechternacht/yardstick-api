(ns yardstick-api.middlewares.config)

;; TODO probaby use component or mount for this
;; TODO this should be injected
(def ^:private config {:auth0 {:base-url "dev-zimmr.us.auth0.com/"
                               :client-id "26th5lB0f97TdLXdCFPSYV0kqI9FXh5z"
                               :client-secret "65JhapG9xp4cfStFSj6jKZggvOFg_VdyIRKLClQ8BeG8u_fLaRY5vbcCfQA73vVj"
                               :redirect-url "http://localhost:3001/v0.1/auth0/callback"}
                       :front-end {:base-url "http://localhost:4000"}})

; This form has the advantage that changes to wrap-debug-impl are
; automatically reflected in the handler (due to the lookup in `wrap-db`)
(defn wrap-config-impl [handler request]
  (handler (assoc request :config config)))

(defn wrap-config [h] (partial #'wrap-config-impl h))
