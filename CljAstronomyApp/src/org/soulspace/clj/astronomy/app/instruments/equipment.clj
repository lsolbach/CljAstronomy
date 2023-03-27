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

(ns org.soulspace.clj.astronomy.app.instruments.equipment
  (:require [org.soulspace.clj.astronomy.instruments.visual :as i]))

(def optic-types [:monocular :binocular :achromat :apochromat :newton :dobson :maksutov :sc :rc :schiefspiegler :other])
(def barlow-reducer-types [:barlow :reducer :flattener :flattener-reducer])
(def filter-types [:color :skyglow :uhc :oiii :h-alpha :h-beta :sii :nii :line :infrared])

(def optics-list (ref [{:name "Celestron C925 Edge HD" :aperture 235 :focal-length 2350 :effectiveness 95 :available true}
                       {:name "GSO Dobson 10\"" :aperture 254 :focal-length 1250 :effectiveness 85  :available true}]))
(def eyepieces-list (ref [{:name "Baader Hyperion Zoom 8-24mm" :zoom true :focal-length 8  :focal-length-zoom 24 :fov 50 :available true}
                          {:name "Baader Hyperion Aspheric 36mm" :zoom false :focal-length 36 :fov 72 :available true}
                          {:name "ES 82° 4,7mm" :zoom false :focal-length 4.7 :fov 82 :available true}
                          {:name "ES 82° 6,7mm" :zoom false :focal-length 6.7 :fov 82 :available true}
                          {:name "ES 82° 8,8mm" :zoom false :focal-length 8.8 :fov 82 :available true}]))
(def barlows-reducers-list (ref [{:name "University Optics" :type :barlow}
                                 {:name "Baader Hyperion" :type :barlow}]))
(def filters-list (ref [{:name "Baader UHC 2\"" :available true}
                        {:name "Baader OIII 2\"" :available true}
                        {:name "Baader H-beta 2\"" :available true}]))


(defprotocol Optic
  "Protocol for optics (e.g. telescopes, binoculars)."
  (objective-area [optic] "Calculates the effective area of the objective by taking into account the area of the obstruction.")
  (aperture-ratio [optic] "Calculates the aperture ratio of a given telescope.")
  (f-factor [optic] "Calculates the aperture ratio as a factor of the focal lenght of a given telescope.")
  (ocular-focal-length [optic exit-pupil] "Calculates the focal length of an ocular for the given telescope and exit pupil")
  (magnification-by-aperture [optic exit-pupil] "Calculates the magnification by the aperture of the telescope and the exit pupil.")
  (angular-resolution [optic wave-length] "Calculates the resolution for the wave length, if given, by the Dawes formula.")
  (dawes-resolution [optic] "Calculates the resolution by the Dawes formula.")
  (rayleigh-resolution [optic] "Calculates the resolution by the Rayleigh formula.")
  (fixed-magnification? [optic] "Returns true if the magnification cannot be changed, false otherwise."))


(defprotocol FixedMagnificationOptic
  (fixed-exit-pupil [optic] "Calculates the exit pupil of the optic."))


(defprotocol VariableMagnificationOptic
  (magnification [optic ocular] "Calculates the magnification of the optic with the given ocular.")
  (field-of-view [optic ocular] "Calculates the field of view seen with the given ocular.")
  (exit-pupil [optic ocular] "Calculates the exit pupil of the optic with the given ocular.")
  (minimal-magnification [optic] "Calculates the minimal useful magnification for the telescope.")
  (normal-magnification [optic] "Calculates the normal magnification for the telescope.")
  (beneficial-magnification [optic] "Calculates the normal magnification for the telescope.")
  (comfortable-magnification [optic] "Calculates the maximal useful magnification for the telescope."))

(defrecord OpticImpl
           [name type aperture focal-length obstruction effectiveness magnification available]
  Optic
  (objective-area [optic]
    (i/objective-area (:aperture optic) (:obstruction optic)))
  (aperture-ratio [optic]
    (i/aperture-ratio (:aperture optic) (:focal-length optic)))
  (f-factor [optic]
    (i/f-factor (:aperture optic) (:focal-length optic)))
  (ocular-focal-length [optic exit-pupil]
    (* (aperture-ratio optic) exit-pupil))
  (magnification-by-aperture [optic exit-pupil]
    (i/magnification-by-aperture (:aperture optic) exit-pupil))
  (angular-resolution [optic wave-length]
    (i/angular-resolution (:aperture optic) wave-length))
  (dawes-resolution [optic]
    (i/dawes-resolution (:aperture optic)))
  (rayleigh-resolution [optic]
    (i/rayleigh-resolution (:aperture optic)))
  (fixed-magnification? [optic] false))

(defrecord VariableOpticImpl
           [name type aperture focal-length obstruction effectiveness magnification available]
  Optic
  (objective-area [optic]
    (i/objective-area (:aperture optic) (:obstruction optic)))
  (aperture-ratio [optic]
    (i/aperture-ratio (:aperture optic) (:focal-length optic)))
  (f-factor [optic]
    (i/f-factor (:aperture optic) (:focal-length optic)))
  (ocular-focal-length [optic exit-pupil]
    (* (aperture-ratio optic) exit-pupil))
  (magnification-by-aperture [optic exit-pupil]
    (i/magnification-by-aperture (:aperture optic) exit-pupil))
  (angular-resolution [optic wave-length]
    (i/angular-resolution (:aperture optic) wave-length))
  (dawes-resolution [optic]
    (i/dawes-resolution (:aperture optic)))
  (rayleigh-resolution [optic]
    (i/rayleigh-resolution (:aperture optic)))
  (fixed-magnification? [optic] false)

  VariableMagnificationOptic
  (magnification [optic ocular]
    (i/magnification (:focal-length optic) (:focal-length ocular)))
  (field-of-view [optic ocular]
    (i/field-of-view (:focal-length optic) (:focal-length ocular) (:field-of-view ocular)))
  (exit-pupil [optic ocular]
    (i/exit-pupil (:aperture optic) (:focal-length optic) (:focal-length ocular)))
  (minimal-magnification [optic]
    (ocular-focal-length optic 7))
  (normal-magnification [optic]
    (ocular-focal-length optic 3))
  (beneficial-magnification [optic]
    (ocular-focal-length optic 1))
  (comfortable-magnification [optic]
    (ocular-focal-length optic 2/3)))


(defprotocol Ocular
  "Protocol for oculars.")


(defrecord OcularImpl
           [name type focal-length field-of-view]
  Ocular)


(defprotocol BarlowReducer
  "Protocol for barlows and reducers.")


(defrecord BarlowReducerImpl
           [name type])


(defprotocol Filter
  "Protocol for filters.")


(defrecord FilterImpl
           [name type])

;
; Construction
;
(comment
(defn create-optic
  "Constructor for optic instances."
  ([value-map]
   (->OpticImpl value-map))
  ([name type aperture focal-length obstruction effectiveness fixed-magnification available]
   (OpticImpl. name type aperture focal-length obstruction effectiveness fixed-magnification magnification available)))

(defn create-ocular
  "Constructor for ocular instances."
  ([value-map]
   (OcularImpl. (get value-map :name "Ocular") (get value-map :type "Unknown")
                (get value-map :focal-length 20) (get value-map :field-of-view 50)))
  ([name type focal-length field-of-view]
   (OcularImpl. name type focal-length field-of-view)))
  
  )
