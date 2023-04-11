(ns org.soulspace.astronomy.app.system
  (:require [integrant.core :as ig]
            [clojure.core.async :as a]
            [org.soulspace.astronomy.app.data.hyg-dso-catalog :as chdc]
            [org.soulspace.astronomy.app.data.hyg-dso-catalog :as chsc]
            [org.soulspace.astronomy.app.data.hyg-dso-catalog :as cmes]))

(def catalog-requests (a/chan 10))
(def catalog-responses (a/chan 10))

(def config
  {:catalog/hyg-dso {:data-dir "data"}
   :catalog/hyg-star {:data-dir "data"}
   :catalog/messier {:data-dir "data"}
   :swing/ui {}})

(defmethod ig/init-key :catalog/hyg-dso [_ {:keys [data-dir] :as opts}]
  (fn [_] (-> (chdc/map->HygDSOCatalog opts)
              (chdc/init))))

(defmethod ig/init-key :catalog/hyg-star [_ {:keys [data-dir] :as opts}]
  (fn [_] (-> (chsc/map->HygDSOCatalog opts)
              (chsc/init))))

(defmethod ig/init-key :catalog/messier [_ {:keys [data-dir] :as opts}]
  (fn [_] (-> (cmes/map->HygDSOCatalog opts)
              (cmes/start))))

(defmethod ig/init-key swing/ui [args]
  (fn [_] nil)) ; TODO implement

(defmethod ig/halt-key :catalog/hyg-dso  [_ catalog]
  (chdc/stop catalog))

(defmethod ig/halt-key :catalog/hyg-star  [_ catalog]
  (chsc/stop catalog))

(defmethod ig/halt-key :catalog/messier  [_ catalog]
  (cmes/stop catalog))

(defmethod ig/halt-key :swing/ui  [_ ui]
  (ui/stop ui))

