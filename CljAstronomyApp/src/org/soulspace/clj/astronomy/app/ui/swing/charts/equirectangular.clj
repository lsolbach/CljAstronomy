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
        [org.soulspace.clj.astronomy.app.ui.swing.charts common]))


; references to the chart data
(def equirectangular-chart-objects (ref []))

(def equirectangular-panel-spec (ref {:x-max 2880 :y-max 1440}))

; accessors for the chart data
(defn- panel-spec [] @equirectangular-panel-spec)
(defn- panel-dimension [] (let [{:keys [x-max y-max]} @equirectangular-panel-spec]
                            [x-max y-max]))

(def rectangular-user-coordinates (user-coordinate-transformer (panel-dimension)))
(def reverse-rectangular-user-coordinates (reverse-user-coordinate-transformer (panel-dimension)))

(defn equirectangular-scale
  "Scales ra/dec coordinates into user coordinates for drawing using a rectangular mapping."
  [[long lat]]
;  (rectangular-user-coordinates (relative-coordinates [long lat]))
  (-> [long lat]
    relative-coordinates
    rectangular-user-coordinates))

(defn reverse-equirectangular-scale
  "Scales x/y coordinates into ra/dec coordinates using a reverse equirectangular projection."
  [[x y]]
;  (reverse-relative-coordinates (reverse-rectangular-user-coordinates [x y]))
  (-> [x y]
    reverse-rectangular-user-coordinates
    reverse-relative-coordinates))

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
  (draw-dso-labels gfx equirectangular-scale (filter (mag-filter 3) (get-stars)))
  (draw-dso-labels gfx equirectangular-scale (filter common-name? (filter (mag-filter 6) (get-deep-sky-objects))))
  (draw-dsos gfx equirectangular-scale (filter (mag-filter 8) (get-deep-sky-objects)))
  (draw-dsos gfx equirectangular-scale (filter (mag-filter 6) (get-stars))))

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
        popup-menu (chart-popup-menu)
        d (dialog {:title (i18n "label.chart.equirectangular.title")} [(scroll-pane panel)])]
    (add-mouse-listener panel
                        (mouse-clicked-listener chart-panel-mouse-clicked d reverse-equirectangular-scale (filter (mag-filter 10.5) (get-deep-sky-objects))))
    (.setComponentPopupMenu panel popup-menu)
    (add-mouse-listener panel (popup-listener popup-menu))
    d))

(def equirectangular-star-chart-action
  (action (fn [_]
            (let [chart-dialog (equirectangular-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (i18n "action.view.starchart.equirectangular")
           :accelerator (key-stroke \c :ctrl)
           :mnemonic nil}))
