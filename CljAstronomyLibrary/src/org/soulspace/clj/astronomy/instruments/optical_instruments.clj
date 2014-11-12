;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.instruments.optical-instruments
  (:use [org.soulspace.clj.math math java-math]))

;(def telescope-types )
;(def ocular-types )

(defprotocol Telescope
  "Protocol for Telescopes."
  (objective-area [telescope] "Calculates the effective area of the objective by taking into account the area of the obstruction.")
  (aperture-ratio [telescope] "Calculates the aperture ratio of a given telescope.")
  (f-factor [telescope] "Calculates the aperture ratio as a factor of the focal lenght of a given telescope.")
  (ocular-focal-length [telescope exit-pupil] "Calculates the focal length of an ocular for the given telescope and exit pupil")
  (magnification [telescope ocular] "Calculates the magnification by the focal lengths of the telescope and the ocular.")
  (magnification-by-aperture [telescope exit-pupil] "Calculates the magnification by the aperture of the telescope and the exit pupil.")
  (minimal-magnification [telescope] "Calculates the minimal useful magnification for the telescope.")
  (normal-magnification [telescope] "Calculates the normal magnification for the telescope.")
  (beneficial-magnification [telescope] "Calculates the normal magnification for the telescope.")
  (comfortable-magnification [telescope] "Calculates the maximal useful magnification for the telescope.")
  (field-of-view [telescope ocular] "Estimates the field of view seen with the given ocular.")
  (angular-resolution [telescope] "Calculates the resolution by the Dawes formula.")
  (angular-resolution [telescope wave-length] "Calculates the angular resolution for the given wave length.")
  (dawes-resolution [telescope] "Calculates the resolution by the Dawes formula.")
  (rayleigh-resolution [telescope] "Calculates the resolution by the Rayleigh formula."))

(defrecord TelescopeImpl
  [name type aperture focal-length obstruction]
  Telescope
  (objective-area [telescope] (- (* pi (/ (:aperture telescope) 2)) (* pi (/ (:obstruction telescope) 2))))
  (aperture-ratio [telescope]
    (/ (:focal-length telescope) (:aperture telescope)))
  (f-factor [telescope]
    (/ (:aperture telescope) (:focal-length telescope)))
  (ocular-focal-length [telescope exit-pupil]
    (* (aperture-ratio telescope) exit-pupil))
  (magnification [telescope ocular]
    (/ (:focal-length telescope) (:focal-length ocular)))
  (magnification-by-aperture [telescope exit-pupil]
    (/ (:aperture telescope) exit-pupil))
  (minimal-magnification [telescope]
    (ocular-focal-length telescope 7))
  (normal-magnification [telescope]
    (ocular-focal-length telescope 3))
  (beneficial-magnification [telescope]
    (ocular-focal-length telescope 1))
  (comfortable-magnification [telescope]
    (ocular-focal-length telescope 2/3))
  (field-of-view [telescope ocular]
    (* (:field-of-view ocular) (/ (:focal-length ocular) (:focal-length telescope))))
  (angular-resolution [telescope wave-length]
    (* 1.22 (/ wave-length (:aperture telescope))))
  (dawes-resolution [telescope]
    (/ 116.0 (:aperture telescope)))  
  (rayleigh-resolution [telescope]
    (/ 138.4 (:aperture telescope)))
  )

(defprotocol Ocular
  "Protocol for Oculars."
  )

(defrecord OcularImpl
  [name type focal-length field-of-view]
  Ocular
  )

(defn create-telescope
  "Constructor for telecope instances."
  ([value-map]
    (TelescopeImpl. (get value-map :name "Telescope") (get value-map :type "Unknown")
                    (get value-map :aperture 203) (get value-map :focal-length 1220)
                    (get value-map :obstruction 50)))
  ([name type aperture focal-length obstruction]
    (TelescopeImpl. name type aperture focal-length obstruction)))

(defn create-ocular
  "Constructor for ocular instances."
  ([value-map]
    (OcularImpl. (get value-map :name "Ocular") (get value-map :type "Unknown")
                    (get value-map :focal-length 20) (get value-map :field-of-view 50)))
  ([name type focal-length field-of-view]
    (OcularImpl. name type focal-length field-of-view)))

(defn geometric-luminous-intensity
  "Estimates the geometric luminous intensity of a spacial object."
  [telescope ocular]
  (sqr (/ (objective-area telescope) (magnification telescope ocular))))

(defn contrast
  "Calculates the contrast of between a (spacial) object and the background."
  [object-luminosity background-luminosity]
  (/ (- object-luminosity background-luminosity) object-luminosity))

(defn twilight-number
  "Calculates the subjective contrast (Daemmerungszahl)."
  [telescope ocular]
  (sqrt (* (objective-area telescope) (magnification telescope ocular))))

(defn twilight-number-by-magnification
  "Calculates the subjective contrast (Daemmerungszahl) by the given magnification."
  [telescope magnification]
  (sqrt (* (objective-area telescope) magnification)))
