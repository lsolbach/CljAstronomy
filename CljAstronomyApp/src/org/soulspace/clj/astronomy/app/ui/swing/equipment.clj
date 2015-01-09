(ns org.soulspace.clj.astronomy.app.ui.swing.equipment
  (:use [org.soulspace.clj.java.awt]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.astronomy.app i18n])
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
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})} [])))

(defn eyepiece-panel
  []
  (let [f-name (text-field)
        f-focal-length (number-field)
        f-field-of-view (number-field)
        f-available (check-box)]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})} [])))

(defn filter-panel
  []
  (let [f-name (text-field)
        f-type (text-field) ; TODO combo box SKYGLOW/UHC/LINE/NEUTRAL DENSITY
        ]
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})} [])))

(defn barlow-reducer-panel
  []
  (let []
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})} [])))

(defn equipment-panel
  []
  (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})} []))
