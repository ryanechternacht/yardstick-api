(ns yardstick-api.routes
  (:require [compojure.core :refer [GET]]
            [ring.util.response :refer [response]]))

(def GET-students
  (GET "/v0.1/students" []
    (response [{:name {:first "Darryl"
                       :last "Hurt"
                       :full "Darryl Hurt"
                       :possessive "Darryl's"}
                :pronouns {:nominative "`he"
                           :nominativeUpper "He"
                           :possessive "his"
                           :possessiveUpper "His"
                           :accusative "him"
                           :accusativeUpper "Him"}
                :grade {:ordinal "8th"
                        :cardinal 8}
                :id 1}])))

; TODO this should be 
(def GET-settings)