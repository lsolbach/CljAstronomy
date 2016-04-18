(ns org.soulspace.clj.astronomy.app.ui.swing.equipment
  (:import [javax.swing Action BorderFactory])
  (:use [org.soulspace.clj.java.awt]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.astronomy.app i18n equipment]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

(defn optics-table-model
  [coll]
  )

(defn optic-panel
  "Creates the optic panel."
  []
  (let [f-name (text-field {})
        f-type (text-field {})
        f-aperture (number-field {})
        f-focal-length (number-field {})
        f-effectiveness (number-field {})
        f-fixed-magnification (check-box {}) ; TODO Radio buttons
        f-magnification (number-field {})
        f-field-of-view (number-field {})
        f-available (check-box {})]
    (defn update-optic-panel
      [optic])
    (defn clear-optic-panel
      [])
    (defn get-optic-panel
      [])
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

(defn optics-list-model
  [optics]
  (seq-list-model (map :name optics)))

(defn optics-panel
  "Creates the optics panel."
  [optics]
  (let [l-optics (j-list {:model (optics-list-model optics)
                          :selctionMode (list-selection-keys :single)})]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.optics.title") :font heading-font}) "left, wrap 10"]
            (scroll-pane l-optics)
            ])))

(defn optics-dialog
  "Creates the optics dialog"
  ([optics]
    (let [d (dialog {:title (i18n "label.equipment.optics.title")}
                    [(vertical-split-pane {}
                       [(optics-panel optics)
                        (optic-panel)])])]
      d))
  ([parent optics]
    (let [d (dialog parent {:title (i18n "label.equipment.optics.title")}
                    [(vertical-split-pane {}
                       [(optics-panel optics)
                        (optic-panel)])])]
      d)))

(defn eyepiece-panel
  "Creates the eyepiece panel."
  []
  (let [f-name (text-field {})
        f-focal-length (number-field {})
        f-field-of-view (number-field {})
        f-available (check-box {})]
    (defn update-eyepiece-panel
      [eyepiece])
    (defn clear-eyepiece-panel
      [])
    (defn get-eyepiece-panel
      [])
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.eyepiece.title") :font heading-font}) "left, wrap 10"]
            (label {:text (i18n "label.equipment.eyepiece.name")}) f-name
            (label {:text (i18n "label.equipment.eyepiece.focal-length")}) f-focal-length
            (label {:text (i18n "label.equipment.eyepiece.field-of-view")}) f-field-of-view
            (label {:text (i18n "label.equipment.eyepiece.available")}) f-available
            ])))

(defn eyepieces-list-model
  [eyepieces]
  (seq-list-model (map :name eyepieces)))

(defn eyepieces-panel
  "Creates the eyepieces panel."
  [eyepieces]
  (let [l-eyepieces (j-list {:model (eyepieces-list-model eyepieces)
                             :selctionMode (list-selection-keys :single)})]
        (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.eyepieces.title") :font heading-font}) "left, wrap 10"]
            (scroll-pane l-eyepieces)
            ])))

(defn eyepieces-dialog
  "Creates the eyepieces dialog."
  ([]
    (eyepieces-dialog @eyepieces-list))
  ([eyepieces]
    (let [d (dialog {:title (i18n "label.equipment.eyepieces.title")}
                    [(vertical-split-pane {}
                       [(eyepieces-panel eyepieces)
                        (eyepiece-panel)])])]
      d))
  ([parent eyepieces]
    (let [d (dialog parent {:title (i18n "label.equipment.eyepieces.title")}
                    [(vertical-split-pane {}
                       [(eyepieces-panel eyepieces)
                        (eyepiece-panel)])])]
      d)))

(defn barlow-reducer-panel
  "Creates the barlow/reducer panel."
  []
  (let [f-name (text-field {})
        f-type (text-field {})
        f-factor (number-field {})
        f-available (check-box {})]
    (defn update-barlow-reducer-panel
      [barlow-reducer])
    (defn clear-barlow-reducer-panel
      [])
    (defn get-barlow-reducer-panel
      [])
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.barlow-reducer.title") :font heading-font}) "left, wrap 10"]
            (label {:text (i18n "label.equipment.barlow-reducer.name")}) f-name
            (label {:text (i18n "label.equipment.barlow-reducer.type")}) f-type
            (label {:text (i18n "label.equipment.barlow-reducer.factor")}) f-factor
            ])))

