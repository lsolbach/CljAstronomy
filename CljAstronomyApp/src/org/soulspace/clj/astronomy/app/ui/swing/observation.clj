(ns org.soulspace.clj.astronomy.app.ui.swing.observation
  (:import [javax.swing Action BorderFactory])
  (:use [org.soulspace.clj.java.awt]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.astronomy.app i18n observation]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

(defn observations-table-model
  ""
  [coll]
  (mapseq-table-model
    [{:label (i18n "label.observation.time.start")}
     {:label (i18n "label.observation.time.end")}
     {:label (i18n "label.observation.location.name")}]
    coll))

(defn observation-conditions-panel
  []
  (let [f-time-start (text-field)
        f-time-end (text-field)
        f-location-name (text-field)
        f-location-latitude (text-field)
        f-location-longitude (text-field)
        f-seeing (text-field)
        f-transparency (text-field)
        f-faintest-star (text-field)
        f-sky-quality-meter (text-field)
        f-condition-notes (text-area)]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.observation.conditions.title") :font heading-font}) "left, wrap 10"]
            (label {:text (i18n "label.observation.time.start")}) f-time-start
            (label {:text (i18n "label.observation.time.end")}) f-time-end
            (label {:text (i18n "label.observation.location.name")}) f-location-name
            (label {:text (i18n "label.observation.location.latitude")}) f-location-latitude
            (label {:text (i18n "label.observation.location.longitude")}) f-location-longitude
            (label {:text (i18n "label.observation.conditions.seeing")}) f-seeing
            (label {:text (i18n "label.observation.conditions.transparency")}) f-transparency
            (label {:text (i18n "label.observation.conditions.fainteststar")}) f-faintest-star
            (label {:text (i18n "label.observation.conditions.sqm")}) f-sky-quality-meter
            (label {:text (i18n "label.observation.conditions.notes")}) f-condition-notes
            ])))

(defn observation-equipment-panel
  []
  (let [f-optics (text-field)]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.observation.equipment.title") :font heading-font}) "left, wrap 10"]
            ])))

(defn observation-deepsky-panel
  []
  (let []
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.observation.deepsky.title") :font heading-font}) "left, wrap 10"]
            ])))

(defn observation-sun-panel
  []
  (let []
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.observation.sun.title") :font heading-font}) "left, wrap 10"]
            ])))

(defn observation-panel
  []
  (let []
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.observation.title") :font heading-font}) "left, wrap 10"]
            ])))

(defn observations-panel
  [observations]
  (let [table-model (observations-table-model observations)
        table (table {:model table-model
                      :gridColor java.awt.Color/DARK_GRAY})]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.observations.title") :font heading-font}) "left, wrap 10"]
            table
            ])))

(defn observations-dialog
  "The observations dialog shows a list of all observations."
  ([observations]
    (let [d (dialog {:title (i18n "label.observations.title")}
                    [(observations-panel observations)])]
      (.setVisible d true)
      d))
  ([parent observations]
    (let [d (dialog parent {:title (i18n "label.observations.title")}
                    [(observations-panel observations)])]
      (.setVisible d true)
      d)))

(def observations-action
  (action (fn [_]
            (let [dialog-observations (observations-dialog @observations-list)]
              (.setVisible dialog-observations true)))
          {:name (i18n "action.observation.observations")
           }))

