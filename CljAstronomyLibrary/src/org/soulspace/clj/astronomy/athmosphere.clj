;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;
(ns org.soulspace.clj.astronomy.earth.athmosphere
  (:use [org.soulspace.clj.math math java-math]))

;;
;; Functions for calculations regarding the athmospere of the earth.
;;
;; References:
;; Erik Wischnewski; Astronomie in Theorie und Praxis; 6. Aufl.; Erik Wischnewski; Kapitel 2: Atmosphäre der Erde
;; Wikipedia.de; Rayleigh-Streuung
;; Wikipedia.de; Mie-Streuung
;;

;
; Extinction
;
(defn airmass
 "Calculates the increase factor of the air mass of the atmosphere depending on the zenital distance of the source."
 [zenital-distance]
 (/ 1
    (+ (cos zenital-distance)
       (* 0.025 (pow e (* -11 (cos zenital-distance)))))))

(defn airmass-simplified
  "Calculates the increase factor of the air mass of the atmosphere depending on the zenital distance of the source.
   The simplified formula may be used for zenital distances < 70°."
  [zenital-distance]
  (/ 1
     (cos zenital-distance)))

(defn rayleigh-optical-depth
  "Calculates the optical depth (tau) of the rayleigh scattering."
  ([wave-length]
   (/ 0.0084
      (pow wave-length 4)))
  ([zenital-distance wave-length]
   (/ 0.0084
      (* (cos zenital-distance) (pow wave-length 4)))))

(defn rayleigh-extinction
  "Calculates the extinction by rayleigh scattering in magnitudes."
  ([wave-length]
   (* 1.086 (rayleigh-optical-depth wave-length)))
  ([zenital-distance wave-length]
   (* 1.086 (rayleigh-optical-depth zenital-distance wave-length))))

; TODO find formula and implement extiction by mie scattering
(defn mie-extinction
  "Calculates the extinction by mie scattering in magnitudes."
  ([wave-length])

  ([wave-length condition]))


; TODO add mie extinction to rayleigh extinction
; TODO add tests
(defn extinction
  "Calculates the athmospheric extinction of an object in a given zenital distance."
  ([zenital-distance]
   (extinction zenital-distance 550))
  ([zenital-distance wave-length]
   (* (airmass zenital-distance) (+ (rayleigh-extinction wave-length)))))

;
; Refraction
;
(defn temperature-pressure-correction
 "Calculates the correction for temperature and air pressure"
 [temperature pressure]
 (/ pressure 101) (/ 283 (+ 273 temperature)))

(defn refraction-by-apparent-altitude
  "Calculates the athmospheric refraction for an object at a given apparent altitude."
  ([altitude]
    ; TODO add simplified equations for altitudes above 10° and 30°
   (/ 1
      (tan (+ altitude (/ 7.31
                        (+ altitude 4.4))))))
  ([altitude temperature pressure]
   (* (refraction-by-apparent-altitude altitude) (temperature-pressure-correction temperature pressure))))

(defn refraction-by-true-altitude
  "Calculates the athmospheric refraction for an object at a given true altitude."
  ; TODO add simplified equations for altitudes above 10° and 30°
  ([altitude]
   (* 1.02 (/ 1
              (tan (+ altitude (/ 10.3
                                (+ altitude 5.11)))))))
  ([altitude temperature pressure]
   (* (refraction-by-true-altitude altitude) (temperature-pressure-correction temperature pressure))))

;
; Seeing
;
