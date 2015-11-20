(ns org.soulspace.clj.astronomy.app.equipment)

(defprotocol Optic
  ""
  )

(defprotocol Eyepiece
  ""
  )

(defprotocol BarlowReducer
  ""
  )

(defprotocol Filter
  ""
  )


(def optics-list (ref []))
(def eyepiece-list (ref []))
(def barlow-list (ref []))
(def filter-list (ref []))

