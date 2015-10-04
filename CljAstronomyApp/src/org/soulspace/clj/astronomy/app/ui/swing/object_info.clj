(ns org.soulspace.clj.astronomy.app.ui.swing.object-info
  (:use [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.data common labels filters constellations]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

(defn objectlist-table-model
  [coll]
  (mapseq-table-model
    [{:label (i18n "label.object.id") :key :id :edit false :converter str}
     {:label (i18n "label.object.name") :key :name :edit false :converter str} ; object-label
     {:label (i18n "label.object.constellation") :key :constellation :edit false :converter constellation-label}
     {:label (i18n "label.object.type") :key :type :edit false :converter type-label}
     {:label (i18n "label.object.ra") :key :ra :edit false :converter ra-label}
     {:label (i18n "label.object.dec") :key :dec :edit false :converter dec-label}
     {:label (i18n "label.object.mag") :key :mag :edit false }
     ;{:label (i18n "label.object.mag-abs") :key :mag-abs :edit false}
     ]
    coll))

(defn objectlist-table-model
  [coll]
  (mapseq-table-model
    [{:label (i18n "label.object.id") :key :id :edit false :converter str}
     {:label (i18n "label.object.name") :key identity :edit false :converter object-label} ; object-label
     {:label (i18n "label.object.constellation") :key :constellation :edit false :converter constellation-label}
     {:label (i18n "label.object.type") :key :type :edit false :converter type-label}
     {:label (i18n "label.object.ra") :key :ra :edit false :converter ra-label}
     {:label (i18n "label.object.dec") :key :dec :edit false :converter dec-label}
     {:label (i18n "label.object.mag") :key :mag :edit false }]
    coll))

(defn catalog-list-model
  "Returns the catalog list model."
  []
  (seq-list-model (vals catalog-name)))

(defn object-type-list-model
  "Returns the catalog list model."
  []
  (seq-list-model (vals catalog-name)))

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
    p))

(defn object-info-dialog
  "Creates an object info dialog"
  [parent o]
  (let [b-ok (button {:text (i18n "button.ok")})
        d (dialog parent {:title (i18n "label.object.info.title")}
                  [(panel {:layout (mig-layout {:layoutConstraints "wrap 1"})}
                         [(object-panel o)
                          [b-ok "span, tag ok"]])])]
    (.setVisible d true)
    (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
    d))

(defn object-filter-panel
  "Creates an object filter panel."
  []
  (let [f-name (text-field {:text "" :columns 20})
        f-constellation (text-field {:text "" :columns 20})
        f-ra-min (number-field {:text "" :columns 20})
        f-ra-max (number-field {:text "" :columns 20})
        f-dec-min (number-field {:text "" :columns 20})
        f-dec-max (number-field {:text "" :columns 20})
        f-mag-min (number-field {:text "" :columns 20})
        f-mag-max (number-field {:text "" :columns 20})
        l-catalogs (j-list {:model (catalog-list-model)})
        b-apply (button {:text (i18n "button.apply")})
        b-clear (button {:text (i18n "button.clear")})
        p (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 4, fill"
                                       :columnConstraints "[left|grow]"})}
                 [(label {:text (i18n "label.object.filter.name")}) f-name
                  (label {:text (i18n "label.object.filter.constellation")}) f-constellation
                  (label {:text (i18n "label.object.filter.ra-min")}) f-ra-min
                  (label {:text (i18n "label.object.filter.ra-max")}) f-ra-max
                  (label {:text (i18n "label.object.filter.dec-min")}) f-dec-min
                  (label {:text (i18n "label.object.filter.dec-max")}) f-dec-max
                  (label {:text (i18n "label.object.filter.mag-min")}) f-mag-min
                  (label {:text (i18n "label.object.filter.mag-max")}) f-mag-max
                  ])]
    (defn apply-filter
      ""
      [])
    (defn clear-filter
      ""
      [])
    p
    ))

(defn object-list-panel
  "Creates an object list panel."
  [coll]
    (let [t-object-list (table {:model (objectlist-table-model coll) :gridColor java.awt.Color/DARK_GRAY})
          p (panel {:layout (mig-layout {:layoutConstraints "wrap 1, insets 10, fill"
                                         :columnConstraints "[left|grow]"
                                         :rowConstraints "[grow]"})}
                   [(scroll-pane t-object-list)])]
      p))

(defn object-list-dialog
  ""
  ([coll]
    (let [b-ok (button {:text (i18n "button.ok")})
          filter-panel (object-filter-panel)
          list-panel (object-list-panel coll)
          d (dialog {:title (i18n "label.object.list.title")}
                    [(panel {:layout (mig-layout {:layoutConstraints "wrap 1"
                                                  :columnConstraints "[left|grow]"})}
                            [[(label {:text (i18n "label.object.filter.title") :font heading-font}) "left"]
                             filter-panel
                             [(label {:text (i18n "label.object.list.title") :font heading-font}) "left"]
                             (scroll-pane list-panel)
                             [b-ok "span, tag ok"]])])]
      (.setVisible d true)
      (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
      d))
  ([parent coll]
    (let [b-ok (button {:text (i18n "button.ok")})
          p (object-list-panel coll)
          d (dialog parent {:title (i18n "label.object.list.title")}
                    [(panel {:layout (mig-layout {:layoutConstraints "wrap 1"})}
                            [p
                             [b-ok "span, tag ok"]])])]
      (.setVisible d true)
      (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
      d)))
