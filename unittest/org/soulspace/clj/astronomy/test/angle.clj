(ns org.soulspace.clj.astronomy.test.angle
  (:use
    [clojure.test]
    [org.soulspace.clj.astronomy.angle]))

(deftest angletest
  (is (= (angle-by-hour-angle 12.0) 180.0))
  (is (= (angle-by-hour-angle -12.0) -180.0))
  (is (= (angle-by-hour-angle 0.5) 7.5))
  (is (= (angle-by-hour-angle -0.5) -7.5))
  (is (= (angle-by-hour-angle 25.0) 375.0))
  (is (= (hour-angle-by-angle 180.0) 12.0))
  (is (= (hour-angle-by-angle -180.0) -12.0))
  (is (= (hour-angle-by-angle 7.5) 0.5))
  (is (= (hour-angle-by-angle -7.5) -0.5))
  (is (= (hour-angle-by-angle 375.0) 25.0))
  (is (= (angle-by-deg-min-sec 180 0 0.0) 180.0))
  (is (= (angle-by-deg-min-sec 1 0 0.0) 1.0))
  (is (= (angle-by-deg-min-sec 0 30 0.0) 0.5))
  (is (= (angle-by-deg-min-sec 0 0 45.0) 0.0125))
  (is (= (deg-min-sec-by-angle 0.0125) {:deg 0 :min 0 :sec 45.0}))
  (is (= (deg-min-sec-by-angle 0.5) {:deg 0 :min 30 :sec 0.0}))
  (is (= (deg-min-sec-by-angle 1.0) {:deg 1 :min 0 :sec 0.0}))
  (is (= (deg-min-sec-by-angle 18.5) {:deg 18 :min 30 :sec 0.0}))
  )


(run-tests)