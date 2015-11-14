(ns org.soulspace.clj.astronomy.app.data.dso-catalog
  (:require [clojure.string :as str])
  (:use [clojure.set :only [map-invert]]
        [clojure.java.io]
        [clojure.data.csv]
        [org.soulspace.clj.astronomy.app.data common constellations greek]))

(def dso-file (str data-dir "/catalogs/dso.csv"))

(def dso-source-map {"0" "miscellaneous, limited detail (e.g. Wikipedia)."
                     "1" "NGC 2000 (Sinott, 1988)."
                     "2" "Historically Corrected New General Catalogue from the NGC/IC project (http://www.ngcic.org)."
                     "3" "PGC galaxy catalog (http://leda.univ-lyon1.fr/)."
                     "4" "Collinder open cluster catalog, items not already in Messier,Caldwell,NGC,IC and with defined size and magnitude (http://www.cloudynights.com/item.php?item_id=2544)."
                     "5" "Perek-Kohoutek catalog IDs, from original (Perek + Kouhoutek, 1967) and update (Perek + Kohoutek, 2001)."
                     "6" "Faint globulars (Palomar + Terzian) from http://www.astronomy-mall.com/Adventures.In.Deep.Space/obscure.htm and http://www.astronomy-mall.com/Adventures.In.Deep.Space/palglob.htm."
                     })

(def dso-type-map {"*" "Single Star"
                   "**" "Double Star"
                   "***" "Triple Star"
                   "Ast" "Asterism"
                   "Gxy" "Galaxy"
                   "GxyCld" "Bright cloud/knot in a galaxy"
                   "GC" "Globular Cluster"
                   "HIIRgn" "HII Region"
                   "Neb" "Nebula (emission or reflection)"
                   "NF" "Not Found"
                   "OC" "Open Cluster"
                   "PN" "Planetary Nebula"
                   "DN" "Dark Nebula"
                   "SNR" "Supernova Remnant"
                   "MWSC" "Milky Way Star Cloud"
                   "Neb?" "Nebula?"
                   "?" "Unknown"
                   "" "Unknown"
                   })

(defn dso-type
  "Returns the type of the dso object."
  [type]
  ;type ; TODO parse type
  (cond
    (= type "*") :star
    (= type "**") :double-star
    (= type "***") :triple-star
    (= type "Ast") :asterism
    (= type "Gxy") :galaxy
    (= type "GxyCld") :galaxy-cloud
    (= type "MWSC") :star-cloud
    (= type "OC") :open-cluster
    (= type "OC+Neb") :open-cluster
    (= type "GC") :globular-cluster
    (= type "HIIRgn") :emission-nebula
    (= type "Neb") :emission-nebula
    (= type "SNR") :supernova-remnant
    (= type "PN") :planetary-nebula
    (= type "DN") :dark-nebula
    (= type "Neb?") :unknown
    (= type "NF") :unknown
    (= type "PD") :unknown
    (= type "?") :unknown
    (= type "") :unknown
    :default (do (println type) :unknown)
    ))

(defn catalog-id
  "Extracts the catalog id of the object."
  [catalog id1 cat1 id2 cat2]
  (cond
    (= cat1 catalog) id1
    (= cat2 catalog) id2))

(defn parse-dso
  "Parse a line of Messier data."
  [[ra dec type const mag common-name ra-rad dec-rad id r1 r2 angle dso-source id1 cat1 id2 cat2 dupid dupcat display-mag]]
  {:ra (java.lang.Double/valueOf ra)  ; TODO store rad angle
   :dec (java.lang.Double/valueOf dec)  ; TODO store rad angle
   :type (dso-type type)
   :constellation (keyword const)
   :const const
   :mag (if (seq mag) (java.lang.Double/valueOf mag) 100.0)
   :common-name (if (seq common-name) common-name)
   :ra-rad (java.lang.Double/valueOf ra-rad)  ; TODO store rad angle
   :dec-rad (java.lang.Double/valueOf dec-rad)  ; TODO store rad angle
   :id id
   :r1 (if (seq r1) (java.lang.Double/valueOf r1))
   :r2 (if (seq r2) (java.lang.Double/valueOf r2))
   :pos-angle (if (seq angle) (java.lang.Double/valueOf angle))
   :source dso-source
   :id1 id1
   :cat1 cat1
   :id2 id2
   :cat2 cat2
   :dupid dupid
   :dupcat dupcat
   :display-mag (if (seq display-mag) (java.lang.Double/valueOf display-mag))
   :messier (catalog-id "M" id1 cat1 id2 cat2)
   :ngc (catalog-id "NGC" id1 cat1 id2 cat2)
   :ic (catalog-id "IC" id1 cat1 id2 cat2)
   :c (catalog-id "C" id1 cat1 id2 cat2)
   :col (catalog-id "Col" id1 cat1 id2 cat2)
   :mel (catalog-id "Mel" id1 cat1 id2 cat2)
   :pk (catalog-id "PK" id1 cat1 id2 cat2)
   :pgc (catalog-id "PGC" id1 cat1 id2 cat2)
   })

(defn read-dso
  "Read the messier catalog."
  []
  (with-open [in-file (reader dso-file)]
    (->>
      (read-csv in-file)
      (drop 1)
      (map parse-dso)
      (filter #(not= (:type %) :unknown))
      ;(filter #(contains? #{"M" "NGC" "IC" "PK" "Col"} (:cat1 %)))
      (filter #(not (:dupcat %)))
      (filter #(not= (:messier %) "40"))
      ; force the sequence because the stream is closed when leaving the macro
      (doall))))
