(ns org.soulspace.clj.astronomy.coordinates.test.coordinates
  (:use
    [clojure.test]
    [org.soulspace.clj.astronomy.coordinates projection]
    [org.soulspace.clj.math java-math]))

(deftest stereographic-projection-test
  (is (= (stereographic-projection 1 1))))
