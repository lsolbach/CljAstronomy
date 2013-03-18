(ns org.soulspace.clj.astronomy.time
  (:use [org.soulspace.clj.math math java-math])
  (:import [java.util Date]))

; Implements Time based on 'Astronomical Algorithms' from Jean Meeus

(def julian-century 36525)
(def days [:sunday :monday :tuesday :wednesday :thursday :friday :saturday])

(defn julian-day [y m d b]
  "Calculates the julian day from a calender date."
  (+ (floor (* 365.25 (+ y 4716))) (floor (* 30.6001 (+ m 1))) d b -1524.5))

(defn modified-julian-day [jd]
  "Calculates the modified julian day from the julian day"
  (- jd 2400000.5))

(defn- gregorian-b [y]
  (let [a (floor (/ y 100))] 
    (+ 2 (- a) (floor (/ a 4)))))

(defn gregorian-date-to-julian-day [year month day]
  (if (> month 2)
    (julian-day year month day (gregorian-b year))
    (let [y (- year 1)
          m (+ month 12)]
      (julian-day y m day (gregorian-b y)))))

(defn julian-date-to-julian-day [year month day]
  (if (> month 2)
    (julian-day year month day 0)
    (let [y (- year 1)
          m (+ month 12)]
      (julian-day y m day 0))))

(defn java-date-to-julian-day [date]
  (let [time-millis (.getTime date)]
    (+ (/ time-millis 1000 60 60 24) (julian-day-to-date 1970 1 1) (- 0.5))))

(defn date-to-julian-day [year month day]
  (if (or (< year 1582)
          (and (<= year 1582) (< month 10))
          (and (<= year 1582) (= month 10) (< day 5)))
    (julian-date-to-julian-day year month day)
    (gregorian-date-to-julian-day year month day)))

(defn- jd-a [z]
  (if (< z 2299161)
    z
    (let [a (floor (/ (- z 1876216.25) 36524.25))]
      (+ z 1 a (- (floor (/ a 4)))))))

(defn time-of-day [day]
  (let [df (rem day 1)
        h (rem (* df 24) 24)
        m (rem (* (rem h 1) 60) 60)
        s (rem (* (rem m 1) 60) 60)]
    {:hour (floor h) :min (floor m) :sec (floor s)}))

(defn julian-day-to-date [jd]
  (let [x (+ jd 0.5)
        z (floor x)
        f (- x z)
        a (jd-a z)
        b (+ a 1524)
        c (floor (/ (- b 122.1) 365.25))
        d (floor (* 365.25 c))
        e (floor (/ (- b d) 30.6001))
        day (+ (- b d (floor (* 30.6001 e))) f)
        month (if (< e 14) (- e 1) (- e 13))
        year (if (> month 2) (- c 4716) (- c 4715))]
    {:year year :month month :day day}))

(defn julian-leap-year? [year]
    (zero? (mod year 4)))

(defn gregorian-leap-year? [year]
  (if (zero? (mod year 4))
    (if (zero? (mod year 100))
      (if (zero? (mod year 400))
        true
        false)
      true)
    false))

(defn leap-year? [year]
  (if (< year 1583) ; 1582 10 15
    (julian-leap-year? year)
    (gregorian-leap-year? year)))

(defn leap-year-by-julian-day? [jd]
  (let [{year :year} (julian-day-to-date jd)]
    (if (< jd 2299160.5) ; 1582 10 15
      (julian-leap-year? year)
      (gregorian-leap-year? year))))

(defn day-of-week [jd]
  (nth days (floor (rem (+ jd 1.5) 7))))

(defn day-of-year
  ([jd]
    (let [{year :year month :month day :day} (julian-day-to-date jd)]
      (day-of-year year month day)))
  ([year month day]
    (let [k (if (leap-year? year) 1 2)]
      (+ (floor (/ (* 275 month) 9)) (* (- k) (floor (/ (+ month 9) 12))) day -30))))

(defn time-by-julian-day [jd]
  (let [x (+ jd 0.5)
        f (- x (floor x))]
    (time-of-day f)))

(defn easter-date-by-gregorian-date [year]
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

(defn easter-date-by-julian-date [year]
  (let [a (rem year 4)
        b (rem year 7)
        c (rem year 19)
        d (rem (+ (* 19 c) 15) 30)
        e (rem (+ (* 2 a) (* 4 b) (- d) 34) 7)
        f (quot (+ d e 114) 31)
        g (rem (+ d e 114) 31)
        ]
    {:year year :month f :day (+ g 1)}))

(defn easter-date [year]
  (if (< year 1583)
    (easter-date-by-julian-date year)
    (easter-date-by-gregorian-date year)))

(defn easter-date-by-julian-day [jd]
  (let [{year :year} (julian-day-to-date jd)]
    (easter-date year)))

(defn delta-t [year]
  (let [t (/ (- year 2000) 100)]
    (cond
      (< year 948) (+ 2177 (* 497 t) (* 44.1 t t))
      (< year 1601) (+ 102 (* 102 t) (* 25.3 t t))
      (< year 2000) (+ 102 (* 102 t) (* 25.3 t t)) ; FIXME use (delta-t-table year)
      (< year 2101) (+ 102 (* 102 t) (* 25.3 t t) (* 0.37 (- year 2100)))
      (true) (+ 102 (* 102 t) (* 25.3 t t)))))

(defn universal-time-to-dynamic-time [year ut]
  (+ (delta-t year) ut))

(defn dynamic-time-to-universal-time [year td]
  (- td (delta-t year)))

(defprotocol PointInTime
  (julianDay [date])
  (date [date]))

(defrecord JulianDay [jd]
  PointInTime
  (julianDay [this] jd)
  (date [this] (julian-day-to-date jd)))

(defn- t [jd]
  "helper function for T"
  (/ (- jd 2451545.0) julian-century))

(defn mean-siderial-time-greenwich-0ut [jd]
  )
