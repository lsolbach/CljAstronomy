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

(ns org.soulspace.astronomy.app.ui.swing.charts
  (:require [org.soulspace.math.core :as m]
            [org.soulspace.clj.java.awt.core :as awt]
            [org.soulspace.clj.java.awt.events :as aevt]
            [org.soulspace.clj.java.awt.graphics :as agfx]
            [org.soulspace.clj.java.swing.core :as swing]
            [org.soulspace.clj.java.swing.events :as sevt]
            [org.soulspace.astronomy.app.common :as app]
            [org.soulspace.astronomy.app.data.common :as adc]
            [org.soulspace.astronomy.app.data.hyg-dso-catalog :as chdc]
            [org.soulspace.astronomy.app.data.hyg-star-catalog :as chsc]
            [org.soulspace.astronomy.app.data.messier-catalog :as cmes]
            [org.soulspace.astronomy.app.chart.common :as cco]
            [org.soulspace.astronomy.app.chart.drawing :as cdr]
            [org.soulspace.astronomy.app.chart.scaling :as csc]
            [org.soulspace.astronomy.app.ui.swing.common :as swc]
            [org.soulspace.astronomy.app.ui.swing.objects :as oi])
  (:import [java.awt.event MouseAdapter]))


; TODO
; define protocol for charts
; ref fields for changeable stuff like chart specs, current objects and scaling functions, etc.
; functions for chart actions like scrolling, zooming, etc.

; implementation of common functions here in common and usage in protocol implementations

(def chart-spec (ref {:faintest-stellar-mag 6.0
                                      :faintest-dso-mag 10.0}))



(def up-action (swing/action (fn [_] (println "UP"))))
(def down-action (swing/action (fn [_] (println "DOWN"))))
(def left-action (swing/action (fn [_] (println "LEFT"))))
(def right-action (swing/action (fn [_] (println "RIGHT"))))
(def zoom-in-action (swing/action (fn [_] (println "ZOOM IN"))))
(def zoom-out-action (swing/action (fn [_] (println "ZOOM OUT"))))

; TODO actions for toggling stars, deep sky objects, solar system objects, etc
; TODO actions for raising/lowering brightness limits

(defn chart-filter-dialog
  "Creates the chart filter panel."
  ([]
   (chart-filter-dialog chart-spec))
  ([chart-spec]
   (let [f-faintest-stellar-mag (swing/number-field {:columns 5})
         f-faintest-dso-mag (swing/number-field {:columns 5})
         b-ok (swing/button {})
         b-cancel (swing/button {})
         p (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                                    :columnConstraints "[left|grow]"})}
                        [[(swing/label {:text (app/i18n "label.chart.filter.title") :font swc/heading-font}) "left, wrap 10"]
                         (swing/label {:text (app/i18n "label.chart.filter.mag-faintest-stars")}) f-faintest-stellar-mag
                         (swing/label {:text (app/i18n "label.chart.filter.mag-faintest-dsos")}) f-faintest-dso-mag])
         filter-dialog (swing/dialog {}
                                     [p])]
     (defn update-chart-filter-panel
       "Updates the panel with the given chart spec."
       [chart-spec]
       (.setText f-faintest-stellar-mag (chart-spec :faintest-stellar-mag))
       (.setText f-faintest-dso-mag (chart-spec :faintest-dso-mag)))

     (defn read-chart-filter-panel
       "Reads the panel values and returns a chart spec."
       []
       {:faintest-stellar-mag (swing/get-number f-faintest-stellar-mag)
        :faintest-dso-mag (swing/get-number f-faintest-dso-mag)})

     (defn chart-filter-ok
       "Ok behaviour of the filter dialog."
       [])

     (defn chart-filter-cancel
       "Cancel behaviour of the filter dialog."
       []
       (.setVisible filter-dialog false))
     (.setVisible filter-dialog true)
     filter-dialog)))

(defn- point-coordinates
  ""
  [point]
  [(.getX point) (.getY point)])

