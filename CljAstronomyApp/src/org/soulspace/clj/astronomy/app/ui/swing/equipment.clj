(ns org.soulspace.clj.astronomy.app.ui.swing.equipment
  (:import [javax.swing Action BorderFactory])
  (:use [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event]
        [org.soulspace.clj.java.swing constants event swinglib]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.instruments equipment]
        [org.soulspace.clj.astronomy.app.ui.swing common]))


; TODO wire and show ok and cancel buttons
; TODO create an equipment subpackage and move the different dialogs into their own namespaces

(defn optics-table-model
  [coll]
  )

(defn optic-panel
  "Creates the optic panel."
  []
  (let [f-name (text-field {:columns 15})
        f-type (text-field {:columns 15})
        f-aperture (number-field {:columns 15})
        f-focal-length (number-field {:columns 15})
        f-effectiveness (number-field {:columns 15})
        f-fixed-magnification (check-box {}) ; TODO Radio buttons
        f-magnification (number-field {:columns 15})
        f-field-of-view (number-field {:columns 15})
        f-available (check-box {})]
    (defn update-optic-panel
      [optic]
      )
    (defn clear-optic-panel
      []
      )
    (defn get-optic-panel
      []
      )
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
                          :selectionMode (list-selection-keys :single)})
        b-add (button {:text (i18n "button.add")})
        b-edit (button {:text (i18n "button.edit")})
        b-remove (button {:text (i18n "button.remove")})
        ;l-selection-listener (list-selection-listener )
        ]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.optics.title") :font heading-font}) "left, wrap 10"]
            (scroll-pane l-optics)
            ])))

(defn optics-dialog
  "Creates the optics dialog"
  ([optics]
    (let [b-ok (button {:text (i18n "button.ok")})
          b-cancel (button {:text (i18n "button.cancel")})
          d (dialog {:title (i18n "label.equipment.optics.title")}
                    [(vertical-split-pane {}
                       [(optics-panel optics)
                        (optic-panel)])
                     (panel {} [[b-cancel "tag cancel"]
                                [b-ok "span, tag ok"]])])]
      ; TODO add selection listener to update the optic panel on list selections
      (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
      (add-action-listener b-cancel (action-listener (fn [_] (.setVisible d false))))
      d))
  ([parent optics]
    (let [b-ok (button {:text (i18n "button.ok")})
          b-cancel (button {:text (i18n "button.cancel")})
          d (dialog parent {:title (i18n "label.equipment.optics.title")}
                    [(vertical-split-pane {}
                       [(optics-panel optics)
                        (optic-panel)])
                     (panel {} [[b-cancel "tag cancel"]
                                [b-ok "span, tag ok"]])])]
      d)))

(defn eyepiece-panel
  "Creates the eyepiece panel."
  []
  (let [f-name (text-field {:columns 15})
        f-focal-length (number-field {:columns 15})
        f-field-of-view (number-field {:columns 15})
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
                             :selectionMode (list-selection-keys :single)})]
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
    (let [b-ok (button {:text (i18n "button.ok")})
          b-cancel (button {:text (i18n "button.cancel")})
          d (dialog {:title (i18n "label.equipment.eyepieces.title")}
                    [(vertical-split-pane {}
                       [(eyepieces-panel eyepieces)
                        (eyepiece-panel)])
                     (panel {} [[b-cancel "tag cancel"]
                                [b-ok "span, tag ok"]])])]
      (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
      (add-action-listener b-cancel (action-listener (fn [_] (.setVisible d false))))
      d))
  ([parent eyepieces]
    (let [b-ok (button {:text (i18n "button.ok")})
          b-cancel (button {:text (i18n "button.cancel")})
          d (dialog parent {:title (i18n "label.equipment.eyepieces.title")}
                    [(vertical-split-pane {}
                       [(eyepieces-panel eyepieces)
                        (eyepiece-panel)])
                     (panel {} [[b-cancel "tag cancel"]
                                [b-ok "span, tag ok"]])])]
      d)))

(defn barlow-reducer-panel
  "Creates the barlow/reducer panel."
  []
  (let [f-name (text-field {:columns 15})
        f-type (text-field {:columns 15})
        f-factor (number-field {:columns 15})
        f-available (check-box {})]
    (defn update-barlow-reducer-panel
      [barlow-reducer]
      (.setText f-name (:name barlow-reducer))
      (.setText f-type (:type barlow-reducer))
      (.setText f-factor (:type barlow-reducer))
      )
    (defn clear-barlow-reducer-panel
      []
      (.setText f-name "")
      (.setText f-type "")
      )
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
                                    :selectionMode (list-selection-keys :single)})]
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
    (let [b-ok (button {:text (i18n "button.ok")})
          b-cancel (button {:text (i18n "button.cancel")})
          d (dialog {:title (i18n "label.equipment.barlows-reducers.title")}
                    [(vertical-split-pane {}
                       [(barlows-reducers-panel barlows-reducers)
                        (barlow-reducer-panel)])
                     (panel {} [[b-cancel "tag cancel"]
                                [b-ok "span, tag ok"]])])]
      (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
      (add-action-listener b-cancel (action-listener (fn [_] (.setVisible d false))))
      d))
  ([parent barlows-reducers]
    (let [b-ok (button {:text (i18n "button.ok")})
          b-cancel (button {:text (i18n "button.cancel")})
          d (dialog parent {:title (i18n "label.equipment.barlows-reducers.title")}
                    [(vertical-split-pane {}
                       [(barlows-reducers-panel barlows-reducers)
                        (barlow-reducer-panel)])
                     (panel {} [[b-cancel "tag cancel"]
                                [b-ok "span, tag ok"]])])]
      d)))

(defn filter-panel
  "Creates the filter panel."
  []
  (let [f-name (text-field {:columns 15})
        f-type (text-field {:columns 15}) ; TODO combo box SKYGLOW/UHC/LINE/NEUTRAL DENSITY
        f-available (check-box {})]
    (defn update-filter-panel
      [filter]
      (.setText f-name (:name filter))
      (.setText f-type (:type filter))
      )
    (defn clear-filter-panel
      []
      (.setText f-name "")
      (.setText f-type "")
      )
    (defn get-filter-panel
      []
      )
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
                           :selectionMode (list-selection-keys :single)})]
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
    (let [b-ok (button {:text (i18n "button.ok")})
          b-cancel (button {:text (i18n "button.cancel")})
          d (dialog {:title (i18n "label.equipment.filters.title")}
                    [(vertical-split-pane {}
                       [(filters-panel filters)
                        (filter-panel)])
                     (panel {} [[b-cancel "tag cancel"]
                                [b-ok "span, tag ok"]])])]
      (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
      (add-action-listener b-cancel (action-listener (fn [_] (.setVisible d false))))
      d))
  ([parent filters]
    (let [b-ok (button {:text (i18n "button.ok")})
          b-cancel (button {:text (i18n "button.cancel")})
          d (dialog parent {:title (i18n "label.equipment.filters.title")}
                    [(vertical-split-pane {}
                       [(filters-panel filters)
                        (filter-panel)])
                     (panel {} [[b-cancel "tag cancel"]
                                [b-ok "span, tag ok"]])])]
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

