(ns org.soulspace.clj.astronomy.app.ui.swing.chart
  (:import [javax.swing Action BorderFactory JFrame])
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
        [org.soulspace.clj.astronomy.app.data catalogs filters common constellations greek]
        [org.soulspace.clj.astronomy.app.ui.swing common object-info]))

; TODO make charts configurable
(def azimutal-panel-spec {:x-max 1440 :y-max 1440})
(def rectangular-panel-spec {:x-max 5760 :y-max 2880})

(def equirectangular-chart-spec (ref {:ra 0.0
                                      :dec pi
                                      :scale 1
                                      :aspect 1
                                      :star-mag-brightest -30.0
                                      :star-mag-faintest 6.0
                                      :ds-mag-brightest 0.0
                                      :ds-mag-faintest 10.0}))

(def orthoscopic-chart-spec (ref {:ra 0.0
                                  :dec pi
                                  :scale 1
                                  :aspect 1
                                  :star-mag-brightest -30.0
                                  :star-mag-faintest 6.0
                                  :ds-mag-brightest 0.0
                                  :ds-mag-faintest 10.0}))

(def stereoscopic-chart-spec (ref {:ra 0.0
                                   :dec pi
                                   :scale 1
                                   :aspect 1
                                   :star-mag-brightest -30.0
                                   :star-mag-faintest 6.0
                                   :ds-mag-brightest 0.0
                                   :ds-mag-faintest 10.0}))

(def rectangular-user-coordinates (user-coordinate-transformer [(:x-max rectangular-panel-spec) (:y-max rectangular-panel-spec)]))
(def reverse-rectangular-user-coordinates (reverse-user-coordinate-transformer [(:x-max rectangular-panel-spec) (:y-max rectangular-panel-spec)]))

(def azimutal-user-coordinates (user-coordinate-transformer [(:x-max azimutal-panel-spec) (:y-max azimutal-panel-spec)]))
(def reverse-azimutal-user-coordinates (reverse-user-coordinate-transformer [(:x-max azimutal-panel-spec) (:y-max azimutal-panel-spec)]))

(defn equirectangular-scale
  "Scales ra/dec coordinates into user coordinates for drawing using a rectangular mapping."
  [[long lat]]
  (rectangular-user-coordinates (relative-coordinates [long lat])))

(defn reverse-equirectangular-scale
  "Scales x/y coordinates into ra/dec coordinates using a reverse equirectangular projection."
  [[x y]]
  (reverse-relative-coordinates (reverse-rectangular-user-coordinates [x y])))

(defn orthographic-scale
  "Scales ra/dec coordinates into user coordinates for drawing using an orthographic projection."
  [[long lat]]
  (azimutal-user-coordinates (orthographic-relative-coordinates (north-pole-orthographic-projector [long lat]))))

(defn reverse-orthographic-scale
  "Scales x/y coordinates into ra/dec coordinates using a reverse orthographic projection."
  [[x y]]
  (north-pole-reverse-orthographic-projector (reverse-orthographic-relative-coordinates (reverse-azimutal-user-coordinates [x y]))))

(defn stereographic-scale
  "Scales ra/dec coordinates into user coordinates for drawing using a stereographic projection."
  [[long lat]]
  (azimutal-user-coordinates (stereographic-relative-coordinates (north-pole-stereographic-projector [long lat]))))

(defn reverse-stereographic-scale
  "Scales x/y coordinates into ra/dec coordinates using a reverse stereographic projection."
  [[x y]]
  (north-pole-reverse-stereographic-projector (reverse-stereographic-relative-coordinates (reverse-azimutal-user-coordinates [x y]))))


(defn draw-chart-background
  "Draws the chart background."
  [^java.awt.Graphics2D gfx]
  (fill-colored-rect gfx 0 0 (:x-max rectangular-panel-spec) (:y-max rectangular-panel-spec) (chart-colors :black)))

(defn draw-equirectangular-chart
  "Draw the rectangular star chart."
  [^java.awt.Graphics2D gfx]
  (set-rendering-hint gfx (rendering-hint-keys :antialialising) (antialias-hints :on))
  (draw-chart-background gfx)
  (draw-chart-grid gfx equirectangular-scale)
  (draw-dso-labels gfx equirectangular-scale (filter common-name? (filter (mag-filter 4) (get-stars))))
  (draw-dso-labels gfx equirectangular-scale (filter common-name? (filter (mag-filter 6) (get-deep-sky-objects))))
  (draw-dsos gfx equirectangular-scale (filter (mag-filter 10.5) (get-deep-sky-objects)))
  (draw-dsos gfx equirectangular-scale (filter (mag-filter 6.5) (get-stars))))

