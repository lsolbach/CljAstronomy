;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.objects.solarsystem.planet
  (:use
    [org.soulspace.clj.math.math]
    [org.soulspace.clj.math.java-math]
    [org.soulspace.clj.astronomy angle]))

; TODO the protocol is about topological/topographical calculations not only relevant for planets, rename to topology!?
; TODO move protocol and record to domain layer, use functions from topology namespace
(defprotocol Planet
  "Protocol for planets."
  (planetocentric-latitude
    [planet planetographic-latitude]
    "Calculates the distance of the center of the planet in equatorial radiuses.")

  (parallel-radius
    [planet planetographic-latitude]
    "Calculates the radius of the parallel circle at the given planetographic latitude.")

  (longitude-distance-per-degree
    [planet planetographic-latitude]
    "Calculates the distance per degree of longitude for the given planetographic latitude.")

  (curvature-radius
    [planet planetographic-latitude]
    "Calculates the curvature radius for the given planetographic latitude.")

  (latitude-distance-per-degree
    [planet planetographic-latitude]
    "Calculates the distance per degree of latitude for the given planetographic latitude.")

  (linear-velocity
    [planet planetographic-latitude]
    "Calculates The linear velocity with respect to the stars at the given latitude in meters per second.")

  (planetocentric-parameters-by-height
    [planet planetographic-latitude height]
    "Calculates values needed for the planetocentric distance.")

  (planetocentric-distance
    [planet geographic-latitude height]
    "Calculates the distance of the center of the earth in equatorial radiuses.")

  (planetodesic-distance
    [planet long1 lat1 long2 lat2]
    "Calculates the planetodesic distance between 2 positions on the planet."))



(defrecord PlanetImpl
  [equatorial-radius flattening polar-radius eccentricity omega]
  Planet
  (planetocentric-latitude
    [planet planetographic-latitude]
    (atan (* (/ (sqr (:equatorial-radius planet)) (sqr (:polar-radius planet)))
           (tan planetographic-latitude))))

  (parallel-radius
    [planet planetographic-latitude]
    (/ (* (:equatorial-radius planet)
          (cos planetographic-latitude))
       (sqrt (- 1
                (* (sqr (:eccentricity planet))
                   (sqr (sin planetographic-latitude)))))))

  (longitude-distance-per-degree
    [planet planetographic-latitude]
    (* (/ pi 180)
       (parallel-radius planet planetographic-latitude)))

  (curvature-radius
    [planet planetographic-latitude]
    (/ (* (:equatorial-radius planet)
          (- 1 (sqr (:eccentricity planet))))
       (pow (- 1
               (* (sqr (:eccentricity planet))
                  (sqr (sin planetographic-latitude))))
            3/2)))

  (latitude-distance-per-degree
    [planet planetographic-latitude]
    (* (/ pi 180)
       (curvature-radius planet planetographic-latitude)))

  (linear-velocity
    [planet planetographic-latitude]
    (* (:omega planet) (parallel-radius planet planetographic-latitude)))

  (planetocentric-parameters-by-height
    [planet planetographic-latitude height]
    (let [u (atan (* (/ (:polar-radius planet)
                        (:equatorial-radius planet))
                     (tan planetographic-latitude)))
          rho-sin-gc-lat (+ (* (/ (:polar-radius planet)
                                  (:equatorial-radius planet))
                               (sin u))
                            (* (/ height
                                  (:equatorial-radius planet))
                               (sin planetographic-latitude)))
          rho-cos-gc-lat (+ (cos u)
                            (* (/ height
                                  (:equatorial-radius planet))
                               (cos planetographic-latitude)))
          rho (if (> (abs planetographic-latitude) (/ pi 4))
                (/ rho-sin-gc-lat
                   (sin (planetocentric-latitude planet planetographic-latitude)))
                (/ rho-cos-gc-lat
                   (cos (planetocentric-latitude planet planetographic-latitude))))]
      {:u u
       :rho rho
       :rho-sin-planetocentric-lat rho-sin-gc-lat
       :rho-cos-planetocentric-lat rho-cos-gc-lat}))

  (planetocentric-distance
    [planet geographic-latitude height]
    (:rho (planetocentric-parameters-by-height planet geographic-latitude height)))

  (planetodesic-distance
    [planet long1 lat1 long2 lat2]
    (let [F (/ (+ lat1 lat2)
               2)
          G (/ (- lat1 lat2)
               2)
          L (/ (+ long1 long2)
               2)
          S (+ (* (sqr (sin G)) (sqr (cos L)))
               (* (sqr (cos F)) (sqr (sin L))))
          C (+ (* (sqr (cos G)) (sqr (cos L)))
               (* (sqr (sin F)) (sqr (sin L))))
          w (atan (sqrt (/ S
                           C)))
          R (/ (sqrt (* S C))
               w)
          D (* 2 w (:equatorial-radius planet))
          H1 (/ (- (* 3 R) 1)
                (* 2 C))
          H2 (/ (+ (* 3 R) 1)
                (* 2 S))
          s (* D
               (+ 1
                  (* (:flattening planet) H1 (sqr (sin F)) (sqr (cos G)))
                  (* -1 (:flattening planet) H2 (sqr (cos F)) (sqr (sin G)))))]
      s)))
