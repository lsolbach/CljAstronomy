;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.time.time
  (:use [org.soulspace.clj.math math java-math])
  (:import [java.util Date]))

; Implements Time based on 'Astronomical Algorithms' from Jean Meeus

(def julian-century 36525)
(def J2000 2451545)
(def days [:sunday :monday :tuesday :wednesday :thursday :friday :saturday])

(defn julian-day
  "Calculates the julian day from a calender date."
  [y m d b]
  (+ (floor (* 365.25M (+ y 4716))) (floor (* 30.6001M (+ m 1))) d b -1524.5M))

(defn modified-julian-day
  "Calculates the modified julian day from the julian day"
  [jd]
  (- jd 2400000.5M))

(defn- gregorian-b
  [y]
  (let [a (floor (/ y 100))] 
    (+ 2 (- a) (floor (/ a 4)))))

(defn- jd-a
  [z]
  (if (< z 2299161)
    z
    (let [a (floor (/ (- z 1876216.25M) 36524.25M))]
      (+ z 1 a (- (floor (/ a 4)))))))

(defn julian-day-to-date
  "Calculates the calender date of the julian day instant."
  [jd]
  (let [x (+ jd 0.5M)
        z (floor x)
        f (- x z)
        a (jd-a z)
        b (+ a 1524)
        c (floor (/ (- b 122.1M) 365.25M))
        d (floor (* 365.25M c))
        e (floor (/ (- b d) 30.6001M))
        day (+ (- b d (floor (* 30.6001M e))) f)
        month (if (< e 14) (- e 1) (- e 13))
        year (if (> month 2) (- c 4716) (- c 4715))]
    {:year year :month month :day day}))

(defn year
  "Calculates the year of the julian day instant"
  [jd]
  (:year (julian-day-to-date jd)))


(defn gregorian-date-to-julian-day
  "Calculates the julian day for the date given by year, month and day using the gregorian calender."
  [year month day]
  (if (> month 2)
    (julian-day year month day (gregorian-b year))
    (let [y (- year 1)
          m (+ month 12)]
      (julian-day y m day (gregorian-b y)))))

(defn julian-date-to-julian-day
  "Calculates the julian day for the date given by year, month and day using the julian calender."
  [year month day]
  (if (> month 2)
    (julian-day year month day 0)
    (let [y (- year 1)
          m (+ month 12)]
      (julian-day y m day 0))))

(defn date-to-julian-day
  "Calculates the julian day for the date given by year, month and day."
  [year month day]
  (if (or (< year 1582)
          (and (<= year 1582) (< month 10))
          (and (<= year 1582) (= month 10) (< day 5)))
    (julian-date-to-julian-day year month day)
    (gregorian-date-to-julian-day year month day)))

(defn java-date-to-julian-day
  "Calculates the julian day for the java.util.Date instant."
  [date]
  (let [time-millis (.getTime date)]
    (+ (/ time-millis 1000 60 60 24) (date-to-julian-day 1970 1 1) (- 0.5))))

(defn time-of-day
  "Calculates the time in hours, minutes and seconds for the given decimal day."
  [day]
  (let [df (rem day 1)
        h (rem (* df 24) 24)
        m (rem (* (rem h 1) 60) 60)
        s (rem (* (rem m 1) 60) 60)]
    {:hour (floor h) :min (floor m) :sec (floor s)}))

(defn julian-leap-year?
  "Returns true if the given year is a leap year in the julian calender."
  [year]
  (zero? (mod year 4)))

(defn gregorian-leap-year?
  "Returns true if the given year is a leap year in the gregorian calender."
  [year]
  (if (zero? (mod year 4))
    (if (zero? (mod year 100))
      (if (zero? (mod year 400))
        true
        false)
      true)
    false))

(defn leap-year?
  "Returns true if the given year is a leap year."
  [year]
  (if (< year 1583) ; 1582 10 15
    (julian-leap-year? year)
    (gregorian-leap-year? year)))

