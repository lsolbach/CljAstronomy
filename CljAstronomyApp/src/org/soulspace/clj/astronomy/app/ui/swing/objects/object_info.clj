(ns org.soulspace.clj.astronomy.app.ui.swing.objects.object-info
  (:use [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.data common labels filters constellations]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

(defn object-panel
  "Creates an object panel."
  [o]
  (let [f-name (text-field {:columns 20 :text (object-label o) :editable false})
        f-constellation (text-field {:columns 20 :text (constellation-label (:constellation o)) :editable false})
        f-type (text-field {:columns 20 :text (object-type-name (:type o)) :editable false})
        f-ra (number-field {:columns 20 :text (str (:ra o)) :editable false})
        f-dec (number-field {:columns 20 :text (str (:dec o)) :editable false})
        f-mag (number-field {:columns 20 :text (str (:mag o)) :editable false})
        p (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                       :columnConstraints "[left|grow]"})}
                 [[(label {:text (i18n "label.object.info.title") :font heading-font}) "left, wrap 10"]
                  (label {:text (i18n "label.object.name")}) f-name
                  (label {:text (i18n "label.object.constellation")}) f-constellation
                  (label {:text (i18n "label.object.type")}) f-type
                  (label {:text (i18n "label.object.ra")}) f-ra
                  (label {:text (i18n "label.object.dec")}) f-dec
                  (label {:text (i18n "label.object.mag")}) f-mag])]
    (defn update-object-panel
      [object])

    (defn clear-object-panel
      [])

    p))

(defn object-info-dialog
  "Creates an object info dialog"
  [parent o]
  (if (seq o)
    (let [b-ok (button {:text (i18n "button.ok")})
          d (dialog parent {:title (i18n "label.object.info.title")}
                    [(panel {:layout (mig-layout {:layoutConstraints "wrap 1"})}
                           [(object-panel o)
                            [b-ok "span, tag ok"]])])]
      (.setVisible d true)
      (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
      d)))
