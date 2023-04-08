;;;;
;;;;   Copyright (c) Ludger Solbach. All rights reserved.
;;;;
;;;;   The use and distribution terms for this software are covered by the
;;;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;;;   which can be found in the file license.txt at the root of this distribution.
;;;;   By using this software in any fashion, you are agreeing to be bound by
;;;;   the terms of this license.
;;;;
;;;;   You must not remove this notice, or any other, from this software.
;;;;

(ns org.soulspace.astronomy.app.ui.swing.map
  (:require [clojure.java.io :as io]
            [org.soulspace.clj.math.core :as m]
            [org.soulspace.clj.java.awt.core :as awt]
            [org.soulspace.clj.java.awt.graphics :as agfx]
            [org.soulspace.clj.java.swing.core :as swing]
            [org.soulspace.astronomy.coordinates :as cpr]
            [org.soulspace.astronomy.app.data.common :as adc]
            [org.soulspace.astronomy.app.chart.scaling :as csc]))

; TODO use global state atom with get-in/update-in

(def moon-texture "/textures/albers/moon.jpg")
(def moon-image (agfx/buffered-image (io/as-file (str adc/data-dir moon-texture))))

(def jupiter-texture "/textures/albers/jupiter.jpg")
(def jupiter-image (agfx/buffered-image (io/as-file (str adc/data-dir jupiter-texture))))

(def map-panel-spec {:x-max 360 :y-max 360})

(def azimutal-user-coordinates (csc/user-coordinate-transformer [(:x-max map-panel-spec) (:y-max map-panel-spec)]))
(def reverse-azimutal-user-coordinates (csc/reverse-user-coordinate-transformer [(:x-max map-panel-spec) (:y-max map-panel-spec)]))

(def source-coordinates (csc/user-coordinate-transformer [4096 2048]))
(def source-scale (csc/relative-scale-transformer [0 (/ m/PI -2)] [(* m/PI 2) (/ m/PI 2)]))

(defn orthographic-scale
  "Scales ra/dec coordinates into user coordinates for drawing using an orthographic projection."
  [[long lat]]
  (azimutal-user-coordinates (cpr/orthographic-relative-coordinates (cpr/equatorial-orthographic-projector [long lat]))))

(defn reverse-orthographic-scale
  "Scales x/y coordinates into ra/dec coordinates using a reverse orthographic projection."
  [[x y]]
  (cpr/equatorial-reverse-orthographic-projector (cpr/reverse-orthographic-relative-coordinates (reverse-azimutal-user-coordinates [x y]))))

(defn projected-image
  "Returns a projected image of the source img."
  [img w h]
  (let [bi (agfx/buffered-image w h (agfx/image-type :3byte-bgr))
        pixels (for [x (range 0 w) y (range 0 h)]
                 [x y])]
      (doseq [pixel pixels]
        (agfx/set-rgb bi pixel (agfx/get-rgb img (reverse-orthographic-scale pixel))))
;        (set-rgb bi pixel (get-rgb img (reverse-orthographic-scale pixel))))
;        (set-rgb bi pixel (get-rgb img pixel)))
      bi))

(defn projected-moon-image [] (projected-image moon-image (:x-max map-panel-spec) (:y-max map-panel-spec)))

(defn draw-map
  "Draws an orthoscopic map projection of the image."
  ([^java.awt.Graphics2D gfx]
    (agfx/draw-image gfx (projected-moon-image) 0 0))
  ([^java.awt.Graphics2D gfx image]
    ; map pixel
    ))

(defn moon-dialog
  "Creates the moon dialog."
  ([parent]
    (let [p (swing/canvas-panel draw-map {:preferredSize (awt/dimension (:x-max map-panel-spec) (:y-max map-panel-spec))
                                    :maximumSize (awt/dimension (:x-max map-panel-spec) (:y-max map-panel-spec))}
                          [])
          d (swing/dialog {} [(swing/scroll-pane p)])]
      (.setVisible d true)
      )))
