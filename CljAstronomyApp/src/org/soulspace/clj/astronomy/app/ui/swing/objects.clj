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

(ns org.soulspace.clj.astronomy.app.ui.swing.objects
  (:require [org.soulspace.clj.java.awt.core :as awt]
            [org.soulspace.clj.java.awt.events :as aevt]
            [org.soulspace.clj.java.swing.core :as swing]
            [org.soulspace.clj.java.swing.events :as sevt]
            [org.soulspace.clj.astronomy.app.common :as app]
            [org.soulspace.clj.astronomy.app.data.common :as adc]
            [org.soulspace.clj.astronomy.app.data.hyg-dso-catalog :as chdc]
            [org.soulspace.clj.astronomy.app.data.hyg-star-catalog :as chsc]
            [org.soulspace.clj.astronomy.app.data.messier-catalog :as cmes]
            [org.soulspace.clj.astronomy.app.ui.swing.common :as swc]))

;;;
;;; Object list
;;;

(def object-list (ref []))

(defn set-object-list
  "Sets the object list."
  [coll]
  (dosync (ref-set object-list coll)))

(defn objectlist-table-model
  [coll]
  (swing/mapseq-table-model
   [{:label (app/i18n "label.object.id") :key :id :edit false :converter str}
    {:label (app/i18n "label.object.name") :key identity :edit false :converter adc/object-label} ; object-label
    {:label (app/i18n "label.object.constellation") :key :constellation :edit false :converter adc/constellation-label}
    {:label (app/i18n "label.object.type") :key :type :edit false :converter adc/type-label}
    {:label (app/i18n "label.object.ra") :key :ra :edit false :converter adc/ra-label}
    {:label (app/i18n "label.object.dec") :key :dec :edit false :converter adc/dec-label}
    {:label (app/i18n "label.object.mag") :key :mag :edit false}]
     ;{:label (i18n "label.object.mag-abs") :key :mag-abs :edit false}

   coll))

(defn catalog-list-model
  "Returns the catalog list model."
  []
  (swing/seq-list-model (vals adc/catalog-name)))

(defn object-type-list-model
  "Returns the catalog list model."
  []
  (swing/seq-list-model (vals adc/catalog-name)))

(defn object-filter-panel
  "Creates an object filter panel."
  []
  (let [f-name (swing/text-field {:text "" :columns 20})
        f-constellation (swing/text-field {:text "" :columns 20})
        f-ra-min (swing/number-field {:text "" :columns 20})
        f-ra-max (swing/number-field {:text "" :columns 20})
        f-dec-min (swing/number-field {:text "" :columns 20})
        f-dec-max (swing/number-field {:text "" :columns 20})
        f-mag-min (swing/number-field {:text "" :columns 20})
        f-mag-max (swing/number-field {:text "" :columns 20})
        l-catalogs (swing/j-list {:model (catalog-list-model)})]

    (defn update-object-filter-panel
      "Updates the filter panel."
      [object-filter])

    (defn read-object-filter-panel
      "Reads the filter panel."
      [])
    (defn clear-object-filter-panel
      "Clears the filter panel."
      [])
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 4, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.object.filter.title") :font swc/heading-font}) "left, wrap"]
            (swing/label {:text (app/i18n "label.object.filter.name")}) f-name
            (swing/label {:text (app/i18n "label.object.filter.constellation")}) f-constellation
            (swing/label {:text (app/i18n "label.object.filter.ra-min")}) f-ra-min
            (swing/label {:text (app/i18n "label.object.filter.ra-max")}) f-ra-max
            (swing/label {:text (app/i18n "label.object.filter.dec-min")}) f-dec-min
            (swing/label {:text (app/i18n "label.object.filter.dec-max")}) f-dec-max
            (swing/label {:text (app/i18n "label.object.filter.mag-min")}) f-mag-min
            (swing/label {:text (app/i18n "label.object.filter.mag-max")}) f-mag-max
            (swing/button {:text (app/i18n "button.clear")})
            (swing/button {:text (app/i18n "button.apply")})])))



