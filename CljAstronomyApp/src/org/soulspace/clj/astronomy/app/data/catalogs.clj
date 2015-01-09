(ns org.soulspace.clj.astronomy.app.data.catalogs
  (:use [org.soulspace.clj.astronomy.app.data common constellations greek hyg-catalog messier-catalog dso-catalog]))

(def ^:dynamic star-catalog nil)
(def ^:dynamic dso-catalog nil)

(defn load-hyg-catalog
  "Loads the HYG catalog."
  []
  (def star-catalog (read-hyg)))

(defn load-messier-catalog
  "Loads the Messier catalog."
  []
  (def dso-catalog (read-messier)))

(defn load-dso-catalog
  "Loads the DSO catalog."
  []
  (def dso-catalog (read-dso)))

(defn get-stars
  "Returns the catalog of stars. If no catalog is loaded, the default catalog gets loaded."
  []
  (if-not (seq star-catalog)
    (do
      (load-hyg-catalog)
      star-catalog)
    star-catalog))

(defn get-deep-sky-objects
  "Returns the catalog of deep sky objects. If no catalog is loaded, the default catalog gets loaded."
  []
  (if-not (seq dso-catalog)
    (do
      (load-messier-catalog)
      dso-catalog)
    dso-catalog))
