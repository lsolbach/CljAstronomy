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

(ns org.soulspace.clj.astronomy.app.data.common
  (:require [clojure.set :refer [map-invert]]
            [org.soulspace.clj.astronomy.coordinates.coordinates :refer [angular-distance]]))

;;;
;;; Catalogs
;;;

(def catalog-names {:hip "Hipparchos Catalog"
                    :hd "Henry Draper Catalog"
                    :hr "Harvard Revised/Yale Bright Star Catalog"
                    :gliese "Gliese Catalog"
                    :messier "Messier Catalog"
                    :ngc "New General Catalogue"
                    :ic "Index Catalogue"
                    :col "Collinder Catalog"
                    :mel "Melotte Catalog"
                    :tr "Trumpler Catalog"
                    :ter "Terzan Catalog"
                    :pgc "Principal Galaxies Catalog"
                    :ugc "Uppsala General Catalog of Galaxies"
                    :eso "European Southern Observatory"
                    :c "Caldwell Catalog"
                    :pal "Palomar Catalog"
                    :pk "Perek and Kohoutek Catalog"
                    :harvard "Harvard Catalog"})

(def catalog-keys (keys catalog-names))
(def catalog-key (map-invert catalog-names))


;;;
;;; Constellations
;;;

(def constellation-data
  [[:And "And" "Andromeda" "Andromedae"]
   [:Ant "Ant" "Antlia" "Antliae"]
   [:Aps "Aps" "Apus" "Apodis"]
   [:Aql "Aql" "Aquila" "Aquila"]
   [:Aqr "Aqr" "Aquarius" "Aquarii"]
   [:Ara "Ara" "Ara" "Arae"]
   [:Ari "Ari" "Aries" "Arietis"]
   [:Aur "Aur" "Auriga" "Aurigae"]
   [:Boo "Boo" "Bootes" "Bootis"]
   [:CMa "CMa" "Canis Major" "Canis Majoris"]
   [:CMi "CMi" "Canis Minor" "Canis Minoris"]
   [:CVn "CVn" "Canes Venatici" "Canum Venaticorum"]
   [:Cae "Cae" "Caelum" "Caeli"]
   [:Cam "Cam" "Camelopadis" "Camelopardalis"]
   [:Cap "Cap" "Capricornus" "Capricorni"]
   [:Car "Car" "Carina" "Carinae"]
   [:Cas "Cas" "Cassiopeia" "Cassiopeiae"]
   [:Cen "Cen" "Centarus" "Centauri"]
   [:Cep "Cep" "Cepheus" "Cephi"]
   [:Cet "Cet" "Cetus" "Ceti"]
   [:Cha "Cha" "Chamelaeon" "Chamaeleontis"]
   [:Cir "Cir" "Circinus" "Circini "]
   [:Cnc "Cnc" "Cancer" "Cancri"]
   [:Col "Col" "Columba" "Columbae"]
   [:Com "Com" "Coma Berenices" "Comae Berenices"]
   [:CrA "CrA" "Corona Australis" "Coronae Australis"]
   [:CrB "CrB" "Corona Borealis" "Coronae Borealis"]
   [:Crt "Crt" "Crater" "Crateris"]
   [:Cru "Cru" "Crux" "Cruxis"]
   [:Crv "Crv" "Corvus" "Corvii"]
   [:Cyg "Cyg" "Cygnus" "Cygni"]
   [:Del "Del" "Delphinus" "Delphini"]
   [:Dor "Dor" "Dorado" "Doradus"]
   [:Dra "Dra" "Draco" "Draconis"]
   [:Equ "Equ" "Equuleus" "Equulei"]
   [:Eri "Eri" "Eridianus" "Eridiani"]
   [:For "For" "Fornax" "Fornacis"]
   [:Gem "Gem" "Gemini" "Geminorum"]
   [:Gru "Gru" "Grus" "Gruis"]
   [:Her "Her" "Hercules" "Herculi"]
   [:Hor "Hor" "Horologium" "Horologii"]
   [:Hya "Hya" "Hydra" "Hydrae"]
   [:Hyi "Hyi" "Hydrus" "Hydri"]
   [:Ind "Ind" "Indus" "Indi"]
   [:LMi "LMi" "Leo Minor" "Leonis Minoris"]
   [:Lac "Lac" "Lacerta" "Lacertae"]
   [:Leo "Leo" "Leo" "Leonis"]
   [:Lep "Lep" "Lepus" "Leporis"]
   [:Lib "Lib" "Libra" "Librae"]
   [:Lup "Lup" "Lupus" "Lupi"]
   [:Lyn "Lyn" "Lynx" "Lyncis"]
   [:Lyr "Lyr" "Lyra" "Lyrae"]
   [:Men "Men" "Mensa" "Mensae"]
   [:Mic "Mic" "Microscopium" "Microscopii"]
   [:Mon "Mon" "Monoceros" "Monocerotis"]
   [:Mus "Mus" "Musca" "Muscae"]
   [:Nor "Nor" "Norma" "Normae"]
   [:Oct "Oct" "Octans" "Octantis"]
   [:Oph "Oph" "Ophiuchus" "Ophiuchi"]
   [:Ori "Ori" "Orion" "Orionis"]
   [:Pav "Pav" "Pavo" "Pavonis"]
   [:Peg "Peg" "Pegasus" "Pegasi"]
   [:Per "Per" "Perseus" "Persei"]
   [:Phe "Phe" "Phoenix" "Phoenicis"]
   [:Pic "Pic" "Pictor" "Pictoris"]
   [:PsA "PsA" "Piscis Austrinus" "Piscis Austrini"]
   [:Psc "Psc" "Pisces" "Piscium"]
   [:Pup "Pup" "Puppis" "Puppis"]
   [:Pyx "Pyx" "Pyxis" "Pyxidis"]
   [:Ret "Ret" "Reticulum" "Reticuli"]
   [:Scl "Scl" "Sculptor" "Sculptoris"]
   [:Sco "Sco" "Scorpius" "Scorpii"]
   [:Sct "Sct" "Scutum" "Scuti"]
   [:Ser "Ser" "Serpens" "Serpentis"]
   [:Sex "Sex" "Sextans" "Sextantis"]
   [:Sge "Sge" "Sagitta" "Sagittae"]
   [:Sgr "Sgr" "Sagittarius" "Sagittarii"]
   [:Tau "Tau" "Taurus" "Tauri"]
   [:Tel "Tel" "Telescopium" "Telescopii"]
   [:TrA "TrA" "Triangulum Australis" "Trianguli Australis"]
   [:Tri "Tri" "Triangulum" "Trianguli"]
   [:Tuc "Tuc" "Tucan" "Tucanis"]
   [:UMa "UMa" "Ursa Major" "Ursa Majoris"]
   [:UMi "UMi" "Ursa Minor" "Ursa Minoris"]
   [:Vel "Vel" "Vela" "Velorum"]
   [:Vir "Vir" "Virgo" "Virgonis"]
   [:Vol "Vol" "Volans" "Volantis"]
   [:Vul "Vul" "Vulpecula" "Vulpecularis"]])