(defn chart-panel-mouse-clicked
  "Called when a mouse click happens in the chart panel."
  [event parent reverse-scale objects]
  (let [o (adc/find-object-by-coordinates (reverse-scale (point-coordinates (.getPoint event)))
                                      (filter (adc/in-mag-range? 10.5) objects))]
    (println o)
    (oi/object-info-dialog parent o)))

(defn chart-dialog-resized
  "Called when chart dialog is resized."
  [event args]
  (println "Resize" event))

(def chart-filter-action (swing/action (fn [e] (println "filter") (chart-filter-dialog)) {:name (app/i18n "action.chart.filter")}))
(def chart-info-action (swing/action (fn [e] (println "info")) {:name (app/i18n "action.chart.info")}))

(defn chart-popup-menu
  "Creates a popup menu for the star charts."
  []
  (swing/popup-menu
    {}
    [(swing/menu-item {:action chart-info-action})
     (swing/menu-item {:action chart-filter-action})]))

(defn- show-popup
  [popup event]
  (if (.isPopupTrigger event)
     (.show popup (.getComponent event) (.getX event) (.getY event))))

(defn popup-listener
  [popup]
  (proxy [MouseAdapter] []
    (mousePressed [event] (show-popup popup event))
    (mouseReleased [event] (show-popup popup event))))

(defn star-chart-panel
  "Creates the star chart panel."
  [f panel-spec]
  (let [panel (swing/canvas-panel f {:minimumSize (awt/dimension 360 180)
                               :maximumSize (awt/dimension (:x-max panel-spec) (:y-max panel-spec))
                               :preferredSize (awt/dimension (:x-max panel-spec) (:y-max panel-spec))}
                            [])
        popup-menu (chart-popup-menu)]
    (swing/add-key-binding panel "srcoll-up" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_UP 0) up-action)
    (swing/add-key-binding panel "srcoll-down" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_DOWN 0) down-action)
    (swing/add-key-binding panel "srcoll-left" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_LEFT 0) left-action)
    (swing/add-key-binding panel "srcoll-right" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_RIGHT 0) right-action)
    (swing/add-key-binding panel "zoom-in" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_PLUS 0) zoom-in-action)
    (swing/add-key-binding panel "zoom-out" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_MINUS 0) zoom-out-action)
    (.setComponentPopupMenu panel popup-menu)
    panel))


;;;
;;; Equirectangular chart
;;;

; references to the chart data
(def equirectangular-chart-objects (ref []))

(def equirectangular-panel-spec (ref {:x-max 2880 :y-max 1440}))

; accessors for the chart data
(defn- equirectangular-panel-dimension [] (let [{:keys [x-max y-max]} @equirectangular-panel-spec]
                            [x-max y-max]))

(def rectangular-user-coordinates (csc/user-coordinate-transformer (equirectangular-panel-dimension)))
(def reverse-rectangular-user-coordinates (csc/reverse-user-coordinate-transformer (equirectangular-panel-dimension)))

(defn equirectangular-scale
  "Scales ra/dec coordinates into user coordinates for drawing using a rectangular mapping."
  [[long lat]]
;  (rectangular-user-coordinates (relative-coordinates [long lat]))
  (-> [long lat]
      csc/relative-coordinates
      rectangular-user-coordinates))

(defn reverse-equirectangular-scale
  "Scales x/y coordinates into ra/dec coordinates using a reverse equirectangular projection."
  [[x y]]
;  (reverse-relative-coordinates (reverse-rectangular-user-coordinates [x y]))
  (-> [x y]
      reverse-rectangular-user-coordinates
      csc/reverse-relative-coordinates))

