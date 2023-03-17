(ns org.soulspace.clj.astronomy.app.chart.scaling
  (:require [org.soulspace.math.core :as m]
            [org.soulspace.clj.astronomy.coordinates.projection :as cp]))

(def north-pole [(m/deg-to-rad 0.0) (m/deg-to-rad 90.0)])
(def south-pole [(m/deg-to-rad 0.0) (m/deg-to-rad -90.0)])
(def equator [(m/deg-to-rad 0.0) (m/deg-to-rad 0.0)])
(def home [(m/deg-to-rad 9.25) (m/deg-to-rad 48.75)])

(def north-pole-stereographic-projector (cp/stereographic-projector 1 1 north-pole))
(def south-pole-stereographic-projector (cp/stereographic-projector 1 1 south-pole))
(def equatorial-stereographic-projector (cp/stereographic-projector 1 1 equator))
(def home-stereographic-projector (cp/stereographic-projector 1 1 home))

(def north-pole-reverse-stereographic-projector (cp/reverse-stereographic-projector 1 1 north-pole))
(def south-pole-reverse-stereographic-projector (cp/reverse-stereographic-projector 1 1 south-pole))
(def equatorial-reverse-stereographic-projector (cp/reverse-stereographic-projector 1 1 equator))
(def home-reverse-stereographic-projector (cp/reverse-stereographic-projector 1 1 home))

(def north-pole-orthographic-projector (cp/orthographic-projector 1 north-pole))
(def south-pole-orthographic-projector (cp/orthographic-projector 1 south-pole))
(def equatorial-orthographic-projector (cp/orthographic-projector 1 equator))
(def home-orthographic-projector (cp/orthographic-projector 1 home))

(def north-pole-reverse-orthographic-projector (cp/reverse-orthographic-projector 1 north-pole))
(def south-pole-reverse-orthographic-projector (cp/reverse-orthographic-projector 1 south-pole))
(def equatorial-reverse-orthographic-projector (cp/reverse-orthographic-projector 1 equator))
(def home-reverse-orthographic-projector (cp/reverse-orthographic-projector 1 home))

(defn user-coordinate-transformer
  "Returns a scaling transformer function which scales the coordinates in the intervall [0,1] to java user
  coordinates in the intervals [0,width] and [0,height]. The user coordinate [0,0] represents the top left."
  ([[width height]]
   (user-coordinate-transformer width height))
  ([width height]
   (fn [[x y]]
     [(* (+ (* -1 x) 1) width)
      (* (+ (* -1 y) 1) height)])))

(defn reverse-user-coordinate-transformer
  "Returns a scaling transformer function which scales the coordinates in java user coordinates in the
  intervals [0,width] and [0,height] to the intervall [0,1]. The user coordinate [0,0] represents the top left."
  ([[width height]]
   (reverse-user-coordinate-transformer width height))
  ([width height]
   (fn [[x y]]
     [(+ 1 (* -1 (/ x width)))
      (+ 1 (* -1 (/ y height)))])))

; TODO add aspect-ratio parameter
(defn relative-scale-transformer
  "Returns a scaling transformer function which scales the coordinates between [x-min y-min] [x-max y-max] to the interval [0,1] in x and y.
   Takes the minimum and maximum coordinates as input."
  ([[x-min y-min] [x-max y-max]]
   (relative-scale-transformer x-min y-min x-max y-max))
  ([x-min y-min x-max y-max]
   (fn [[x y]]
     [(/ (- x x-min) (- x-max x-min))
      (/ (- y y-min) (- y-max y-min))])))

(defn reverse-relative-scale-transformer
  "Returns a scaling transformer function which scales the interval [0,1] in x and y to the coordinates between [x-min y-min] [x-max y-max].
   Takes the minimum and maximum coordinates as input."
  ([[x-min y-min] [x-max y-max]]
   (reverse-relative-scale-transformer x-min y-min x-max y-max))
  ([x-min y-min x-max y-max]
   (fn [[x y]]
     [(+ x-min (* x (- x-max x-min)))
      (+ y-min (* y (- y-max y-min)))])))

; TODO implement, add aspect-ratio parameter
(defn standard-scale-transformer
  "Returns a scaling transformer function which scales the coordinates to the interval [-1,1].
   Takes the minimum and maximum coordinates as input."
  ([[x-min y-min] [x-max y-max]]
   (standard-scale-transformer x-min y-min x-max y-max))
  ([x-min y-min x-max y-max]
   (fn [[x y]]
     [(/ (- x x-min) (- x-max x-min))
      (/ (- y y-min) (- y-max y-min))])))

(def relative-coordinates
  (relative-scale-transformer [(m/deg-to-rad 0.0) (m/deg-to-rad -90.0)] [(m/deg-to-rad 360.0) (m/deg-to-rad 90.0)]))
(def reverse-relative-coordinates
  (reverse-relative-scale-transformer [(m/deg-to-rad 0.0) (m/deg-to-rad -90.0)] [(m/deg-to-rad 360.0) (m/deg-to-rad 90.0)]))

(def stereographic-relative-coordinates
  (relative-scale-transformer [-2.0 -2.0] [2.0 2.0]))
(def reverse-stereographic-relative-coordinates
  (reverse-relative-scale-transformer [-2.0 -2.0] [2.0 2.0]))

(def orthographic-relative-coordinates
  (relative-scale-transformer [-1.0 -1.0] [1.0 1.0]))
(def reverse-orthographic-relative-coordinates
  (reverse-relative-scale-transformer [-1.0 -1.0] [1.0 1.0]))
