;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.coordinate
  (:use [org.soulspace.clj.math math java-math])
  )

;Montenbruck, Oliver; Grundlagen der Ephemeridenrechnung, 7. Aufl., Spektrum Akademischer Verlag
(defn- calc-beta
  [z rho]
  (if (== rho 0)
    (cond
      (< z 0) (deg-to-rad -90)
      (== z 0) 0
      (> z 0) (deg-to-rad 90))
    (atan (/ z rho))))

(defn- calc-lambda
  [x y phi]
  (cond
    (and (== x 0) (== y 0)) 0.0
    (and (>= x 0) (>= y 0)) phi
    (and (>= x 0) (< y 0)) (+ (deg-to-rad 360) phi)
    (< x 0) (- (deg-to-rad 180) phi)))

(defn cartesian-to-spherical
  "Convert cartesian to spherical coordinates."
  ([cartesian-coords]
    (let [[x y z] cartesian-coords]
      (cartesian-to-spherical x y z)))
  ([x y z]
    (let [r (sqrt (+ (sqr x) (sqr y) (sqr z)))
          rho (sqrt (+ (sqr x) (sqr y)))
          beta (calc-beta z rho)
          phi (* 2 (atan (/ y (+ (abs x) rho))))
          lambda (calc-lambda x y phi)]
      [r beta lambda])))

(defn spherical-to-cartesian
  "Convert spherical to cartesian coordinates."
  ([spherical-coords]
    (let [[r beta lambda] spherical-coords]
      (spherical-to-cartesian r beta lambda)))
  ([r beta lambda]
    [(* r (cos beta) (cos lambda))
     (* r (cos beta) (sin lambda))
     (* r (sin beta))]))