(defn draw-equirectangular-chart
  "Draw the rectangular star chart."
  [^java.awt.Graphics2D gfx]
  (let [stars (chsc/get-objects)
        dsos  (cmes/get-objects)]
    (agfx/set-rendering-hint gfx (agfx/rendering-hint-keys :antialialising) (agfx/antialias-hints :on))
    (cdr/draw-chart-background gfx (equirectangular-panel-dimension))
    (cdr/draw-chart-grid gfx equirectangular-scale)
    (cdr/draw-dso-labels gfx equirectangular-scale (filter (adc/in-mag-range? 3) stars))
    (cdr/draw-dso-labels gfx equirectangular-scale (filter adc/common-name? (filter (adc/in-mag-range? 6) dsos)))
    (cdr/draw-dsos gfx equirectangular-scale (filter (adc/in-mag-range? 8) dsos))
    (cdr/draw-dsos gfx equirectangular-scale (filter (adc/in-mag-range? 6) stars))))

(defn equirectangular-star-chart-panel
  "Creates the star chart panel."
  [f [x-max y-max]]
  (let [panel (swing/canvas-panel f {:minimumSize (awt/dimension 360 180)
                                     :maximumSize (awt/dimension x-max y-max)
                                     :preferredSize (awt/dimension x-max y-max)}
                            [])]
    (swing/add-key-binding panel "srcoll-up" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_UP 0) up-action)
    (swing/add-key-binding panel "srcoll-down" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_DOWN 0) down-action)
    (swing/add-key-binding panel "srcoll-left" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_LEFT 0) left-action)
    (swing/add-key-binding panel "srcoll-right" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_RIGHT 0) right-action)
    panel))

(defn equirectangular-star-chart-dialog
  "Creates the star chart dialog."
  []
  (let [panel (equirectangular-star-chart-panel draw-equirectangular-chart (equirectangular-panel-dimension))
        popup-menu (chart-popup-menu)
        d (swing/dialog {:title (app/i18n "label.chart.equirectangular.title")}
                        [(swing/scroll-pane panel)]) ]
    (aevt/add-mouse-listener panel
                        (aevt/mouse-clicked-listener chart-panel-mouse-clicked d reverse-equirectangular-scale
                                                                                  ;; => Syntax error compiling at (src/org/soulspace/clj/astronomy/app/ui/swing/charts.clj:1:8045).
                                                                                  ;;    Unable to resolve symbol: reverse-equirectangular-scale in this context
 (filter (adc/in-mag-range? 10.5) (cmes/get-objects))))
    (.setComponentPopupMenu panel popup-menu)
    (aevt/add-mouse-listener panel (popup-listener popup-menu))
    d))

