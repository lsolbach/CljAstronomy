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

(ns org.soulspace.clj.astronomy.app.data.types)

(defprotocol AstronomicalObject)

 ; positional object?
(defprotocol Coordinates
  (to-ra-dec [obj] "Returns the right ascension and declination of the object.")
  ()
  )

(defprotocol DeepSkyObject
  "Protocol for objects outside our solar system.")
  ; TODO add fns


(defprotocol Planet
  "Protocol for planets.")
  ; TODO add fns
