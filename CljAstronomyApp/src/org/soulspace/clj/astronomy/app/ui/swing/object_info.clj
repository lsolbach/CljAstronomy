(ns org.soulspace.clj.astronomy.app.ui.swing.object-info
  (:use [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.ui.swing common]))

(defn ra-label
  "Returns the right ascension as string."
  [ra]
  (str ra))

(defn dec-label
  "Returns the declination as string."
  [dec]
  (str dec))

(defn objectlist-table-model
  [coll]
  (mapseq-table-model
    [{:label (i18n "label.object.id") :key :id :edit false}
     {:label (i18n "label.object.name") :key :name :edit false}
     {:label (i18n "label.object.ra") :key :ra :edit false :converter ra-label}
     {:label (i18n "label.object.dec") :key :dec :edit false :converter dec-label}
     {:label (i18n "label.object.mag") :key :mag :edit false }
     {:label (i18n "label.object.mag-abs") :key :mag-abs :edit false}
     ]
    coll))

(defn object-panel
  "Creates an object panel."
  []
  (let []
    ))

(defn object-filter-panel
  "Creates an object filter panel."
  []
  (let []
    ))

(defn object-list-panel
  "Creates an object list panel."
  []
  (let []
    ))

