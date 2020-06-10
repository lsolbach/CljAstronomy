(ns org.soulspace.clj.astronomy.app.data.filters
  (:use [org.soulspace.clj.astronomy.coordinates coordinates]))

;
; object filters
;
(defn common-name?
  "Returns true if the object has a common name."
  [o]
  (not (empty? (:common-name o))))

(defn bayer-letter?
  "Returns true if the object has a bayer letter."
  [o]
  (not (nil? (:bayer o))))

(defn flamsteed-object?
  "Returns true if the object has a flamsteed."
  [o]
  (not (empty? (:flamsteed o))))

(defn hd-object?
  "Returns true if the object has a hd number."
  [o]
  (not (empty? (:hd o))))

(defn hr-object?
  "Returns true if the object has a hr number."
  [o]
  (not (empty? (:hr o))))

(defn gliese-object?
  "Returns true if the object has a gliese number."
  [o]
  (not (empty? (:gliese o))))

(defn hip-object?
  "Returns true if the object has a hip number."
  [o]
  (not (empty? (:hip o))))

(defn messier-object?
  "Returns true if the object is a messier object."
  [o]
  (not (empty? (:messier o))))

(defn ngc-object?
  "Returns true if the object is listed in the New General Catalog."
  [o]
  (not (empty? (:ngc o))))

(defn ic-object?
  "Returns true if the object is listed in the Index Catalog."
  [o]
  (not (empty? (:ic o))))

(defn ngc-ic-object
  "Returns true if the object is listed in the New General Catalog or Index Catalog."
  [o]
  (or (ngc-object? o) (ic-object? o)))

(defn pk-object?
  "Returns true if the object is listed in the Perek and Kohoutek Catalog."
  [o]
  (not (empty? (:pk o))))

(defn c-object?
  "Returns true if the object is listed in the Cadwell Catalog."
  [o]
  (not (empty? (:c o))))

(defn col-object?
  "Returns true if the object is listed in the Collinder Catalog."
  [o]
  (not (empty? (:col o))))

(defn mel-object?
  "Returns true if the object is listed in the Melotte Catalog."
  [o]
  (not (empty? (:mel o))))

(defn pgc-object?
  "Returns true if the object is listed in the PGC Catalog."
  [o]
  (not (empty? (:pgc o))))

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

(defn rad-ra-dec-filter
  "Returns a filter for the RA and Dec coordinates of an object."
  ([[ra-min dec-min] [ra-max dec-max]]
   (rad-ra-dec-filter ra-min dec-min ra-max dec-max))
  ([ra-min dec-min ra-max dec-max]
   (fn [obj] (and (>= (:ra-rad obj) ra-min) (>= (:dec-rad obj) dec-min) (<= (:ra-rad obj) ra-max) (<= (:dec-rad obj) dec-max)))))

(defn angular-distance-filter
  "Returns a filter for the angular distance of an object"
  ([dist [ra dec]]
   (angular-distance-filter dist ra dec))
  ([dist ra dec]
   (fn [obj] (<= (angular-distance [(:ra-rad obj) (:dec-rad obj)] [ra dec]) dist))))

(defn common-name-filter
  "Returns a filter for objects with a common name."
  ([]
   (fn [obj] (seq (:common-name obj))))
  ([common-name]
   (fn [obj] (re-matches (re-pattern common-name) (:common-name obj)))))

(defn type-filter
  "Returns a filter for typed objects."
  ([]
   #(and (:type %) (not= (:type %) :unknown)))
  ([type]
   #(and (:type %) (not= (:type %) (keyword type)))))
