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
    (bayer-letter? star) ((:bayer star) greek-letters)
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
    (messier-object? dso) (str "M" (:messier dso))
    (ngc-object? dso) (str "NGC" (:ngc dso))
    (ic-object? dso) (str "IC" (:ngc dso))
    (pk-object? dso) (str "IC" (:ngc dso))
    :default ""))

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
