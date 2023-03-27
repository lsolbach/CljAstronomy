(ns org.soulspace.clj.astronomy.app.utility.data
  "Utility functions for astronomical data.")

(defn write-data
  "Writes the data to a file."
  [filename data]
  (spit filename data))

