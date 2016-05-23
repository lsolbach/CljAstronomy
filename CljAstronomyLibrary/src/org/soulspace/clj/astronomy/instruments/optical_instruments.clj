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

;(def optic-types )
;(def ocular-types )

(defprotocol Optic
  "Protocol for Telescopes."
  (objective-area [optic] "Calculates the effective area of the objective by taking into account the area of the obstruction.")
  (aperture-ratio [optic] "Calculates the aperture ratio of a given telescope.")
  (f-factor [optic] "Calculates the aperture ratio as a factor of the focal lenght of a given telescope.")
  (ocular-focal-length [optic exit-pupil] "Calculates the focal length of an ocular for the given telescope and exit pupil")
  (magnification-by-aperture [optic exit-pupil] "Calculates the magnification by the aperture of the telescope and the exit pupil.")
  (minimal-magnification [optic] "Calculates the minimal useful magnification for the telescope.")
  (normal-magnification [optic] "Calculates the normal magnification for the telescope.")
  (beneficial-magnification [optic] "Calculates the normal magnification for the telescope.")
  (comfortable-magnification [optic] "Calculates the maximal useful magnification for the telescope.")
  (magnification [optic ocular] "Calculates the magnification by the focal lengths of the telescope and the ocular.")
  (field-of-view [optic ocular] "Estimates the field of view seen with the given eyepiece.")
  (exit-pupil [optic ocular] "Calculates the exit pupil of the optic with the given eyepiece.")
  (angular-resolution [optic wave-length] "Calculates the resolution for the wave length, if given, by the Dawes formula.")
  (dawes-resolution [optic] "Calculates the resolution by the Dawes formula.")
  (rayleigh-resolution [optic] "Calculates the resolution by the Rayleigh formula."))

(defrecord OpticImpl
  [name type aperture focal-length obstruction]
  Optic
  (objective-area [optic] (- (* pi (/ (:aperture optic) 2)) (* pi (/ (:obstruction optic) 2))))
  (aperture-ratio [optic]
    (/ (:focal-length optic) (:aperture optic)))
  (f-factor [optic]
    (/ (:aperture optic) (:focal-length optic)))
  (ocular-focal-length [optic exit-pupil]
    (* (aperture-ratio optic) exit-pupil))
  (magnification-by-aperture [optic exit-pupil]
    (/ (:aperture optic) exit-pupil))
  (minimal-magnification [optic]
    (ocular-focal-length optic 7))
  (normal-magnification [optic]
    (ocular-focal-length optic 3))
  (beneficial-magnification [optic]
    (ocular-focal-length optic 1))
  (comfortable-magnification [optic]
    (ocular-focal-length optic 2/3))
  (magnification [optic ocular]
    (/ (:focal-length optic) (:focal-length ocular)))
  (field-of-view [optic ocular]
    (* (:field-of-view ocular) (/ (:focal-length ocular) (:focal-length optic))))
  (exit-pupil [optic ocular]
    (/ (:aperture optic) (magnification optic ocular)))
  (angular-resolution [optic wave-length]
      (* 1.22 (/ wave-length (:aperture optic))))
  (dawes-resolution [optic]
    (/ 116.0 (:aperture optic)))  
  (rayleigh-resolution [optic]
    (/ 138.4 (:aperture optic)))
  )

(defprotocol Ocular
  "Protocol for Oculars."
  )

(defrecord OcularImpl
  [name type focal-length field-of-view]
  Ocular
  )


(defn create-optic
  "Constructor for optic instances."
  ([value-map]
    (OpticImpl. (get value-map :name "Telescope") (get value-map :type "Unknown")
                    (get value-map :aperture 203) (get value-map :focal-length 1220)
                    (get value-map :obstruction 50)))
  ([name type aperture focal-length obstruction]
    (OpticImpl. name type aperture focal-length obstruction)))

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