(defn object-list-panel
  "Creates an object list panel."
  ([]
   (object-list-panel @object-list))
  ([coll]
   (set-object-list coll)
   (let [t-object-list (swing/table {:model (objectlist-table-model coll) :gridColor java.awt.Color/DARK_GRAY})
         t-selection-model (swing/get-selection-model t-object-list)
         p (swing/panel {:layout (swing/mig-layout {:layoutConstraints "wrap 1, insets 10, fill"
                                        :columnConstraints "[left|grow]"
                                        :rowConstraints "[grow]"})}
                  [[(swing/label {:text (app/i18n "label.object.list.title") :font swc/heading-font}) "left, wrap"]
                   [(swing/scroll-pane t-object-list) "span, grow"]])]


     (defn show-object
       [e]
       (when-not (.getValueIsAdjusting e)
         (println (nth coll (.getFirstIndex e)))))

     (defn update-object-list-panel
       [object-list])

     (defn clear-object-list-panel
       [object-list])

     (swing/set-selection-mode t-selection-model (swing/list-selection-keys :single))
     (sevt/add-list-selection-listener t-selection-model (sevt/list-selection-listener show-object))

     p)))

(defn object-list-dialog
  "Creates the object list dialog."
  ([]
   (object-list-dialog @object-list))
  ([coll]
   (set-object-list coll)
   (let [b-ok (swing/button {:text (app/i18n "button.ok")})
         filter-panel (object-filter-panel)
         list-panel (object-list-panel)
         d (swing/dialog {:title (app/i18n "label.object.list.title")}
                   [(swing/panel {:layout (swing/mig-layout {:layoutConstraints "fill, wrap 1"
                                                 :columnConstraints "[left|grow]"})}
                           [filter-panel
                            [list-panel "span, grow"]
                            [b-ok "span, tag ok"]])])]
     (.setVisible d true)
     (aevt/add-action-listener b-ok (aevt/action-listener (fn [_] (.setVisible d false))))
     d))
  ([parent coll]
   (set-object-list coll)
   (let [b-ok (swing/button {:text (app/i18n "button.ok")})
         filter-panel (object-filter-panel)
         p (object-list-panel)
         d (swing/dialog parent {:title (app/i18n "label.object.list.title")}
                   [(swing/panel {:layout (swing/mig-layout {:layoutConstraints "wrap 1"})}
                           [p
                            [b-ok "span, tag ok"]])])]
     (.setVisible d true)
     (aevt/add-action-listener b-ok (aevt/action-listener (fn [_] (.setVisible d false))))
     d)))

(def object-list-action
  (swing/action (fn [_]
            (let [object-list (chsc/get-objects) ; TODO replace with command on channel
                  dialog-object-list (object-list-dialog object-list)]
              (.setVisible dialog-object-list true)))
          {:name (app/i18n "action.view.object-list")
           :accelerator (swing/key-stroke \l :ctrl)
           :mnemonic nil}))

;;;
;;; Object info
;;;

(defn object-panel
  "Creates an object panel."
  [o]
  (let [f-name (swing/text-field {:columns 20 :text (adc/object-label o) :editable false})
        f-constellation (swing/text-field {:columns 20 :text (adc/constellation-label (:constellation o)) :editable false})
        f-type (swing/text-field {:columns 20 :text (adc/object-type (:type o)) :editable false})
        f-ra (swing/number-field {:columns 20 :text (str (:ra o)) :editable false})
        f-dec (swing/number-field {:columns 20 :text (str (:dec o)) :editable false})
        f-mag (swing/number-field {:columns 20 :text (str (:mag o)) :editable false})
        p (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                       :columnConstraints "[left|grow]"})}
                 [[(swing/label {:text (app/i18n "label.object.info.title") :font swc/heading-font}) "left, wrap 10"]
                  (swing/label {:text (app/i18n "label.object.name")}) f-name
                  (swing/label {:text (app/i18n "label.object.constellation")}) f-constellation
                  (swing/label {:text (app/i18n "label.object.type")}) f-type
                  (swing/label {:text (app/i18n "label.object.ra")}) f-ra
                  (swing/label {:text (app/i18n "label.object.dec")}) f-dec
                  (swing/label {:text (app/i18n "label.object.mag")}) f-mag])]
    (defn update-object-panel
      [object])

    (defn clear-object-panel
      [])

    p))

(defn object-info-dialog
  "Creates an object info dialog"
  [parent o]
  (if (seq o)
    (let [b-ok (swing/button {:text (app/i18n "button.ok")})
          d (swing/dialog parent {:title (app/i18n "label.object.info.title")}
                    [(swing/panel {:layout (swing/mig-layout {:layoutConstraints "wrap 1"})}
                            [(object-panel o)
                             [b-ok "span, tag ok"]])])]
      (.setVisible d true)
      (aevt/add-action-listener b-ok (aevt/action-listener (fn [_] (.setVisible d false))))
      d)))
