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
  ""
  ([instant ra]
    (- (mean-siderial-time-greenwich instant) ra))
  )