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

(ns org.soulspace.clj.astronomy.app.data.hyg-star-catalog
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

(def hyg-star-file (str adc/data-dir "/catalogs/hygdata_v3.csv"))

(defn bayer-flamsteed-designations
  "Extract the Flamsteed number, Bayer letter, Bayer superscript and Constellation from the string s."
  [s]
  (if (seq s)
    (re-matches #"(\d+)?(\s|[A-Za-z]+)?(\s|\d+)?([A-Za-z]{3})" s)
    [nil nil nil nil nil]))

(defn parse-hyg-star
  "Parse a line of HYG 3.0 star catalog data."
  [[id hip hd hr gliese bayer-flamsteed proper-name ra dec distance
    pm-ra pm-deg rv mag abs-mag spectral-type color-index
    x y z v-x v-y v-z ra-rad dec-rad pm-ra-rad pm-dec-rad
    bayer flam con comp comp-primary base lum var var-min var-max]]
  (let [[_ flamsteed bayer superscript constellation] (bayer-flamsteed-designations bayer-flamsteed)]
    {:hyg-id id
     :type :star
     :hd (if (seq hd) hd)
     :hr (if (seq hr) hr)
     :hip (if (seq hip) hip)
     :gliese (if (seq gliese) gliese)
     :bayer (if (seq bayer) (adc/greek-abbrev-keys bayer))
     :bayer-superscript (if (not (= superscript " ")) superscript)
     :flamsteed (if (seq flam) flam)
     :constellation (if (and (seq con) (= (count con) 3)) (keyword con))
     :common-name (str/trim proper-name)
     :ra (Double/valueOf ra)
     :dec (Double/valueOf dec)
     :distance distance
     :mag (Double/valueOf mag)
     :mag-abs (Double/valueOf abs-mag)
     :spectral-type spectral-type
     :color-index color-index
     :x x
     :y y
     :z z
     :v-x v-x
     :v-y v-y
     :v-z v-z
     :ra-rad (Double/valueOf ra-rad) ; (if (seq ra-rad) (java.lang.Double/valueOf ra-rad))
     :dec-rad (Double/valueOf dec-rad) ; (if (seq dec-rad) (java.lang.Double/valueOf dec-rad))
     :pm-ra-rad (if (seq pm-ra-rad) (Double/valueOf pm-ra-rad))
     :pm-dec-rad (if (seq pm-dec-rad) (Double/valueOf pm-dec-rad))
     :lum (if (seq lum) (Double/valueOf lum))
     :var (if (seq var) var)
     :mag-var-min (if (seq var-min) (Double/valueOf var-min))
     :mag-var-max (if (seq var-max) (Double/valueOf var-max))
     :comp (if (seq comp) comp)
     :comp-primary (if (seq comp-primary) comp-primary)
     :base (if (seq base) base)}))

(defn parse-hyg-star-mapping-transformer
  "Creates a mapping transformation from csv vector to hyg catalog star."
  []
  (map #(parse-hyg-star %)))

(defn read-hyg-star
  "Read and parse the HYG star data."
  []
  (with-open [in-file (io/reader hyg-star-file)]
    (into []
      (comp
        (drop 2) ; line 0 are the headers, line 1 is sol, our own star
        (map parse-hyg-star))
      (csv/read-csv in-file :separator \,))))

(defn load-hyg-star-catalog
  "Loads the HYG star catalog."
  []
  ; load catalog asynchronously so the application start is not delayed by catalog loading
  (let [t (thread (read-hyg-star))]
    (go (reset! objects (<!! t)))))

(defn filter-xf
  ""
  [criteria]
  )

(defn get-objects
  "Returns the loaded objects of this catalog, optionally filtered by the given criteria."
  ([]
   @objects)
  ([criteria]
   (into [] (filter (cat/filter-xf criteria)) @objects)))

(defrecord HygStarCatalog
  [in out]
  )