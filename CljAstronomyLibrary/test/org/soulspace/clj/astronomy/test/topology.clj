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
(ns org.soulspace.clj.astronomy.test.topology
  (:require [org.soulspace.math.core :as m])
  (:use [clojure.test]
        [org.soulspace.clj.astronomy.test]
        [org.soulspace.clj.astronomy topology angle]))

(def flattening (/ 1 298.257)) ; flattening

(def earth
  (let [equatorial-radius 6378140 ; equatorial radius in meters
        flattening (/ 1 298.257) ; flattening
        polar-radius (polar-radius equatorial-radius flattening) ; polar radius in meters
        eccentricity (eccentricity flattening) ; eccentricity of the meridian
        omega 7.292114992e-5] ; rotational angular velocity with respect to the stars at epoch 1996.5 (but earth is slowing down)
    {:equatorial-radius equatorial-radius
     :flattening flattening
     :polar-radius polar-radius
     :eccentricity eccentricity
     :omega omega}))
  
(deftest polar-radius-test
  (is (= (polar-radius {:equatorial-radius earth} {:flattening earth}) 6356755)))

(deftest eccentricity-test
  (is (= (eccentricity flattening) 0.08181922)))

; Location and height of the Palomar Observatory
(deftest topocentric-parameters-by-height-test
  (let [lat (m/deg-to-rad (dms-to-deg "33° 21' 22\""))
        equatorial-radius {:equatorial-radius earth}
        polar-radius {:polar-radius earth}
        p (topocentric-parameters-by-height lat 1706 equatorial-radius polar-radius)]
    (is (= (m/rad-to-deg (:u p)) 33.267796 ))
    (is (= (:rho-sin-topocentric-lat p) 0.546861))
    (is (= (:rho-cos-topocentric-lat p) 0.836339))))
