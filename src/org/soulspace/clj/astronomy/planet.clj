;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.planet
  (:use
    [org.soulspace.clj.math.math]
    [org.soulspace.clj.math.java-math]
    [org.soulspace.clj.astronomy angle]))

(defprotocol OrbitalElements
  )

(defprotocol Planet
  (geocentric-latitude [planet geographic-latitude])
  (geocentric-parameters-by-height [geographic-latitude height])
  (parallel-radius [geographic-latitude])
  (longitude-distance-per-degree [geographic-latitude])
  (curvature-radius [geographic-latitude])
  (latitude-distance-per-degree [geographic-latitude])
  (linear-velocity [geographic-latitude])
  (geodesic-distance [long1 lat1 long2 lat2]))

;(defrecord PlanetImpl
;  [equatorial-radius
;   flattening
;   polar-radius
;   eccentricity
;   omega]
;  Planet
;  )
