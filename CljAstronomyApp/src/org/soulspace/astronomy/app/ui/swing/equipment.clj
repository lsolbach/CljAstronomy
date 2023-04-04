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

(ns org.soulspace.astronomy.app.ui.swing.equipment
  (:require [org.soulspace.clj.java.awt.core :as awt]
            [org.soulspace.clj.java.awt.events :as aevt]
            [org.soulspace.clj.java.swing.core :as swing]
            [org.soulspace.clj.java.swing.events :as sevt]
            [org.soulspace.astronomy.app.common :as app]
            [org.soulspace.clj.astronomy.app.instruments.equipment :as ieq]
            [org.soulspace.clj.astronomy.app.ui.swing.common :as swc]))

; TODO wire and show ok and cancel buttons

(defn equipment-panel
  "Creates the equipment panel."
  []
  (let []
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                   :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.equipment.title") :font swc/heading-font}) "left, wrap 10"]])))

;;;
;;; barlows and reducers
;;;

(defn barlows-reducers-list-model
  [barlows-reducers]
  (swing/seq-list-model (map :name barlows-reducers)))

(defn barlow-reducer-panel
  "Creates the barlow/reducer panel."
  []
  (let [f-name (swing/text-field {:columns 15})
        f-type (swing/text-field {:columns 15})
        f-factor (swing/number-field {:columns 15})
        f-available (swing/check-box {})]
    (defn update-barlow-reducer-panel
      [barlow-reducer]
      (.setText f-name (:name barlow-reducer))
      (.setText f-type (:type barlow-reducer))
      (.setText f-factor (:type barlow-reducer)))

    (defn clear-barlow-reducer-panel
      []
      (.setText f-name "")
      (.setText f-type ""))

    (defn get-barlow-reducer-panel
      [])
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.equipment.barlow-reducer.title") :font swc/heading-font}) "left, wrap 10"]
             (swing/label {:text (app/i18n "label.equipment.barlow-reducer.name")}) f-name
             (swing/label {:text (app/i18n "label.equipment.barlow-reducer.type")}) f-type
             (swing/label {:text (app/i18n "label.equipment.barlow-reducer.factor")}) f-factor])))

(defn barlows-reducers-panel
  "Creates the barlows/reducers panel."
  [barlows-reducers]
  (let [l-barlows-reducers (swing/j-list {:model (barlows-reducers-list-model barlows-reducers)
                                    :selectionMode (swing/list-selection-keys :single)})]
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.equipment.barlows-reducers.title") :font swc/heading-font}) "left, wrap 10"]
            (swing/scroll-pane l-barlows-reducers)])))

(defn barlows-reducers-dialog
  "Creates the barlows/reducers dialog."
  ([]
   (barlows-reducers-dialog @ieq/barlows-reducers-list))
  ([barlows-reducers]
   (let [b-ok (swing/button {:text (app/i18n "button.ok")})
         b-cancel (swing/button {:text (app/i18n "button.cancel")})
         d (swing/dialog {:title (app/i18n "label.equipment.barlows-reducers.title")}
                   [(swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                           [(swing/vertical-split-pane {}
                                                 [(barlows-reducers-panel barlows-reducers)
                                                  (barlow-reducer-panel)])
                            (swing/panel {} [[b-cancel "tag cancel"]
                                       [b-ok "span, tag ok"]])])])]
     (aevt/add-action-listener b-ok (aevt/action-listener (fn [_] (.setVisible d false))))
     (aevt/add-action-listener b-cancel (aevt/action-listener (fn [_] (.setVisible d false))))
     d))
  ([parent barlows-reducers]
   (let [b-ok (swing/button {:text (app/i18n "button.ok")})
         b-cancel (swing/button {:text (app/i18n "button.cancel")})
         d (swing/dialog parent {:title (app/i18n "label.equipment.barlows-reducers.title")}
                   [(swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                           [(swing/vertical-split-pane {}
                                                 [(barlows-reducers-panel barlows-reducers)
                                                  (barlow-reducer-panel)])
                            (swing/panel {} [[b-cancel "tag cancel"]
                                       [b-ok "span, tag ok"]])])])]
     d)))

