(ns org.soulspace.clj.astronomy.app.ui.swing.equipment.optics
  (:import [javax.swing Action BorderFactory])
  (:use [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event]
        [org.soulspace.clj.java.swing constants event swinglib]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.instruments equipment]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

(defn optics-table-model
  [coll])


(defn optic-panel
  "Creates the optic panel."
  []
  (let [f-name (text-field {:columns 30})
        f-type (text-field {:columns 30})
        f-aperture (number-field {:columns 30})
        f-focal-length (number-field {:columns 30})
        f-effectiveness (number-field {:columns 30})
        f-fixed-magnification (check-box {}) ; TODO Radio buttons
        f-magnification (number-field {:columns 30})
        f-field-of-view (number-field {:columns 30})
        f-available (check-box {})
        b-add (button {:text (i18n "button.add")})]
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
            b-add])))

(defn optics-list-model
  [optics]
  (seq-list-model (map :name optics)))

(defn optics-panel
  "Creates the optics panel."
  [optics]
  (let [l-optics (j-list {:model (optics-list-model optics)
                          :selectionMode (list-selection-keys :single)
                          :prototypeCellValue "012345678901234567890123456789"})
        ;b-add (button {:text (i18n "button.add")})
        b-edit (button {:text (i18n "button.edit")})
        b-remove (button {:text (i18n "button.remove")})]
        ;l-selection-listener (list-selection-listener )

    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.optics.title") :font heading-font}) "left, wrap 10"]
            (scroll-pane l-optics)])))

(defn optics-dialog
  "Creates the optics dialog"
  ([optics]
   (let [b-ok (button {:text (i18n "button.ok")})
         b-cancel (button {:text (i18n "button.cancel")})
         d (dialog {:title (i18n "label.equipment.optics.title")}
                   [(panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                     [(vertical-split-pane {}
                       [(optics-panel optics)
                        (optic-panel)])
                      (panel {} [[b-cancel "tag cancel"]
                                 [b-ok "span, tag ok"]])])])]
     ; TODO add selection listener to update the optic panel on list selections
     (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
     (add-action-listener b-cancel (action-listener (fn [_] (.setVisible d false))))
     d))
  ([parent optics]
   (let [b-ok (button {:text (i18n "button.ok")})
         b-cancel (button {:text (i18n "button.cancel")})
         d (dialog parent {:title (i18n "label.equipment.optics.title")}
                   [(panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 1, fill"
                                                 :columnConstraints "[left|grow]"})}
                     [(vertical-split-pane {}
                       [(optics-panel optics)
                        (optic-panel)])
                      (panel {} [[b-cancel "tag cancel"]
                                 [b-ok "span, tag ok"]])])])]
     ; TODO add selection listener to update the optic panel on list selections
     (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
     (add-action-listener b-cancel (action-listener (fn [_] (.setVisible d false))))
     d)))

(def optics-action
  (action (fn [_]
            (let [dialog-optics (optics-dialog @optics-list)]
              (.setVisible dialog-optics true)))
          {:name (i18n "action.equipment.optics")}))
