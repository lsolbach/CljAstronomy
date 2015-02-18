(ns org.soulspace.clj.astronomy.app.data.common
  (:use [clojure.set :only [map-invert]]
        [org.soulspace.clj.astronomy.coordinates.coordinates :only [angular-distance]]
        [org.soulspace.clj.astronomy.app.data constellations greek]))

(def data-dir "/home/soulman/devel/tmp/astro/")

(def catalog-name {:hip "Hipparchos Catalog"
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

(def catalog-keys (keys catalog-name))
(def catalog-key (map-invert catalog-name))

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

(def object-hierarchy ""
  (->
    (make-hierarchy)
    (derive :double-star :star)
    (derive :triple-star :star)
    (derive :multiple-star :star)
    (derive :variable-star :star)
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
    (derive :irregular-galaxy :galaxy)
    ))

;
; angular distance
;
(defn angular-distance-of-object-and-coords
  "Calculates the angular distance of the coordinates and the object."
  [[ra dec] o]
  (angular-distance [ra dec] [(:ra-rad o) (:dec-rad o)]))

(defn object-with-smaller-angular-distance
  "Returns of the two objects the object with the smaller distance from the coordinates."
  [[ra dec] o1 o2]
  (if (<= (angular-distance-of-object-and-coords [ra dec] o1)
          (angular-distance-of-object-and-coords [ra dec] o2))
    o1
    o2))

(defn find-object-by-coordinates
  "Returns the object nearest to the coordinates."
  [[ra dec] coll]
  (reduce (partial object-with-smaller-angular-distance [ra dec]) coll))
