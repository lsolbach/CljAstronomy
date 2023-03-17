;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.app.ui.swing.charts.stereographic
  (:require [org.soulspace.math.core :as m])
  (:use [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event graphics]
        [org.soulspace.clj.java.swing constants event swinglib]
        [org.soulspace.clj.astronomy.coordinates projection]
        [org.soulspace.clj.astronomy.app common i18n]
        [org.soulspace.clj.astronomy.app.chart common drawing scaling]
        [org.soulspace.clj.astronomy.app.data catalogs filters common constellations greek]
        [org.soulspace.clj.astronomy.app.ui.swing common]
        [org.soulspace.clj.astronomy.app.ui.swing.charts common]))


; references to the chart data
(def stereographic-panel-spec (ref {:x-max 1440 :y-max 1440}))
(def stereographic-chart-objects (ref []))

; accessors for the chart data
(defn- panel-spec [] @stereographic-panel-spec)
(defn- panel-dimension [] (let [{:keys [x-max y-max]} @stereographic-panel-spec]
                            [x-max y-max]))

(def stereographic-user-coordinates (user-coordinate-transformer (panel-dimension)))
(def reverse-stereographic-user-coordinates (reverse-user-coordinate-transformer (panel-dimension)))

(defn stereographic-scale
  "Scales ra/dec coordinates into user coordinates for drawing using a stereographic projection."
  [[long lat]]
  ;(stereographic-user-coordinates (stereographic-relative-coordinates (north-pole-stereographic-projector [long lat])))
  (-> [long lat]
    home-stereographic-projector
    stereographic-relative-coordinates
    stereographic-user-coordinates))

(defn reverse-stereographic-scale
  "Scales x/y coordinates into ra/dec coordinates using a reverse stereographic projection."
  [[x y]]
  ;(north-pole-reverse-stereographic-projector (reverse-stereographic-relative-coordinates (reverse-stereographic-user-coordinates [x y])))
  (-> [x y]
    reverse-stereographic-user-coordinates
    reverse-stereographic-relative-coordinates
    home-reverse-stereographic-projector))


(defn draw-stereographic-chart-background
  "Draws the chart background."
  [^java.awt.Graphics2D gfx]
  (fill-colored-rect gfx 0 0 (:x-max (panel-spec)) (:y-max (panel-spec)) (chart-colors :black)))

(defn draw-stereographic-chart
  "Draw the stereographic star chart."
  [^java.awt.Graphics2D gfx]
  (set-rendering-hint gfx (rendering-hint-keys :antialialising) (antialias-hints :on))
  (draw-stereographic-chart-background gfx)
  ;(draw-chart-grid gfx)
  ;TODO use transducers
  (draw-dso-labels gfx stereographic-scale
                   (filter common-name?
                           (filter (angular-distance-filter (/ pi 2) home)
                                   (filter (mag-filter 6) (get-deep-sky-objects)))))
  (draw-dso-labels gfx stereographic-scale
                   (filter common-name?
                     (filter (angular-distance-filter (/ pi 2) home)
                       (filter (mag-filter 2) (get-stars)))))
  (draw-dsos gfx stereographic-scale
             (filter (angular-distance-filter (/ pi 2) home)
                     (filter (mag-filter 10) (get-deep-sky-objects))))
  (draw-dsos gfx stereographic-scale
             (filter (angular-distance-filter (/ pi 2) home)
                     (filter (mag-filter 7) (get-stars)))))

(defn stereographic-star-chart-panel
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

(defn stereographic-star-chart-dialog
  "Creates the star chart dialog."
  []
  (let [panel (stereographic-star-chart-panel draw-stereographic-chart (panel-spec))
        d (dialog {:title (i18n "label.chart.stereographic.title")} [(scroll-pane panel)])]
    (add-mouse-listener panel
                        (mouse-clicked-listener chart-panel-mouse-clicked d reverse-stereographic-scale (filter (mag-filter 10.5) (get-deep-sky-objects))))
    d))

(def stereographic-star-chart-action
  (action (fn [_]
            (let [chart-dialog (stereographic-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (i18n "action.view.starchart.stereographic")
           :accelerator (key-stroke \c :ctrl)
           :mnemonic nil}))
