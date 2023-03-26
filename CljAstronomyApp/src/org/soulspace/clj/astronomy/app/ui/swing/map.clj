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

(ns org.soulspace.clj.astronomy.app.ui.swing.map
  (:use [clojure.java.io :only [as-file]]
        [org.soulspace.clj.math math java-math]
        [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt graphics]
        [org.soulspace.clj.java.swing swinglib]
        [org.soulspace.clj.astronomy.coordinates projection]
        [org.soulspace.clj.astronomy.app.data common]
        [org.soulspace.clj.astronomy.app.chart scaling]))

(def moon-texture "/textures/albers/moon.jpg")
(def moon-image (buffered-image (as-file (str data-dir moon-texture))))

(def jupiter-texture "/textures/albers/jupiter.jpg")
(def jupiter-image (buffered-image (as-file (str data-dir jupiter-texture))))

(def map-panel-spec {:x-max 360 :y-max 360})

(def azimutal-user-coordinates (user-coordinate-transformer [(:x-max map-panel-spec) (:y-max map-panel-spec)]))
(def reverse-azimutal-user-coordinates (reverse-user-coordinate-transformer [(:x-max map-panel-spec) (:y-max map-panel-spec)]))

(def source-coordinates (user-coordinate-transformer [4096 2048]))
(def source-scale (relative-scale-transformer [0 (/ pi -2)] [(* pi 2) (/ pi 2)]))

(defn orthographic-scale
  "Scales ra/dec coordinates into user coordinates for drawing using an orthographic projection."
  [[long lat]]
  (azimutal-user-coordinates (orthographic-relative-coordinates (equatorial-orthographic-projector [long lat]))))

(defn reverse-orthographic-scale
  "Scales x/y coordinates into ra/dec coordinates using a reverse orthographic projection."
  [[x y]]
  (equatorial-reverse-orthographic-projector (reverse-orthographic-relative-coordinates (reverse-azimutal-user-coordinates [x y]))))

(defn projected-image
  "Returns a projected image of the source img."
  [img w h]
  (let [bi (buffered-image w h (image-type :3byte-bgr))
        pixels (for [x (range 0 w) y (range 0 h)]
                 [x y])]
      (doseq [pixel pixels]
        (set-rgb bi pixel (get-rgb img (reverse-orthographic-scale pixel))))
;        (set-rgb bi pixel (get-rgb img (reverse-orthographic-scale pixel))))
;        (set-rgb bi pixel (get-rgb img pixel)))
      bi))

(defn projected-moon-image [] (projected-image moon-image (:x-max map-panel-spec) (:y-max map-panel-spec)))

(defn draw-map
  "Draws an orthoscopic map projection of the image."
  ([^java.awt.Graphics2D gfx]
    (draw-image gfx (projected-moon-image) 0 0))
  ([^java.awt.Graphics2D gfx image]
    ; map pixel
    ))

(defn moon-dialog
  "Creates the moon dialog."
  ([parent]
    (let [p (canvas-panel draw-map {:preferredSize (dimension (:x-max map-panel-spec) (:y-max map-panel-spec))
                                    :maximumSize (dimension (:x-max map-panel-spec) (:y-max map-panel-spec))}
                          [])
          d (dialog {} [(scroll-pane p)])]
      (.setVisible d true)
      )))
