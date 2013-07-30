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
  (is (= (dms-to-angle "+" 180 0 0.0) 180.0))
  (is (= (dms-to-angle "+" 1 0 0.0) 1.0))
  (is (= (dms-to-angle "+" 0 30 0.0) 0.5))
  (is (= (dms-to-angle "+" 0 0 45.0) 0.0125))
  (is (= (angle-to-dms 0.0125) {:sign 1 :deg 0 :min 0 :sec 45.0}))
  (is (= (angle-to-dms 0.5) {:sign 1 :deg 0 :min 30 :sec 0.0}))
  (is (= (angle-to-dms 1.0) {:sign 1 :deg 1 :min 0 :sec 0.0}))
  (is (= (angle-to-dms 18.5) {:sign 1 :deg 18 :min 30 :sec 0.0}))
  )


(run-tests)