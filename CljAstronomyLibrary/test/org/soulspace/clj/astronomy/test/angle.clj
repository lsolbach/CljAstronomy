;;;;
;;;;   Copyright (c) Ludger Solbach. All rights reserved.
;;;;
;;;;   The use and distribution terms for this software are covered by the
;;;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;;;   which can be found in the file license.txt at the root of this distribution.
;;;;   By using this software in any fashion, you are agreeing to be bound by
;;;;   the terms of this license.
;;;;
;;;;   You must not remove this notice, or any other, from this software.
;;;;
(ns org.soulspace.clj.astronomy.test.angle
  (:require [clojure.test :refer :all]
            [org.soulspace.math.core :as m])
  (:use
   [org.soulspace.clj.astronomy.test]
   [org.soulspace.clj.astronomy angle]))

(deftest dms-to-deg-test
  (is (= (dms-to-deg "+180°") 180.0))
  (is (= (dms-to-deg "1°") 1.0))
  (is (= (dms-to-deg "0° 30'") 0.5))
  (is (= (dms-to-deg "0° 0' 45\"") 0.0125))
  (is (= (dms-to-deg "-0° 0' 45.0\"") -0.0125))
  (is (about-equal (dms-to-deg "19° 10' 57\"") 19.1825))
  (is (about-equal (dms-to-deg "-11° 09' 41\"") -11.1614)))

(deftest deg-to-dms-test
  (is (= (deg-to-dms 0.0125) {:sign 1 :deg 0 :min 0 :sec 45.0}))
  (is (= (deg-to-dms 0.5) {:sign 1 :deg 0 :min 30 :sec 0.0}))
  (is (= (deg-to-dms 1.0) {:sign 1 :deg 1 :min 0 :sec 0.0}))
  (is (= (deg-to-dms 18.5) {:sign 1 :deg 18 :min 30 :sec 0.0})))

(deftest hms-to-ha-test
  (is (== (hms-to-ha "1h 0m 0s") 1.0))
  (is (== (hms-to-ha "2h 30m 0.0s") 2.5))
  (is (about-equal (ha-to-deg (hms-to-ha "14h15m39.7s")) 213.9154))
  (is (about-equal (ha-to-deg (hms-to-ha "13h25m11.6s")) 201.2983)))

(deftest ha-to-hms-tests
  (is (= (ha-to-hms 1.0) {:h 1 :min 0 :sec 0.0}))
  (is (= (ha-to-hms 2.5) {:h 2 :min 30 :sec 0.0})))

(deftest ha-to-deg-test
  (is (= (ha-to-deg 12.0) 180.0))
  (is (= (ha-to-deg -12.0) -180.0))
  (is (= (ha-to-deg 0.5) 7.5))
  (is (= (ha-to-deg -0.5) -7.5))
  (is (= (ha-to-deg 25.0) 375.0)))

(deftest deg-to-ha-test
  (is (= (deg-to-ha 180.0) 12.0))
  (is (= (deg-to-ha -180.0) 12.0))
  (is (= (deg-to-ha 7.5) 0.5))
  (is (= (deg-to-ha -7.5) 23.5))
  (is (= (deg-to-ha 375.0) 1.0)))

(def deg-180 (->DegreeAngle 180.0))
(def deg-540 (->DegreeAngle 540.0))

(deftest degree-angle-tests
  (is (= (to-rad deg-180) m/PI))
  (is (= (to-deg deg-180) 180.0))
  (is (= (to-ha deg-180) 12.0))
  (is (= (to-rad deg-540) m/PI))
  (is (= (to-deg deg-540) 180.0))
  (is (= (to-ha deg-540) 12.0)))

;(run-tests)
