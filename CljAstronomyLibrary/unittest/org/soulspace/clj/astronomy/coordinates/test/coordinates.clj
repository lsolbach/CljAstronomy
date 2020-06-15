(ns org.soulspace.clj.astronomy.coordinates.test.coordinates
  (:use
    [clojure.test]
    [org.soulspace.clj.astronomy.coordinates.coordinates]
    [org.soulspace.clj.astronomy.angle]
    [org.soulspace.clj.astronomy.test]
    [org.soulspace.clj.math.java-math]))

; Arcturus
(def ra1 (deg-to-rad (ha-to-deg (hms-to-ha "14h15m39.7s"))))
(def dec1 (deg-to-rad (dms-to-deg "19°10'57\"")))
;(def ra1 (deg-to-rad 213.9154))
;(def dec1 (deg-to-rad 19.1825))

; Spica
(def ra2 (deg-to-rad (ha-to-deg (hms-to-ha "13h25m11.6s"))))
(def dec2 (deg-to-rad (dms-to-deg "-11°09'41\"")))
;(def ra2 (deg-to-rad 201.2983))
;(def dec2 (deg-to-rad -11.1614))


(deftest angular-distance-test
  (is (= (angular-distance [0 0] [0 0]) 0.0))
  (is (= (angular-distance [1 1] [1 1]) 0.0))
  (is (= (angular-distance [4 4] [4 4]) 0.0))
  (is (= (angular-distance [1 1] [1 2]) 1.0))
  (is (= (angular-distance [1 2] [1 1]) 1.0))
  (is (= (angular-distance [1 2] [1 1]) 1.0))
  (is (= (angular-distance [(deg-to-rad 90) (deg-to-rad 0)] [(deg-to-rad -90) (deg-to-rad 0)]) (deg-to-rad 180)))
  (is (= (angular-distance [(deg-to-rad -180) (deg-to-rad 0)] [(deg-to-rad 180) (deg-to-rad 0)]) (deg-to-rad 0)))
  (is (= (angular-distance [(deg-to-rad -180) (deg-to-rad 0)] [(deg-to-rad 360) (deg-to-rad 0)]) (deg-to-rad 180)))
  (is (= (angular-distance [(deg-to-rad -180) (deg-to-rad 0)] [(deg-to-rad 540) (deg-to-rad 0)]) (deg-to-rad 0)))
  (is (= (angular-distance [(deg-to-rad -180) (deg-to-rad 90)] [(deg-to-rad 180) (deg-to-rad -90)]) (deg-to-rad 180)))
  (is (= (angular-distance [(deg-to-rad 0) (deg-to-rad 90)] [(deg-to-rad 0) (deg-to-rad -90)]) (deg-to-rad 180)))
  (is (about-equal  (angular-distance [(deg-to-rad 10) (deg-to-rad 0)] [(deg-to-rad -10) (deg-to-rad 0)]) (deg-to-rad 20)))
  (is (about-equal (rad-to-deg (angular-distance [ra1 dec1] [ra2 dec2])) 32.7930))) ; Arcturus <-> Spica

(deftest altitude-by-zenit-distance-tests
  (is (= (altitude-by-zenit-distance (deg-to-rad 0)) (deg-to-rad 90)))
  (is (= (altitude-by-zenit-distance (deg-to-rad 90)) (deg-to-rad 0)))
  (is (= (altitude-by-zenit-distance (deg-to-rad 10)) (deg-to-rad 80)))
  (is (= (altitude-by-zenit-distance (deg-to-rad 45)) (deg-to-rad 45))))

(deftest zenit-distance-by-altitude-test
  (is (= (zenit-distance-by-altitude (deg-to-rad 0)) (deg-to-rad 90)))
  (is (= (zenit-distance-by-altitude (deg-to-rad 90)) (deg-to-rad 0)))
  (is (= (zenit-distance-by-altitude (deg-to-rad 10)) (deg-to-rad 80)))
  (is (= (zenit-distance-by-altitude (deg-to-rad 45)) (deg-to-rad 45))))

;(run-tests)
