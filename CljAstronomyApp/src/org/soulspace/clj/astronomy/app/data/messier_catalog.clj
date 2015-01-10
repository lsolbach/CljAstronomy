(ns org.soulspace.clj.astronomy.app.data.messier-catalog
  (:use [clojure.set :only [map-invert]]
        [clojure.java.io]
        [clojure.data.csv]
        [org.soulspace.clj.math java-math]
        [org.soulspace.clj.astronomy.app.data common constellations greek]))

(def messier-file (str data-dir "messier.csv"))

(defn messier-type
  "Determines the type of the messier object."
  [type detailed-type]
  (cond 
    (= type "Galaxy") :galaxy ; TODO add galaxy types?
    (= type "Globular Cluster") :globular-cluster
    (= type "Open Cluster") (cond 
                              (= detailed-type "Asterism") :asterism
                              (= detailed-type "Binary Star") :binary-star
                              :default :open-cluster)
    (= type "Nebula") (cond 
                        (= detailed-type "Diffuse Nebula") :emission-nebula
                        (= detailed-type "Planetary Nebula") :planetary-nebula
                        (= detailed-type "Supernova Remnant") :supernova-remnant)))

(defn parse-messier-id
  "Parses the messier id of the messier label."
  [s]
  (second (re-matches #"M(\d+)" s)))

(defn parse-ngc-id
  "Parses the ngc id of the ngc label."
  [s]
  (second (re-matches #"NGC(\d+\w*)" s)))

(defn parse-messier
  "Parse a line of Messier data."
  [[id type ra dec mag size ngc constellation detailed-type common-name]]
  {:messier (parse-messier-id id)
   :type (messier-type type detailed-type)
   :ra (java.lang.Double/valueOf ra)
   :dec (java.lang.Double/valueOf dec)
   :ra-rad (deg-to-rad (* 15 (java.lang.Double/valueOf ra)))
   :dec-rad (deg-to-rad (java.lang.Double/valueOf dec))
   :mag (java.lang.Double/valueOf mag)
   :size size
   :ngc (parse-ngc-id ngc)
   :constellation (constellation-by-name-map constellation)
   :common-name common-name})

(defn read-messier
  "Read the messier catalog."
  []
  (with-open [in-file (reader messier-file)]
    (->>
      (read-csv in-file)
      (drop 1)
      (map parse-messier)
      (doall))))