(def barlows-reducers-action
  (swing/action (fn [_]
            (let [barlows-reducers-dialog (barlows-reducers-dialog @ieq/barlows-reducers-list)]
              (.setVisible barlows-reducers-dialog true)))
          {:name (app/i18n "action.equipment.barlows-reducers")}))

;;;
;;; Eyepieces/Oculars
;;;

(defn eyepieces-list-model
  [eyepieces]
  (swing/seq-list-model (map :name eyepieces)))

(defn eyepiece-panel
  "Creates the eyepiece panel."
  []
  (let [f-name (swing/text-field {:columns 15})
        f-focal-length (swing/number-field {:columns 15})
        f-field-of-view (swing/number-field {:columns 15})
        f-available (swing/check-box {})]
    (defn update-eyepiece-panel
      [eyepiece])
    (defn clear-eyepiece-panel
      [])
    (defn get-eyepiece-panel
      [])
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.equipment.eyepiece.title") :font swc/heading-font}) "left, wrap 10"]
            (swing/label {:text (app/i18n "label.equipment.eyepiece.name")}) f-name
            (swing/label {:text (app/i18n "label.equipment.eyepiece.focal-length")}) f-focal-length
            (swing/label {:text (app/i18n "label.equipment.eyepiece.field-of-view")}) f-field-of-view
            (swing/label {:text (app/i18n "label.equipment.eyepiece.available")}) f-available])))


(defn eyepieces-panel
  "Creates the eyepieces panel."
  [eyepieces]
  (let [l-eyepieces (swing/j-list {:model (eyepieces-list-model eyepieces)
                             :selectionMode (swing/list-selection-keys :single)})]
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.equipment.eyepieces.title") :font swc/heading-font}) "left, wrap 10"]
            (swing/scroll-pane l-eyepieces)])))


(defn eyepieces-dialog
  "Creates the eyepieces dialog."
  ([]
   (eyepieces-dialog @ieq/eyepieces-list))
  ([eyepieces]
   (let [b-ok (swing/button {:text (app/i18n "button.ok")})
         b-cancel (swing/button {:text (app/i18n "button.cancel")})
         d (swing/dialog {:title (app/i18n "label.equipment.eyepieces.title")}
                   [(swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                           [(swing/vertical-split-pane {}
                                                 [(eyepieces-panel eyepieces)
                                                  (eyepiece-panel)])
                            (swing/panel {} [[b-cancel "tag cancel"]
                                       [b-ok "span, tag ok"]])])])]
     (aevt/add-action-listener b-ok (aevt/action-listener (fn [_] (.setVisible d false))))
     (aevt/add-action-listener b-cancel (aevt/action-listener (fn [_] (.setVisible d false))))
     d))
  ([parent eyepieces]
   (let [b-ok (swing/button {:text (app/i18n "button.ok")})
         b-cancel (swing/button {:text (app/i18n "button.cancel")})
         d (swing/dialog parent {:title (app/i18n "label.equipment.eyepieces.title")}
                   [(swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                           [(swing/vertical-split-pane {}
                                                 [(eyepieces-panel eyepieces)
                                                  (eyepiece-panel)])
                            (swing/panel {} [[b-cancel "tag cancel"]
                                       [b-ok "span, tag ok"]])])])]
     d)))

(def eyepieces-action
  (swing/action (fn [_]
            (let [dialog-eyepieces (eyepieces-dialog @ieq/eyepieces-list)]
              (.setVisible dialog-eyepieces true)))
          {:name (app/i18n "action.equipment.eyepieces")}))

;;;
;;; Filters
;;;

(defn filters-list-model
  [filters]
  (swing/seq-list-model (map :name filters)))

