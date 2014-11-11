;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.test.angle
  (:use
    [clojure.test]
    [org.soulspace.clj.astronomy.angle]))

(deftest angletest
  (is (= (hour-angle-to-angle 12.0) 180.0))
  (is (= (hour-angle-to-angle -12.0) -180.0))
  (is (= (hour-angle-to-angle 0.5) 7.5))
  (is (= (hour-angle-to-angle -0.5) -7.5))
  (is (= (hour-angle-to-angle 25.0) 375.0))
  (is (= (angle-to-hour-angle 180.0) 12.0))
  (is (= (angle-to-hour-angle -180.0) -12.0))
  (is (= (angle-to-hour-angle 7.5) 0.5))
  (is (= (angle-to-hour-angle -7.5) -0.5))
  (is (= (angle-to-hour-angle 375.0) 25.0))
  (is (= (dms-angle-to-angle "+180°") 180.0))
  (is (= (dms-angle-to-angle "1°") 1.0))
  (is (= (dms-angle-to-angle "0° 5'" 0 30 0.0) 0.5))
  (is (= (dms-angle-to-angle "0° 0' 45\"") 0.0125))
  (is (= (dms-angle-to-angle "-0° 0' 45.0\"") -0.0125))
  (is (= (angle-to-dms-angle 0.0125) {:sign 1 :deg 0 :min 0 :sec 45.0}))
  (is (= (angle-to-dms-angle 0.5) {:sign 1 :deg 0 :min 30 :sec 0.0}))
  (is (= (angle-to-dms-angle 1.0) {:sign 1 :deg 1 :min 0 :sec 0.0}))
  (is (= (angle-to-dms-angle 18.5) {:sign 1 :deg 18 :min 30 :sec 0.0}))
  )


(run-tests)