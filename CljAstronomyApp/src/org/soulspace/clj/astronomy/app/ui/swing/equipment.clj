(ns org.soulspace.clj.astronomy.app.ui.swing.equipment
  (:use [clojure.tools swing-utils]
        [org.soulspace.clj.java.awt]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.astronomy.app.application i18n])
  (:import [javax.swing Action BorderFactory JFrame]))

(defn optics-panel
  []
  (let [f-name (text-field)
        f-type (text-field)
        f-aperture (number-field)
        f-focal-length (number-field)
        f-effectivness (number-field)
        f-fixed-magnification (check-box) ; TODO Radio buttons
        f-magnification (number-field)
        f-field-of-view (number-field)
        f-available (check-box)]
    ))

(defn eyepiece-panel
  []
  (let [f-name (text-field)
        f-focal-length (number-field)
        f-field-of-view (number-field)
        f-available (check-box)]
    ))

(defn filter-panel
  []
  (let [f-name (text-field)
        f-type (text-field) ; TODO combo box SKYGLOW/UHC/LINE/NEUTRAL DENSITY
        ]
    ))

(defn barlow-reducer-panel
  []
  (let []
    ))

(defn equipment-panel
  []
  )

