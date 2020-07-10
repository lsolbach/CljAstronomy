(ns org.soulspace.clj.astronomy.nutation
  (:use [org.soulspace.clj.math math java-math]
        [org.soulspace.clj.astronomy.time time instant]))

;;
;; Functions for the calculation nutation and the obliquity of the ecliptic.
;;
;; References:
;; Jean Meeus; Astronomical Algorithms, 2. Ed.; Willmann-Bell
;;

(defn mean-longitude-sun
  "Calculates the mean longitude of the sun at the given instant."
  [t]
  (+ 280.4665M (* 36000.7698M t)))

(defn mean-longitude-moon
  "Calculates the mean longitude of the moon at the given instant."
  [t]
  (+ 218.3165 (* 481267.8813 t)))

(defn longitude-ascending-node-moon
  "Calculates the longitude of ascending node of the moons mean orbit on the
  ecliptic at the given instant, measured from the mean equinox of the date."
  [t]
  (+ 125.04452M (* -1934.136261M t) (* 0.0020708M t) (/ (* t t t) 450000M)))

(defn nutation-in-longitude
  "Calculates the nutation in longitude (delta psi) in arc seconds for the
  julian ephemerides day with an accuracy of 0.5 arc seconds. "
  [jde]
  (let [t (julian-centuries jde)
        omega (longitude-ascending-node-moon t)
        l-sun (mean-longitude-sun t)
        l-moon (mean-longitude-moon t)]
    ; TODO test
    (+ (* -17.20 (sin omega)) (* -1.32 (sin (* 2 l-sun))) (* -0.23 (sin (* 2 l-moon))) (0.21 (sin (* 2 omega))))))

(defn nutation-in-obliquity
  "Calculates the nutation in obliquity (delta epsilon) in arc seconds for the
  julian ephemerides day with an accuracy of 0.1 arc seconds."
  [jde]
  (let [t (julian-centuries jde)
        omega (longitude-ascending-node-moon t)
        l-sun (mean-longitude-sun t)
        l-moon (mean-longitude-moon t)]
    ; TODO test
    (+ (* 9.20 (cos omega)) (* 0.57 (cos (* 2 l-sun))) (* 0.10 (cos (* 2 l-moon))) (* 0.09 (cos (* 2 omega))))))