(defn barlows-reducers-list-model
  [barlows-reducers]
  (seq-list-model (map :name barlows-reducers)))

(defn barlows-reducers-panel
  "Creates the barlows/reducers panel."
  [barlows-reducers]
  (let [l-barlows-reducers (j-list {:model (barlows-reducers-list-model barlows-reducers)
                                    :selctionMode (list-selection-keys :single)})]
        (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.barlows-reducers.title") :font heading-font}) "left, wrap 10"]
            (scroll-pane l-barlows-reducers)
            ])))

(defn barlows-reducers-dialog
  "Creates the barlows/reducers dialog."
  ([]
    (barlows-reducers-dialog @barlows-reducers-list))
  ([barlows-reducers]
    (let [d (dialog {:title (i18n "label.equipment.barlows-reducers.title")}
                    [(vertical-split-pane {}
                       [(barlows-reducers-panel barlows-reducers)
                        (barlow-reducer-panel)])])]
      d))
  ([parent barlows-reducers]
    (let [d (dialog parent {:title (i18n "label.equipment.barlows-reducers.title")}
                    [(vertical-split-pane {}
                       [(barlows-reducers-panel barlows-reducers)
                        (barlow-reducer-panel)])])]
      d)))

(defn filter-panel
  "Creates the filter panel."
  []
  (let [f-name (text-field {})
        f-type (text-field {}) ; TODO combo box SKYGLOW/UHC/LINE/NEUTRAL DENSITY
        f-available (check-box {})]
    (defn update-filter-panel
      [filter]
      )
    (defn clear-filter-panel
      [])
    (defn get-filter-panel
      [])
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.filter.title") :font heading-font}) "left, wrap 10"]
            (label {:text (i18n "label.equipment.filter.name")}) f-name
            (label {:text (i18n "label.equipment.filter.type")}) f-type
            (label {:text (i18n "label.equipment.filter.available")}) f-available
            ])))

(defn filters-list-model
  [filters]
  (seq-list-model (map :name filters)))

(defn filters-panel
  "Creates the filters panel."
  [filters]
  (let [l-filters (j-list {:model (filters-list-model filters)
                           :selctionMode (list-selection-keys :single)})]
        (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.filters.title") :font heading-font}) "left, wrap 10"]
            (scroll-pane l-filters)
            ])))

(defn filters-dialog
  "Creates the filters dialog."
  ([]
    (filters-dialog @filters-list))
  ([filters]
    (let [d (dialog {:title (i18n "label.equipment.filters.title")}
                    [(vertical-split-pane {}
                       [(filters-panel filters)
                        (filter-panel)])])]
      d))
  ([parent filters]
    (let [d (dialog parent {:title (i18n "label.equipment.filters.title")}
                    [(vertical-split-pane {}
                       [(filters-panel filters)
                        (filter-panel)])])]
      d)))

(defn equipment-panel
  "Creates the equipment panel."
  []
  (let []
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                   :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.title") :font heading-font}) "left, wrap 10"]
            ])))

(def optics-action
  (action (fn [_]
            (let [dialog-optics (optics-dialog @optics-list)]
              (.setVisible dialog-optics true)))
          {:name (i18n "action.equipment.optics")
           }))

(def eyepieces-action
  (action (fn [_]
            (let [dialog-eyepieces (eyepieces-dialog @eyepieces-list)]
              (.setVisible dialog-eyepieces true)))
          {:name (i18n "action.equipment.eyepieces")
           }))

(def filters-action
  (action (fn [_]
            (let [dialog-filters (filters-dialog @filters-list)]
              (.setVisible dialog-filters true)))
          {:name (i18n "action.equipment.filters")
           }))

(def barlows-reducers-action
  (action (fn [_]
            (let [barlows-reducers-dialog (barlows-reducers-dialog @barlows-reducers-list)]
              (.setVisible barlows-reducers-dialog true)))
          {:name (i18n "action.equipment.barlows-reducers")
           }))

