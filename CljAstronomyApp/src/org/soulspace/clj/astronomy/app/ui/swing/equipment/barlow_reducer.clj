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

(ns org.soulspace.clj.astronomy.app.ui.swing.equipment.barlow-reducer
  (:import [javax.swing Action BorderFactory])
  (:use [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event]
        [org.soulspace.clj.java.swing constants event swinglib]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.instruments equipment]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

(defn barlows-reducers-list-model
  [barlows-reducers]
  (seq-list-model (map :name barlows-reducers)))

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
      (.setText f-factor (:type barlow-reducer)))

    (defn clear-barlow-reducer-panel
      []
      (.setText f-name "")
      (.setText f-type ""))

    (defn get-barlow-reducer-panel
      [])
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.barlow-reducer.title") :font heading-font}) "left, wrap 10"]
            (label {:text (i18n "label.equipment.barlow-reducer.name")}) f-name
            (label {:text (i18n "label.equipment.barlow-reducer.type")}) f-type
            (label {:text (i18n "label.equipment.barlow-reducer.factor")}) f-factor])))

(defn barlows-reducers-panel
  "Creates the barlows/reducers panel."
  [barlows-reducers]
  (let [l-barlows-reducers (j-list {:model (barlows-reducers-list-model barlows-reducers)
                                    :selectionMode (list-selection-keys :single)})]
       (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                    :columnConstraints "[left|grow]"})}
          [[(label {:text (i18n "label.equipment.barlows-reducers.title") :font heading-font}) "left, wrap 10"]
           (scroll-pane l-barlows-reducers)])))

(defn barlows-reducers-dialog
  "Creates the barlows/reducers dialog."
  ([]
   (barlows-reducers-dialog @barlows-reducers-list))
  ([barlows-reducers]
   (let [b-ok (button {:text (i18n "button.ok")})
         b-cancel (button {:text (i18n "button.cancel")})
         d (dialog {:title (i18n "label.equipment.barlows-reducers.title")}
                   [(panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                      [(vertical-split-pane {}
                         [(barlows-reducers-panel barlows-reducers)
                          (barlow-reducer-panel)])
                       (panel {} [[b-cancel "tag cancel"]
                                  [b-ok "span, tag ok"]])])])]
     (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
     (add-action-listener b-cancel (action-listener (fn [_] (.setVisible d false))))
     d))
  ([parent barlows-reducers]
   (let [b-ok (button {:text (i18n "button.ok")})
         b-cancel (button {:text (i18n "button.cancel")})
         d (dialog parent {:title (i18n "label.equipment.barlows-reducers.title")}
                   [(panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                      [(vertical-split-pane {}
                         [(barlows-reducers-panel barlows-reducers)
                          (barlow-reducer-panel)])
                       (panel {} [[b-cancel "tag cancel"]
                                  [b-ok "span, tag ok"]])])])]
     d)))

(def barlows-reducers-action
  (action (fn [_]
            (let [barlows-reducers-dialog (barlows-reducers-dialog @barlows-reducers-list)]
              (.setVisible barlows-reducers-dialog true)))
          {:name (i18n "action.equipment.barlows-reducers")}))
