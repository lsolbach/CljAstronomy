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
            [org.soulspace.math.core :as m]
            [org.soulspace.clj.java.awt.core :as awt]
            [org.soulspace.clj.java.awt.graphics :as agfx]
            [org.soulspace.clj.java.swing.core :as swing]
            [org.soulspace.astronomy.coordinates :as coord]
            [org.soulspace.astronomy.app.data.common :as adc]
            [org.soulspace.astronomy.app.chart.scaling :as csc]))

; TODO use global state atom with get-in/update-in

(def moon-texture "/textures/albers/moon.jpg") ; (4096x2048 px)
(def moon-image (agfx/buffered-image (io/as-file (str adc/data-dir moon-texture))))

(def jupiter-texture "/textures/albers/jupiter.jpg") ; (4096x2048 px)
(def jupiter-image (agfx/buffered-image (io/as-file (str adc/data-dir jupiter-texture))))

(def map-panel-spec {:x-max 360 :y-max 360})

(def azimutal-user-coordinates (csc/user-coordinate-transformer [(:x-max map-panel-spec) (:y-max map-panel-spec)]))
(def reverse-azimutal-user-coordinates (csc/reverse-user-coordinate-transformer [(:x-max map-panel-spec) (:y-max map-panel-spec)]))

(def source-coordinates (csc/user-coordinate-transformer [4096 2048]))
(def source-scale (csc/relative-scale-transformer [0 (/ m/PI -2)] [(* m/PI 2) (/ m/PI 2)]))

;;
(comment
  (source-coordinates [30.69829878584778 0.0])
  (source-coordinates [-17.23840435334437 2.8659839825988644])
  )


(def map-panel-spec (ref {:x-max 360 :y-max 360}))
(defn- map-panel-dimension
  []
  (let [{:keys [x-max y-max]} @map-panel-spec]
    [x-max y-max]))

;;; TODO: Transformation process
;;;
;;; transform from texture size to ra/dec to check angular distance
;;; transform to texture to select pixel color
;;; orthographic projection
;;; transform projected coordinates to panel


(def orthographic-user-coordinates (csc/user-coordinate-transformer
                                    (map-panel-dimension)))
(def reverse-orthographic-user-coordinates (csc/reverse-user-coordinate-transformer
                                            (map-panel-dimension)))

(defn orthographic-scale
  "Scales ra/dec coordinates into user coordinates for drawing using an orthographic projection."
  [[long lat]]
  (->> [long lat]
     (map m/deg-to-rad)
      csc/equatorial-orthographic-projector
      csc/orthographic-relative-coordinates
      orthographic-user-coordinates))

(defn reverse-orthographic-scale
  "Scales x/y coordinates into ra/dec coordinates using a reverse orthographic projection."
  [[x y]]
  (->> [x y]
       reverse-orthographic-user-coordinates
       csc/reverse-orthographic-relative-coordinates
       csc/equatorial-reverse-orthographic-projector
       
       (map m/rad-to-deg)))

;;
(comment
  (defn orthographic-scale
    "Scales ra/dec coordinates into user coordinates for drawing using an orthographic projection."
    [[long lat]]
    (->>
     (map m/deg-to-rad)
     coord/equatorial-orthographic-projector
     coord/orthographic-relative-coordinates
     azimutal-user-coordinates))

  (defn reverse-orthographic-scale
    "Scales x/y coordinates into ra/dec coordinates using a reverse orthographic projection."
    [[x y]]
    (->> [x y]
         reverse-azimutal-user-coordinates
         coord/reverse-orthographic-relative-coordinates
         coord/equatorial-reverse-orthographic-projector
         (map m/rad-to-deg)))
)

(defn projected-image
  "Returns a projected image of the source img."
  ([img [w h]]
   (projected-image img w h))
  ([img w h]
   (let [bi (agfx/buffered-image w h (agfx/image-type :3byte-bgr))
         pixels (for [x (range 0 w) y (range 0 h)]
                  [x y])]
     (doseq [pixel pixels]
       (let [src-pixel (reverse-orthographic-scale pixel)]
         (if (< (coord/angular-distance src-pixel csc/north-pole) m/HALF-PI)
           (do
             (println "Pixel" pixel "Src" src-pixel)
             (agfx/set-rgb bi pixel (agfx/get-rgb img src-pixel)))
;        (set-rgb bi pixel (get-rgb img (reverse-orthographic-scale pixel))))
;        (set-rgb bi pixel (get-rgb img pixel)))
           )))
     bi)))

(defn projected-moon-image []
  (projected-image moon-image (map-panel-dimension)))

(defn draw-map
  "Draws an orthoscopic map projection of the image."
  ([^java.awt.Graphics2D gfx]
    (agfx/draw-image gfx (projected-moon-image) 0 0))
  ([^java.awt.Graphics2D gfx image]
    ; map pixel
    ))

(defn moon-dialog
  "Creates the moon dialog."
  ([]
    (let [w (:x-max @map-panel-spec)
          h (:y-max @map-panel-spec)
          p (swing/canvas-panel
             draw-map {:preferredSize (awt/dimension w h)
                       :maximumSize (awt/dimension w h)}
             [])
          d (swing/dialog {} [(swing/scroll-pane p)])]
      (.setVisible d true)
      )))
