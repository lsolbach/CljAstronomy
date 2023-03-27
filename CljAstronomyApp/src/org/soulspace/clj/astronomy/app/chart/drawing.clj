(ns org.soulspace.clj.astronomy.app.chart.drawing
  (:require [org.soulspace.math.core :as m]
            [clojure.string :as str]
            [org.soulspace.clj.string :as sstr]
            [org.soulspace.clj.java.awt.graphics :as agfx]
            [org.soulspace.clj.astronomy.coordinates.coordinates :as coord]
            [org.soulspace.clj.astronomy.app.data.common :as adc]
            [org.soulspace.clj.astronomy.app.chart.common :as cco])
  (:import [java.awt Graphics2D Color])
  )

(defn color-by-spectral-type
  "Calculates the color in the chart based on the spectral type of the star."
  [spectral-type]
  (if (empty? spectral-type)
    (cco/chart-colors :white)
    (get cco/simple-star-color-map
         (sstr/substring  0 1 (sstr/first-upper-case spectral-type))
         (cco/chart-colors :white))))

(defn diameter-by-mag
  "Calculates the diameter in the chart based on the magnitude of the star."
  ([mag]
   (diameter-by-mag mag 6.0))
  ([mag min-mag]
   (+ 1 (* -1.5 (- mag min-mag)))))

(defn draw-chart-background
  "Draws the chart background."
  [^java.awt.Graphics2D gfx [x-max y-max]]
  (agfx/fill-colored-rect gfx 0 0 x-max y-max (cco/chart-colors :black)))

;"Draws a deep sky object."
(defmulti draw-dso (fn [gfx scale dso] (:type dso)) :hierarchy #'adc/object-hierarchy)

(defmethod draw-dso :star [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra-rad dso) (:dec-rad dso)])
        dia (diameter-by-mag (:mag dso))
        col (color-by-spectral-type (:spectral-type dso))
        radius (/ dia 2)]
    (agfx/fill-circle gfx (- x radius) (- y radius) (+ dia 1) (cco/chart-colors :black)) ; draw an outer black filled circle
    (agfx/fill-circle gfx (- x radius) (- y radius) dia col))) ; draw an inner filled circle with a diameter based on mag and color

(defmethod draw-dso :galaxy [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra-rad dso) (:dec-rad dso)])
        col (cco/chart-colors :galaxy)]
    (agfx/draw-ellipse gfx (- x 5) (- y 3) 10 6 col)))

(defmethod draw-dso :open-cluster [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra-rad dso) (:dec-rad dso)])
        col (cco/chart-colors :open-cluster)]
    ;(println x y)
    (agfx/draw-circle gfx (- x 5) (- y 5) 10 col)))

(defmethod draw-dso :globular-cluster [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra-rad dso) (:dec-rad dso)])
        col (cco/chart-colors :open-cluster)]
    ;(println x y)
    (agfx/draw-circle gfx (- x 6) (- y 6) 12 col)
    (agfx/draw-line gfx (- x 6) y (+ x 6) y col)
    (agfx/draw-line gfx x (- y 6) x (+ y 6) col)))

(defmethod draw-dso :emission-nebula [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra-rad dso) (:dec-rad dso)])
        col (cco/chart-colors :emission-nebula)]
    ;(println x y)
    (agfx/draw-line gfx (- x 6) y x (- y 6) col)
    (agfx/draw-line gfx x (- y 6) (+ x 6) y col)
    (agfx/draw-line gfx (+ x 6) y x (+ y 6) col)
    (agfx/draw-line gfx x (+ y 6) (- x 6) y col)))

(defmethod draw-dso :planetary-nebula [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra-rad dso) (:dec-rad dso)])
        col (cco/chart-colors :planetary-nebula)]
    (agfx/draw-circle gfx (- x 4) (- y 4) 8 col)
    (agfx/draw-line gfx (- x 6) y (+ x 6) y col)
    (agfx/draw-line gfx x (- y 6) x (+ y 6) col)))

