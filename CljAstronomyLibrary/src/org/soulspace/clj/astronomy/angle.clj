;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;
(ns org.soulspace.clj.astronomy.angle
  (:require [clojure.spec.alpha :as s])
  (:use [org.soulspace.clj.math math java-math]))

;; TODO move protocol and records to a domain layer

; TODO define an angle protocol?
; TODO angle operations: +, - (*, /)?

; pattern for parsing an angle string given in signed degrees, minutes and seconds, e.g. -80° 7' 30\"
(def dms-pattern #"(\+|-)?(\d+)°\s*(?:(\d+)'\s*(?:(\d+(?:\.\d+)?)\")?)?")
; pattern for parsing a hour angle string given in hours, minutes and seconds, e.g. 10h 7m 30s
(def hms-pattern #"(\d+)h\s*(?:(\d+)m\s*(?:(\d+(?:\.\d+)?)s)?)?")

(defn- parse-long
  "Resilient long conversion."
  [x]
  (try
    (Long/parseLong x)
    (catch Exception e 0)))

(defn- parse-double
  "Resilient double conversion."
  [x]
  (try
    (Double/parseDouble x)
    (catch Exception e 0.0)))

(defn hms-to-ha
  "Converts an hour angle given in hours minutes and seconds into a hour angle given in decimal hours."
  ([hms]
   (cond
     (map? hms)
     (hms-to-ha {:h hms} {:min hms} {:sec hms})
     (string? hms)
     (let [[_ h min sec] (re-matches hms-pattern hms)]
       (hms-to-ha (parse-long h) (parse-long min) (parse-double sec)))))
  ([h min]
   (hms-to-ha h min 0.0))
  ([h min sec]
   (+ h (/ min 60) (/ sec 3600.0))))

(defn ha-to-hms
  "Converts an hour angle given in decimal hours in an hour angle in hours, minutes and seconds."
  [ha]
  (let [h (long (floor ha))
        hf (rem ha 1)
        m (long (floor (* hf 60)))
        mf (rem (* hf 60) 1)]
    {:h h
     :min m
     :sec (double (* mf 60.0))}))

(defn hms-string
  "Returns the string representation of the hour angle."
  [h]
  (if (map? h)
    (str (:h h) "h" (:min h) "m" (:sec h) "s")
    (hms-string (ha-to-hms h))))

(defn ha-to-deg
  "Converts an hour angle to an angle in degrees."
  [ha]
  (* 15 ha))

(defn deg-to-ha
  "Converts an angle in degrees to an hour angle."
  [a]
  (/ (mod a 360) 15))


(defn dms-to-deg
  "Converts an angle given in degrees, minutes and seconds into an angle given in decimal degrees."
  ([dms]
   (cond
     (map? dms)
     (dms-to-deg (:sign dms) (:deg dms) (:min dms) (:sec dms))
     (string? dms)
     (let [[_ sgn deg min sec] (re-matches dms-pattern dms)]
       (dms-to-deg (if (= sgn "-") -1 1) (parse-long deg) (parse-long min) (parse-double sec)))))
  ([sgn deg min]
   (dms-to-deg sgn deg min 0.0))
  ([sgn deg min sec]
   (* sgn (+ deg (/ min 60) (/ sec 3600.0)))))

(defn deg-to-dms
  "Converts an angle given in decimal degrees into an angle given in degrees, minutes and seconds."
  [a]
  (let [abs-a (abs a)
        af (rem abs-a 1)
        mf (rem (* af 60) 1)]
    {:sign (if (< a 0) -1 1)
     :deg (long (floor abs-a))
     :min (long (floor (* af 60)))
     :sec (* mf 60.0)}))

(defn dms-to-rad
  "Converts an angle given in degrees, minutes and seconds into an angle given in radians."
  [dms]
  (deg-to-rad (dms-to-deg dms)))

(defn dms-string
  "Returns the string representation of the hour angle."
  [a]
  (if (map? a)
    (str (when (= (:sign a) -1) "-") (:deg a) "°" (:min a) "'" (:sec a) "\"")
    (dms-string (deg-to-dms a))))


(defprotocol Angle
  "Protocol for Angles."
  (to-rad [angle] "Returns the angle as radian value.")
  (to-deg [angle] "Returns the angle as degree value.")
  (to-ha [angle] "Returns the angle as hour value.")
  (to-arcmin [angle] "Returns the angle as arc minutes value.")
  (to-arcsec [angle] "Returns the angle as arc seconds value.")
  (to-dms [angle] "Returns the angle as a map of sign, deg, min and sec.")
  (to-hms [angle] "Returns the angle as hour angle, a map of h, min and sec.")
  (to-string [angle] "Returns a matching human readable string representation of the angle."))

; TODO add specs, add modulo 360, 2*pi, 24 on respective constructors

; Implementation of the Angle protocol that stores the angle as a degree value.
(defrecord DegreeAngle
  [degrees]
  Angle
  (to-rad [angle] (deg-to-rad (:degrees angle)))
  (to-deg [angle] (:degrees angle))
  (to-ha [angle] (/ (:degrees angle) 15))
  (to-arcmin [angle] (* 60 (:degrees angle)))
  (to-arcsec [angle] (* 3600 (:degrees angle)))
  (to-dms [angle] (deg-to-dms (:degrees angle)))
  (to-hms [angle] (ha-to-hms (deg-to-ha (:degrees angle))))
  (to-string [angle] (dms-string (:degrees angle))))

; Implementation of the Angle protocol that stores the angle as a degree value.
(defrecord HourAngle
  [ha]
  Angle
  (to-rad [angle] (deg-to-rad (ha-to-deg (:ha angle))))
  (to-deg [angle] (ha-to-deg (:ha angle)))
  (to-ha [angle] (:ha angle))
  (to-arcmin [angle] (* 60 (ha-to-deg (:ha angle))))
  (to-arcsec [angle] (* 3600 (ha-to-deg (:ha angle))))
  (to-dms [angle] (deg-to-dms (:ha angle)))
  (to-hms [angle] (ha-to-hms (:ha angle)))
  (to-string [angle] (hms-string (:ha angle))))

; Implementation of the Angle protocol that stores the angle as a radian value.
(defrecord RadianAngle
  [radians]
  Angle
  (to-rad [angle] (:radians angle))
  (to-deg [angle] (rad-to-deg (:radians angle)))
  (to-ha [angle] (/ (rad-to-deg (:radians angle)) 15))
  (to-arcmin [angle] (* 60 (rad-to-deg (:radians angle))))
  (to-arcsec [angle] (* 3600 (rad-to-deg (:radians angle))))
  (to-dms [angle] (deg-to-dms (rad-to-deg (:radians angle))))
  (to-hms [angle] (ha-to-hms (deg-to-ha (rad-to-deg (:radians angle)))))
  (to-string [angle] (dms-string (rad-to-deg (:radians angle)))))
