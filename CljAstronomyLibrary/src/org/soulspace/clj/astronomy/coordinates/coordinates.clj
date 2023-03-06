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
(ns org.soulspace.clj.astronomy.coordinates.coordinates
  (:require [org.soulspace.math.core :as m])
  (:use [org.soulspace.clj.astronomy.time time instant]))

(def pi-ninetieth "Defines Pi/90 for speed." (/ m/PI 90)) ; 2 degrees

(defn angular-distance
  "Calculates the angular distance between the coordinates (given in rad)."
  ([[ra1 dec1] [ra2 dec2]]
   (angular-distance ra1 dec1 ra2 dec2))
  ([ra1 dec1 ra2 dec2]
   (let [delta-ra (- ra1 ra2)
         delta-dec (- dec1 dec2)]
     (if (or (< (abs (- (abs dec1) m/HALF-PI)) pi-ninetieth) (< (abs (- (abs dec1) m/HALF-PI)) pi-ninetieth))
       (m/ahav (+ (m/hav delta-dec) (* (m/cos dec1) (m/cos dec2) (m/hav delta-ra)))) ; use haversine if declinations are near the poles
       (m/acos (+ (* (m/sin dec1) (m/sin dec2)) (* (m/cos dec1) (m/cos dec2) (m/cos delta-ra))))))))

(defn zenit-distance-by-altitude
  "Calculates the zenit distance by altitude (given in rad)."
  [altitude]
  (- m/HALF-PI (min (abs altitude) m/HALF-PI)))

(defn altitude-by-zenit-distance
  "Calculates the altitude by zenit distance (given in rad)."
  [zenit-distance]
  (- m/HALF-PI (min (abs zenit-distance))))

(defn hour-angle
  "Calculates the hour angle of the right ascension at the given instant."
  ([instant ra]
   (- (mean-siderial-time-greenwich instant) ra)))

; TODO move protocol and records to a domain layer
(defprotocol CelestialObject
  "Protocol for celestial coordinates."
  (horizontal-coordinates [obj time location])
  (equatorial-coordinates [obj time location])
  (magnitude [obj time location]))

;; TODO convert to records
(defprotocol HorizontalCoordinates
  "Protocol for horizontal coordinate system (Alt/Az).")

(defprotocol EquatorialCoordinates
  "Protocol for equatorial coordinate system (RA/Dec).")

(defprotocol EclipticalCoordinates
  "Protocol for ecliptical coordinate system.")

(defprotocol GalacticalCoordinates
  "Protocol for galactical coordinate system.")