(defmethod draw-dso :supernova-remnant [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra-rad dso) (:dec-rad dso)])
        col (cco/chart-colors :supernova-remnant)]
    (agfx/draw-line gfx (- x 6) y x (- y 6) col)
    (agfx/draw-line gfx x (- y 6) (+ x 6) y col)
    (agfx/draw-line gfx (+ x 6) y x (+ y 6) col)
    (agfx/draw-line gfx x (+ y 6) (- x 6) y col)))

(defmethod draw-dso :nebula [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra-rad dso) (:dec-rad dso)])
        col (cco/chart-colors :nebula)]
    (agfx/draw-line gfx (- x 6) y x (- y 6) col)
    (agfx/draw-line gfx x (- y 6) (+ x 6) y col)
    (agfx/draw-line gfx (+ x 6) y x (+ y 6) col)
    (agfx/draw-line gfx x (+ y 6) (- x 6) y col)))

(defmethod draw-dso :default [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra-rad dso) (:dec-rad dso)])
        col (cco/chart-colors :nebula)]
    (agfx/draw-line gfx (- x 6) y x (- y 6) col)
    (agfx/draw-line gfx x (- y 6) (+ x 6) y col)
    (agfx/draw-line gfx (+ x 6) y x (+ y 6) col)
    (agfx/draw-line gfx x (+ y 6) (- x 6) y col)))

(defn draw-dsos
  "Draws the collection of deep sky objects."
  [^java.awt.Graphics2D gfx scale dsos]
  (doseq [dso dsos]
    (draw-dso gfx scale dso)))

(defmulti draw-dso-label (fn [gfx scale dso] (:type dso)) :hierarchy #'adc/object-hierarchy)

(defmethod draw-dso-label :star
  [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra-rad dso) (:dec-rad dso)])
        col (color-by-spectral-type (:spectral-type dso))]
    (agfx/draw-string gfx (adc/object-label dso) (+ (int x) 10) (+ (int y) 10) col)))

(defmethod draw-dso-label :dso
  [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra-rad dso) (:dec-rad dso)])
        col (cco/chart-colors :white)]
    (agfx/draw-string gfx (adc/object-label dso) (+ (int x) 10) (+ (int y) 10) col)))

(defn draw-dso-labels
  "Draws the labels for the collection of stars."
  [^java.awt.Graphics2D gfx scale dsos]
  (doseq [dso dsos]
    (draw-dso-label gfx scale dso)))

(defn draw-chart-grid
  "Draws the chart grid."
  [^java.awt.Graphics2D gfx scale]
  (let [col (cco/chart-colors :grid)
        rad-0 (m/deg-to-rad 0)
        rad-45 (m/deg-to-rad 45)
        rad--45 (m/deg-to-rad -45)
        rad-90 (m/deg-to-rad 90)
        rad--90 (m/deg-to-rad -90)
        rad-135 (m/deg-to-rad 135)
        rad-180 (m/deg-to-rad 180)
        rad-225 (m/deg-to-rad 225)
        rad-270 (m/deg-to-rad 270)
        rad-315 (m/deg-to-rad 315)
        rad-360 (m/deg-to-rad 360)]
    ;
    (agfx/draw-line gfx (scale [rad-0 rad--45]) (scale [rad-360 rad--45]) col)
    (agfx/draw-line gfx (scale [rad-0 rad-0]) (scale [rad-360 rad-0]) col)
    (agfx/draw-line gfx (scale [rad-0 rad-45]) (scale [rad-360 rad-45]) col)
    ;
    (agfx/draw-line gfx (scale [rad-45 rad--90]) (scale [rad-45 rad-90]) col)
    (agfx/draw-line gfx (scale [rad-90 rad--90]) (scale [rad-90 rad-90]) col)
    (agfx/draw-line gfx (scale [rad-135 rad--90]) (scale [rad-135 rad-90]) col)
    (agfx/draw-line gfx (scale [rad-180 rad--90]) (scale [rad-180 rad-90]) col)
    (agfx/draw-line gfx (scale [rad-225 rad--90]) (scale [rad-225 rad-90]) col)
    (agfx/draw-line gfx (scale [rad-270 rad--90]) (scale [rad-270 rad-90]) col)
    (agfx/draw-line gfx (scale [rad-315 rad--90]) (scale [rad-315 rad-90]) col)))
