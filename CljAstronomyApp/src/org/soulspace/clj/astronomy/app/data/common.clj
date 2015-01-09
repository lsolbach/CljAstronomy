(ns org.soulspace.clj.astronomy.app.data.common
  (:use [clojure.set :only [map-invert]]
        [org.soulspace.clj.astronomy.app.data constellations greek]))

(def data-dir "/home/soulman/devel/tmp/astro/")

(def catalogs {:hip "Hipparchos Catalog"
               :hd "Henry Drape Catalogr"
               :hr "Harvard Revised/Yale Bright Star Catalog"
               :gliese "Gliese Catalog"
               :messier "Messier Catalog"
               :ngc "New General Catalog"
               :ic "Index Catalog"})

(def deep-sky-name {:star "Star"
                    :variable-star "Variable Star"
                    :double-star "Double Star"
                    :triple-star "Triple Star"
                    :stellar-black-hole "Stellar Black Hole"
                    :asterism "Asterism"
                    :open-cluster "Open Cluster"
                    :globular-cluster "Globular Cluster"
                    :planetary-nebula "Planetary Nebula"
                    :emission-nebula "Emission Nebula"
                    :reflection-nebula "Reflection Nebula"
                    :dark-nebula "Dark Nebula"
                    :supernova-remnant "Supernova Remnant"
                    :star-cloud "Star Cloud"
                    :galaxy-cloud "Galaxy Cloud"
                    :galaxy "Galaxy"
                    :spiral-galaxy "Spiral Galaxy"
                    :elliptical-galaxy "Elliptical Galaxy"
                    :lenticular-galaxy "Lenticular Galaxy"
                    :irregular-galaxy "Irregular Galaxy"
                    :quasar "Quasar"
                    :galaxy-cluster "Galaxy Cluster"
                    })

(def deep-sky-type-keys (keys deep-sky-name))
(def deep-sky-type (map-invert deep-sky-name))

(defn has-common-name?
  "Returns true if the object has a common name."
  [o]
  (not (empty? (:common-name o))))

(defn has-bayer-letter?
  "Returns true if the object has a common name."
  [o]
  (not (empty? (:bayer o))))

(defn has-flamsteed-number?
  "Returns true if the object has a common name."
  [o]
  (not (empty? (:flamsteed o))))

(defn messier-object?
  "Returns true if the object is a messier object."
  [o]
  (not (empty? (:messier o))))

(defn ngc-object?
  "Returns true if the object is listed in the New General Catalog."
  [o]
  (not (empty? (:messier o))))

(defn ic-object?
  "Returns true if the object is listed in the Index Catalog."
  [o]
  (not (empty? (:messier o))))

(defn ngc-ic-object
  "Returns true if the object is listed in the New General Catalog or Index Catalog."
  [o]
  (or (ngc-object? o) (ic-object? o)))

(defn open-cluster?
  "Returns true if the object is an open cluster."
  [o]
  (= (:type o) :open-cluster))

(defn globular-cluster?
  "Returns true if the object is a globular cluster."
  [o]
  (= (:type o) :globular-cluster))

(defn galaxy?
  "Returns true if the object is a galaxy."
  [o]
  (or (= (:type o) :galaxy)
      (= (:type o) :spiral-galaxy)
      (= (:type o) :elliptical-galaxy)
      (= (:type o) :lenticular-galaxy)
      (= (:type o) :irregular-galaxy)))

(defn emission-nebula?
  "Returns true if the object is an emmission nebula."
  [o]
  (= (:type o) :emission-nebula))

(defn reflection-nebula?
  "Returns true if the object is a reflection nebula."
  [o]
  (= (:type o) :reflection-nebula))

(defn planetary-nebula?
  "Returns true if the object is a planetary nebula."
  [o]
  (= (:type o) :planetary-nebula))

(defn dark-nebula?
  "Returns true if the object is a dark nebula."
  [o]
  (= (:type o) :dark-nebula))

(defn mag-filter
  "Returns a filter for magnification of objects. Faintest and optionally brightest can be given."
  ([faintest]
    (mag-filter faintest -30))
  ([faintest brightest]
    (fn [obj] (and (<= (:mag obj) faintest) (>= (:mag obj) brightest)))))

(defn ra-dec-filter
  "Returns a filter for the RA and Dec coordinates of an object."
  ([[ra-min dec-min] [ra-max dec-max]]
    (ra-dec-filter ra-min dec-min ra-max dec-max))
  ([ra-min dec-min ra-max dec-max]
     (fn [obj] (and (>= (:ra obj) ra-min) (>= (:dec obj) dec-min) (<= (:ra obj) ra-max) (<= (:dec obj) dec-max)))))

(defn common-name-filter
  "Returns a filter for objects with a common name."
  ([]
    (fn [obj] (seq (:common-name obj))))
  ([common-name]
    (fn [obj] (re-matches (re-pattern common-name) (:common-name obj)))))

(defn star-label
  "Returns the label for the star."
  [star]
  (cond
    (has-common-name? star) (:common-name star)
    (has-bayer-letter? star) ((:bayer star) greek-letters)
    (has-flamsteed-number? star) (:flamsteed star)
    :default ""))

(defn dso-label
  "Returns the label for the deep sky object."
  [dso]
  (cond 
    (has-common-name? dso) (:common-name dso)
    (messier-object? dso) (str "M" (:messier dso))
    (ngc-object? dso) (str "NGC" (:ngc dso))
    (ic-object? dso) (str "IC" (:ngc dso))
    :default ""))

