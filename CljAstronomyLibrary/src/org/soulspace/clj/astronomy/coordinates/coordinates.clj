(ns org.soulspace.clj.astronomy.coordinates.coordinates
  (:use [org.soulspace.clj.astronomy.time time instant]))

; TODO use angle abstractions
(defn zenit-distance-by-altitude
  "Calculates the zenit distance by altitude."
  [altitude]
  (- 90 altitude))

(defn altitude-by-zenit-distance
  "Calculates the altitude by zenit distance."
  [zenit-distance]
  (- 90 zenit-distance))

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
