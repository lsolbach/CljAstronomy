;;;;
;;;;   Copyright (c) Ludger Solbach. All rights reserved.
;;;;
;;;;   The use and distribution terms for this software are covered by the
;;;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;;;   which can be found in the file license.txt at the root of this distribution.
;;;;   By using this software in any fashion, you are agreeing to be bound by
;;;;   the terms of this license.
;;;;
;;;;   You must not remove this notice, or any other, from this software.
;;;;

(ns org.soulspace.clj.astronomy.app.data.hyg-dso-catalog
  (:require [clojure.string :as str]
            [clojure.set :refer [map-invert]]
            [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]

            [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [org.soulspace.clj.astronomy.app.data.common :as adc]
            [org.soulspace.clj.astronomy.app.data.catalog :as cat]))

(def objects (atom []))

(def hyg-dso-file (str adc/data-dir "/catalogs/dso.csv"))

(def hyg-dso-sources {"0" "miscellaneous, limited detail (e.g. Wikipedia)."
                         "1" "NGC 2000 (Sinott, 1988)."
                         "2" "Historically Corrected New General Catalogue from the NGC/IC project (http://www.ngcic.org)."
                         "3" "PGC galaxy catalog (http://leda.univ-lyon1.fr/)."
                         "4" "Collinder open cluster catalog, items not already in Messier,Caldwell,NGC,IC and with defined size and magnitude (http://www.cloudynights.com/item.php?item_id=2544)."
                         "5" "Perek-Kohoutek catalog IDs, from original (Perek + Kouhoutek, 1967) and update (Perek + Kohoutek, 2001)."
                         "6" "Faint globulars (Palomar + Terzian) from http://www.astronomy-mall.com/Adventures.In.Deep.Space/obscure.htm and http://www.astronomy-mall.com/Adventures.In.Deep.Space/palglob.htm."})

(def hyg-dso-types {"*" "Single Star"
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
                    "" "Unknown"})

(defn hyg-dso-type
  "Returns the type of the dso object."
  [type]
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
    :default (do (println "Unknown type in HYG DSO catalog:" type) :unknown)))

(defn catalog-id
  "Extracts the catalog id of the object."
  [catalog id1 cat1 id2 cat2]
  (cond
    (= cat1 catalog) id1
    (= cat2 catalog) id2))

(defn parse-hyg-dso
  "Parse a line of HYG DSO catalog data."
  [[ra dec type const mag common-name ra-rad dec-rad id r1 r2 angle dso-source id1 cat1 id2 cat2 dupid dupcat display-mag]]
  {:ra (Double/valueOf ra)
   :dec (Double/valueOf dec)
   :type (hyg-dso-type type)
   :constellation (keyword const)
   :mag (if (seq mag) (Double/valueOf mag) 100.0)
   :common-name (if (seq common-name) common-name)
   :ra-rad (Double/valueOf ra-rad)
   :dec-rad (Double/valueOf dec-rad)
   :id id
   :r1 (if (seq r1) (Double/valueOf r1))
   :r2 (if (seq r2) (Double/valueOf r2))
   :pos-angle (if (seq angle) (Double/valueOf angle))
   :source dso-source
   :id1 id1
   :cat1 cat1
   :id2 id2
   :cat2 cat2
   :dupid dupid
   :dupcat dupcat
   :display-mag (if (seq display-mag) (Double/valueOf display-mag))
   :messier (catalog-id "M" id1 cat1 id2 cat2)
   :ngc (catalog-id "NGC" id1 cat1 id2 cat2)
   :ic (catalog-id "IC" id1 cat1 id2 cat2)
   :c (catalog-id "C" id1 cat1 id2 cat2)
   :col (catalog-id "Col" id1 cat1 id2 cat2)
   :mel (catalog-id "Mel" id1 cat1 id2 cat2)
   :pk (catalog-id "PK" id1 cat1 id2 cat2)
   :pgc (catalog-id "PGC" id1 cat1 id2 cat2)})

(defn read-xf
  "Returns a transducer to transform the HYG DSO data on read."
  []
  (comp
   (drop 1)
   (map parse-hyg-dso)
   (filter #(< (:mag %) 16))
   (filter #(not= (:type %) :unknown))
   (filter #(not= (:messier %) "40"))))


(defn read-hyg-dso
  "Read the HYG DSO catalog."
  []
  (with-open [in-file (io/reader hyg-dso-file)]
    (into []
          (comp
            (drop 1)
            (map parse-hyg-dso)
            (filter #(< (:mag %) 16))
            (filter #(not= (:type %) :unknown))
            ;(filter #(contains? #{"M" "NGC" "IC" "PK" "Col"} (:cat1 %)))
            ; filter messier 40 double star
            (filter #(not= (:messier %) "40")))
          (csv/read-csv in-file))))

(defn load-hyg-dso-catalog
  "Loads the HYG dso catalog."
  []
  ; load catalog asynchronously so the application start is not delayed by catalog loading
  (let [t (thread (read-hyg-dso))]
    (go (reset! objects (<!! t)))))

(defn get-objects
  "Returns the loaded objects of this catalog, optionally filtered by the given criteria."
  ([]
   @objects)
  ([criteria]
   (into [] (filter (cat/filter-xf criteria)) @objects)))

; defrecord or deftype?
(defrecord HygDSOCatalog
           [in out objects] 
  )

