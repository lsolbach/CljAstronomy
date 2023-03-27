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

(ns org.soulspace.clj.astronomy.app.data.catalog
  (:require [clojure.set :as set]
            [org.soulspace.clj.astronomy.app.data.common :as acm]))

; use core.async with channels and go's to load the catalogs asynchronously

(comment
  ; filter criteria example, use criteria to build a filter transducer
  {:catalog-designations #{:hd :hip :ngc :ic}                            ; catalogs to include
   :object-types #{:star :galaxy}                            ; object types to include
   :magnitudes {:max -30 :min 6}                             ; magnitudes to include 
   :bounding-box {:coordinates-min {:ra-min 0 :deg-min -90}  ; bounding box on coordinates 
                  :coordinates-max {:ra-max 24 :deg-max 90}}
   :distance {:coordinates {:ra 12 :dec 0}                   ; angular distance from coordinates
              :max-distance 10}
   :constellations #{}}

  ; capabilities of the catalog
  {:catalog-designations #{:ngc :ic}
   :object-types #{:globular-cluster :galaxy :planetary-nebula
                   :emission-nebula :open-cluster :reflection-nebula}
   :magnitudes {:max -30. :min 6.}})

;;;
;;; Catalog filter predicates
;;;

(defn criterium-predicate
  "Returns a filter predicate for the given criterium."
  [[k v]]
  (cond
    ; object type criterium
    (= :object-types k)
    #(contains? v (:object-type %))
    ; catalog-designation criterium
    (= :catalog-designation k)
    #(contains? (:catalog-designations %) v)
    (= :magnitudes k)
    #(let [max (get v :max -30)
           min (get v :max 6)]
       (and (< max (:mag %) min)))))
    ; TODO add functions for other criteria
    
(defn filter-xf
  "Creates a filter transducer for the criteria."
  [criteria]
  (loop [remaining criteria filter-predicates []]
    (if (seq remaining)
      (recur (rest remaining) (conj filter-predicates (criterium-predicate (first remaining))))
      ; compose the filtering functions and create a filter transducer
      (filter (apply comp (remove nil? filter-predicates))))))

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
          :catalog-designations #{:messier :ngc}}]) 
  )
  