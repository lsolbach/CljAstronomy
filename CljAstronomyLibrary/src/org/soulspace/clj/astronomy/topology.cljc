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
(ns org.soulspace.clj.astronomy.topology
  (:require [org.soulspace.math.core :as m])
  (:use [org.soulspace.clj.astronomy.time time instant]))

;;;
;;; Functions for topological calculations.
;;;
;;; The equatorial and polar radius are expected as meters.
;;; 
;;; References:
;;; Jean Meeus; Astronomical Algorithms, 2. Ed.; Willmann-Bell
;;;

(def location-types #{:topographic :topocentric})

(defn polar-radius
  "Calculates the polar-radius from the equatorial-radius and the flattening."
  [equatorial-radius flattening]
  (* equatorial-radius (- 1 flattening)))

(defn eccentricity
  "Calculates the eccentricity of the meridian from the given flattening."
  [flattening]
  (m/sqrt (- (* 2 flattening) (m/sqr flattening))))

(defn topocentric-latitude
  "Calculates the topocentric latitude for the given topographic latitude."
  [topographic-latitude equatorial-radius polar-radius]
  (m/atan (* (/ (m/sqr equatorial-radius ) (m/sqr polar-radius))
           (m/tan topographic-latitude))))

(defn topocentric-parameters-by-height
  [topographic-latitude height equatorial-radius polar-radius]
  (let [u (m/atan (* (/ polar-radius equatorial-radius) (m/tan topographic-latitude)))
        rho-sin-gc-lat (+ (* (/ polar-radius equatorial-radius) (m/sin u)) (* (/ height equatorial-radius) (m/sin topographic-latitude)))
        rho-cos-gc-lat (+ (m/cos u)(* (/ height equatorial-radius) (m/cos topographic-latitude)))
        rho (if (> (abs topographic-latitude) (/ m/PI 4))
              (/ rho-sin-gc-lat (m/sin (topocentric-latitude topographic-latitude equatorial-radius polar-radius)))
              (/ rho-cos-gc-lat (m/cos (topocentric-latitude topographic-latitude equatorial-radius polar-radius))))]
    {:u u :rho rho :rho-sin-topocentric-lat rho-sin-gc-lat :rho-cos-topocentric-lat rho-cos-gc-lat}))

(defn topocentric-distance
  "Calculates the distance of the center of the body in equatorial radiuses."
  [topographic-latitude height equatorial-radius polar-radius]
  (:rho (topocentric-parameters-by-height topographic-latitude height equatorial-radius polar-radius)))

(defn parallel-radius
  "Calculates the radius of the parallel circle at the given topographic latitude."
  [topographic-latitude equatorial-radius eccentricity]
  (/ (* equatorial-radius (m/cos topographic-latitude))
     (m/sqrt (- 1 (* (m/sqr eccentricity) (m/sqr (m/sin topographic-latitude)))))))

(defn longitude-distance-per-degree
  "Calculates the distance per degree of longitude for the given topographic latitude."
  [topographic-latitude equatorial-radius eccentricity]
  (* (/ m/PI 180) (parallel-radius topographic-latitude equatorial-radius eccentricity)))

(defn curvature-radius
  "Calculates the curvature radius for the given topographic latitude."
  [topographic-latitude equatorial-radius eccentricity]
  (/ (* equatorial-radius (- 1 (m/sqr eccentricity)))
     (m/pow (- 1 (* (m/sqr eccentricity) (m/sqr (m/sin topographic-latitude)))) 3/2)))

(defn latitude-distance-per-degree
  "Calculates the distance per degree of latitude for the given topographic latitude."
  [topographic-latitude equatorial-radius eccentricity]
  (* (/ m/PI 180) (curvature-radius topographic-latitude equatorial-radius eccentricity)))

(defn linear-velocity
  "Calculates the linear velocity with respect to the stars at the given latitude in meters per second.
  Omega is the rotational angular velocity with respect to the stars at the epoch."
  [topographic-latitude equatorial-radius eccentricity omega]
  (* omega (parallel-radius topographic-latitude equatorial-radius eccentricity)))

(defn topodesic-distance
  "Calculates the topodesic distance (great circle distance) between 2 positions on the body."
  [long1 lat1 long2 lat2 equatorial-radius flattening]
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
        s (* D (+ 1 (* flattening H1 (m/sqr (m/sin F)) (m/sqr (m/cos G))) (* -1 flattening H2 (m/sqr (m/cos F)) (m/sqr (m/sin G)))))]
    s))
