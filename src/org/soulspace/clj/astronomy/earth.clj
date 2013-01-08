(ns org.soulspace.clj.astronomy.earth
  (:use
    [org.soulspace.clj.math.math]
    [org.soulspace.clj.math.java-math]
    ))

(def eqr 6378140) ; equatorial radius in meters
(def f (/ 1 298.257)) ; flattening
(def plr (* eqr (- 1 f))) ; polar radius in meters
(def ecc (sqrt (- (* 2 f) (sqr f)))) ; eccentricity of meridian

(defn geocentric-lat [gg-lat]
  (atan (* (/ (sqr eqr) (sqr plr))
           (tan gg-lat))))

(defn geocentric-lat-by-height [gg-lat height]
  (let [u (atan (* (/ plr eqr) (tan gg-lat)))
        rho-sin-gc-lat (+ (* (/ plr eqr) (sin u)) (* (/ height eqr) (sin gg-lat)))
        rho-cos-gc-lat (+ (cos u)(* (/ height eqr) (cos gg-lat)))
        rho (/ rho-cos-gc-lat (cos (geocentric-lat gg-lat)))]
    {:u u :rho rho :rho-sin-geocentric-lat rho-sin-gc-lat :rho-cos-geocentric-lat rho-cos-gc-lat}
    ))

(defn distance-from-center-at-sealevel [gg-lat]
  (+ 0.9983271 (* 0.0016764 (cos (* 2 gg-lat))) (* -0.0000035 (cos (* 4 gg-lat)))))

(defn parallel-radius [gg-lat]
  (/ (* eqr (cos gg-lat)) (sqrt (- 1 (* (sqr ecc) (sqr (sin gg-lat)))))))