(def constellation-keys (map first constellation-data))
(def constellation-abbrev-map (zipmap constellation-keys (map second constellation-data)))
(def constellation-name-map (zipmap constellation-keys (map #(nth % 2) constellation-data)))
(def constellation-genitivum-map (zipmap constellation-keys (map #(nth % 3) constellation-data)))
(def constellation-by-name-map (map-invert constellation-name-map))

;;;
;;; Greek alphabet
;;;

(def greek-data [[:alpha "Alpha" "α" "Alp"]
                 [:beta "Beta" "β" "Bet"]
                 [:gamma "Gamma" "γ" "Gam"]
                 [:delta "Delta" "δ" "Del"]
                 [:epsilon "Epsilon" "ε" "Eps"]
                 [:zeta "Zeta" "ζ" "Zet"]
                 [:eta "Eta" "η" "Eta"]
                 [:theta "Theta" "θ" "The"]
                 [:iota "Iota" "ι" "Iot"]
                 [:kappa "Kappa" "κ" "Kap"]
                 [:lambda "Lambda" "λ" "Lam"]
                 [:my "My" "μ" "My"]
                 [:ny "Ny" "ν" "Ny"]
                 [:xi "Xi" "ξ" "Xi"]
                 [:omikron "Omikron" "ο" "Omi"]
                 [:pi "Pi" "π" "Pi"]
                 [:rho "Rho" "ρ" "Rho"]
                 [:sigma "Sigma" "σ" "Sig"]
                 [:tau "Tau" "τ" "Tau"]
                 [:ypsilon "Ypsilon" "υ" "Yps"]
                 [:phi "Phi" "φ" "Phi"]
                 [:chi "Chi" "χ" "Chi"]
                 [:psi "Psi" "ψ" "Psi"]
                 [:omega "Omega" "ω" "Ome"]])

(def greek-keys (map first greek-data))
(def greek-letter-names (zipmap greek-keys (map second greek-data)))
(def greek-letters  (zipmap greek-keys (map #(nth % 2) greek-data)))
(def greek-abbrevs (zipmap greek-keys (map #(nth % 3) greek-data)))
(def greek-letter-name-keys (map-invert greek-letter-names))
(def greek-letter-keys (map-invert greek-letters))
(def greek-abbrev-keys (map-invert greek-abbrevs))


;;;
;;; object predicates
;;;

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


; TODO rename to object type
(def object-type-name {:satellite "Satellite"
                       :moon "Moon"
                       :planet "Planet"
                       :minor-planet "Minor Planet"
                       :comet "Comet"
                       :star "Star"
                       :variable-star "Variable Star"
                       :double-star "Double Star"
                       :triple-star "Triple Star"
                       :neutron-star "Neutron Star"
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
                       :galaxy-cluster "Galaxy Cluster"})

(def object-type-keys (keys object-type-name))
(def object-type-key (map-invert object-type-name))

(def object-hierarchy "Celestial object hierarchy"
  (->
   (make-hierarchy)
   (derive :double-star :star)
   (derive :triple-star :star)
   (derive :multiple-star :star)
   (derive :variable-star :star)
   (derive :neutron-star :star)
   (derive :asterism :dso)
   (derive :nebula :dso)
   (derive :galaxy :dso)
   (derive :open-cluster :dso)
   (derive :globular-cluster :dso)
   (derive :nebulous-open-cluster :open-cluster)
   (derive :star-cloud :open-cluster)
   (derive :galaxy-cloud :open-cluster)
   (derive :planetary-nebula :nebula)
   (derive :emission-nebula :nebula)
   (derive :reflection-nebula :nebula)
   (derive :supernova-remnant :nebula)
   (derive :dark-nebula :nebula)
   (derive :spiral-galaxy :galaxy)
   (derive :elliptical-galaxy :galaxy)
   (derive :lenticular-galaxy :galaxy)
   (derive :irregular-galaxy :galaxy)))

;
; default labels
;
(defmulti object-label :type :hierarchy #'object-hierarchy)

; "Returns the label for the star."
(defmethod object-label :star
  [star]
  (cond
    (common-name? star) (:common-name star)
    (bayer-letter? star) (greek-letters (:bayer star))
    (flamsteed-object? star) (:flamsteed star)
    (hd-object? star) (str "HD" (:hd star))
    (hr-object? star) (str "HR" (:hr star))
    (gliese-object? star) (str "Gliese" (:gliese star))
    (hip-object? star) (str "Hip" (:hip star))
    :default ""))

; "Returns the label for the deep sky object."
(defmethod object-label :dso
  [dso]
  (cond
    (common-name? dso) (:common-name dso)
    (messier-object? dso) (str "M " (:messier dso))
    (ngc-object? dso) (str "NGC " (:ngc dso))
    (ic-object? dso) (str "IC " (:ic dso))
    (pk-object? dso) (str "PK " (:pk dso))
    (c-object? dso) (str "C " (:c dso))
    (col-object? dso) (str "Col " (:col dso))
    (mel-object? dso) (str "Mel " (:mel dso))
    (pgc-object? dso) (str "PGC " (:pgc dso))
    :default ""))

(defmethod object-label nil
  [obj]
  (println (:id obj) (:type obj))
  "")

(defn ra-label
  "Returns the right ascension as string."
  [ra]
  (str ra))

(defn dec-label
  "Returns the declination as string."
  [dec]
  (str dec))

(defn constellation-label
  "Returns the constellation as string."
  [constellation]
  (if constellation
    (constellation-name-map constellation)
    ""))

(defn type-label
  "Returns the type as string."
  [type]
  (if type
    (object-type-name type)
    ""))

;;;
;;; object filters
;;;


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


;;;
;;; Celestial object labels
;;;

(defmulti object-label :type  :hierarchy #'object-hierarchy)

; "Returns the label for the star."
(defmethod object-label :star
  [star]
  (cond
    (common-name? star) (:common-name star)
    (bayer-letter? star) (greek-letters (:bayer star))
    (flamsteed-object? star) (:flamsteed star)
    (hd-object? star) (str "HD" (:hd star))
    (hr-object? star) (str "HR" (:hr star))
    (gliese-object? star) (str "Gliese" (:gliese star))
    (hip-object? star) (str "Hip" (:hip star))
    :default ""))

; "Returns the label for the deep sky object."
(defmethod object-label :dso
  [dso]
  (cond
    (common-name? dso) (:common-name dso)
    (messier-object? dso) (str "M " (:messier dso))
    (ngc-object? dso) (str "NGC " (:ngc dso))
    (ic-object? dso) (str "IC " (:ic dso))
    (pk-object? dso) (str "PK " (:pk dso))
    (c-object? dso) (str "C " (:c dso))
    (col-object? dso) (str "Col " (:col dso))
    (mel-object? dso) (str "Mel " (:mel dso))
    (pgc-object? dso) (str "PGC " (:pgc dso))
    :default ""))

(defmethod object-label nil
  [obj]
  (println (:id obj) (:type obj))
  "")

(defn ra-label
  "Returns the right ascension as string."
  [ra]
  (str ra))

(defn dec-label
  "Returns the declination as string."
  [dec]
  (str dec))

(defn constellation-label
  "Returns the constellation as string."
  [constellation]
  (if constellation
    (constellation-name-map constellation)
    ""))

(defn type-label
  "Returns the type as string."
  [type]
  (if type
    (object-type-name type)
    ""))

;;;
;;; angular distance
;;;

(defn angular-distance-of-object-and-coords
  "Calculates the angular distance of the coordinates and the object."
  [[ra dec] o]
  (angular-distance [ra dec] [(:ra-rad o) (:dec-rad o)]))

(defn find-object-by-coordinates
  "Returns the object nearest to the coordinates."
  [[ra dec] coll]
  (if (seq coll)
    (apply min-key (partial angular-distance-of-object-and-coords [ra dec]) coll)))
