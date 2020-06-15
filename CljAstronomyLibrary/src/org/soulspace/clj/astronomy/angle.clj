;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.angle
  (:use [org.soulspace.clj.math math java-math]))

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

(defn dms-string
  "Returns the string representation of the hour angle."
  [a]
  (if (map? a)
    (str (when (= (:sign a) -1) "-") (:deg a) "°" (:min a) "'" (:sec a) "\"")
    (dms-string (deg-to-dms a))))


(defprotocol Angle
  "Protocol for Angles."
  (to-radians [angle] "Returns the angle as radian value.")
  (to-degrees [angle] "Returns the angle as degree value.")
  (to-hours [angle] "Returns the angle as hour value.")
  (to-minutes [angle] "Returns the angle as arc minutes value.")
  (to-seconds [angle] "Returns the angle as arc seconds value.")
  (to-dms [angle] "Returns the angle as a map of sign, degrees, minutes and seconds."))

; Implementation of the Angle protocol that stores the angle as a radian value.
(defrecord RadianAngleImpl
  [radian]
  Angle
  (to-radians [angle] (:radian angle))
  (to-degrees [angle] (rad-to-deg (:radian angle)))
  (to-hours [angle] (/ (rad-to-deg (:radian angle)) 15))
  (to-minutes [angle] (* 60 (rad-to-deg (:radian angle))))
  (to-seconds [angle] (* 3600 (rad-to-deg (:radian angle))))
  (to-dms [angle] (deg-to-dms (rad-to-deg (:radian angle)))))
