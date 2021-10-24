(ns yardstick-api.routes.helpers.coercions
  (:require [compojure.coercions :as comp-coer]))

(defn as-int-req
  "Parse a string into an integer, or `nil` if the string cannot be parsed.
   If nil is returned, the route will 500. Calls to `compojure.coercions` `as-int`"
  [s]
  (comp-coer/as-int s))

(defn as-int-opt
  "Parse a string into an integer, or an empty string if the string
   cannot be parsed. If the parameter cannot be coerced, the route will 
   still succeed"
  [s]
  (try
    (Long/parseLong s)
    (catch NumberFormatException _ "")))
