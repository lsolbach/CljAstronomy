(ns org.soulspace.clj.astronomy.precession
  (:use [org.soulspace.clj.math math java-math]
        [org.soulspace.clj.astronomy.time time instant]))

;;
;; Functions for calculating the precession
;;
;; References:
;; Jean Meeus; Astronomical Algorithms, 2. Ed.; Willmann-Bell
;;

(defn calc-m
  "Calculates m in seconds for t in centuries from 2000.0."
  [t]
  (+ 3.07496 (* 0.00186 t)))

(defn calc-n
  "Calculates m in seconds for t in centuries from 2000.0."
  [t]
  (- 1.33621 (* 0.00057 t)))

(defn annual-precession-low-accuracy
  "Calculates the annual precession with low accuracy.
  This formula may be used, if no great accuracy is required, the epochs
  are not to widely separated and if the position is not too close to
  one of the celestial poles."
  [t [ra dec]]
  (let [m (calc-m t)
        n (calc-n t)]
   {:delta-ra (+ m (* n (sin ra) (cos dec)))
    :delta-dec (* 15 n (cos ra))}))
