(ns org.soulspace.clj.astronomy.test
  (:use
    [org.soulspace.clj.math java-math]))

(defn about-equal
  "Tests if the actual and expected values are equal in the given error margin."
  ([actual expected]
   (about-equal actual expected 0.0001))
  ([actual expected error-margin]
   (<= (abs (- actual expected)) error-margin)))