(defn leap-year-by-julian-day?
  "Returns true if the year of the given julian day instant is a leap year in the gregorian calender."
  [jd]
  (let [{year :year} (julian-day-to-date jd)]
    (if (< jd 2299160.5M) ; 1582 10 15
      (julian-leap-year? year)
      (gregorian-leap-year? year))))

(defn day-of-week
  "Calculates the index of the day in the week for the given julian day instant."
  [jd]
  (nth days (floor (rem (+ jd 1.5) 7))))

(defn day-of-year
  "Calculates the index of the day in the year for the given julian day instant."
  ([jd]
    (let [{year :year month :month day :day} (julian-day-to-date jd)]
      (day-of-year year month day)))
  ([year month day]
    (let [k (if (leap-year? year) 1 2)]
      (+ (floor (/ (* 275 month) 9)) (* (- k) (floor (/ (+ month 9) 12))) day -30))))

(defn time-by-julian-day
  "Calculates the time for the given julian day instant."
  [jd]
  (let [x (+ jd 0.5)
        f (- x (floor x))]
    (time-of-day f)))

(defn easter-date-by-gregorian-date
  "Calculates the easter date for the given year in the gregorian calender."
  [year]
  (let [a (rem year 19)
        b (quot year 100)
        c (rem year 100)
        d (quot b 4)
        e (rem b 4)
        f (quot (+ b 8) 25)
        g (quot (+ b (- f) 1) 3)
        h (rem (+ (* 19 a) b (- d) (- g) 15) 30)
        i (quot c 4)
        k (rem c 4)
        l (rem (+ 32 (* 2 e) (* 2 i) (- h) (- k)) 7)
        m (quot (+ a (* 11 h) (* 22 l)) 451)
        n (quot (+ h l (* -7 m) 114) 31)
        p (rem (+ h l (* -7 m) 114) 31)
        ]
    {:year year :month n :day (+ p 1)}))

(defn easter-date-by-julian-date
  "Calculates the easter date for the given year in the julian calender."
  [year]
  (let [a (rem year 4)
        b (rem year 7)
        c (rem year 19)
        d (rem (+ (* 19 c) 15) 30)
        e (rem (+ (* 2 a) (* 4 b) (- d) 34) 7)
        f (quot (+ d e 114) 31)
        g (rem (+ d e 114) 31)
        ]
    {:year year :month f :day (+ g 1)}))

(defn easter-date
  "Calculates the easter date for the given year."
  [year]
  (if (< year 1583)
    (easter-date-by-julian-date year)
    (easter-date-by-gregorian-date year)))

(defn easter-date-by-julian-day
  "Calculates the easter date for the year of the julian day."
  [jd]
  (let [{year :year} (julian-day-to-date jd)]
    (easter-date year)))

(defn delta-t-meeus
  "Calculates Delta T after the formulars given in Astronomical Algorithms."
  [year]
  (let [t (/ (- year 2000) 100)]
    (cond
      (< year 948) (+ 2177 (* 497 t) (* 44.1 t t))
      (< year 1601) (+ 102 (* 102 t) (* 25.3 t t))
      (< year 2000) (+ 102 (* 102 t) (* 25.3 t t)) ; FIXME use (delta-t-table year)
      (< year 2101) (+ 102 (* 102 t) (* 25.3 t t) (* 0.37 (- year 2100)))
      :default (+ 102 (* 102 t) (* 25.3 t t)))))