(defn draw-orthographic-chart
  "Draw the orthographic star chart."
  [^java.awt.Graphics2D gfx]
  (set-rendering-hint gfx (rendering-hint-keys :antialialising) (antialias-hints :on))
  (draw-chart-background gfx)
  (draw-dso-labels gfx orthographic-scale
                   (filter common-name? 
                           (filter (rad-ra-dec-filter [0.0 0.0] [(* 2 pi) (/ pi 2)]) 
                                   (filter (mag-filter 2) (get-stars)))))
  (draw-dso-labels gfx orthographic-scale
                   (filter common-name? 
                           (filter (rad-ra-dec-filter [0.0 0.0] [(* 2 pi) (/ pi 2)]) 
                                   (filter (mag-filter 6) (get-deep-sky-objects)))))
  (draw-dsos gfx orthographic-scale 
             (filter (rad-ra-dec-filter [0.0 0.0] [(* 2 pi) (/ pi 2)])
                     (filter (mag-filter 10) (get-deep-sky-objects))))
  (draw-dsos gfx orthographic-scale 
             (filter (rad-ra-dec-filter [0.0 0.0] [(* 2 pi) (/ pi 2)])
                     (filter (mag-filter 7) (get-stars)))))

(defn draw-stereographic-chart
  "Draw the stereographic star chart."
  [^java.awt.Graphics2D gfx]
  (set-rendering-hint gfx (rendering-hint-keys :antialialising) (antialias-hints :on))
  (draw-chart-background gfx)
  ;(draw-chart-grid gfx)
  (draw-dso-labels gfx stereographic-scale
                   (filter common-name? 
                           (filter (ra-dec-filter [0.0 0.0] [360.0 90.0]) 
                                   (filter (mag-filter 2) (get-stars)))))
  (draw-dso-labels gfx stereographic-scale
                   (filter common-name? 
                           (filter (rad-ra-dec-filter [0.0 0.0] [(* 2 pi) (/ pi 2)]) 
                                   (filter (mag-filter 6) (get-deep-sky-objects)))))
  (draw-dsos gfx stereographic-scale 
             (filter (ra-dec-filter [0.0 0.0] [360.0 90.0])
                     (filter (mag-filter 10) (get-deep-sky-objects))))
  (draw-dsos gfx stereographic-scale 
             (filter (ra-dec-filter [0.0 0.0] [360.0 90.0]) 
                     (filter (mag-filter 7) (get-stars)))))

(def up-action (action (fn [_] (println "UP"))))
(def down-action (action (fn [_] (println "DOWN"))))
(def left-action (action (fn [_] (println "LEFT"))))
(def right-action (action (fn [_] (println "RIGHT"))))

(defn chart-panel-mouse-clicked
  "Called when a mouse click happens in the chart panel."
  [event parent reverse-scale]
  (let [o (find-object-by-coordinates (reverse-scale (point-coordinates (.getPoint event)))
                                                 (filter (mag-filter 10) (get-deep-sky-objects)))]
    (println o)
    (object-info-dialog parent o)))

(defn chart-dialog-resized
  "Called when chart dialog is resized."
  [event args]
  (println "Resize" event))

(defn chart-filter-dialog
  "Creates the chart filter panel."
  [chart-spec]
  (let [f-faintest-stellar-mag (number-field {:columns 5})
        f-faintest-dso-mag (number-field {:columns 5})
        b-ok (button {})
        b-cancel (button {})
        p (panel {} [])
        filter-dialog (dialog {}
                              [])])
  (defn chart-filter-ok
    ""
    [])
  )

(defn star-chart-panel
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
  (let [panel (star-chart-panel draw-equirectangular-chart rectangular-panel-spec)
        d (dialog {:title (i18n "label.chart.equirectangular.title")} [(scroll-pane panel)])]
    (add-mouse-listener panel (mouse-clicked-listener chart-panel-mouse-clicked d reverse-equirectangular-scale))
    d))

(defn stereographic-star-chart-dialog
  "Creates the star chart dialog."
  []
  (let [panel (star-chart-panel draw-stereographic-chart azimutal-panel-spec)
        d (dialog {:title (i18n "label.chart.stereographic.title")} [(scroll-pane panel)])]
    (add-mouse-listener panel (mouse-clicked-listener chart-panel-mouse-clicked d reverse-stereographic-scale))
    d))

(defn orthographic-star-chart-dialog
  "Creates the star chart dialog."
  []
  (let [panel (star-chart-panel draw-orthographic-chart azimutal-panel-spec)
        d (dialog {:title (i18n "label.chart.orthographic.title")} [(scroll-pane panel)])]
    (add-mouse-listener panel (mouse-clicked-listener chart-panel-mouse-clicked d reverse-orthographic-scale))
    d))

(comment 
(defprotocol StarChart
  (update-chart-spec [chart spec]))

(defrecord EquirectangularStarChartImpl
  [chart-spec panel-spec]
  StarChart
  (update-chart-spec [chart spec]
    (dosync)))

(defrecord StereographicStarChartImpl
  [chart-spec panel-spec]
  StarChart
  (update-chart-spec [chart spec]
    (dosync)))

(defrecord OrthographicStarChartImpl
  [chart-spec panel-spec]
  StarChart
  (update-chart-spec [chart spec]
    (dosync)))


(defn chart-x
  ""
  [^StarChart chart]
  )
())