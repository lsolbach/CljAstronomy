(ns org.soulspace.clj.astronomy.topology
  (:use [org.soulspace.clj.math math java-math]
        [org.soulspace.clj.astronomy.time time instant]))

;; Functions for topological calculations.
;;
;; The equatorial and polar radius are expected as meters.
;; 
;; References:
;; Jean Meeus; Astronomical Algorithms, 2. Ed.; Willmann-Bell
;;

(def location-types #{:topographic :topocentric})

(defn polar-radius
  "Calculates the polar-radius from the equatorial-radius and the flattening."
  [equatorial-radius flattening]
  (* equatorial-radius (- 1 flattening)))

(defn eccentricity
  "Calculates the eccentricity of the meridian from the given flattening."
  [flattening]
  (sqrt (- (* 2 flattening) (sqr flattening))))

(defn topocentric-latitude
  "Calculates the topocentric latitude for the given topographic latitude."
  [topgraphic-latitude equatorial-radius polar-radius]
  (atan (* (/ (sqr equatorial-radius ) (sqr polar-radius))
           (tan topographic-latitude))))

(defn topocentric-parameters-by-height
  [topographic-latitude height equatorial-radius polar-radius]
  (let [u (atan (* (/ polar-radius equatorial-radius) (tan topographic-latitude)))
        rho-sin-gc-lat (+ (* (/ polar-radius equatorial-radius) (sin u)) (* (/ height equatorial-radius) (sin topographic-latitude)))
        rho-cos-gc-lat (+ (cos u)(* (/ height equatorial-radius) (cos topographic-latitude)))
        rho (if (> (abs topographic-latitude) (/ pi 4))
              (/ rho-sin-gc-lat (sin (topocentric-latitude topographic-latitude equatorial-radius polar-radius)))
              (/ rho-cos-gc-lat (cos (topocentric-latitude topographic-latitude equatorial-radius polar-radius))))]
    {:u u :rho rho :rho-sin-topocentric-lat rho-sin-gc-lat :rho-cos-topocentric-lat rho-cos-gc-lat}))

(defn topocentric-distance
  "Calculates the distance of the center of the body in equatorial radiuses."
  [topographic-latitude height equatorial-radius polar-radius]
  (:rho (topocentric-parameters-by-height topographic-latitude height equatorial-radius polar-radius)))

(defn parallel-radius
  "Calculates the radius of the parallel circle at the given topographic latitude."
  [m topographic-latitude equatorial-radius eccentricity]
  (/ (* equatorial-radius (cos topographic-latitude))
     (sqrt (- 1 (* (sqr eccentricity) (sqr (sin topographic-latitude)))))))

(defn longitude-distance-per-degree
  "Calculates the distance per degree of longitude for the given topographic latitude."
  [topographic-latitude equatorial-radius eccentricity]
  (* (/ pi 180) (parallel-radius topographic-latitude equatorial-radius eccentricity)))

(defn curvature-radius
  "Calculates the curvature radius for the given topographic latitude."
  [topographic-latitude equatorial-radius eccentricity]
  (/ (* equatorial-radius (- 1 (sqr eccentricity)))
     (pow (- 1 (* (sqr eccentricity) (sqr (sin topographic-latitude)))) 3/2)))

(defn latitude-distance-per-degree
  "Calculates the distance per degree of latitude for the given topographic latitude."
  [topographic-latitude equatorial-radius eccentricity]
  (* (/ pi 180) (curvature-radius topographic-latitude equatorial-radius eccentricity)))

(defn linear-velocity
  "Calculates the linear velocity with respect to the stars at the given latitude in meters per second.
  Omega is the rotational angular velocity with respect to the stars at the epoch."
  [topographic-latitude equatorial-radius eccentricity omega]
  (* omega (parallel-radius topographic-latitude equatorial-radius eccentricity)))

(defn topodesic-distance
  "Calculates the topodesic distance (great circle distance) between 2 positions on the body."
  [long1 lat1 long2 lat2 equatorial-radius flattening]
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
