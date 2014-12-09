(ns org.soulspace.clj.astronomy.app.application.catalog
  (:require [clojure.string :as str])
  (:use [clojure.java.io]
        [clojure.data.csv]))

(def data-dir "/home/soulman/devel/tmp/astro/")
(def hyg-file "hygxyz.csv")
(def messier-file "messier.csv")
(def constellation-file "data/constellations.clj")

(def bayer-flamsteed-pattern #"(\d+)?(\s|[A-Za-z]+)?(\s|\d+)?([A-Za-z]{3})")
(def messier-pattern #"M(d+)")
(def ngc-pattern #"NGC(\d+)")

; TODO move to data files
(def catalogs {:hip "Hipparchos"
               :hd "Henry Draper"
               :hr "Harvard Revised/Yale Bright Star"
               :gliese "Gliese"
               :messier "Messier"
               :ngc "New General"
               :ic "Index"
               })

(def constellations (load-file constellation-file))

(defn bayer-flamsteed-designations
  "Extract the Flamsteed number, Bayer letter and Constellation from the string s."
  [s]
  (if (seq s)
    (re-matches bayer-flamsteed-pattern s)
    [nil nil nil nil nil]))

(defn mag-filter
  "Returns a filter for magnification of objects. Faintest and optionally brightest can be given."
  ([faintest]
    (mag-filter faintest -30))
  ([faintest brightest]
    (fn [obj] (and (<= (:mag obj) faintest) (>= (:mag obj) brightest)))))

(defn ra-dec-filter
  "Returns a filter for the RA and Dec coordinates of an object."
  ([ra-min dec-min ra-max dec-max]
    (fn [obj] (and (>= (:ra obj) ra-min) (>= (:dec obj) dec-min) (<= (:ra obj) ra-max) (<= (:dec obj) dec-max)))))

(defn common-name-filter
  "Returns a filter for objects with a common name."
  []
  (fn [obj] (seq (:common-name obj))))

(defn parse-hyg
  "Parse a line of HYP 2.0 data."
  [[id hip hd hr gliese bayer-flamsteed proper-name ra dec distance pm-ra pm-deg rv mag abs-mag spectral-type color-index x y z v-x v-y v-z]]
  (let [[_ flamsteed bayer superscript constellation] (bayer-flamsteed-designations bayer-flamsteed)]
    {:hyg-id id
     :hip hip
     :hd hd
     :hr hr
     :gliese gliese
     :bayer bayer
     :flamsteed flamsteed
     :constellation constellation
     :common-name (str/trim proper-name)
     :ra (java.lang.Double/valueOf ra)
     :dec (java.lang.Double/valueOf dec)
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
     }))

(defn read-hyg
  []
  (with-open [in-file (reader hyg-file)]
    (->>
      (read-csv in-file :separator \,)
      (drop 2)
      (map parse-hyg)
      (filter (mag-filter 6))
      ;(filter (ra-dec-filter -180 0 180 90))
      ;(filter #(seq (:proper-name %1)))
      (map :constellation)
      ;(count)
      (take 10000)
      (into #{})
      (sort)
      (map #(str "[\"" %1 "\" \"\" \"\"]"))
      ;(str/join ",")
      (println))))

(defn parse-messier
  "Parse a line of Messier data."
  [[id type ra dec mag size ngc constellation detailed-type common-name]]
  {:messier id
   :type type
   :ra ra ;(java.lang.Double/valueOf ra)
   :dec dec ;(java.lang.Double/valueOf dec)
   :mag mag ;(java.lang.Double/valueOf mag)
   :size size
   :ngc ngc
   :constellation constellation
   :detailed-type detailed-type
   :common-name common-name
   })

(defn read-messier
  []
  (with-open [in-file (reader messier-file)]
    (->>
      (read-csv in-file)
      (drop 1)
      (map parse-messier)
      (map :constellation)
      ;(take 10)
      (into #{})
      (sort)
      (str/join ",")
      (println)
      )))
