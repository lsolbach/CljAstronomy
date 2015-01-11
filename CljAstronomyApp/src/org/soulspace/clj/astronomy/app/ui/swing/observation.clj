(ns org.soulspace.clj.astronomy.app.ui.swing.observation
  (:import [javax.swing Action BorderFactory])
  (:use [org.soulspace.clj.java.awt]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

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
