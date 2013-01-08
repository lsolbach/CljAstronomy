(ns org.soulspace.clj.astronomy.angle
  (:use
    [org.soulspace.clj.math.java-math]
    ))

(defn angle-by-hour-angle [ha]
  (* 15 ha))

(defn hour-angle-by-angle [a]
  (/ a 15))

(defn angle-by-deg-min-sec [deg min sec]
  (+ deg (/ min 60) (/ sec 3600)))

(defn deg-min-sec-by-angle [a]
  (let [af (rem a 1)
        mf (rem (* af 60) 1)]
  {:deg (long (floor a)) :min (long (floor (* af 60))) :sec (* mf 60)}))

(defn dec-angle-by-min-angle
  ([deg m s]
    (+ deg (* m 60) (* s 3600))))

