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
(ns org.soulspace.clj.astronomy.precession
  (:require [org.soulspace.math.core :as m])
  (:use [org.soulspace.clj.astronomy.time time instant]))

;;;
;;; Functions for calculating the precession
;;;
;;; References:
;;; Jean Meeus; Astronomical Algorithms, 2. Ed.; Willmann-Bell
;;;

(defn calc-m
  "Calculates m in seconds for t in centuries from 2000.0."
  [t]
  (+ 3.07496 (* 0.00186 t)))

(defn calc-n
  "Calculates m in seconds for t in centuries from 2000.0."
  [t]
  (- 1.33621 (* 0.00057 t)))

(defn annual-precession-low-accuracy
  "Calculates the annual precession with low accuracy.
  This formula may be used, if no great accuracy is required, the epochs
  are not to widely separated and if the position is not too close to
  one of the celestial poles."
  [t [ra dec]]
  (let [m (calc-m t)
        n (calc-n t)]
   {:delta-ra (+ m (* n (m/sin ra) (m/cos dec)))
    :delta-dec (* 15 n (m/cos ra))}))