(defn filter-panel
  "Creates the filter panel."
  []
  (let [f-name (swing/text-field {:columns 15})
        f-type (swing/text-field {:columns 15}) ; TODO combo box SKYGLOW/UHC/LINE/NEUTRAL DENSITY
        f-available (swing/check-box {})]
    (defn update-filter-panel
      [filter]
      (.setText f-name (:name filter))
      (.setText f-type (:type filter)))

    (defn clear-filter-panel
      []
      (.setText f-name "")
      (.setText f-type ""))

    (defn get-filter-panel
      [])

    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.equipment.filter.title") :font swc/heading-font}) "left, wrap 10"]
            (swing/label {:text (app/i18n "label.equipment.filter.name")}) f-name
            (swing/label {:text (app/i18n "label.equipment.filter.type")}) f-type
            (swing/label {:text (app/i18n "label.equipment.filter.available")}) f-available])))

(defn filters-panel
  "Creates the filters panel."
  [filters]
  (let [l-filters (swing/j-list {:model (filters-list-model filters)
                           :selectionMode (swing/list-selection-keys :single)})]
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.equipment.filters.title") :font swc/heading-font}) "left, wrap 10"]
            (swing/scroll-pane l-filters)])))


(defn filters-dialog
  "Creates the filters dialog."
  ([]
   (filters-dialog @ieq/filters-list))
  ([filters]
   (let [b-ok (swing/button {:text (app/i18n "button.ok")})
         b-cancel (swing/button {:text (app/i18n "button.cancel")})
         d (swing/dialog {:title (app/i18n "label.equipment.filters.title")}
                   [(swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                           [(swing/vertical-split-pane {}
                                                 [(filters-panel filters)
                                                  (filter-panel)])
                            (swing/panel {} [[b-cancel "tag cancel"]
                                       [b-ok "span, tag ok"]])])])]
     (aevt/add-action-listener b-ok (aevt/action-listener (fn [_] (.setVisible d false))))
     (aevt/add-action-listener b-cancel (aevt/action-listener (fn [_] (.setVisible d false))))
     d))
  ([parent filters]
   (let [b-ok (swing/button {:text (app/i18n "button.ok")})
         b-cancel (swing/button {:text (app/i18n "button.cancel")})
         d (swing/dialog parent {:title (app/i18n "label.equipment.filters.title")}
                   [(swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                           [(swing/vertical-split-pane {}
                                                 [(filters-panel filters)
                                                  (filter-panel)])
                            (swing/panel {} [[b-cancel "tag cancel"]
                                       [b-ok "span, tag ok"]])])])]
     d)))

(def filters-action
  (swing/action (fn [_]
            (let [dialog-filters (filters-dialog @ieq/filters-list)]
              (.setVisible dialog-filters true)))
          {:name (app/i18n "action.equipment.filters")}))

;;;
;;; Optics
;;;

(comment
  (def ui-elements {:f-name (swing/text-field {:columns 30})
                    :f-type (swing/text-field {:columns 30})
                    :f-aperture (swing/number-field {:columns 30})
                    :f-focal-length (swing/number-field {:columns 30})
                    :f-effectiveness (swing/number-field {:columns 30})
                    :f-fixed-magnification (swing/check-box {}) ; TODO Radio buttons
                    :f-magnification (swing/number-field {:columns 30})
                    :f-field-of-view (swing/number-field {:columns 30})
                    :f-available (swing/check-box {})
                    :b-add (swing/button {:text (app/i18n "button.add")})})
  )

(defn optics-table-model
  [coll])

