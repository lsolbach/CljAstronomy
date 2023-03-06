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
(ns org.soulspace.clj.astronomy.earth
  (:require [org.soulspace.math.core :as m])
  (:use [org.soulspace.clj.astronomy.time time instant]))

;; References:
;; Jean Meeus; Astronomical Algorithms, 2. Ed.; Willmann-Bell

;; TODO rename methods to topo* instead of geo* to generalize them to other celestial bodies
;; TODO abstract into the planet protocol? Move protocols and records to a domain layer 

(def equatorial-radius 6378140) ; equatorial radius in meters
(def flattening (/ 1 298.257)) ; flattening
(def polar-radius (* equatorial-radius (- 1 flattening))) ; polar radius in meters
(def eccentricity (m/sqrt (- (* 2 flattening) (m/sqr flattening)))) ; eccentricity of the meridian
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
  (m/atan (* (/ (m/sqr equatorial-radius) (m/sqr polar-radius))
           (m/tan geographic-latitude))))

(defn geocentric-parameters-by-height
  [geographic-latitude height]
  (let [u (m/atan (* (/ polar-radius equatorial-radius) (m/tan geographic-latitude)))
        rho-sin-gc-lat (+ (* (/ polar-radius equatorial-radius) (m/sin u)) (* (/ height equatorial-radius) (m/sin geographic-latitude)))
        rho-cos-gc-lat (+ (m/cos u)(* (/ height equatorial-radius) (m/cos geographic-latitude)))
        rho (if (> (abs geographic-latitude) (/ m/PI 4))
              (/ rho-sin-gc-lat (m/sin (geocentric-latitude geographic-latitude)))
              (/ rho-cos-gc-lat (m/cos (geocentric-latitude geographic-latitude))))]
    {:u u :rho rho :rho-sin-geocentric-lat rho-sin-gc-lat :rho-cos-geocentric-lat rho-cos-gc-lat}))

(defn geocentric-distance
  "Calculates the distance of the center of the earth in equatorial radiuses."
  ([geographic-latitude]
   (+ 0.9983271 (* 0.0016764 (m/cos (* 2 geographic-latitude))) (* -0.0000035 (m/cos (* 4 geographic-latitude)))))
  ([geographic-latitude height]
   (:rho (geocentric-parameters-by-height geographic-latitude height))))

(defn parallel-radius [geographic-latitude]
  "Calculates the radius of the parallel circle at the given geographic latitude."
  (/ (* equatorial-radius (m/cos geographic-latitude))
     (m/sqrt (- 1 (* (m/sqr eccentricity) (m/sqr (m/sin geographic-latitude)))))))

(defn longitude-distance-per-degree
  "Calculates the distance per degree of longitude for the given geographic latitude."
  [geographic-latitude]
  (* (/ m/PI 180) (parallel-radius geographic-latitude)))

(defn curvature-radius
  "Calculates the curvature radius for the given geographic latitude."
  [geographic-latitude]
  (/ (* equatorial-radius (- 1 (m/sqr eccentricity)))
     (m/pow (- 1 (* (m/sqr eccentricity) (m/sqr (m/sin geographic-latitude)))) 3/2)))

(defn latitude-distance-per-degree
  "Calculates the distance per degree of latitude for the given geographic latitude."
  [geographic-latitude]
  (* (/ m/PI 180) (curvature-radius geographic-latitude)))

(defn linear-velocity [geographic-latitude]
  "Calculates The linear velocity with respect to the stars at the given latitude in meters per second."
  (* omega (parallel-radius geographic-latitude)))

(defn geodesic-distance
  "Calculates the geodesic distance between 2 positions on earth."
  [long1 lat1 long2 lat2]
  (let [F (/ (+ lat1 lat2) 2)
        G (/ (- lat1 lat2) 2)
        L (/ (+ long1 long2) 2)
        S (+ (* (m/sqr (m/sin G)) (m/sqr (m/cos L))) (* (m/sqr (m/cos F)) (m/sqr (m/sin L))))
        C (+ (* (m/sqr (m/cos G)) (m/sqr (m/cos L))) (* (m/sqr (m/sin F)) (m/sqr (m/sin L))))
        w (m/atan (m/sqrt (/ S C)))
        R (/ (m/sqrt (* S C)) w)
        D (* 2 w equatorial-radius)
        H1 (/ (- (* 3 R) 1) (* 2 C))
        H2 (/ (+ (* 3 R) 1) (* 2 S))
        s (* D (+ 1 (* flattening H1 (m/sqr (m/sin F)) (m/sqr (m/cos G)))
                  (* -1 flattening H2 (m/sqr (m/cos F)) (m/sqr (m/sin G)))))]
    s))

