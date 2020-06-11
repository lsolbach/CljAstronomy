;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.coordinates.coordinates
  (:use [org.soulspace.clj.math math java-math]
        [org.soulspace.clj.astronomy.time time instant]))

(defn angular-distance
  "Calculates the angular distance between the coordinates (given in rad)."
  ([[ra1 dec1] [ra2 dec2]]
   (angular-distance ra1 dec1 ra2 dec2))
  ([ra1 dec1 ra2 dec2]
   (let [delta-ra (- ra1 ra2)
         delta-dec (- dec1 dec2)
         pi-half (/ pi 2) ; 180 degrees
         pi-ninetieth (/ pi 90)] ; 2 degrees

     (if (or (< (abs (- (abs dec1) pi-half)) pi-ninetieth) (< (abs (- (abs dec1) pi-half)) pi-ninetieth))
       (ahav (+ (hav delta-dec) (* (cos dec1) (cos dec2) (hav delta-ra)))) ; use haversine if declinations are near the poles
       (acos (+ (* (sin dec1) (sin dec2)) (* (cos dec1) (cos dec2) (cos delta-ra))))))))

(defn zenit-distance-by-altitude
  "Calculates the zenit distance by altitude (given in rad)."
  [altitude]
  (- (/ pi 2) altitude))

(defn altitude-by-zenit-distance
  "Calculates the altitude by zenit distance (given in rad)."
  [zenit-distance]
  (- (/ pi 2) zenit-distance))

(defn hour-angle
  "Calculates the hour angle of the right ascension at the given instant."
  ([instant ra]
   (- (mean-siderial-time-greenwich instant) ra)))

(defprotocol Horizontal
  "Protocol for horizontal coordinate system (Alt/Az).")

(defprotocol Equatorial
  "Protocol for equatorial coordinate system (RA/Dec).")

(defprotocol Ecliptical
  "Protocol for ecliptical coordinate system.")

(defprotocol Galactical
  "Protocol for galactical coordinate system.")
