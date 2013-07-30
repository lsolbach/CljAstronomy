(ns org.soulspace.clj.astronomy.earth
  (:use
    [org.soulspace.clj.math.math]
    [org.soulspace.clj.math.java-math]
    [org.soulspace.clj.astronomy angle]))


; TODO abstract into a planet protocol? the methods should work with other constants for other planets too

(def equatorial-radius 6378140) ; equatorial radius in meters
(def flattening (/ 1 298.257)) ; flattening
(def polar-radius (* equatorial-radius (- 1 flattening))) ; polar radius in meters
(def ecc (sqrt (- (* 2 flattening) (sqr flattening)))) ; eccentricity of the meridian
(def omega 7.292114992e-5) ; rotational angular velocity with respect to the stars at epoch 1996.5 (but earth is slowing down)

(defn geocentric-latitude [geographic-latitude]
  (atan (* (/ (sqr equatorial-radius) (sqr polar-radius))
           (tan geographic-latitude))))

(defn geocentric-parameters-by-height [geographic-latitude height]
  (let [u (atan (* (/ polar-radius equatorial-radius) (tan geographic-latitude)))
        rho-sin-gc-lat (+ (* (/ polar-radius equatorial-radius) (sin u)) (* (/ height equatorial-radius) (sin geographic-latitude)))
        rho-cos-gc-lat (+ (cos u)(* (/ height equatorial-radius) (cos geographic-latitude)))
        rho (if (> (abs geographic-latitude) (/ pi 4))
              (/ rho-sin-gc-lat (sin (geocentric-latitude geographic-latitude)))
              (/ rho-cos-gc-lat (cos (geocentric-latitude geographic-latitude))))]
    {:u u :rho rho :rho-sin-geocentric-lat rho-sin-gc-lat :rho-cos-geocentric-lat rho-cos-gc-lat}))

(defn geocentric-distance
  "distance of the center of the earth in equatorial radiuses"
  ([geographic-latitude]
    (+ 0.9983271 (* 0.0016764 (cos (* 2 geographic-latitude))) (* -0.0000035 (cos (* 4 geographic-latitude)))))
  ([geographic-latitude height]
    (:rho (geocentric-parameters-by-height geographic-latitude height))))

(defn parallel-radius [geographic-latitude]
  "radius of the parallel circle at the given geographic latitude"
  (/ (* equatorial-radius (cos geographic-latitude)) (sqrt (- 1 (* (sqr ecc) (sqr (sin geographic-latitude)))))))

(defn longitude-distance-per-degree [geographic-latitude]
  (* (/ pi 180) (parallel-radius geographic-latitude)))

(defn curvature-radius [geographic-latitude]
  (/ (* equatorial-radius (- 1 (sqr ecc)))
     (pow (- 1 (* (sqr ecc) (sqr (sin geographic-latitude)))) 3/2)))

(defn latitude-distance-per-degree [geographic-latitude]
  (* (/ pi 180) (curvature-radius geographic-latitude)))

(defn linear-velocity [geographic-latitude]
  "the linear velocity with respect to the stars at the given latitude in meters per second"
  (* omega (parallel-radius geographic-latitude)))

(defn geodesic-distance [long1 lat1 long2 lat2]
  (let [F (/ (+ lat1 lat2) 2)
        G (/ (- lat1 lat2) 2)
        L (/ (+ long1 long2) 2)
        S (+ (* (sqr (sin G)) (sqr (cos L))) (* (sqr (cos F)) (sqr (sin L))))
        C (+ (* (sqr (cos G)) (sqr (cos L))) (* (sqr (sin F)) (sqr (sin L))))
        w (atan (sqrt (/ S C)))
        R (/ (sqrt (* S C)) w)
        D (* 2 w equatorial-radius)
        H1 (/ (- (* 3 R) 1) (* 2 C))
        H2 (/ (+ (* 3 R) 1) (* 2 S))
        s (* D (+ 1 (* flattening H1 (sqr (sin F)) (sqr (cos G))) (* -1 flattening H2 (sqr (cos F)) (sqr (sin G)))))]
    s))
