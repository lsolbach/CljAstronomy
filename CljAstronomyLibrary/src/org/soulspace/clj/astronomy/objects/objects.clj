;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.objects.objects)

; TODO move protocol and records to a domain layer
; basic protocol for all astronomical objects (e.g. stars, galaxies, clusters)
(defprotocol AstronomicalObject
  "Base protocol for all astronomical objects (e.g. stars, galaxies, clusters).")


(defprotocol StellarObject
  "Protocol for stellar objects (e.g. stars).")


(defprotocol NonStellarObject
  "Protocol for non stellar objects (e.g. galaxies, clusters and nebulars).")


(defprotocol Star
  "Protocol for stars.")


(defprotocol EmissionNebula
  "Protocol for emission nebulae.")


(defprotocol ReflectionNebula
  "Protocol for reflection nebulae.")


(defprotocol PlanetaryNebula
  "Protocol for planetary nebulae.")


(defprotocol DarkNebula
  "Protocol for dark nebula.")


(defprotocol OpenCluster
  "Protocol for open clusters.")


(defprotocol GlobularCluster
  "Protocol for globular clusters.")


(defprotocol Galaxy
  "Protocol for galaxies.")


(defprotocol GalaxyCluster
  "Protocol for galaxy clusters.")
  