; http://eclipse.gsfc.nasa.gov/SEcat5/deltatpoly.html
(defn delta-t-nasa
  "Calculates Delta T after the formulars given on the web page 'Polynomial Expressions for Delta T (ΔT)'."
  [year month]
  (let [y (+ year (/ (- month 0.5M) 12M))]
    (cond
      (< year -501) (let [u (/ (- y 1820M) 100M)]
                      (+ -20M (* 32M u u)))
      (< year 501) (let [u (/ (- y 1000M) 100M)]
                     (+ 10583.6M (* -1014.41M u) (* 33.78311M u u) (* -5.952053M u u u)
                        (* -0.1798452M u u u u) (* 0.022174192M u u u u u)
                        (* 0.0090316521M u u u u u u)))
      (< year 1601) (let [u (/ (- y 1000M) 100M)]
                      (+ 1574.2M (* -556.01M u) (* 71.23472M u u) (* 0.319781M u u u)
                         (* -0.8503463M u u u u) (* -0.005050998M u u u u u)
                         (* 0.0083572073M u u u u u u)))
      (< year 1701) (let [t (- y 1600M)]
                      (+ 120 (* -0.9808M t) (* -0.01532M t t) (/ (* t t t) 7129M)))
      (< year 1801) (let [t (- y 1700M)]
                      (+ 8.83M (* 0.1603M t) (* -0.0059285M t t) (* 0.00013336M t t t)
                         (* -1 (/ (* t t t t) 1174000M))))
      (< year 1861) (let [t (- y 1800M)]
                      (+ 13.72M (* -0.332447M t) (* 0.0068612M t t) (* 0.0041116M t t t)
                         (* -0.00037436M t t t t) (* 0.0000121272M t t t t t)
                         (* -0.0000001699M t t t t t t) (* 0.000000000875M * t t t t t t t)))
      (< year 1901) (let [t (- y 1860M)]
                      (+ 7.62M (* 0.5737M t) (* -0.251754M t t) (* 0.01680668M t t t)
                         (* -0.0004473624M t t t t) (/ (* t t t t t) 233174M)))
      (< year 1921) (let [t (- y 1900M)]
                      (+ -2.79M (* 1.494119M t) (* -1 0.0598939M t t) (* 0.0061966M t t t)
                         (* -0.000197M t t t t)))
      (< year 1941) (let [t (- y 1920M)]
                      (+ 21.20 (* 0.84493M t) (* -0.076100M t t) (* 0.0020936M t t t)))
      (< year 1961) (let [t (- y 1950M)]
                      (+ 29.07M (* 0.407M t) (* -1 (/ (* t t) 233M)) (/ (* t t t) 2547M)))
      (< year 1987) (let [t (- y 1970M)]
                      (+ 45.45M (* 1.067M t) (* -1 (/ (* t t) 260M)) (* -1 (/ (* t t t) 718M))))
      (< year 2006) (let [t (- y 2000M)]
                      (+ 63.86M (* 0.3345M t) (* -1 0.060374M t t) (* 0.0017275M t t t)
                         (* 0.000651814M t t t t) (* 0.00002373599M t t t t t)))
      (< year 2051) (let [t (- y 2000M)]
                      (+ 62.92M (* 0.32217M t) (* 0.005589M t t)))
      (< year 2151) (let [u (/ (- y 1820M) 100M)]
                      (+ -20M (* 32M u u) (* -0.5628M (- 2150 y))))
      :default (let [u (/ (- y 1820M) 100M)]
                      (+ -20M (* 32M u u))))))

(def delta-t delta-t-nasa)

(defn universal-time-to-dynamic-time
  "Calculates the dynamic time by for the universal time."
  [year ut]
  (+ (delta-t year) ut))

(defn dynamic-time-to-universal-time
  "Calculates the universal time by for the dynamic time."
  [year td]
  (- td (delta-t year)))

(defn julian-ephemeris-day
  "Calculates the julian ephemeris day (JDE) from the given julian day instant."
  [jd]
  (+ jd (delta-t )))

(defn julian-centuries
  "Calculates the julian centuries since the epoch (default J2000.0)."
  ([jd]
    (julian-centuries jd J2000))
  ([jd epoch]
    (/ (- jd epoch) julian-century)))

(defn mean-siderial-time-greenwich-0hut
  "Calculates the mean sidereal time in degrees at greenwich at 0h UT."
  [jd]
  (let [T (julian-centuries jd)]
    (+ 100.46061837M (* 36000.770053608M T) (* 0.000387933M T T) (* -1 (/ (pow T 3) 38710000M)))))

(defn mean-siderial-time-greenwich
  "Calculates the mean sidereal time in degrees at greenwich for the given instant in UT."
  [jd]
  (let [T (julian-centuries jd)]
    (+ 280.46061837M (* 360.98564736629M (- jd 2451545.0M)) (* 0.000387933M T T) (* -1 (/ (pow T 3) 38710000M)))))
