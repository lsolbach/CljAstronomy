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
(ns org.soulspace.clj.astronomy.instruments.visual
  (:reuire [org.soulspace.math.core :as m]))

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
  (- (* m/PI (/ aperture 2)) (* m/PI (/ obstruction 2))))

(defn aperture-ratio
  "Calculates the aperture ratio of a given telescope."
  [aperture focal-length]
  (/ focal-length
     aperture))

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
  (m/sqr (/ (objective-area aperture obstruction)
            (magnification optic-focal-length ocular-focal-length))))

(defn contrast
  "Calculates the contrast of between a (spacial) object and the background."
  [object-luminosity background-luminosity]
  (/ (- object-luminosity background-luminosity) object-luminosity))

(defn twilight-number
  "Calculates the subjective contrast (Daemmerungszahl)."
  [aperture obstruction optic-focal-length ocular-focal-length]
  (m/sqrt (* (objective-area aperture obstruction) (magnification optic-focal-length ocular-focal-length))))

(defn twilight-number-by-magnification
  "Calculates the subjective contrast (Daemmerungszahl) by the given magnification."
  [aperture obstruction magnification]
  (m/sqrt (* (objective-area aperture obstruction) magnification)))

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
