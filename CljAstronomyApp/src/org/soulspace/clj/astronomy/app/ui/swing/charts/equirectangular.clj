;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.app.ui.swing.charts.equirectangular
  (:use [org.soulspace.clj.math math java-math]
        [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event graphics]
        [org.soulspace.clj.java.swing constants event swinglib]
        [org.soulspace.clj.astronomy.coordinates projection]
        [org.soulspace.clj.astronomy.app common i18n]
        [org.soulspace.clj.astronomy.app.chart common drawing scaling]
        [org.soulspace.clj.astronomy.app.data catalogs filters common constellations greek]
        [org.soulspace.clj.astronomy.app.ui.swing common]
        [org.soulspace.clj.astronomy.app.ui.swing.charts common]
        ))

; references to the chart data
(def equirectangular-chart-spec (ref {:ra 0.0
                                      :dec pi
                                      :scale 1
                                      :aspect 1
                                      :star-mag-brightest -30.0
                                      :star-mag-faintest 6.0
                                      :ds-mag-brightest 0.0
                                      :ds-mag-faintest 10.0}))
(def equirectangular-chart-objects (ref []))
(def equirectangular-panel-spec (ref {:x-max 5760 :y-max 2880}))

; accessors for the chart data
(defn- chart-spec [] @equirectangular-chart-spec)
(defn- panel-spec [] @equirectangular-panel-spec)
(defn- panel-dimension [] (let [{:keys [x-max y-max]} @equirectangular-panel-spec]
                            [x-max y-max]))

(def rectangular-user-coordinates (user-coordinate-transformer (panel-dimension)))
(def reverse-rectangular-user-coordinates (reverse-user-coordinate-transformer (panel-dimension)))

(defn equirectangular-scale
  "Scales ra/dec coordinates into user coordinates for drawing using a rectangular mapping."
  [[long lat]]
  (rectangular-user-coordinates (relative-coordinates [long lat])))

(defn reverse-equirectangular-scale
  "Scales x/y coordinates into ra/dec coordinates using a reverse equirectangular projection."
  [[x y]]
  (reverse-relative-coordinates (reverse-rectangular-user-coordinates [x y])))

(defn- draw-equirectangular-chart-background
  "Draws the chart background."
  [^java.awt.Graphics2D gfx]
  (fill-colored-rect gfx 0 0 (:x-max (panel-spec)) (:y-max (panel-spec)) (chart-colors :black)))

(defn draw-equirectangular-chart
  "Draw the rectangular star chart."
  [^java.awt.Graphics2D gfx]
  (set-rendering-hint gfx (rendering-hint-keys :antialialising) (antialias-hints :on))
  (draw-equirectangular-chart-background gfx)
  (draw-chart-grid gfx equirectangular-scale)
  (draw-dso-labels gfx equirectangular-scale (filter common-name? (filter (mag-filter 4) (get-stars))))
  (draw-dso-labels gfx equirectangular-scale (filter common-name? (filter (mag-filter 6) (get-deep-sky-objects))))
  (draw-dsos gfx equirectangular-scale (filter (mag-filter 10.5) (get-deep-sky-objects)))
  (draw-dsos gfx equirectangular-scale (filter (mag-filter 6.5) (get-stars))))

(defn equirectangular-star-chart-panel
  "Creates the star chart panel."
  [f panel-spec]
  (let [panel (canvas-panel f {:minimumSize (dimension 360 180)
                               :maximumSize (dimension (:x-max panel-spec) (:y-max panel-spec))
                               :preferredSize (dimension (:x-max panel-spec) (:y-max panel-spec))}
                            [])]
    (add-key-binding panel "srcoll-up" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_UP 0) up-action)
    (add-key-binding panel "srcoll-down" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_DOWN 0) down-action)
    (add-key-binding panel "srcoll-left" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_LEFT 0) left-action)
    (add-key-binding panel "srcoll-right" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_RIGHT 0) right-action)
    panel))

(defn equirectangular-star-chart-dialog
  "Creates the star chart dialog."
  []
  (let [panel (equirectangular-star-chart-panel draw-equirectangular-chart (panel-spec))
        d (dialog {:title (i18n "label.chart.equirectangular.title")} [(scroll-pane panel)])]
    (add-mouse-listener panel 
                        (mouse-clicked-listener chart-panel-mouse-clicked d reverse-equirectangular-scale (filter (mag-filter 10.5) (get-deep-sky-objects))))
    d))