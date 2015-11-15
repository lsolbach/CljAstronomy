(ns org.soulspace.clj.astronomy.app.ui.swing.objects.object-list
  (:use [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.data common labels filters constellations]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

(def object-list (ref []))

(defn set-object-list
  "Updates the object list."
  [coll]
  (dosync (ref-set object-list coll)))

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

(defn catalog-list-model
  "Returns the catalog list model."
  []
  (seq-list-model (vals catalog-name)))

(defn object-type-list-model
  "Returns the catalog list model."
  []
  (seq-list-model (vals catalog-name)))

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
        ]
    (defn apply-filter
      "Applies the specified filter to the data."
      [])
    (defn clear-filter
      "Clears the specified filter."
      [])
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 4, fill"
                                 :columnConstraints "[left|grow]"})}
           [(label {:text (i18n "label.object.filter.name")}) f-name
            (label {:text (i18n "label.object.filter.constellation")}) f-constellation
            (label {:text (i18n "label.object.filter.ra-min")}) f-ra-min
            (label {:text (i18n "label.object.filter.ra-max")}) f-ra-max
            (label {:text (i18n "label.object.filter.dec-min")}) f-dec-min
            (label {:text (i18n "label.object.filter.dec-max")}) f-dec-max
            (label {:text (i18n "label.object.filter.mag-min")}) f-mag-min
            (label {:text (i18n "label.object.filter.mag-max")}) f-mag-max
            (button {:text (i18n "button.clear")}) (button {:text (i18n "button.apply")})
            ])
    ))

(defn object-list-panel
  "Creates an object list panel."
  ([]
    (object-list-panel @object-list))
  ([coll]
    (let [t-object-list (table {:model (objectlist-table-model coll) :gridColor java.awt.Color/DARK_GRAY})
          p (panel {:layout (mig-layout {:layoutConstraints "wrap 1, insets 10, fill"
                                         :columnConstraints "[left|grow]"
                                         :rowConstraints "[grow]"})}
                   [(scroll-pane t-object-list)])]
      p)))

(defn object-list-dialog
  "Creates the object list dialog."
  ([]
    (object-list-dialog @object-list))
  ([coll]
    (set-object-list coll)
    (let [b-ok (button {:text (i18n "button.ok")})
          filter-panel (object-filter-panel)
          list-panel (object-list-panel)
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
    (set-object-list coll)
    (let [b-ok (button {:text (i18n "button.ok")})
          filter-panel (object-filter-panel)
          p (object-list-panel)
          d (dialog parent {:title (i18n "label.object.list.title")}
                    [(panel {:layout (mig-layout {:layoutConstraints "wrap 1"})}
                            [p
                             [b-ok "span, tag ok"]])])]
      (.setVisible d true)
      (add-action-listener b-ok (action-listener (fn [_] (.setVisible d false))))
      d)))
