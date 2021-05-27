(ns yardstick-api.routes.helpers.auth)

(def unauthorized
  "Returns a 401 'Unauthorized' response."
  {:status 401
   :headers {}
   :body nil})

(defn has-student-access? [db user student-id permission]
  ; TODO this isn't fully implemented
  ; it currently just checks if you have a 
  user)
