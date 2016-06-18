(ns org.soulspace.clj.astronomy.app.data.labels
  (:use [org.soulspace.clj.astronomy.app.data common filters greek constellations]))

;
; default labels
;
(defmulti object-label :type  :hierarchy #'object-hierarchy)

; "Returns the label for the star."
(defmethod object-label :star
  [star]
  (cond
    (common-name? star) (:common-name star)
    (bayer-letter? star) (greek-letters (:bayer star))
    (flamsteed-object? star) (:flamsteed star)
    (hd-object? star) (str "HD" (:hd star))
    (hr-object? star) (str "HR" (:hr star))
    (gliese-object? star) (str "Gliese" (:gliese star))
    (hip-object? star) (str "Hip" (:hip star))
    :default ""))

; "Returns the label for the deep sky object."
(defmethod object-label :dso
  [dso]
  (cond 
    (common-name? dso) (:common-name dso)
    (messier-object? dso) (str "M " (:messier dso))
    (ngc-object? dso) (str "NGC " (:ngc dso))
    (ic-object? dso) (str "IC " (:ic dso))
    (pk-object? dso) (str "PK " (:pk dso))
    (c-object? dso) (str "C " (:c dso))
    (col-object? dso) (str "Col " (:col dso))
    (mel-object? dso) (str "Mel " (:mel dso))
    (pgc-object? dso) (str "PGC " (:pgc dso))    
    :default ""))

(defmethod object-label nil
  [obj]
  (println (:id obj) (:type obj))
  "")

(defn ra-label
  "Returns the right ascension as string."
  [ra]
  (str ra))

(defn dec-label
  "Returns the declination as string."
  [dec]
  (str dec))

(defn constellation-label
  "Returns the constellation as string."
  [constellation]
  (if constellation
    (constellation-name-map constellation)
    ""))

(defn type-label
  "Returns the type as string."
  [type]
  (if type
    (object-type-name type)
    ""))
