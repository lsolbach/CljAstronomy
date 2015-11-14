(ns org.soulspace.clj.astronomy.app.data.catalogs
  (:use [org.soulspace.clj.astronomy.app.data common constellations greek hyg-catalog messier-catalog dso-catalog])
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]))

; use core.async with channels and go's to load the catalogs asynchronously

(def star-catalog (atom []))
(def dso-catalog (atom []))

(defn load-hyg-catalog
  "Loads the HYG catalog."
  []
  (let [t (thread (read-hyg))]
    (go (reset! star-catalog (<! t)))))


(defn load-messier-catalog
  "Loads the Messier catalog."
  []
  (let [t (thread (read-messier))]
    (go (reset! dso-catalog (<! t)))))

(defn load-dso-catalog
  "Loads the DSO catalog."
  []
  (let [t (thread (read-dso))]
    (go (reset! dso-catalog (<! t)))))

(defn get-stars
  "Returns the catalog of stars. If no catalog is loaded, the default catalog gets loaded."
  []
  (if-not (seq @star-catalog)
    (do
      (load-hyg-catalog)
      @star-catalog)
    @star-catalog))

(defn get-deep-sky-objects
  "Returns the catalog of deep sky objects. If no catalog is loaded, the default catalog gets loaded."
  []
  (if-not (seq @dso-catalog)
    (do
      (load-messier-catalog)
      @dso-catalog)
    @dso-catalog))
