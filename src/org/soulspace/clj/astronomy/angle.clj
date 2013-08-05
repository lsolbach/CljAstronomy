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
  (:use
    [org.soulspace.clj.math math java-math]))

; TODO define an angle protocol?
; TODO angle operations: +, - (*, /)?

; pattern for the parsing of an angle in degree minutes and seconds
(def dms-pattern #"(\+|-)?(\d+)°(?:(\d+)'(?:(\d+(?:\.\d+)?)\")?)?")

(defn- parse-long
  "Resilient long conversion."
  [x]
  (try 
    (Long/parseLong x)
    (catch Exception e 0)))

(defn- parse-double
  [x]
  "Resilient double conversion."
  (try 
    (Double/parseDouble x)
    (catch Exception e 0.0)))

(defn hour-angle-to-angle
  [ha]
  (* 15 ha))

(defn angle-to-hour-angle
  [a]
  (/ a 15))

(defn dms-to-angle
  ([s]
    (let [[_ sgn deg min sec] (re-matches dms-pattern s)]
      (dms-to-angle sgn (parse-long deg) (parse-long min) (parse-double sec))))
  ([sgn deg min]
    (dms-to-angle sgn deg min 0.0))
  ([sgn deg min sec]
    (let [sign (if (= sgn "-") -1 1)]
      (+ (* sign deg) (/ (* sign min) 60) (/ (* sign sec) 3600)))))

(defn angle-to-dms
  [a]
  (let [af (rem a 1)
        mf (rem (* af 60) 1)]
  {:sign (if (< a 0) -1 1) :deg (long (floor a)) :min (long (floor (* af 60))) :sec (* mf 60)}))
