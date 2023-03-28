(ns org.soulspace.clj.astronomy.app.system
  (:require [integrant.core :as ig]
            [org.soulspace.clj.astronomy.app.data.hyg-dso-catalog :as chdc]
            [org.soulspace.clj.astronomy.app.data.hyg-dso-catalog :as chsc]
            [org.soulspace.clj.astronomy.app.data.hyg-dso-catalog :as cmes]
            ))


(def config
  {:catalog/hyg-dso {:data-dir "data"}
   :catalog/hyg-star {:data-dir "data"}
   :catalog/messier {:data-dir "data"}})

(defmethod ig/init-key :catalog/hyg-dso [_ {:keys [data-dir] :as opts}]
  (fn [_] (-> (chdc/map->HygDSOCatalog opts)
              (chdc/init))))

(defmethod ig/init-key :catalog/hyg-star [_ {:keys [data-dir] :as opts}]
  (fn [_] (-> (chsc/map->HygDSOCatalog opts)
              (chsc/init))))

(defmethod ig/init-key :catalog/messier [_ {:keys [data-dir] :as opts}]
  (fn [_] (-> (cmes/map->HygDSOCatalog opts)
              (cmes/start))))


(defmethod ig/halt-key :catalog/hyg-dso  [_ catalog]
  (chdc/stop catalog))

(defmethod ig/halt-key :catalog/hyg-star  [_ catalog]
  (chsc/stop catalog))

(defmethod ig/halt-key :catalog/messier  [_ catalog]
  (cmes/stop catalog))
