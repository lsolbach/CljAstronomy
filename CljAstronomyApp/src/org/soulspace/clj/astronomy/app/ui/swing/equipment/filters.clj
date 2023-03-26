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

(ns org.soulspace.clj.astronomy.app.ui.swing.equipment.filters
  (:import [javax.swing Action BorderFactory])
  (:use [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event]
        [org.soulspace.clj.java.swing constants event swinglib]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.instruments equipment]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

(defn filters-list-model
  [filters]
  (seq-list-model (map :name filters)))

(defn filter-panel
  "Creates the filter panel."
  []
  (let [f-name (text-field {:columns 15})
        f-type (text-field {:columns 15}) ; TODO combo box SKYGLOW/UHC/LINE/NEUTRAL DENSITY
        f-available (check-box {})]
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

    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.filter.title") :font heading-font}) "left, wrap 10"]
            (label {:text (i18n "label.equipment.filter.name")}) f-name
            (label {:text (i18n "label.equipment.filter.type")}) f-type
            (label {:text (i18n "label.equipment.filter.available")}) f-available])))

(defn filters-panel
  "Creates the filters panel."
  [filters]
  (let [l-filters (j-list {:model (filters-list-model filters)
                           :selectionMode (list-selection-keys :single)})]
       (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                    :columnConstraints "[left|grow]"})}
          [[(label {:text (i18n "label.equipment.filters.title") :font heading-font}) "left, wrap 10"]
           (scroll-pane l-filters)])))


(defn filters-dialog
  "Creates the filters dialog."
  ([]
   (filters-dialog @filters-list))
  ([filters]
   (let [b-ok (button {:text (i18n "button.ok")})
         b-cancel (button {:text (i18n "button.cancel")})
         d (dialog {:title (i18n "label.equipment.filters.title")}
                   [(panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                      [(vertical-split-pane {}
                         [(filters-panel filters)
                          (filter-panel)])
                       (panel {} [[b-cancel "tag cancel"]
                                  [b-ok "span, tag ok"]])])])]
     (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
     (add-action-listener b-cancel (action-listener (fn [_] (.setVisible d false))))
     d))
  ([parent filters]
   (let [b-ok (button {:text (i18n "button.ok")})
         b-cancel (button {:text (i18n "button.cancel")})
         d (dialog parent {:title (i18n "label.equipment.filters.title")}
                   [(panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                      [(vertical-split-pane {}
                         [(filters-panel filters)
                          (filter-panel)])
                       (panel {} [[b-cancel "tag cancel"]
                                  [b-ok "span, tag ok"]])])])]
     d)))

(def filters-action
  (action (fn [_]
            (let [dialog-filters (filters-dialog @filters-list)]
              (.setVisible dialog-filters true)))
          {:name (i18n "action.equipment.filters")}))
