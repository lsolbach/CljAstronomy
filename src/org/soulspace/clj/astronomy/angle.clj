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

; pattern for parsing an angle given in signed degrees, minutes and seconds (e.g. -80° 7' 30")
(def dms-pattern #"(\+|-)?(\d+)°\s*(?:(\d+)'\s*(?:(\d+(?:\.\d+)?)\")?)?")

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

(defn hour-angle-to-angle
  ""
  [ha]
  (* 15 ha))

(defn angle-to-hour-angle
  ""
  [a]
  (/ a 15))

(defn dms-angle-to-angle
  "Converts an angle given in degrees, minutes and seconds into anangle given in decimal degrees."
  ([dms]
    (cond
      (map? dms)
      (dms-angle-to-angle (:sign dms) (:deg dms) (:min dms) (:sec dms))
      (string? dms)
      (let [[_ sgn deg min sec] (re-matches dms-pattern dms)]
        (dms-angle-to-angle (if (= sgn "-") -1 1) (parse-long deg) (parse-long min) (parse-double sec)))))
  ([sgn deg min]
    (dms-angle-to-angle sgn deg min 0.0))
  ([sgn deg min sec]
    (* sgn (+ deg (/ min 60) (/ sec 3600)))))

(defn angle-to-dms-angle
  "Converts an angle given in decimal degrees into an angle given in degrees, minutes and seconds."
  [a]
  (let [abs-a (abs a)
        af (rem abs-a 1)
        mf (rem (* af 60) 1)]
    {:sign (if (< a 0) -1 1)
     :deg (long (floor abs-a))
     :min (long (floor (* af 60)))
     :sec (* mf 60)}))

(defprotocol Angle
  "Protocol for Angles."
  (to-radians [angle] "Returns the angle as radian value.")
  (to-degrees [angle] "Returns the angle as degree value.")
  (to-hours [angle] "Returns the angle as hour value.")
  (to-minutes [angle] "Returns the angle as arc minutes value.")
  (to-seconds [angle] "Returns the angle as arc seconds value.")
  (to-dms [angle] "Returns the angle as a map of sign, degrees, minutes and seconds.")
  )

(defrecord RadianAngleImpl
  [radian]
  ;"Implementation of the Angle protocol that stores the angle as a radian value."
  Angle
  (to-radians [angle] (:radian angle))
  (to-degrees [angle] (rad-to-deg (:radian angle)))
  (to-hours [angle] (/ (rad-to-deg (:radian angle)) 15))
  (to-minutes [angle] (* 60 (rad-to-deg (:radian angle))))
  (to-seconds [angle] (* 3600 (rad-to-deg (:radian angle))))
  (to-dms [angle] (angle-to-dms-angle (rad-to-deg (:radian angle))))
  )
