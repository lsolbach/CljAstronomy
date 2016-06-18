;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.instruments.instruments
  (:use [org.soulspace.clj.math math java-math]))

;
; optics
;
(defn magnification
  "Calculates the magnification from the focal length of the optic and the focal length of the ocular."
  [optic-focal-length ocular-focal-length]
  (/ optic-focal-length ocular-focal-length))

(defn objective-area
  "Calculates the effective area of the objective by taking into account the area of the obstruction."
  [aperture obstruction]
  (- (* pi (/ aperture 2)) (* pi (/ obstruction 2))))

(defn aperture-ratio
  "Calculates the aperture ratio of a given telescope."
  [aperture focal-length]
  (/ focal-length aperture))

(defn f-factor
  "Calculates the aperture ratio as a factor of the focal lenght of a given telescope."
  [aperture focal-length]
  (/ aperture focal-length))

(defn ocular-focal-length
  "Calculates the required focal length of an ocular for the given aperture and focal-length of an objective and exit pupil."
  [aperture focal-length exit-pupil]
    (* (aperture-ratio focal-length aperture) exit-pupil))
  
(defn geometric-luminous-intensity
  "Estimates the geometric luminous intensity of a spacial object."
  [optic-focal-length aperture obstruction ocular-focal-length]
  (sqr (/ (objective-area aperture obstruction) (magnification optic-focal-length ocular-focal-length))))

(defn contrast
  "Calculates the contrast of between a (spacial) object and the background."
  [object-luminosity background-luminosity]
  (/ (- object-luminosity background-luminosity) object-luminosity))

(defn twilight-number
  "Calculates the subjective contrast (Daemmerungszahl)."
  [aperture obstruction optic-focal-length ocular-focal-length]
  (sqrt (* (objective-area aperture obstruction) (magnification optic-focal-length ocular-focal-length))))

(defn twilight-number-by-magnification
  "Calculates the subjective contrast (Daemmerungszahl) by the given magnification."
  [aperture obstruction magnification]
  (sqrt (* (objective-area aperture obstruction) magnification)))

(defn magnification-by-aperture
  "Calculates the magnification by the aperture of the objective and the exit pupil."
  [aperture exit-pupil]
  (/ aperture exit-pupil))

(defn minimal-magnification
  "Calculates the minimal useful magnification for the objective."
  [aperture focal-length]
  (ocular-focal-length aperture focal-length 7))

(defn normal-magnification
  "Calculates the normal magnification for the objective."
  [aperture focal-length]
  (ocular-focal-length aperture focal-length 3))

(defn beneficial-magnification
  "Calculates the normal magnification for the objective."
  [aperture focal-length]
  (ocular-focal-length aperture focal-length 1))

(defn comfortable-magnification
  "Calculates the maximal useful magnification for the objective."
  [aperture focal-length]
  (ocular-focal-length aperture focal-length 2/3))

(defn field-of-view
  "Calculates the field of view seen with the given ocular."
  [objective-focal-length ocular-focal-length ocular-field-of-view]
  (* ocular-field-of-view (/ ocular-focal-length objective-focal-length)))

(defn exit-pupil
  "Calculates the exit pupil of the optic with the given eyepiece."
  [aperture objective-focal-length ocular-focal-length]
  (/ aperture (magnification objective-focal-length ocular-focal-length)))

(defn angular-resolution
  "Calculates the resolution for the wave length, if given, by the Dawes formula."
  [aperture wave-length]
  (* 1.22 (/ wave-length aperture)))

(defn dawes-resolution
  "Calculates the resolution by the Dawes formula."
  [aperture]
  (/ 116.0 aperture))  

(defn rayleigh-resolution
  "Calculates the resolution by the Rayleigh formula."
  [aperture]
  (/ 138.4 aperture))

;
; photographic
;
(def pixel-size-eos600d (/ 0.0222 5184))

(defn exposure-value
"Calculates the exposure value for the given f-stop and shutter speed."
([f-stop shutter-speed]
  (log2 (/ (* f-stop f-stop) shutter-speed)))
([f-stop shutter-speed iso]
  (log2 (/ (* 100 f-stop f-stop) (* iso shutter-speed)))))

(defn f-stop
  "Calculates the aperture for the given exposure value and shutter speed (with the optional iso value)."
  ([exposure-value shutter-speed]
    (sqrt (* shutter-speed (pow 2 exposure-value))))
  ([exposure-value shutter-speed iso]
    (sqrt (/ (* shutter-speed (pow 2 exposure-value) iso) 100))))

(defn shutter-speed
  "Calculates the aperture for the given exposure value and f-stop (with the optional iso value)."
  ([exposure-value f-stop]
    (/ (* f-stop f-stop) (pow 2 exposure-value)))
  ([exposure-value f-stop iso]
    (/ (* 100 f-stop f-stop) (* (pow 2 exposure-value) iso))))

(defn iso
  "Calculates the iso value given exposure value, f-stop and shutter speed."
  [exposure-value f-stop shutter-speed]
    (/ (* 100 f-stop f-stop) (* (pow 2 exposure-value) shutter-speed)))

(defn hyperfocal-distance
  "Calculates the hyperfocal distance."
  ([focal-length f-stop]
    (hyperfocal-distance focal-length f-stop 1/1500))
  ([focal-length f-stop circle-of-confusion]
    (+ (/ (* focal-length focal-length) (* f-stop circle-of-confusion)) focal-length)))

(defn hyperfocal-distance-approximation
  "Calculates the hyperfocal distance."
  ([focal-length f-stop]
    (hyperfocal-distance-approximation focal-length f-stop 1/1500))
  ([focal-length f-stop circle-of-confusion]
    (/ (* focal-length focal-length) (* f-stop circle-of-confusion))))

