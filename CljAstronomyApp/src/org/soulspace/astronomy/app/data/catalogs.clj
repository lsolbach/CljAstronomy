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

(ns org.soulspace.astronomy.app.data.catalogs
  "Namespace for the catalog component."
  (:require [clojure.core.async :as a :refer [>! >!! <! <!!]]
            [org.soulspace.astronomy.app.data.common :as adc]))

;;;;
;;;; Catalog component
;;;;
;;;; Responsible for managing the catalogs
;;;;

(def catalogs (atom {:star-catalog nil
                     :dso-catalog nil}))


;;;
;;; Catalog filter predicates
;;;

(comment
  ; example for the filter criteria, use criteria to build a filter transducer
  {:catalog-designations #{:hd :hip :ngc :ic}                ; catalogs to include
   :object-types #{:star :galaxy}                            ; object types to include
   :constellations #{:And :Peg}                              ; constellations to include
   :magnitude {:max -30 :min 6}                              ; magnitudes to include 
   :bounding-box {:ra-min 0 :dec-min -90                     ; bounding box on coordinates 
                  :ra-max 24 :dec-max 90}
   :distance {:ra 12 :dec 0 :max-distance 10}}               ; angular distance from coordinates


  ; idea for the capabilities of a catalog
  {:catalog-designations #{:ngc :ic}
   :object-types #{:globular-cluster :galaxy :planetary-nebula
                   :emission-nebula :open-cluster :reflection-nebula}
   :magnitudes {:max -30. :min 6.}})

(defn criterium-predicate
  "Returns a filter predicate for the given criterium."
  [[k v]]
  (cond
    ; TODO add functions for other criteria
    ; constellation criterium
    (= :constellations k)
    #(contains? v (:constellation %))
    ; object type criterium
    (= :object-types k)
    #(contains? v (:object-type %))
    ; catalog-designation criterium
    (= :catalogs k)
    #(contains? (:catalogs %) v)
    (= :magnitude k)
    #(let [brightest (get v :brightest -30)
           faintest (get v :faintest 10)]
       (and (< brightest (:mag %) faintest)))
    (= :distance k)
    #(adc/angular-distance-below? (:max-distance v) (:ra v) (:dec v))
    (= :bounding-box k)
    #(let [ra-min (get v :ra-min)
           dec-min (get v :dec-min)
           ra-max (get v :ra-max)
           dec-max (get v :dec-max)]
       (and (<= ra-min (:ra-rad %))
            (<= dec-min (:dec-rad %))
            (< (:ra-rad %) ra-max)
            (< (:dec-rad %) dec-max)))))

(defn filter-xf
  "Creates a filter transducer for the map of criteria."
  [criteria]
  (loop [remaining criteria
         filter-predicates []]
    (if (seq remaining)
      (recur (rest remaining)
             (conj filter-predicates (criterium-predicate (first remaining))))
      ; compose the filtering functions and create a filter transducer
      (filter (apply every-pred (remove nil? filter-predicates))))))

(comment
  (first {:object-types #{:star :galaxy}})
  (criterium-predicate (first {:object-types #{:star :galaxy}}))
  (filter-xf {:object-types #{:star :galaxy}})
  (filter-xf {})
  (filter (comp))
  (into []
        (filter-xf {:object-types #{:star :galaxy}})
        [{:name "M33"
          :object-type :galaxy}
         {:name "M13"
          :object-type :globular-cluster}
         {:name "Polaris"
          :object-type :star}
         {:name "M57"
          :object-type :planetary-nebula}])
  (into []
        (filter-xf {:catalog-designation :ngc})
        [{:name "M33"
          :object-type :galaxy
          :catalog-designations #{:messier :ngc}}
         {:name "M13"
          :object-type :globular-cluster
          :catalog-designations #{:messier :ngc}}
         {:name "Polaris"
          :object-type :star
          :catalog-designations #{:bayer}}
         {:name "M57"
          :object-type :planetary-nebula
          :catalog-designations #{:messier :ngc}}]))

