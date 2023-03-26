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

(ns org.soulspace.clj.astronomy.app.ui.swing.equipment.eyepieces
  (:import [javax.swing Action BorderFactory])
  (:use [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event]
        [org.soulspace.clj.java.swing constants event swinglib]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.instruments equipment]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

(defn eyepieces-list-model
  [eyepieces]
  (seq-list-model (map :name eyepieces)))

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
            (label {:text (i18n "label.equipment.eyepiece.available")}) f-available])))


(defn eyepieces-panel
  "Creates the eyepieces panel."
  [eyepieces]
  (let [l-eyepieces (j-list {:model (eyepieces-list-model eyepieces)
                             :selectionMode (list-selection-keys :single)})]
       (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                    :columnConstraints "[left|grow]"})}
          [[(label {:text (i18n "label.equipment.eyepieces.title") :font heading-font}) "left, wrap 10"]
           (scroll-pane l-eyepieces)])))


(defn eyepieces-dialog
  "Creates the eyepieces dialog."
  ([]
   (eyepieces-dialog @eyepieces-list))
  ([eyepieces]
   (let [b-ok (button {:text (i18n "button.ok")})
         b-cancel (button {:text (i18n "button.cancel")})
         d (dialog {:title (i18n "label.equipment.eyepieces.title")}
                   [(panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                     [(vertical-split-pane {}
                       [(eyepieces-panel eyepieces)
                        (eyepiece-panel)])
                      (panel {} [[b-cancel "tag cancel"]
                                 [b-ok "span, tag ok"]])])])]
     (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
     (add-action-listener b-cancel (action-listener (fn [_] (.setVisible d false))))
     d))
  ([parent eyepieces]
   (let [b-ok (button {:text (i18n "button.ok")})
         b-cancel (button {:text (i18n "button.cancel")})
         d (dialog parent {:title (i18n "label.equipment.eyepieces.title")}
                   [(panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                     [(vertical-split-pane {}
                        [(eyepieces-panel eyepieces)
                         (eyepiece-panel)])
                      (panel {} [[b-cancel "tag cancel"]
                                 [b-ok "span, tag ok"]])])])]
     d)))

(def eyepieces-action
  (action (fn [_]
            (let [dialog-eyepieces (eyepieces-dialog @eyepieces-list)]
              (.setVisible dialog-eyepieces true)))
          {:name (i18n "action.equipment.eyepieces")}))
