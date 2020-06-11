(ns org.soulspace.clj.astronomy.coordinates.test.coordinates
  (:use
    [clojure.test]
    [org.soulspace.clj.astronomy.coordinates.coordinates]))

(deftest angular-distance-test
  (is (= (angular-distance [0 0] [0 0]) 0.0))
  (is (= (angular-distance [1 1] [1 1]) 0.0))
  (is (= (angular-distance [4 4] [4 4]) 0.0))
  (is (= (angular-distance [1 1] [1 2]) 1.0))
  (is (= (angular-distance [1 2] [1 1]) 1.0))
  (is (= (angular-distance [1 2] [1 1]) 1.0)))


;(run-tests)
