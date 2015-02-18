(ns org.soulspace.clj.astronomy.app.common
  (import [java.util Date])
  (:use [org.soulspace.clj.astronomy.time time]))

(def current-jd (ref (java-date-to-julian-day (Date.))))

(def location (ref {:name "Ostfildern"
                    :longitude "9°17'"
                    :latitude "48°42'"}))

(defn update-time
  "Updates the time."
  [jd]
  (dosync (ref-set current-jd jd)))

(defn update-location
  "Updates the location."
  [loc]
  (dosync (ref-set location loc)))
