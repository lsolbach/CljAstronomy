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

(defn equipment-panel
  "Creates the equipment panel."
  []
  (let []
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                   :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.equipment.title") :font heading-font}) "left, wrap 10"]])))
