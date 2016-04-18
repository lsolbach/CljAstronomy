(ns org.soulspace.clj.astronomy.app.data.catalogs
  (:use [org.soulspace.clj.astronomy.app.data common constellations greek hyg-star-catalog hyg-dso-catalog messier-catalog])
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]))

; use core.async with channels and go's to load the catalogs asynchronously

(def star-catalog (atom []))
(def dso-catalog (atom []))

(defn load-hyg-star-catalog
  "Loads the HYG star catalog."
  []
  ; load catalog asynchronously so the application start is not delayed by catalog loading
  (let [t (thread (read-hyg-star))]
    (go (reset! star-catalog (<! t)))))


(defn load-messier-catalog
  "Loads the Messier catalog."
  []
  ; load catalog asynchronously so the application start is not delayed by catalog loading
  (let [t (thread (read-messier))]
    (go (reset! dso-catalog (<! t)))))

(defn load-hyg-dso-catalog
  "Loads the HYG dso catalog."
  []
  ; load catalog asynchronously so the application start is not delayed by catalog loading
  (let [t (thread (read-hyg-dso))]
    (go (reset! dso-catalog (<! t)))))

(defn get-stars
  "Returns the loaded stars."
  []
  @star-catalog)

(defn get-deep-sky-objects
  "Returns the loaded deep sky objects."
  []
  @dso-catalog)

(defn get-objects
  "Returns the loaded objects."
  []
  (concat @star-catalog @dso-catalog))
