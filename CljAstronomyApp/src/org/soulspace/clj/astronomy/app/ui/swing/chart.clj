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

(defn paint-buffered-image
  "Paint the chart."
  [^java.awt.Graphics2D g]
  (with-graphics-context g
    (fill-colored-rect 0 0 720 360 (color-by-name :black))
    ))

(defn filter-panel
  []
  )

(defn star-chart-frame
  []
  (frame {:layout (mig-layout {:layoutConstraints "wrap 1, insets 10, fill, top"})
                       :preferredSize (dimension 740 380)}
         [(canvas-panel paint-buffered-image
                      {:preferredSize (dimension 720 360)}
                      [])]))
