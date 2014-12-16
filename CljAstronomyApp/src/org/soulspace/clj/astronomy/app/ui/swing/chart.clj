(ns org.soulspace.clj.astronomy.app.ui.swing.chart
    (:use [clojure.tools swing-utils]
          [clojure.java.io]
          [org.soulspace.clj.java awt]
          [org.soulspace.clj.java.awt event graphics]
          [org.soulspace.clj.java.swing constants swinglib]
          [org.soulspace.clj.application classpath]
          [org.soulspace.clj.astronomy.app.application i18n])
    (:import [javax.swing Action BorderFactory JFrame]))

(defn relative-scale-transformer
  [p-min p-max]
  (fn [p]
    {:x (/ (- (:x p) (:x p-min)) (- (:x p-max) (:x p-min)))
    :y (/ (- (:y p) (:y p-min)) (- (:y p-max) (:y p-min)))}))

(defn user-coordinate-transformer
  [width height]
  (fn [p]
    {:x (* {:x p} width) :y (* {:y p} height -1)}))


(defn draw-star
  ""
  [^java.awt.Graphics2D gfx star]
  (draw-point gfx (first star) (second star) (color 255 255 127))
  )

(defn draw-stars
  ""
  [^java.awt.Graphics2D gfx stars]
  (doseq [star (for [x (range 0 1440 72) y (range 0 720 72)]
                 [x y])]
    (draw-star gfx star))
  )

(defn draw-chart-background
  [^java.awt.Graphics2D gfx]
  )

(defn draw-chart
  "Draw the star chart."
  [^java.awt.Graphics2D gfx]
  (fill-colored-rect gfx 0 0 1440 720 (color 0 0 0))
  (draw-line gfx 0 360 1440 360 (color 127 127 127))
  (draw-line gfx 360 0 360 720 (color 127 127 127))
  (draw-line gfx 720 0 720 720 (color 127 127 127))
  (draw-line gfx 1080 0 1080 720 (color 127 127 127))
  (draw-line gfx 1440 0 1440 720 (color 127 127 127))
  (draw-stars gfx nil)
  )

(defn chart-filter-panel
  []
  )

(defn star-chart-panel
  []
  (canvas-panel draw-chart
                      {:preferredSize (dimension 1440 720)}
                      []))

(defn star-chart-frame
  []
)