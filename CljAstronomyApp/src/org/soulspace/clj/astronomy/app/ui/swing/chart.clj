(ns org.soulspace.clj.astronomy.app.ui.swing.chart
    (:use [clojure.java.io]
          [org.soulspace.clj string]
          [org.soulspace.clj.java awt]
          [org.soulspace.clj.java.awt event graphics]
          [org.soulspace.clj.java.swing constants event swinglib]
          [org.soulspace.clj.math math java-math]
          [org.soulspace.clj.application classpath]
          [org.soulspace.clj.astronomy.coordinates projection]
          [org.soulspace.clj.astronomy.app i18n]
          [org.soulspace.clj.astronomy.app.chart common drawing scaling]
          [org.soulspace.clj.astronomy.app.data catalogs common constellations greek])
    (:import [javax.swing Action BorderFactory JFrame]))

; TODO make charts configurable

;(def panel-spec {:x-max 2880 :y-max 2880})
;(def panel-spec {:x-max 1440 :y-max 1440})

(def panel-spec {:x-max 5760 :y-max 2880})
;(def panel-spec {:x-max 2880 :y-max 1440})
;(def panel-spec {:x-max 1440 :y-max 720})

(defn user-coordinate-transformer
  "Returns a scaling transformer function which scales the coordinates in the intervall [0,1]
   to java user coordinates in the intervalls [0,width] and [0,height]. [0,0] represents the top left."
  ([[width height]]
    (user-coordinate-transformer width height))
  ([width height]
    (fn [[x y]]
      [(* (+ (* -1 x) 1) width)
       (* (+ (* -1 y) 1) height)])))

(def user-coordinates (user-coordinate-transformer (:x-max panel-spec) (:y-max panel-spec)))

(defn equirectangular-scale
  "Scales ra/dec coordinates into user coordinates for drawing using a rectangular mapping."
  [p]
  (user-coordinates (relative-coordinates p)))

(defn orthoscopic-scale
  "Scales ra/dec coordinates into user coordinates for drawing using an orthoscopic projection."
  [[long lat]]
  (user-coordinates (orthoscopic-relative-coordinates (north-pole-orthoscopic-projector [(deg-to-rad (* 15 long)) (deg-to-rad lat)]))))

(defn stereoscopic-scale
  "Scales ra/dec coordinates into user coordinates for drawing using a stereoscopic projection."
  [[long lat]]
  (user-coordinates (stereoscopic-relative-coordinates (north-pole-stereoscopic-projector [(deg-to-rad (* 15 long)) (deg-to-rad lat)]))))

(def chart-spec (ref {:ra 0.0
                      :dec pi
                      :scale 1
                      :aspect 1
                      :star-mag-brightest -30.0
                      :star-mag-faintest 6.0
                      :ds-mag-brightest 0.0
                      :ds-mag-faintest 10.0
                      :projection ""
                      }))

; TODO add transactional spec updating
(defn update-scaler
  ""
  [chart-spec scaler]
  (assoc chart-spec :scaler scaler))

(defn draw-chart-background
  "Draws the chart background."
  [^java.awt.Graphics2D gfx]
  (fill-colored-rect gfx 0 0 (:x-max panel-spec) (:y-max panel-spec) (chart-colors :black)))

(defn draw-equirectangular-chart
  "Draw the rectangular star chart."
  [^java.awt.Graphics2D gfx]
  (set-rendering-hint gfx (rendering-hint-keys :antialialising) (antialias-hints :on))
  (draw-chart-background gfx)
  (draw-chart-grid gfx equirectangular-scale)
  (draw-dsos gfx equirectangular-scale (filter (mag-filter 7) deep-sky-catalog))
  (draw-dsos gfx equirectangular-scale (filter (mag-filter 6) star-catalog))
  (draw-dso-labels gfx equirectangular-scale (filter has-common-name? (filter (mag-filter 4) star-catalog))))

(defn draw-orthoscopic-chart
  "Draw the orthoscopic star chart."
  [^java.awt.Graphics2D gfx]
  (set-rendering-hint gfx (rendering-hint-keys :antialialising) (antialias-hints :on))
  (draw-chart-background gfx)
  (draw-dsos gfx orthoscopic-scale 
             (filter (ra-dec-filter [0.0 0.0] [24.0 90.0])
                     (filter (mag-filter 7) deep-sky-catalog)))
  (draw-dsos gfx orthoscopic-scale 
             (filter (ra-dec-filter [0.0 0.0] [24.0 90.0]) 
                     (filter (mag-filter 6) star-catalog)))
  (draw-dso-labels gfx orthoscopic-scale 
                   (filter has-common-name? 
                           (filter (ra-dec-filter [0.0 0.0] [24.0 90.0]) 
                                   (filter (mag-filter 4) star-catalog)))))

(defn draw-stereoscopic-chart
  "Draw the orthoscopic star chart."
  [^java.awt.Graphics2D gfx]
  (set-rendering-hint gfx (rendering-hint-keys :antialialising) (antialias-hints :on))
  (draw-chart-background gfx)
  ;(draw-chart-grid gfx)
  (draw-dsos gfx stereoscopic-scale 
             (filter (ra-dec-filter [0.0 0.0] [24.0 90.0])
                     (filter (mag-filter 7) deep-sky-catalog)))
  (draw-dsos gfx stereoscopic-scale 
             (filter (ra-dec-filter [0.0 0.0] [24.0 90.0]) 
                     (filter (mag-filter 6) star-catalog)))
  (draw-dso-labels gfx stereoscopic-scale 
                   (filter has-common-name? 
                           (filter (ra-dec-filter [0.0 0.0] [24.0 90.0]) 
                                   (filter (mag-filter 4) star-catalog)))))

(defn chart-filter-panel
  "Creates the chart filter panel"
  []
  (let [f-faintest-stellar-mag (number-field {})
        f-faintest-dso-mag (number-field {})])
  )

(def up-action (action (fn [_] (println "UP"))))
(def down-action (action (fn [_] (println "DOWN"))))
(def left-action (action (fn [_] (println "LEFT"))))
(def right-action (action (fn [_] (println "RIGHT"))))

(defn star-chart-panel
  "Creates the star chart panel."
  [f panel-spec]
  (let [panel (canvas-panel f {:minimumSize (dimension 72 36)
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
  (let [d (dialog {} [(scroll-pane (star-chart-panel draw-equirectangular-chart {:x-max 5760 :y-max 2880}))])]
    d))

(defn orthoscopic-star-chart-dialog
  "Creates the star chart dialog."
  []
  (let [d (dialog {} [(scroll-pane (star-chart-panel draw-orthoscopic-chart {:x-max 1440 :y-max 1440}))])]
    d))

(defn stereoscopic-star-chart-dialog
  "Creates the star chart dialog."
  []
  (let [d (dialog {} [(scroll-pane (star-chart-panel draw-stereoscopic-chart {:x-max 1440 :y-max 1440}))])]
    d))
