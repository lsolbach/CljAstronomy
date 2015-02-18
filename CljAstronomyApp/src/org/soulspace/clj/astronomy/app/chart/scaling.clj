(ns org.soulspace.clj.astronomy.app.chart.scaling
  (:use [org.soulspace.clj.math math java-math]
        [org.soulspace.clj.astronomy.coordinates projection]))

(def north-pole [(deg-to-rad 0.0) (deg-to-rad 90.0)])
(def south-pole [(deg-to-rad 0.0) (deg-to-rad -90.0)])
(def equator [(deg-to-rad 0.0) (deg-to-rad 0.0)])
(def home [(deg-to-rad 9.25) (deg-to-rad 48.75)])

(def north-pole-stereographic-projector (stereographic-projector 1 1 north-pole))
(def south-pole-stereographic-projector (stereographic-projector 1 1 south-pole))
(def equatorial-stereographic-projector (stereographic-projector 1 1 equator))
(def ostfildern-stereographic-projector (stereographic-projector 1 1 home))

(def north-pole-reverse-stereographic-projector (reverse-stereographic-projector 1 1 north-pole))
(def south-pole-reverse-stereographic-projector (reverse-stereographic-projector 1 1 south-pole))
(def equatorial-reverse-stereographic-projector (reverse-stereographic-projector 1 1 equator))
(def ostfildern-reverse-stereographic-projector (reverse-stereographic-projector 1 1 home))

(def north-pole-orthographic-projector (orthographic-projector 1 north-pole))
(def south-pole-orthographic-projector (orthographic-projector 1 south-pole))
(def equatorial-orthographic-projector (orthographic-projector 1 equator))
(def ostfildern-orthographic-projector (orthographic-projector 1 home))

(def north-pole-reverse-orthographic-projector (reverse-orthographic-projector 1 north-pole))
(def south-pole-reverse-orthographic-projector (reverse-orthographic-projector 1 south-pole))
(def equatorial-reverse-orthographic-projector (reverse-orthographic-projector 1 equator))
(def ostfildern-reverse-orthographic-projector (reverse-orthographic-projector 1 home))

(defn user-coordinate-transformer
  "Returns a scaling transformer function which scales the coordinates in the intervall [0,1] to java user
  coordinates in the intervalls [0,width] and [0,height]. The user coordinate [0,0] represents the top left."
  ([[width height]]
    (user-coordinate-transformer width height))
  ([width height]
    (fn [[x y]]
      [(* (+ (* -1 x) 1) width)
       (* (+ (* -1 y) 1) height)])))

(defn reverse-user-coordinate-transformer
  "Returns a scaling transformer function which scales the coordinates in java user coordinates in the
  intervalls [0,width] and [0,height] to the intervall [0,1]. The user coordinate [0,0] represents the top left."
  ([[width height]]
    (reverse-user-coordinate-transformer width height))
  ([width height]
    (fn [[x y]]
      [(+ 1 (* -1 (/ x width)))
       (+ 1 (* -1 (/ y height)))])))

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

(defn reverse-relative-scale-transformer
  "Returns a scaling transformer function which scales the intervall [0,1] to the coordinates [x-min y-min] [x-max y-max].
   Takes the minimum and maximum coordinates as input."
  ([[x-min y-min] [x-max y-max]]
    (reverse-relative-scale-transformer x-min y-min x-max y-max))
  ([x-min y-min x-max y-max]
    (fn [[x y]]
      [(+ x-min (* x (- x-max x-min)))
       (+ y-min (* y (- y-max y-min)))])))

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

(def relative-coordinates
  (relative-scale-transformer [(deg-to-rad 0.0) (deg-to-rad -90.0)] [(deg-to-rad 360.0) (deg-to-rad 90.0)]))
(def reverse-relative-coordinates
  (reverse-relative-scale-transformer [(deg-to-rad 0.0) (deg-to-rad -90.0)] [(deg-to-rad 360.0) (deg-to-rad 90.0)]))

(def stereographic-relative-coordinates
  (relative-scale-transformer [-2.0 -2.0] [2.0 2.0]))
(def reverse-stereographic-relative-coordinates
  (reverse-relative-scale-transformer [-2.0 -2.0] [2.0 2.0]))

(def orthographic-relative-coordinates
  (relative-scale-transformer [-1.0 -1.0] [1.0 1.0]))
(def reverse-orthographic-relative-coordinates
  (reverse-relative-scale-transformer [-1.0 -1.0] [1.0 1.0]))
