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
  "Calculates the mean longitude of the sun at the given instant
  in julian centuries."
  [t]
  (+ 280.4665M (* 36000.7698M t)))

(defn mean-longitude-moon
  "Calculates the mean longitude of the moon at the given instant
  in julian centuries."
  [t]
  (+ 218.3165 (* 481267.8813 t)))

(defn mean-anomaly-sun
  "Calculates the mean anomaly of the sun at the given instant
  in julian centuries."
  [t]
  (+ 357.52772M (* 35999.050340M t) (* -1 0.0001603M t t) (/ (* -1 t t t) 300000.0M )))

(defn mean-anomaly-moon
  "Calculates the mean anomaly of the moon at the given instant
  in julian centuries."
  [t]
  (+ 134.96298M (* 477198.867398M t) (* 0.0086972M t t) (/ (* t t t) 56250M)))

(defn argumen-of-latitude-tmoon
  "Calculates the argument of latitude for the moon at the given instant
  in julian centuries."
  [t]
  (+ 93.27191M (* 483202.017538M t) (* -1 0.0036825M t t) (/ (* t t t) 327270M)))

(defn longitude-ascending-node-moon
  "Calculates the longitude of ascending node of the moons mean orbit on the
  ecliptic at the given instant in julian centuries, measured from the mean
  equinox of the date."
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
    (+ (* -17.20 (sin omega)) (* -1.32 (sin (* 2 l-sun)))
       (* -0.23 (sin (* 2 l-moon))) (0.21 (sin (* 2 omega))))))

(defn nutation-in-obliquity
  "Calculates the nutation in obliquity (delta epsilon) in arc seconds for the
  julian ephemerides day with an accuracy of 0.1 arc seconds."
  [jde]
  (let [t (julian-centuries jde)
        omega (longitude-ascending-node-moon t)
        l-sun (mean-longitude-sun t)
        l-moon (mean-longitude-moon t)]
    ; TODO test
    (+ (* 9.20 (cos omega)) (* 0.57 (cos (* 2 l-sun)))
       (* 0.10 (cos (* 2 l-moon))) (* 0.09 (cos (* 2 omega))))))

(defn mean-obliquity-low-accuracy
  "Calculates the mean obliquity of the ecliptic (the inclination of the
  earth's axis) for the julian ephemerides day. The error is about
  1 arc second over a period of 2000 years and 10 arc seconds over a
  period of 4000 years."
  [jde]
  (let [t (julian-centuries jde)]
        ; TODO convert to deg and use the deg values here
        (+ (dms-to-deg "23°26'21.448\"") (* -1 (dms-to-deg "0°0'46.8150\"") t)
           (* -1 (dms-to-deg "0°0'0.00059\"") t t) (* (dms-to-deg "0°0'0.001813\"") t t t))))

; TODO high accuracy variant


