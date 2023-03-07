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
(ns org.soulspace.clj.astronomy.constants)

;;
;; definition of astronomical constants
;;

; defining constants
(def c "Light speed [m/s]" 299792458)

; primary constants
(def G "Constant of gravitation [m^3/kg s^2]" 6.67428e-11)

(def rho "General Precession in longitude per Julian Century, at standard epoch 2000" "5028.796195\"")
(def epsilon "Obliquity of the ecliptic, at standard epoch 2000" "23°26'21.406\"")

; derived constants
(def N "Constant of nutation, at standard epoch 2000" "9.205 2331\"")