(def equirectangular-star-chart-action
  (swing/action (fn [_]
            (let [chart-dialog (equirectangular-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (app/i18n "action.view.starchart.equirectangular")
           :accelerator (swing/key-stroke \c :ctrl)
           :mnemonic nil}))

;;;
;;; Stereographic chart
;;;

; references to the chart data
(def stereographic-panel-spec (ref {:x-max 1080 :y-max 1080}))
(def stereographic-chart-objects (ref []))

; accessors for the chart data
(defn- stereographic-panel-dimension
  []
  (let [{:keys [x-max y-max]} @stereographic-panel-spec]
    [x-max y-max]))

(def stereographic-user-coordinates (csc/user-coordinate-transformer (stereographic-panel-dimension)))
(def reverse-stereographic-user-coordinates (csc/reverse-user-coordinate-transformer (stereographic-panel-dimension)))

(defn stereographic-scale
  "Scales ra/dec coordinates into user coordinates for drawing using a stereographic projection."
  [[long lat]]
  ;(stereographic-user-coordinates (stereographic-relative-coordinates (north-pole-stereographic-projector [long lat])))
  (-> [long lat]
      csc/home-stereographic-projector
      csc/stereographic-relative-coordinates
      stereographic-user-coordinates))

(defn reverse-stereographic-scale
  "Scales x/y coordinates into ra/dec coordinates using a reverse stereographic projection."
  [[x y]]
  ;(north-pole-reverse-stereographic-projector (reverse-stereographic-relative-coordinates (reverse-stereographic-user-coordinates [x y])))
  (-> [x y]
      reverse-stereographic-user-coordinates
      csc/reverse-stereographic-relative-coordinates
      csc/home-reverse-stereographic-projector))


(defn draw-stereographic-chart
  "Draw the stereographic star chart."
  [^java.awt.Graphics2D gfx]
  (let [stars (chsc/get-objects)
        dsos  (cmes/get-objects)]
    (agfx/set-rendering-hint gfx (agfx/rendering-hint-keys :antialialising) (agfx/antialias-hints :on))
    (cdr/draw-chart-background gfx (stereographic-panel-dimension))
  ;(draw-chart-grid gfx)
  ;TODO use transducers
    (cdr/draw-dso-labels gfx stereographic-scale
                         (filter adc/common-name?
                                 (filter (adc/angular-distance-below? m/HALF-PI csc/home)
                                         (filter (adc/in-mag-range? 6) dsos))))
    (cdr/draw-dso-labels gfx stereographic-scale
                         (filter adc/common-name?
                                 (filter (adc/angular-distance-below? m/HALF-PI csc/home)
                                         (filter (adc/in-mag-range? 2) stars))))
    (cdr/draw-dsos gfx stereographic-scale
                   (filter (adc/angular-distance-below? m/HALF-PI csc/home)
                           (filter (adc/in-mag-range? 10) dsos)))
    (cdr/draw-dsos gfx stereographic-scale
                   (filter (adc/angular-distance-below? m/HALF-PI csc/home)
                           (filter (adc/in-mag-range? 7) stars)))))

(defn stereographic-star-chart-panel
  "Creates the star chart panel."
  [f panel-spec]
  (let [panel (swing/canvas-panel f {:minimumSize (awt/dimension 360 180)
                               :maximumSize (awt/dimension (:x-max panel-spec) (:y-max panel-spec))
                               :preferredSize (awt/dimension (:x-max panel-spec) (:y-max panel-spec))}
                            [])]
    (swing/add-key-binding panel "srcoll-up" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_UP 0) up-action)
    (swing/add-key-binding panel "srcoll-down" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_DOWN 0) down-action)
    (swing/add-key-binding panel "srcoll-left" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_LEFT 0) left-action)
    (swing/add-key-binding panel "srcoll-right" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_RIGHT 0) right-action)
    panel))

(defn stereographic-star-chart-dialog
  "Creates the star chart dialog."
  []
  (let [panel (stereographic-star-chart-panel draw-stereographic-chart @stereographic-panel-spec)
        d (swing/dialog {:title (app/i18n "label.chart.stereographic.title")} [(swing/scroll-pane panel)])]
    (aevt/add-mouse-listener panel
                        (aevt/mouse-clicked-listener chart-panel-mouse-clicked d reverse-stereographic-scale
                                                      (filter (adc/in-mag-range? 10.5) (cmes/get-objects))))
    d))

