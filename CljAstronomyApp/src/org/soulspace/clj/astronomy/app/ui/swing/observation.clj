(ns org.soulspace.clj.astronomy.app.ui.swing.observation
  (:use [clojure.tools swing-utils]
        [org.soulspace.clj.java.awt]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.astronomy.app.application i18n])
  (:import [javax.swing Action BorderFactory JFrame]))

(defn observation-conditions-panel
  []
  (let [f-time-start (text-field)
        f-time-end (text-field)
        f-location-name (text-field)
        f-location-latitude (text-field)
        f-location-longitude (text-field)
        f-seeing (text-field)
        f-transparency (text-field)
        f-faintest-star (text-field)
        f-sky-quality-meter (text-field)
        f-condition-notes (text-area)]
    ))

(defn observation-equipment-panel
  []
  (let [f-optics (text-field)]
    ))

(defn observation-deepsky-panel
  []
  (let []
    ))

(defn observation-sun-panel
  []
  (let []
    ))

(defn observation-panel
  []
  )