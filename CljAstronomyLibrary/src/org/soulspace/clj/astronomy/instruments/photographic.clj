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
(ns org.soulspace.clj.astronomy.instruments.photographic
  (:require [org.soulspace.math.core :as m]))

;;
;; photographic functions
;;
; TODO move to a new photography project
(def pixel-size-eos600d (/ 0.0222 5184))

(defn diagonal
  "Calculates the diagonal of a rectangle."
  ([a]
   (m/sqrt (* 2 a a)))
  ([a b]
   (m/sqrt (+ (* a a) (* b b)))))

(defn exposure-value
 "Calculates the exposure value for the given f-stop and shutter speed."
 ([f-stop shutter-speed]
  (m/log2 (/ (* f-stop f-stop) shutter-speed)))
 ([f-stop shutter-speed iso]
  (m/log2 (/ (* 100 f-stop f-stop) (* iso shutter-speed)))))

(defn f-stop
  "Calculates the aperture for the given exposure value and shutter speed (with the optional iso value)."
  ([exposure-value shutter-speed]
   (m/sqrt (* shutter-speed (m/pow 2 exposure-value))))
  ([exposure-value shutter-speed iso]
   (m/sqrt (/ (* shutter-speed (m/pow 2 exposure-value) iso) 100))))

(defn shutter-speed
  "Calculates the aperture for the given exposure value and f-stop (with the optional iso value)."
  ([exposure-value f-stop]
   (/ (* f-stop f-stop) (m/pow 2 exposure-value)))
  ([exposure-value f-stop iso]
   (/ (* 100 f-stop f-stop) (* (m/pow 2 exposure-value) iso))))

(defn iso
  "Calculates the iso value for the given exposure value, f-stop and shutter speed."
  [exposure-value f-stop shutter-speed]
  (/ (* 100 f-stop f-stop) (* (m/pow 2 exposure-value) shutter-speed)))

(defn hyperfocal-distance
  "Calculates the hyperfocal distance for the given focal length, f-stop and optionally the circle of confusion."
  ([focal-length f-stop]
   (hyperfocal-distance focal-length f-stop 1/1500))
  ([focal-length f-stop circle-of-confusion]
   (+ (/ (* focal-length focal-length) (* f-stop circle-of-confusion)) focal-length)))

(defn hyperfocal-distance-approximation
  "Calculates an approximation of the hyperfocal distance for the given focal length, f-stop and optionally the circle of confusion."
  ([focal-length f-stop]
   (hyperfocal-distance-approximation focal-length f-stop 1/1500))
  ([focal-length f-stop circle-of-confusion]
   (/ (* focal-length focal-length) (* f-stop circle-of-confusion))))

(defn angle-of-view
  "Calculates the angle of view of an image for the given focal length and sensor size."
  [focal-length size]
  (* 2 (m/atan (/ size (* 2 focal-length)))))

(defn panoramic-image-count
  "Calculates the number of images needed for a panorama given the focal length, sensor size, panorama arc and overlap."
  ([focal-length size]
   (panoramic-image-count focal-length size (* 2 m/PI) 0))
  ([focal-length size overlap]
   (panoramic-image-count focal-length size (* 2 m/PI) overlap))
  ([focal-length size arc-size overlap]
   (/ arc-size
      (* (angle-of-view focal-length size) (- 1 overlap)))))
