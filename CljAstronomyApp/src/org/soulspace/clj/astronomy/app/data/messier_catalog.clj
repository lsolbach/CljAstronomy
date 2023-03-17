(ns org.soulspace.clj.astronomy.app.data.messier-catalog
  (:require [clojure.set :refer [map-invert]]
            [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [org.soulspace.math.core :as m])
  (:use [org.soulspace.clj.astronomy.app.data common constellations greek]))

(def messier-file "data/catalogs/messier.csv")

(defn messier-type
  "Determines the type of the messier object."
  [type detailed-type]
  (cond 
    (= type "Galaxy") :galaxy ; TODO add galaxy types?
    (= type "Globular Cluster") :globular-cluster
    (= type "Open Cluster") (cond 
                              (= detailed-type "Asterism") :asterism
                              (= detailed-type "Binary Star") :double-star
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
  {:id id
   :messier (parse-messier-id id)
   :type (messier-type type detailed-type)
   :ra (java.lang.Double/valueOf ra)
   :dec (java.lang.Double/valueOf dec)
   :ra-rad (m/deg-to-rad (* 15 (java.lang.Double/valueOf ra)))
   :dec-rad (m/deg-to-rad (java.lang.Double/valueOf dec))
   :mag (java.lang.Double/valueOf mag)
   :size size
   :ngc (parse-ngc-id ngc)
   :constellation (constellation-by-name-map constellation)
   :common-name common-name})

(defn parse-messier-mapping-transformer
  "Creates a mapping transformation from csv vector to messier object."
  []
  (map #(parse-messier %)))

(defn read-messier
  "Read the messier catalog."
  []
  (with-open [in-file (io/reader messier-file)]
    (into []
          (comp
            (drop 1)
            (map parse-messier))
          (csv/read-csv in-file))))
