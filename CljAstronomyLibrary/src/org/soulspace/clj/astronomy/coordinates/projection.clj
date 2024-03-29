;;;;
;;;;   Copyright (c) Ludger Solbach. All rights reserved.
;;;;
;;;;   The use and distribution terms for this software are covered by the
;;;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;;;   which can be found in the file license.txt at the root of this distribution.
;;;;   By using this software in any fashion, you are agreeing to be bound by
;;;;   the terms of this license.
;;;;
;;;;   You must not remove this notice, or any other, from this software.
;;;;
(ns org.soulspace.clj.astronomy.coordinates.projection
  (:require [org.soulspace.math.core :as m]))

; References:
; Snyder, John P.; Map Projections - A Working Manual; USGS Professional Paper 1395

; symbol mapping
; lambda -> longitude
; phi    -> latitude
; R      -> radius of the sphere (either actual or corresponding to the scale of the map)
; k0     -> relative scale factor along a parallel of latitude

; implementations of spherical projections, no ellipsoid projections implemented yet

; TODO move to a separate module
; TODO maybe add simplifications for long-0 or lat-0 = 0 or 90 degrees

(defn stereographic-projection
  "Calculates the stereographic projection of the coordinates of long and lat for a map centered on the coordinates long-0 and lat-0."
    ([R k-0 [long-0 lat-1] [long lat]]
     (stereographic-projection R k-0 long-0 lat-1 long lat))
    ([R k-0 long-0 lat-1 long lat]
     (let [k (/ (* 2 k-0)
                (+ 1
                   (* (m/sin lat-1) (m/sin lat))
                   (* (m/cos lat-1) (m/cos lat) (m/cos (- long long-0)))))
           x (* R k (m/cos lat) (m/sin (- long long-0)))
           y (* R k (- (* (m/cos lat-1) (m/sin lat))
                       (* (m/sin lat-1) (m/cos lat) (m/cos (- long long-0)))))]
           ;h-stroke (+ (* (sin lat-1) (sin lat)) (* (cos lat-1) (cos lat) (cos (- long long-0)))) ; scale
           ;k-stroke 1.0 ; scale
       ;[x y h-stroke k-stroke]
       [x y])))

(defn reverse-stereographic-projection
  "Calculates the coordinates of x and y in a reversed stereographic projection for a map centered on the coordinates long-0 and lat-0."
  ([R k-0 [long-0 lat-1] [x y]]
   (reverse-stereographic-projection R k-0 long-0 lat-1 x y))
  ([R k-0 long-0 lat-1 x y]
   (let [rho (m/sqrt (+ (* x x) (* y y)))
         c (* 2 (m/atan2 rho (* 2 R k-0)))
         ; c (* 2 (atan (/ rho (* 2 R k-0))))
         lat (if (= rho 0.0)
                lat-1
                (m/asin (+ (* (m/cos c) (m/sin lat-1))
                         (/ (* y (m/sin c) (m/cos lat-1))
                            rho))))
         long (cond
                (= rho 0.0) long-0
                (= lat-1 (/ m/PI 2)) (+ long-0 (m/atan2 x (* -1 y)))
                (= lat-1 (/ m/PI -2)) (+ long-0 (m/atan2 x y))
                :default (+ long-0 (m/atan (* x (m/sin (/ c
                                                      (- (* rho (m/cos lat-1) (m/cos c))
                                                         (* y (m/sin lat-1) (m/sin c)))))))))]
     [long lat])))

(defn stereographic-projector
  "Returns a function for stereographic projections."
  ([R]
   (partial stereographic-projection R))
  ([R k-0]
   (partial stereographic-projection R k-0))
  ([R k-0 [long-0 lat-1]]
   (partial stereographic-projection R k-0 [long-0 lat-1]))
  ([R k-0 long-0 lat-1]
   (partial stereographic-projection R k-0 long-0 lat-1)))

(defn reverse-stereographic-projector
  "Returns a function for reverse stereographic projections."
  ([R]
   (partial reverse-stereographic-projection R))
  ([R k-0]
   (partial reverse-stereographic-projection R k-0))
  ([R k-0 [long-0 lat-1]]
   (partial reverse-stereographic-projection R k-0 [long-0 lat-1]))
  ([R k-0 long-0 lat-1]
   (partial reverse-stereographic-projection R k-0 long-0 lat-1)))

(defn orthographic-projection
  "Calculates the orthographic projection of the coordinates of the coordinates of long and lat for a map centered on the coordinates long-0 and lat-0."
  ([R [long-0 lat-1] [long lat]]
   (orthographic-projection R long-0 lat-1 long lat))
  ([R long-0 lat-1 long lat]
   (let [x (* R (m/cos lat) (m/sin (- long long-0)))
         y (* R (- (* (m/cos lat-1) (m/sin lat))
                   (* (m/sin lat-1) (m/cos lat) (m/cos (- long long-0)))))]
         ;h-stroke (+ (* (sin lat-1) (sin lat)) (* (cos lat-1) (cos lat) (cos (- long long-0))))
         ;k-stroke 1.0

     ;[x y h-stroke k-stroke]
     [x y])))

(defn reverse-orthographic-projection
  "Calculates the coordinates of x and y in a reversed orthographic projection for a map centered on the coordinates long-0 and lat-0."
  ([R [long-0 lat-1] [x y]]
   (reverse-orthographic-projection R long-0 lat-1 x y))
  ([R long-0 lat-1 x y]
   (let [rho (m/sqrt (+ (* x x) (* y y)))
         c (m/asin (/ rho R))
         lat (if (= rho 0.0)
                lat-1
                (m/asin (+ (* (m/cos c) (m/sin lat-1))
                         (/ (* y (m/sin c) (m/cos lat-1))
                            rho))))
         long (cond
                (= rho 0.0) long-0
                (= lat-1 (/ m/PI 2)) (+ long-0 (m/atan2 x (* -1 y)))
                (= lat-1 (/ m/PI -2)) (+ long-0 (m/atan2 x y))
                :default (+ long-0 (m/atan (* x (m/sin (/ c
                                                      (- (* rho (m/cos lat-1) (m/cos c))
                                                         (* y (m/sin lat-1) (m/sin c)))))))))]
     [long lat])))

(defn orthographic-projector
  "Returns a function for orthographic projections."
  ([R]
   (partial orthographic-projection R))
  ([R [long-0 lat-1]]
   (partial orthographic-projection R [long-0 lat-1]))
  ([R long-0 lat-1]
   (partial orthographic-projection R long-0 lat-1)))

(defn reverse-orthographic-projector
  "Returns a function for reverse orthographic projections."
  ([R]
   (partial reverse-orthographic-projection R))
  ([R [long-0 lat-1]]
   (partial reverse-orthographic-projection R [long-0 lat-1]))
  ([R long-0 lat-1]
   (partial reverse-orthographic-projection R long-0 lat-1)))


;TODO implement other projections
(defn mercator-projection
  "Calculates the mercator projection of the coordinates."
  []
  (let []))


(defn reverse-mercator-projection
  "Calculates the coordinates in a reversed mercator projection."
  []
  (let []))
