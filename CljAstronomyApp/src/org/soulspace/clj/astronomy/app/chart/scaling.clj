(ns org.soulspace.clj.astronomy.app.chart.scaling
  (:use [org.soulspace.clj.math math java-math]
        [org.soulspace.clj.astronomy.coordinates projection]))

(def north-pole [(deg-to-rad 0.0) (deg-to-rad 90.0)])
(def south-pole [(deg-to-rad 0.0) (deg-to-rad -90.0)])
(def equator [(deg-to-rad 0.0) (deg-to-rad 0.0)])
(def home [(deg-to-rad 9.25) (deg-to-rad 48.75)])

;(def stereoscopic-projector (partial stereoscopic-projection 1 1))
;(def orthoscopic-projector (partial orthoscopic-projection 1))

(def north-pole-stereoscopic-projector (partial stereoscopic-projector 1 1 north-pole))
(def south-pole-stereoscopic-projector (partial stereoscopic-projector 1 1 south-pole))
(def equatorial-stereoscopic-projector (partial stereoscopic-projector 1 1 equator))
(def ostfildern-stereoscopic-projector (partial stereoscopic-projector 1 1 home))

(def north-pole-orthoscopic-projector (partial orthoscopic-projector 1 north-pole))
(def south-pole-orthoscopic-projector (partial orthoscopic-projector 1 south-pole))
(def equatorial-orthoscopic-projector (partial orthoscopic-projector 1 equator))
(def ostfildern-orthoscopic-projector (partial orthoscopic-projector 1 home))

; TODO add aspect-ratio parameter
(defn relative-scale-transformer
  "Returns a scaling transformer function which scales the coordinates to the intervall [0,1].
   Takes the minimum and maximum coordinates as input."
  ([[x-min y-min] [x-max y-max]]
    (relative-scale-transformer x-min y-min x-max y-max))
  ([x-min y-min x-max y-max]
    (fn [[x y]]
      [(/ (- x x-min) (- x-max x-min))
       (/ (- y y-min) (- y-max y-min))])))

; TODO implement, add aspect-ratio parameter
(defn standard-scale-transformer
  "Returns a scaling transformer function which scales the coordinates to the intervall [-1,1].
   Takes the minimum and maximum coordinates as input."
  ([[x-min y-min] [x-max y-max]]
    (standard-scale-transformer x-min y-min x-max y-max))
  ([x-min y-min x-max y-max]
    (fn [[x y]]
      [(/ (- x x-min) (- x-max x-min))
       (/ (- y y-min) (- y-max y-min))])))

(def relative-coordinates (relative-scale-transformer (deg-to-rad 0.0) (deg-to-rad -90.0) (deg-to-rad 360.0) (deg-to-rad 90.0)))

(def stereoscopic-relative-coordinates (relative-scale-transformer -2.0 -2.0 2.0 2.0))
(def orthoscopic-relative-coordinates (relative-scale-transformer -1.0 -1.0 1.0 1.0))

