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

(ns org.soulspace.clj.astronomy.app.ui.swing.observation
  (:require [org.soulspace.clj.java.awt.core :as awt]
        [org.soulspace.clj.java.swing.core :as swing]
        [org.soulspace.clj.astronomy.app.common :as app]
        [org.soulspace.clj.astronomy.app.observation :as obs]
        [org.soulspace.clj.astronomy.app.ui.swing.common :as swc])
  (:import [javax.swing Action BorderFactory]))

(defn observations-table-model
  ""
  [coll]
  (swing/mapseq-table-model
    [{:label (app/i18n "label.observation.time.start")}
     {:label (app/i18n "label.observation.time.end")}
     {:label (app/i18n "label.observation.location.name")}]
    coll))

(defn observation-conditions-panel
  []
  (let [f-time-start (swing/text-field)
        f-time-end (swing/text-field)
        f-location-name (swing/text-field)
        f-location-latitude (swing/text-field)
        f-location-longitude (swing/text-field)
        f-seeing (swing/text-field)
        f-transparency (swing/text-field)
        f-faintest-star (swing/text-field)
        f-sky-quality-meter (swing/text-field)
        f-condition-notes (swing/text-area)]
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.observation.conditions.title") :font swc/heading-font}) "left, wrap 10"]
             (swing/label {:text (app/i18n "label.observation.time.start")}) f-time-start
             (swing/label {:text (app/i18n "label.observation.time.end")}) f-time-end
             (swing/label {:text (app/i18n "label.observation.location.name")}) f-location-name
             (swing/label {:text (app/i18n "label.observation.location.latitude")}) f-location-latitude
             (swing/label {:text (app/i18n "label.observation.location.longitude")}) f-location-longitude
             (swing/label {:text (app/i18n "label.observation.conditions.seeing")}) f-seeing
             (swing/label {:text (app/i18n "label.observation.conditions.transparency")}) f-transparency
             (swing/label {:text (app/i18n "label.observation.conditions.fainteststar")}) f-faintest-star
             (swing/label {:text (app/i18n "label.observation.conditions.sqm")}) f-sky-quality-meter
             (swing/label {:text (app/i18n "label.observation.conditions.notes")}) f-condition-notes
            ])))

(defn observation-equipment-panel
  []
  (let [f-optics (swing/text-field)]
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.observation.equipment.title") :font swc/heading-font}) "left, wrap 10"]
            ])))

(defn observation-deepsky-panel
  []
  (let []
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.observation.deepsky.title") :font swc/heading-font}) "left, wrap 10"]
            ])))

(defn observation-sun-panel
  []
  (let []
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.observation.sun.title") :font swc/heading-font}) "left, wrap 10"]
            ])))

(defn observation-panel
  []
  (let []
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.observation.title") :font swc/heading-font}) "left, wrap 10"]
            ])))

(defn observations-panel
  [observations]
  (let [table-model (observations-table-model observations)
        table (swing/table {:model table-model
                      :gridColor java.awt.Color/DARK_GRAY})]
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.observations.title") :font swc/heading-font}) "left, wrap 10"]
            table
            ])))

(defn observations-dialog
  "The observations dialog shows a list of all observations."
  ([observations]
    (let [d (swing/dialog {:title (app/i18n "label.observations.title")}
                    [(observations-panel observations)])]
      (.setVisible d true)
      d))
  ([parent observations]
    (let [d (swing/dialog parent {:title (app/i18n "label.observations.title")}
                    [(observations-panel observations)])]
      (.setVisible d true)
      d)))

(def observations-action
  (swing/action (fn [_]
            (let [dialog-observations (observations-dialog @obs/observations-list)]
              (.setVisible dialog-observations true)))
          {:name (app/i18n "action.observation.observations")
           }))

