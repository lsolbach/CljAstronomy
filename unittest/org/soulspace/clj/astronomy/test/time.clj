;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.test.time
  (:use
    [clojure.test]
    [org.soulspace.clj.astronomy.time.time]))

(deftest julian-day-test
  (is (= (date-to-julian-day 2000 1 1.5) 2451545.0))
  (is (= (date-to-julian-day 1999 1 1.0) 2451179.5))
  (is (= (date-to-julian-day 1988 1 27.0) 2447187.5))
  (is (= (date-to-julian-day 1988 6 19.5) 2447332.0))
  (is (= (date-to-julian-day 1987 1 27.0) 2446822.5))
  (is (= (date-to-julian-day 1987 6 19.5) 2446966.0))
  (is (= (date-to-julian-day 1957 10 4.81) 2436116.31))
  (is (= (date-to-julian-day 1900 1 1.0) 2415020.5))
  (is (= (date-to-julian-day 1600 1 1.0) 2305447.5))
  (is (= (date-to-julian-day 1600 12 31.0) 2305812.5))
  (is (= (date-to-julian-day 1582 10 4) 2299159.5))
  (is (= (date-to-julian-day 1582 10 15) 2299160.5))
  (is (= (date-to-julian-day 837 4 10.3) 2026871.8))
  (is (= (date-to-julian-day 332 13 27.5) 1842713.0))
  (is (= (date-to-julian-day -123 12 31.0) 1676496.5))
  (is (= (date-to-julian-day -122 1 1.0) 1676497.5))
  (is (= (date-to-julian-day -4712 1 1.5) 0.0))
  )

(deftest leap-year-test
  (is (= (leap-year? 4000) true))
  (is (= (leap-year? 2100) false))
  (is (= (leap-year? 2010) false))
  (is (= (leap-year? 2004) true))
  (is (= (leap-year? 2000) true))
  (is (= (leap-year? 1999) false))
  (is (= (leap-year? 1996) true))
  (is (= (leap-year? 1900) false))
  (is (= (leap-year? 1899) false))
  (is (= (leap-year? 1700) false))
  (is (= (leap-year? 1600) true))
  (is (= (leap-year? 1582) false))
  (is (= (leap-year? 1500) true))
  (is (= (leap-year? 1492) true))
  (is (= (leap-year? 0) true))
  (is (= (leap-year? -1) false))
  (is (= (leap-year? -4) true))
  (is (= (leap-year? -400) true))
  (is (= (leap-year-by-julian-day? 2451545.0) true))
  (is (= (leap-year-by-julian-day? 2451179.5) false))
  (is (= (leap-year-by-julian-day? 2268932.5) true))
  )

(deftest day-of-week-test
  (is (= (day-of-week 2434923.5) :wednesday))
  (is (= (day-of-week 2299159.5) :thursday))
  (is (= (day-of-week 2299160.5) :friday))
  )

(deftest easter-date-gregorian-test
  (is (= (easter-date-by-gregorian-date 1991) {:year 1991 :month 3 :day 31}))
  )

(deftest easter-date-julian-test
  (is (= (easter-date-by-julian-date 179) {:year 179 :month 4 :day 12}))
  )

(deftest day-of-year-test
  (is (== (day-of-year 2299160.5) 288))
  (is (== (day-of-year 1582 10 15) 288))
  )

(run-tests)
