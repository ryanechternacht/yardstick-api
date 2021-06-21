(ns yardstick-api.middlewares.language)


; This form has the advantage that changes to wrap-debug-impl are
; automatically reflected in the handler (due to the lookup in `wrap-language`)
(defn- wrap-language-impl [handler {{language :language} :user :as request}]
  ; Could a query param override this?
  (handler (assoc request :language language)))

(defn wrap-language [h] (partial #'wrap-language-impl h))
