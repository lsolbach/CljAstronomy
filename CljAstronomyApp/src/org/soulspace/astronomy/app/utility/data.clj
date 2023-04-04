(ns org.soulspace.astronomy.app.utility.data
  "Utility functions for astronomical data."
  (:require [org.soulspace.astronomy.app.data.common :as adc]
            [org.soulspace.astronomy.app.data.hyg-dso-catalog :as chdc]))

(defn write-data
  "Writes the data to a file."
  [filename data]
  (spit filename data))

