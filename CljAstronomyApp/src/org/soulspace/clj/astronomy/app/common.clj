(ns org.soulspace.clj.astronomy.app.common
  (:import [java.util Date])
  (:require [org.soulspace.clj.astronomy.time :as time]))


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
