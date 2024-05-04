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

(ns org.soulspace.astronomy.app.data.hyg-star-catalog
  (:require [clojure.string :as str]
            [clojure.set :refer [map-invert]]
            [clojure.core.async :as a :refer [>! >!! <! <!!]]
            [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [org.soulspace.astronomy.app.data.common :as adc]
            [org.soulspace.astronomy.app.data.catalogs :as cat]))

(def catalog (atom {:initialized? false
                    :enabled? false
                    :objects []
                    :object-types #{}
                    :catalog-type :star
                    :catalog-designations #{:hip :bayer :flamsteed :gliese :hd :hr}}))

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
     :object-type :star
     :hd (when (seq hd) hd)
     :hr (when (seq hr) hr)
     :hip (when (seq hip) hip)
     :gliese (when (seq gliese) gliese)
     :bayer (when (seq bayer) (adc/greek-abbrev-keys bayer))
     :bayer-superscript (when (not (= superscript " ")) superscript)
     :flamsteed (when (seq flam) flam)
     :constellation (when (and (seq con) (= (count con) 3)) (keyword con))
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

(defrecord HygStar
  [hyg-id object-type hd hr hip gliese bayer bayer-superscript flamsteed constellation common-name
   ra dec distance mag mag-abs spectral-type color-index x y z v-x v-y v-z ra-rad dec-rad
   pm-ra-rad pm-dec-rad lum var mag-var-min mag-var-max comp comp-primary base])

(defn read-xf
  "Creates a mapping transformation from csv vector to hyg catalog star."
  []
  (comp
   (drop 2) ; line 0 are the headers, line 1 is sol, our own star
   (map parse-hyg-star)
   (map map->HygStar)))

(defn read-hyg-star
  "Read and parse the HYG star data."
  []
  (with-open [in-file (io/reader hyg-star-file)]
    (into [] (read-xf) (csv/read-csv in-file :separator \,))))

(defn load-catalog!
  "Loads the HYG star catalog."
  []
  ; load catalog asynchronously so the application start is not delayed by catalog loading
  (let [t (a/thread (read-hyg-star))]
    (a/go (let [objs  (<! t)]
          (swap! catalog assoc :initialized? true :enabled? true :objects objs)))))

(defn get-objects
  "Returns the loaded objects of this catalog, optionally filtered by the given criteria."
  ([]
   (:objects @catalog))
  ([criteria]
   (into [] (cat/filter-xf criteria) (:objects @catalog))))

(defn handle-requests
  "Reads queries from the in channel and returns the results on the out channel."
  [in out]
  (a/go
    (while (:enabled? @catalog)
      (loop []
        (println "looping...")
        (let [request (<! in)]
          ; (user/data-tapper "Request" request) ; for debugging
          (let [criteria (:data request)
                ; TODO check criteria against the capabilities of the repository
                ;      to skip real searches when not neccessary
                objs (get-objects criteria)
                response {:msg-type :get-objects-result
                          :source"HygStarCatalog"
                          :receiver (:source request)
                          :data objs}]
            ; (user/data-tapper "Response" response) ; for debugging
            (>! out response))
          (recur))))))

(defrecord HygStarCatalog
           [in out]
  adc/ICatalog
  (initialize [this]
              (load-catalog!)
              (handle-requests in out)
              this)
  (get-objects [_]
               (:objects @catalog))
  (get-objects [_ criteria]
               (into [] (cat/filter-xf criteria) (:objects @catalog)))
  (get-capabilities [_]))

;;
;; testing
;;

(comment

  ; testing
  (load-catalog!)
  (get-objects)
  (get-objects {:magnitude {:brightest -30 :faintest 2}
                :object-types #{:star}})


  ; perform simple timing
  (time (load-catalog!))
  (time (get-objects {:magnitude {:brightest -30 :faintest 2}
                      :object-types #{:star}}))

  ; perform benchmarks
  (require '[criterium.core :as crt])
  (crt/bench (load-catalog!))
  (crt/bench (get-objects {:magnitude {:brightest -30 :faintest 2}
                           :object-types #{:star}}))

  )
