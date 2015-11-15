;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.time.instant
  (:use [org.soulspace.clj.astronomy.time time])
  (:import [java.util Date TimeZone]))

(defprotocol Instant
  (as-julian-day [date] "Returns the julian day of this point in time.")
  (as-date [date]))

(defrecord JulianDay [jd timezone]
  Instant
  (as-julian-day [this] jd)
  (as-date [this] (julian-day-to-date jd)))

(defn new-julian-day
  "Creates a new julian day instant."
  ([]
    (new-julian-day (Date.) (TimeZone/getDefault)))
  ([date]
    (new-julian-day date (TimeZone/getDefault)))
  ([date timezone]
    (JulianDay. (java-date-to-julian-day date) timezone)))
