(ns yardstick-api.yardstick-api-test
  (:require [clojure.test :refer [deftest testing is]]
            [yardstick-api.server :as ys]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))
    (is ys/-main)))