(defn optic-panel
  "Creates the optic panel."
  []
  (let [f-name (swing/text-field {:columns 30})
        f-type (swing/text-field {:columns 30})
        f-aperture (swing/number-field {:columns 30})
        f-focal-length (swing/number-field {:columns 30})
        f-effectiveness (swing/number-field {:columns 30})
        f-fixed-magnification (swing/check-box {}) ; TODO Radio buttons
        f-magnification (swing/number-field {:columns 30})
        f-field-of-view (swing/number-field {:columns 30})
        f-available (swing/check-box {})
        b-add (swing/button {:text (app/i18n "button.add")})]
    (defn update-optic-panel
      [optic])

    (defn clear-optic-panel
      [])

    (defn get-optic-panel
      [])

    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.equipment.optic.title") :font swc/heading-font}) "left, wrap 10"]
             (swing/label {:text (app/i18n "label.equipment.optic.name")}) f-name
             (swing/label {:text (app/i18n "label.equipment.optic.type")}) f-type
             (swing/label {:text (app/i18n "label.equipment.optic.aperture")}) f-aperture
             (swing/label {:text (app/i18n "label.equipment.optic.focal-length")}) f-focal-length
             (swing/label {:text (app/i18n "label.equipment.optic.effectiveness")}) f-effectiveness
             (swing/label {:text (app/i18n "label.equipment.optic.fixed-magnification")}) f-fixed-magnification
             (swing/label {:text (app/i18n "label.equipment.optic.field-of-view")}) f-field-of-view
             (swing/label {:text (app/i18n "label.equipment.optic.available")}) f-available
             b-add])))

(defn optics-list-model
  [optics]
  (swing/seq-list-model (map :name optics)))

(defn optics-panel
  "Creates the optics panel."
  [optics]
  (let [l-optics (swing/j-list {:model (optics-list-model optics)
                          :selectionMode (swing/list-selection-keys :single)
                          :prototypeCellValue "012345678901234567890123456789"})
        ;b-add (button {:text (i18n "button.add")})
        b-edit (swing/button {:text (app/i18n "button.edit")})
        b-remove (swing/button {:text (app/i18n "button.remove")})]
        ;l-selection-listener (list-selection-listener )

    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.equipment.optics.title") :font swc/heading-font}) "left, wrap 10"]
            (swing/scroll-pane l-optics)])))

(defn optics-dialog
  "Creates the optics dialog"
  ([optics]
   (let [b-ok (swing/button {:text (app/i18n "button.ok")})
         b-cancel (swing/button {:text (app/i18n "button.cancel")})
         d (swing/dialog {:title (app/i18n "label.equipment.optics.title")}
                   [(swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                           [(swing/vertical-split-pane {}
                                                 [(optics-panel optics)
                                                  (optic-panel)])
                            (swing/panel {} [[b-cancel "tag cancel"]
                                       [b-ok "span, tag ok"]])])])]
     ; TODO add selection listener to update the optic panel on list selections
     (aevt/add-action-listener b-ok (aevt/action-listener (fn [_] (.setVisible d false))))
     (aevt/add-action-listener b-cancel (aevt/action-listener (fn [_] (.setVisible d false))))
     d))
  ([parent optics]
   (let [b-ok (swing/button {:text (app/i18n "button.ok")})
         b-cancel (swing/button {:text (app/i18n "button.cancel")})
         d (swing/dialog parent {:title (app/i18n "label.equipment.optics.title")}
                   [(swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                           [(swing/vertical-split-pane {}
                                                 [(optics-panel optics)
                                                  (optic-panel)])
                            (swing/panel {} [[b-cancel "tag cancel"]
                                       [b-ok "span, tag ok"]])])])]
     ; TODO add selection listener to update the optic panel on list selections
     (aevt/add-action-listener b-ok (aevt/action-listener (fn [_] (.setVisible d false))))
     (aevt/add-action-listener b-cancel (aevt/action-listener (fn [_] (.setVisible d false))))
     d)))

(def optics-action
  (swing/action (fn [_]
                  (let [dialog-optics (optics-dialog @ieq/optics-list)]
                    (.setVisible dialog-optics true)))
                {:name (app/i18n "action.equipment.optics")}))

