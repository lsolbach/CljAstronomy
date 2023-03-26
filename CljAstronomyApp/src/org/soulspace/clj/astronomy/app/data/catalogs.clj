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

(ns org.soulspace.clj.astronomy.app.data.catalogs
  (:require [clojure.set :refer [map-invert]]
            [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]
            [org.soulspace.clj.astronomy.app.data.common :as acm]
            [org.soulspace.clj.astronomy.app.data.hyg-star-catalog :as hsc]
            [org.soulspace.clj.astronomy.app.data.hyg-dso-catalog :as hdc]
            [org.soulspace.clj.astronomy.app.data.messier-catalog :as mc]))

; use core.async with channels and go's to load the catalogs asynchronously


(def star-catalog (atom []))
(def dso-catalog (atom []))

(defn load-hyg-star-catalog
  "Loads the HYG star catalog."
  []
  ; load catalog asynchronously so the application start is not delayed by catalog loading
  (let [t (thread (hsc/read-hyg-star))]
    (go (reset! star-catalog (<! t)))))


(defn load-messier-catalog
  "Loads the Messier catalog."
  []
  ; load catalog asynchronously so the application start is not delayed by catalog loading
  (let [t (thread (mc/read-messier))]
    (go (reset! dso-catalog (<! t)))))

(defn load-hyg-dso-catalog
  "Loads the HYG dso catalog."
  []
  ; load catalog asynchronously so the application start is not delayed by catalog loading
  (let [t (thread (hdc/read-hyg-dso))]
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
