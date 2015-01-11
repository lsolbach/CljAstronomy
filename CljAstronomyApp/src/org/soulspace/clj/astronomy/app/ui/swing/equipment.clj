(ns org.soulspace.clj.astronomy.app.ui.swing.equipment
  (:import [javax.swing Action BorderFactory])
  (:use [org.soulspace.clj.java.awt]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

(defn optic-panel
  "Creates the optic panel."
  []
  (let [f-name (text-field)
        f-type (text-field)
        f-aperture (number-field)
        f-focal-length (number-field)
        f-effectiveness (number-field)
        f-fixed-magnification (check-box) ; TODO Radio buttons
        f-magnification (number-field)
        f-field-of-view (number-field)
        f-available (check-box)]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.optic.title") :font heading-font}) "left, wrap 10"]
            (label (i18n "label.optics.name")) f-name])))

(defn eyepiece-panel
  "Creates the eyepiece panel."
  []
  (let [f-name (text-field)
        f-focal-length (number-field)
        f-field-of-view (number-field)
        f-available (check-box)]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.eyepiece.title") :font heading-font}) "left, wrap 10"]
            ])))

(defn filter-panel
  "Creates the filter panel."
  []
  (let [f-name (text-field)
        f-type (text-field) ; TODO combo box SKYGLOW/UHC/LINE/NEUTRAL DENSITY
        ]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.filter.title") :font heading-font}) "left, wrap 10"]
            ])))

(defn barlow-reducer-panel
  "Creates the barlow/reducer panel."
  []
  (let []
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.barlow-reducer.title") :font heading-font}) "left, wrap 10"]
            ])))

(defn equipment-panel
  "Creates the equipment panel."
  []
  (let []
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                   :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.title") :font heading-font}) "left, wrap 10"]
            ])))
