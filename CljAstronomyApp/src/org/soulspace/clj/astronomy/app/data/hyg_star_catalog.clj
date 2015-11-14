(ns org.soulspace.clj.astronomy.app.data.hyg-star-catalog
  (:require [clojure.string :as str])
  (:use [clojure.set :only [map-invert]]
        [clojure.java.io]
        [clojure.data.csv]
        [org.soulspace.clj.astronomy.app.data common constellations greek]))

(def hyg-star-file (str data-dir "/catalogs/hygdata_v3.csv"))

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
     :bayer (if (seq bayer) bayer)
     :bayer-superscript (if (not (= superscript " ")) superscript)
     :flamsteed (if (seq flam) flam)
     :constellation (if (and (seq con) (= (count con) 3)) (keyword con))
     :common-name (str/trim proper-name)
     :ra (java.lang.Double/valueOf ra) ; TODO store rad angle
     :dec (java.lang.Double/valueOf dec) ; TODO store rad angle
     :distance distance
     :mag (java.lang.Double/valueOf mag)
     :mag-abs (java.lang.Double/valueOf abs-mag)
     :spectral-type spectral-type
     :color-index color-index
     :x x
     :y y
     :z z
     :v-x v-x
     :v-y v-y
     :v-z v-z
     :ra-rad (java.lang.Double/valueOf ra-rad) ; (if (seq ra-rad) (java.lang.Double/valueOf ra-rad))
     :dec-rad (java.lang.Double/valueOf dec-rad) ; (if (seq dec-rad) (java.lang.Double/valueOf dec-rad))
     :pm-ra-rad (if (seq pm-ra-rad) (java.lang.Double/valueOf pm-ra-rad))
     :pm-dec-rad (if (seq pm-dec-rad) (java.lang.Double/valueOf pm-dec-rad))
     :lum (if (seq lum) (java.lang.Double/valueOf lum))
     :var (if (seq var) var)
     :mag-var-min (if (seq var-min) (java.lang.Double/valueOf var-min))
     :mag-var-max (if (seq var-max) (java.lang.Double/valueOf var-max))
     :comp (if (seq comp) comp)
     :comp-primary (if (seq comp-primary) comp-primary)
     :base (if (seq base) base)
     }))

(defn parse-hyg-star-mapping-transformer
  "Creates a mapping transformation from csv vector to hyg catalog star."
  []
  (map #(parse-hyg-star %)))

(defn read-hyg-star
  "Read and parse the HYG star data."
  []
  (with-open [in-file (reader hyg-star-file)]
    (->>
      (read-csv in-file :separator \,)
      (drop 2) ; line 0 are the headers, line 1 is sol, our own star
      (map parse-hyg-star)
      ; force the sequence because the stream is closed when leaving the macro
      (doall))))

(defn write-star-catalog
  "Writes the stars to a file."
  [filename stars]
  (spit filename stars))
