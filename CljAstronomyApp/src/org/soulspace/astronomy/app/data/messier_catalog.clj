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

(ns org.soulspace.astronomy.app.data.messier-catalog
  (:require [clojure.set :refer [map-invert]]
            [clojure.core.async :as a :refer [>! >!! <! >!!]]
            [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [org.soulspace.math.core :as m]
            [org.soulspace.astronomy.app.data.common :as adc]
            [clojure.string :as str]))

(def catalog (atom {:initialized? false
                    :enabled? false
                    :source ""
                    :objects []
                    :object-types #{}
                    :catalog-designations #{:messier}}))

(def messier-file (str adc/data-dir "/catalogs/messier.csv"))

(defn messier-type
  "Determines the type of the messier object."
  [type detailed-type]
  (cond 
    (= type "Galaxy") :galaxy ; TODO add galaxy types?
    (= type "Globular Cluster") :globular-cluster
    (= type "Open Cluster") (cond 
                              (= detailed-type "Asterism") :asterism
                              (= detailed-type "Binary Star") :double-star
                              :default :open-cluster)
    (= type "Nebula") (cond 
                        (= detailed-type "Diffuse Nebula") :emission-nebula
                        (= detailed-type "Planetary Nebula") :planetary-nebula
                        (= detailed-type "Supernova Remnant") :supernova-remnant)))

(defn parse-messier-id
  "Parses the messier id of the messier label."
  [s]
  (second (re-matches #"M(\d+)" s)))

(defn parse-ngc-id
  "Parses the ngc id of the ngc label."
  [s]
  (second (re-matches #"NGC(\d+\w*)" s)))

(defn parse-messier
  "Parse a line of Messier data."
  [[id type ra dec mag size ngc constellation detailed-type common-name]]
  {:id id
   :messier (parse-messier-id id)
   :object-type (messier-type type detailed-type)
   :ra (java.lang.Double/valueOf ra)
   :dec (java.lang.Double/valueOf dec)
   :ra-rad (m/deg-to-rad (* 15 (java.lang.Double/valueOf ra)))
   :dec-rad (m/deg-to-rad (java.lang.Double/valueOf dec))
   :mag (java.lang.Double/valueOf mag)
   :size size
   :ngc (parse-ngc-id ngc)
   :constellation (adc/constellation-by-name-map (str/trim constellation))
   :common-name common-name})

(defrecord MessierObject
  [id messier object-type ra dec ra-rad dec-rad mag size ngc constellation common-name])

(defn read-xf
  "Creates a mapping transducer from csv vector to messier object."
  []
  (comp
   (drop 1)
   (map parse-messier)
   (map map->MessierObject)))

(defn read-messier
  "Read the messier catalog."
  []
  (with-open [in-file (io/reader messier-file)]
    (into [] (read-xf) (csv/read-csv in-file))))

(defn load-catalog!
  "Loads the Messier catalog."
  []
  ; load catalog asynchronously so the application start is not delayed by catalog loading
  (let [t (a/thread (read-messier))]
    (a/go (let [objs  (<! t)]
          (swap! catalog assoc :initialized? true :enabled? true :objects objs)))))

(defn get-objects
  "Returns the loaded objects of this catalog, optionally filtered by the given criteria."
  ([]
   (:objects @catalog))
  ([criteria]
   (into [] (adc/filter-xf criteria) (:objects @catalog))))

(defn handle-requests
  "Reads queries from the in channel and returns the results on the out channel."
  [in out]
  (a/go
    (while (:enabled? @catalog)
      (loop []
        (println "looping...")
        (let [request (<! in)]
          (adc/data-tapper "Request" request) ; for debugging
          (let [criteria (:criteria request)
                ; TODO check criteria against the capabilities of the repository
                ;      to skip real searches when not neccessary
                objs (get-objects criteria)]
            (adc/data-tapper "Response" objs) ; for debugging
            (>! out objs))
          (recur))))))

(defn init-catalog
  [in out]
  (load-catalog!)
  (handle-requests in out))

;;;
;;; Massier catalog component
;;;

(defrecord MessierCatalog
           [in out]
  adc/Catalog
  (initialize [this]
              (load-catalog!)
              (handle-requests in out)
              this)
  (get-objects [_]
    (:objects @catalog))
  (get-objects [_ criteria]
    (into [] (filter (adc/filter-xf criteria)) (:objects @catalog)))
  (get-capabilities [_]))

;;
;; testing
;;

(comment
  ; tests
  (read-messier)
  (load-catalog!)
  (get-objects)
  (get-objects {:magnitude {:brightest -30 :faintest 8}
                :object-types #{:galaxy}
                })

  ; perform simple timing
  (time (load-catalog!))
  (time (get-objects {:magnitude {:brightest -30 :faintest 8}
                      :object-types #{:emission-nebula}}))

  ; perform benchmarks
  (require '[criterium.core :as crt])
  (crt/bench (load-catalog!))
  (crt/bench (get-objects {:magnitude {:brightest -30 :faintest 8}
                           :object-types #{:emission-nebula}}))

  )