(def stereographic-star-chart-action
  (swing/action (fn [_]
            (let [chart-dialog (stereographic-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (app/i18n "action.view.starchart.stereographic")
           :accelerator (swing/key-stroke \c :ctrl)
           :mnemonic nil}))


;;;
;;; Ortographic chart
;;;

; references to the chart data
(def orthographic-panel-spec (ref {:x-max 1080 :y-max 1080}))
(def orthographic-chart-objects (ref []))

; accessors for the chart data
(defn- orthographic-panel-dimension [] (let [{:keys [x-max y-max]} @orthographic-panel-spec]
                                         [x-max y-max]))

(def orthographic-user-coordinates (csc/user-coordinate-transformer (orthographic-panel-dimension)))
(def reverse-orthographic-user-coordinates (csc/reverse-user-coordinate-transformer (orthographic-panel-dimension)))

(defn orthographic-scale
  "Scales ra/dec coordinates into user coordinates for drawing using an orthographic projection."
  [[long lat]]
;  (orthographic-user-coordinates (orthographic-relative-coordinates (north-pole-orthographic-projector [long lat])))
  (-> [long lat]
      csc/north-pole-orthographic-projector
      csc/orthographic-relative-coordinates
      orthographic-user-coordinates))

(defn reverse-orthographic-scale
  "Scales x/y coordinates into ra/dec coordinates using a reverse orthographic projection."
  [[x y]]
;  (north-pole-reverse-orthographic-projector (reverse-orthographic-relative-coordinates (reverse-orthographic-user-coordinates [x y])))
  (-> [x y]
      reverse-orthographic-user-coordinates
      csc/reverse-orthographic-relative-coordinates
      csc/north-pole-reverse-orthographic-projector))

(defn draw-orthographic-chart-background
  "Draws the chart background."
  [^java.awt.Graphics2D gfx]
  (agfx/fill-colored-rect gfx
                          0 0
                          (:x-max @orthographic-panel-spec)
                          (:y-max @orthographic-panel-spec)
                          (cco/chart-colors :black)))

(defn draw-orthographic-chart
  "Draw the orthographic star chart."
  [^java.awt.Graphics2D gfx]
  (let [stars (chsc/get-objects)
        dsos  (cmes/get-objects)]
  (agfx/set-rendering-hint gfx (agfx/rendering-hint-keys :antialialising) (agfx/antialias-hints :on))
  (cdr/draw-chart-background gfx (orthographic-panel-dimension))
  (cdr/draw-dso-labels gfx orthographic-scale
                   (filter adc/common-name?
                           (filter (adc/rad-ra-dec-filter [0.0 0.0] [m/DOUBLE-PI m/HALF-PI])
                                   (filter (adc/in-mag-range? 2) stars))))
  (cdr/draw-dso-labels gfx orthographic-scale
                   (filter adc/common-name?
                           (filter (adc/rad-ra-dec-filter [0.0 0.0] [m/DOUBLE-PI m/HALF-PI])
                                   (filter (adc/in-mag-range? 6) dsos))))
  (cdr/draw-dsos gfx orthographic-scale
             (filter (adc/rad-ra-dec-filter [0.0 0.0] [m/DOUBLE-PI m/HALF-PI])
                     (filter (adc/in-mag-range? 10) dsos)))
  (cdr/draw-dsos gfx orthographic-scale
             (filter (adc/rad-ra-dec-filter [0.0 0.0] [m/DOUBLE-PI m/HALF-PI])
                     (filter (adc/in-mag-range? 7) stars)))))

(defn orthographic-star-chart-panel
  "Creates the star chart panel."
  [f panel-spec]
  (let [panel (swing/canvas-panel f {:minimumSize (awt/dimension 360 180)
                               :maximumSize (awt/dimension (:x-max panel-spec) (:y-max panel-spec))
                               :preferredSize (awt/dimension (:x-max panel-spec) (:y-max panel-spec))}
                            [])]
    (swing/add-key-binding panel "srcoll-up" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_UP 0) up-action)
    (swing/add-key-binding panel "srcoll-down" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_DOWN 0) down-action)
    (swing/add-key-binding panel "srcoll-left" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_LEFT 0) left-action)
    (swing/add-key-binding panel "srcoll-right" (javax.swing.KeyStroke/getKeyStroke java.awt.event.KeyEvent/VK_RIGHT 0) right-action)
    panel))

(defn orthographic-star-chart-dialog
  "Creates the star chart dialog."
  []
  (let [panel (orthographic-star-chart-panel draw-orthographic-chart @orthographic-panel-spec)
        d (swing/dialog {:title (app/i18n "label.chart.orthographic.title")} [(swing/scroll-pane panel)])]
    (aevt/add-mouse-listener panel
                        (aevt/mouse-clicked-listener chart-panel-mouse-clicked d reverse-orthographic-scale
                                                      (filter (adc/in-mag-range? 10.5) (cmes/get-objects))))
    d))

(def orthographic-star-chart-action
  (swing/action (fn [_]
            (let [chart-dialog (orthographic-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (app/i18n "action.view.starchart.orthographic")
           :accelerator (swing/key-stroke \c :ctrl)
           :mnemonic nil}))
