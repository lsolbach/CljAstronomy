(ns org.soulspace.clj.astronomy.app.ui.swing.equipment
  (:import [javax.swing Action BorderFactory])
  (:use [org.soulspace.clj.java.awt]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

(defn optics-table-model
  [coll]
  )

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
           [[(label {:text (i18n "label.equipment.optic.title") :font heading-font}) "left, wrap 10"]
            (label {:text (i18n "label.equipment.optic.name")}) f-name
            (label {:text (i18n "label.equipment.optic.type")}) f-type
            (label {:text (i18n "label.equipment.optic.aperture")}) f-aperture
            (label {:text (i18n "label.equipment.optic.focal-length")}) f-focal-length
            (label {:text (i18n "label.equipment.optic.effectiveness")}) f-effectiveness
            (label {:text (i18n "label.equipment.optic.fixed-magnification")}) f-fixed-magnification
            (label {:text (i18n "label.equipment.optic.field-of-view")}) f-field-of-view
            (label {:text (i18n "label.equipment.optic.available")}) f-available
            ])))

(defn optics-panel
  "Creates the optics panel."
  []
  (let [l-optics (j-list)]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.optics.title") :font heading-font}) "left, wrap 10"]
            
            ])))

(defn eyepiece-panel
  "Creates the eyepiece panel."
  []
  (let [f-name (text-field)
        f-focal-length (number-field)
        f-field-of-view (number-field)
        f-available (check-box)]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.eyepiece.title") :font heading-font}) "left, wrap 10"]
            (label {:text (i18n "label.equipment.eyepiece.name")}) f-name
            (label {:text (i18n "label.equipment.eyepiece.focal-length")}) f-focal-length
            (label {:text (i18n "label.equipment.eyepiece.field-of-view")}) f-field-of-view
            (label {:text (i18n "label.equipment.eyepiece.available")}) f-available
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
            (label {:text (i18n "label.equipment.filter.name")}) f-name
            (label {:text (i18n "label.equipment.filter.type")}) f-type
            ])))

(defn barlow-reducer-panel
  "Creates the barlow/reducer panel."
  []
  (let [f-name (text-field)
        f-type (text-field)
        f-factor (numeric-field)]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.barlow-reducer.title") :font heading-font}) "left, wrap 10"]
            (label {:text (i18n "label.equipment.barlow-reducer.name")}) f-name
            (label {:text (i18n "label.equipment.barlow-reducer.type")}) f-type
            (label {:text (i18n "label.equipment.barlow-reducer.type")}) f-factor
            ])))

(defn equipment-panel
  "Creates the equipment panel."
  []
  (let []
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                   :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.title") :font heading-font}) "left, wrap 10"]
            ])))

(defn optics-dialog
  "Creates the optics dialog"
  ([]
    (let [d (dialog {:title (i18n "label.equipment.optics.title")}
                    [(optics-panel)])]
      d))
  ([parent]
    (let [d (dialog parent {:title (i18n "label.equipment.optics.title")}
                    [(optics-panel)])]
      d))
  )
