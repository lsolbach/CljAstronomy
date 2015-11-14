;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.app.ui.swing.charts.common
  (:use [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event graphics]
        [org.soulspace.clj.java.swing constants event swinglib]
        [org.soulspace.clj.astronomy.app common i18n]
        [org.soulspace.clj.astronomy.app.data common filters catalogs]
        [org.soulspace.clj.astronomy.app.ui.swing common]
        [org.soulspace.clj.astronomy.app.ui.swing.objects object-info]
        ))
 
(def up-action (action (fn [_] (println "UP"))))
(def down-action (action (fn [_] (println "DOWN"))))
(def left-action (action (fn [_] (println "LEFT"))))
(def right-action (action (fn [_] (println "RIGHT"))))

(def chart-filter-action (action (fn [e] (println "filter")) {:text (i18n "action.chart.filter")}))
(def chart-info-action (action (fn [e] (println "info")) {:text (i18n "action.chart.info")}))

(defn chart-popup-menu
  "Creates a popup menu for the star charts."
  []
  (popup-menu
    {}
    [(menu-item {:action chart-info-action})
     (menu-item {:action chart-filter-action})
     ]))

(defn chart-panel-mouse-clicked
  "Called when a mouse click happens in the chart panel."
  [event parent reverse-scale objects]
  (let [o (find-object-by-coordinates (reverse-scale (point-coordinates (.getPoint event)))
                                                 (filter (mag-filter 10.5) objects))]
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
        p (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                       :columnConstraints "[left|grow]"})}
                 [[(label {:text (i18n "label.chart.filter.title") :font heading-font}) "left, wrap 10"]
                  (label {:text (i18n "label.chart.filter.mag-faintest-stars")}) f-faintest-stellar-mag
                  (label {:text (i18n "label.chart.filter.mag-faintest-dsos")}) f-faintest-dso-mag])
        filter-dialog (dialog {}
                              [p])])
  (defn chart-filter-ok
    ""
    [])
  (defn chart-filter-cancel
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
    (.setComponentPopupMenu panel (chart-popup-menu))
    panel))
