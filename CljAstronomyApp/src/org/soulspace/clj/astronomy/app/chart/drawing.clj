(ns org.soulspace.clj.astronomy.app.chart.drawing
  (:import [java.awt Graphics2D Color])
  (:use [org.soulspace.clj string]
        [org.soulspace.clj.java.awt graphics]
        [org.soulspace.clj.astronomy.app.data common constellations greek]
        [org.soulspace.clj.astronomy.app.chart common]))

(defn color-by-spectral-type
  "Calculates the color in the chart based on the spectral type of the star."
  [spectral-type]
  (if (empty? spectral-type)
    (chart-colors :white)
    (get simple-star-color-map (substring  0 1 (first-upper-case spectral-type)) (chart-colors :white))))

(defn diameter-by-mag
  "Calculates the diameter in the chart based on the magnitude of the star."
  ([mag]
    (diameter-by-mag mag 6.0))
  ([mag min-mag]
    (+ 1 (* -1.5 (- mag min-mag)))))


(def draw-dso-hierarchy ""
  (->
    (make-hierarchy)
    (derive :double-star :star)
    (derive :triple-star :star)
    (derive :multiple-star :star)
    (derive :variable-star :star)
    (derive :nebula :dso)
    (derive :galaxy :dso)
    (derive :open-cluster :dso)
    (derive :globular-cluster :dso)
    (derive :planetary-nebula :nebula)
    (derive :emission-nebula :nebula)
    (derive :reflection-nebula :nebula)
    (derive :supernova-remnant :nebula)
    (derive :dark-nebula :nebula)
    (derive :spiral-galaxy :galaxy)
    (derive :elliptical-galaxy :galaxy)
    (derive :lenticular-galaxy :galaxy)
    (derive :irregular-galaxy :galaxy)
    (derive :nebulous-open-cluster :open-cluster)
    (derive :star-cloud :open-cluster)
    (derive :galaxy-cloud :open-cluster)
    ))

;"Draws a deep sky object."
(defmulti draw-dso (fn [gfx scale dso] (:type dso)) :hierarchy #'draw-dso-hierarchy)

(defmethod draw-dso :star [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra dso) (:dec dso)])
        dia (diameter-by-mag (:mag dso))
        col (color-by-spectral-type (:spectral-type dso))
        radius (/ dia 2)]
    (fill-circle gfx (- x radius) (- y radius) (+ dia 1) (chart-colors :black)) ; draw an outer black filled circle
    (fill-circle gfx (- x radius) (- y radius) dia col))) ; draw an inner filled circle with a diameter based on mag and color

(defmethod draw-dso :galaxy [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra dso) (:dec dso)])
        col (chart-colors :galaxy)]
    (draw-ellipse gfx (- x 5) (- y 3) 10 6 col)))

(defmethod draw-dso :open-cluster [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra dso) (:dec dso)])
        col (chart-colors :open-cluster)]
    (draw-circle gfx (- x 5) (- y 5) 10 col)))

(defmethod draw-dso :globular-cluster [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra dso) (:dec dso)])
        col (chart-colors :open-cluster)]
    (draw-circle gfx (- x 6) (- y 6) 12 col)
    (draw-line gfx (- x 6) y (+ x 6) y col)
    (draw-line gfx x (- y 6) x (+ y 6) col)))

(defmethod draw-dso :emission-nebula [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra dso) (:dec dso)])
        col (chart-colors :emission-nebula)]
    (draw-line gfx (- x 6) y x (- y 6) col)
    (draw-line gfx x (- y 6) (+ x 6) y col)
    (draw-line gfx (+ x 6) y x (+ y 6) col)
    (draw-line gfx x (+ y 6) (- x 6) y col)))

(defmethod draw-dso :planetary-nebula [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra dso) (:dec dso)])
        col (chart-colors :planetary-nebula)]
    (draw-circle gfx (- x 4) (- y 4) 8 col)
    (draw-line gfx (- x 6) y (+ x 6) y col)
    (draw-line gfx x (- y 6) x (+ y 6) col)))

(defmethod draw-dso :supernova-remnant [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra dso) (:dec dso)])
        col (chart-colors :supernova-remnant)]
    (draw-line gfx (- x 6) y x (- y 6) col)
    (draw-line gfx x (- y 6) (+ x 6) y col)
    (draw-line gfx (+ x 6) y x (+ y 6) col)
    (draw-line gfx x (+ y 6) (- x 6) y col)))

(defmethod draw-dso :nebula [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra dso) (:dec dso)])
        col (chart-colors :nebula)]
    (draw-line gfx (- x 6) y x (- y 6) col)
    (draw-line gfx x (- y 6) (+ x 6) y col)
    (draw-line gfx (+ x 6) y x (+ y 6) col)
    (draw-line gfx x (+ y 6) (- x 6) y col)))

(defmethod draw-dso :default [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra dso) (:dec dso)])
        col (chart-colors :nebula)]
    (draw-line gfx (- x 6) y x (- y 6) col)
    (draw-line gfx x (- y 6) (+ x 6) y col)
    (draw-line gfx (+ x 6) y x (+ y 6) col)
    (draw-line gfx x (+ y 6) (- x 6) y col)))

(defn draw-dsos
  "Draws the collection of deep sky objects."
  [^java.awt.Graphics2D gfx scale dsos]
  (doseq [dso dsos]
    (draw-dso gfx scale dso)))

(defmulti draw-dso-label (fn [gfx scale dso] (:type dso)) :hierarchy #'draw-dso-hierarchy)

(defmethod draw-dso-label :star
  [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra dso) (:dec dso)])
        col (color-by-spectral-type (:spectral-type dso))]
    (draw-string gfx (star-label dso) (+ (int x) 10) (+ (int y) 10)) col))

(defmethod draw-dso-label :dso
  [^java.awt.Graphics2D gfx scale dso]
  (let [[x y] (scale [(:ra dso) (:dec dso)])
        col (color-by-spectral-type (:spectral-type dso))]
    (draw-string gfx (dso-label dso) (+ (int x) 10) (+ (int y) 10)) col))

(defn draw-dso-labels
  "Draws the labels for the collection of stars."
  [^java.awt.Graphics2D gfx scale dsos]
  (doseq [dso dsos]
    (draw-dso-label gfx scale dso)))

(defn draw-chart-grid
  "Draws the chart grid."
  [^java.awt.Graphics2D gfx scale]
  (let [col (chart-colors :grid)]
    (draw-line gfx (scale [0 -45]) (scale [360 -45]) col)
    (draw-line gfx (scale [0 0]) (scale [360 0]) col)
    (draw-line gfx (scale [0 45]) (scale [360 45]) col)
    ;(draw-line gfx (scale [0 -90]) (scale [0 90]) col)
    (draw-line gfx (scale [3 -90]) (scale [3 90]) col)
    (draw-line gfx (scale [6 -90]) (scale [6 90]) col)
    (draw-line gfx (scale [9 -90]) (scale [9 90]) col)
    (draw-line gfx (scale [12 -90]) (scale [12 90]) col)
    (draw-line gfx (scale [15 -90]) (scale [15 90]) col)
    (draw-line gfx (scale [18 -90]) (scale [18 90]) col)
    (draw-line gfx (scale [21 -90]) (scale [21 90]) col)))

