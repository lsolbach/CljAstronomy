;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.earth.earth
  (:use [org.soulspace.clj.math math java-math]
        [org.soulspace.clj.astronomy.time time instant]))

;; References:
;; Jean Meeus; Astronomical Algorithms, 2. Ed.; Willmann-Bell

;; TODO rename methods to topo* instead of geo* to generalize them to other celestial bodies
;; TODO abstract into the planet protocol? Move protocols and records to a domain layer 

(def equatorial-radius 6378140) ; equatorial radius in meters
(def flattening (/ 1 298.257)) ; flattening
(def polar-radius (* equatorial-radius (- 1 flattening))) ; polar radius in meters
(def eccentricity (sqrt (- (* 2 flattening) (sqr flattening)))) ; eccentricity of the meridian
(def omega 7.292114992e-5) ; rotational angular velocity with respect to the stars at epoch 1996.5 (but earth is slowing down)

(defprotocol Position
  (longitude [position] "Returns the longitude of this position.")
  (latitude [position] "Returns the latitude of this position.")
  (height [position] "Returns the height of this position."))


(defprotocol GeographicPosition
  (geocentric-position [position] "Returns the geocentric position of this geographic position."))

(defrecord GeographicPositionImpl
  [longitude latitude height]
  Position
  (longitude [position] (:longitude position))
  (latitude [position] (:latitude position))
  (height [position] (:height position))
  GeographicPosition
  (geocentric-position [position]))


(defprotocol GeocentricPosition)

(defrecord GeocentricPositionImpl
  [longitude latitude height]
  GeocentricPosition)


(defn geocentric-latitude
  "Calculates the geocentric latitude for the given geographic latitude."
  [geographic-latitude]
  (atan (* (/ (sqr equatorial-radius) (sqr polar-radius))
           (tan geographic-latitude))))

(defn geocentric-parameters-by-height
  [geographic-latitude height]
  (let [u (atan (* (/ polar-radius equatorial-radius) (tan geographic-latitude)))
        rho-sin-gc-lat (+ (* (/ polar-radius equatorial-radius) (sin u)) (* (/ height equatorial-radius) (sin geographic-latitude)))
        rho-cos-gc-lat (+ (cos u)(* (/ height equatorial-radius) (cos geographic-latitude)))
        rho (if (> (abs geographic-latitude) (/ pi 4))
              (/ rho-sin-gc-lat (sin (geocentric-latitude geographic-latitude)))
              (/ rho-cos-gc-lat (cos (geocentric-latitude geographic-latitude))))]
    {:u u :rho rho :rho-sin-geocentric-lat rho-sin-gc-lat :rho-cos-geocentric-lat rho-cos-gc-lat}))

(defn geocentric-distance
  "Calculates the distance of the center of the earth in equatorial radiuses."
  ([geographic-latitude]
   (+ 0.9983271 (* 0.0016764 (cos (* 2 geographic-latitude))) (* -0.0000035 (cos (* 4 geographic-latitude)))))
  ([geographic-latitude height]
   (:rho (geocentric-parameters-by-height geographic-latitude height))))

(defn parallel-radius [geographic-latitude]
  "Calculates the radius of the parallel circle at the given geographic latitude."
  (/ (* equatorial-radius (cos geographic-latitude))
     (sqrt (- 1 (* (sqr eccentricity) (sqr (sin geographic-latitude)))))))

(defn longitude-distance-per-degree
  "Calculates the distance per degree of longitude for the given geographic latitude."
  [geographic-latitude]
  (* (/ pi 180) (parallel-radius geographic-latitude)))

(defn curvature-radius
  "Calculates the curvature radius for the given geographic latitude."
  [geographic-latitude]
  (/ (* equatorial-radius (- 1 (sqr eccentricity)))
     (pow (- 1 (* (sqr eccentricity) (sqr (sin geographic-latitude)))) 3/2)))

(defn latitude-distance-per-degree
  "Calculates the distance per degree of latitude for the given geographic latitude."
  [geographic-latitude]
  (* (/ pi 180) (curvature-radius geographic-latitude)))

(defn linear-velocity [geographic-latitude]
  "Calculates The linear velocity with respect to the stars at the given latitude in meters per second."
  (* omega (parallel-radius geographic-latitude)))

(defn geodesic-distance
  "Calculates the geodesic distance between 2 positions on earth."
  [long1 lat1 long2 lat2]
  (let [F (/ (+ lat1 lat2) 2)
        G (/ (- lat1 lat2) 2)
        L (/ (+ long1 long2) 2)
        S (+ (* (sqr (sin G)) (sqr (cos L))) (* (sqr (cos F)) (sqr (sin L))))
        C (+ (* (sqr (cos G)) (sqr (cos L))) (* (sqr (sin F)) (sqr (sin L))))
        w (atan (sqrt (/ S C)))
        R (/ (sqrt (* S C)) w)
        D (* 2 w equatorial-radius)
        H1 (/ (- (* 3 R) 1) (* 2 C))
        H2 (/ (+ (* 3 R) 1) (* 2 S))
        s (* D (+ 1 (* flattening H1 (sqr (sin F)) (sqr (cos G))) (* -1 flattening H2 (sqr (cos F)) (sqr (sin G)))))]
    s))

;
; Precession
;



;
; Nutation
;
(defn mean-longitude-sun
  "Calculates the mean longitude of the sun."
  [t]
  (+ 280.4665M (* 36000.7698M t)))

(defn mean-longitude-moon
  "Calculates the mean longitude of the moon."
  [t]
  (+ 218.3165 (* 481267.8813 t)))

(defn longitude-ascending-node-moon
  "Calculates the longitude of ascending node of the moons mean orbit on the ecliptic, measured from the mean equinox of the date."
  [t]
  (+ 125.04452M (* -1934.136261M t) (* 0.0020708M t) (/ (* t t t) 450000M)))

(defn nutation-in-longitude
  "Calculates the nutation in longitude (delta psi) in arc seconds  with an accuracy of 0.5 arc seconds."
  [jde]
  (let [t (julian-centuries jde)
        omega (longitude-ascending-node-moon t)
        l-sun (mean-longitude-sun t)
        l-moon (mean-longitude-moon t)]
    ; TODO test
    (+ (* -17.20 (sin omega)) (* -1.32 (sin (* 2 l-sun))) (* -0.23 (sin (* 2 l-moon))) (0.21 (sin (* 2 omega))))))

(defn nutation-in-obliquity
  "Calculates the nutation in obliquity (delta epsilon) in arc seconds with an accuracy of 0.1 arc seconds."
  [jde]
  (let [t (julian-centuries jde)
        omega (longitude-ascending-node-moon t)
        l-sun (mean-longitude-sun t)
        l-moon (mean-longitude-moon t)]
    ; TODO test
    (+ (* 9.20 (cos omega)) (* 0.57 (cos (* 2 l-sun))) (* 0.10 (cos (* 2 l-moon))) (* 0.09 (cos (* 2 omega))))))
