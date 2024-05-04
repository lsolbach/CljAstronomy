(ns org.soulspace.astronomy.app.util.data
  "Utility functions for astronomical data."
  (:require [org.soulspace.astronomy.app.data.common :as adc]
            [org.soulspace.astronomy.app.data.hyg-dso-catalog :as chdc]))

;;;
;;; Tapping data
;;;

(defn data-tapper
  "Sends the data and and optional context to the tap.
     Useful for viewing data and debugging."
  ([data]
   (tap> data)
   data)
  ([ctx data]
   (tap> {:ctx ctx :data data})
   data))

