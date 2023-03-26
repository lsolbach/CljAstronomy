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

(ns org.soulspace.clj.astronomy.app.common
  (:require [org.soulspace.clj.java.i18n :i18n]
            [org.soulspace.clj.astronomy.time :as time])
  (:import [java.util Date]))

(def i18n (partial i18n/bundle-lookup (i18n/bundle "resources")))

(def current-time (ref (time/new-julian-day)))

(def current-location (ref {:name "Ostfildern"
                            :longitude "9°17'"
                            :latitude "48°42'"}))

(defn set-time
  "Updates the time."
  ([]
   (set-time (time/new-julian-day)))
  ([datetime]
   (dosync (ref-set current-time (time/new-julian-day datetime)))))

(defn set-location
  "Updates the location."
  [loc]
  (dosync (ref-set current-location loc)))
