(ns yardstick-api.routes
  (:require [compojure.core :refer [defroutes GET]]
            [ring.util.response :refer [response]]))

; This will need to evolve to include an ID of some form
(def GET-student
  (GET "/v0.1/student" []
    (response {:name {:first "Darryl"
                      :last "Hurt"
                      :full "Darryl Hurt"
                      :possessive "Darryl's"}
               :pronouns {:nominative "he"
                          :nominativeUpper "He"
                          :possessive "his"
                          :possessiveUpper "His"
                          :accusative "him"
                          :accusativeUpper "Him"}
               :grade {:ordinal "8th"
                       :cardinal 8}})))
