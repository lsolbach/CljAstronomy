(ns org.soulspace.clj.astronomy.distance)


(def AU_M "Astronomical Unit [m]" 149597870700)
(def LY_M "Light year [m]" 9460730472580800)
(def LY_AU "Light year [au]" 63241.077)
(def PC_M "Parsec (Parallax second) [m]" 96939420213600000)
(def PC_LY "Parsec (Parallax second) [ly]" 3.2616)

(defn meters-to-astronomical-units
  "Converts the distance given in meters to astronomical units."
  [d]
  (/ d AU_M))

(defn meters-to-light-years
  "Converts the distance given in meters to light years."
  [d]
  (/ d LY_M))

(defn meters-to-parsecs
  "Converts the distance given in meters to parsecs."
  [d]
  (/ d PC_M))

(defn astronomical-units-to-meters
  "Converts the distance given in astronomical units to meters."
  [d]
  (* d AU_M))

(defn astronomical-units-to-light-years
  "Converts the distance given in astronomical units to light years."
  [d]
  (/ d LY_AU))

(defn astronomical-units-to-parsecs
  "Converts the distance given in astronomical units to parsecs."
  [d]
  (/ d LY_AU PC_LY))

(defn light-years-to-meters
  "Converts the distance given in light years to meters."
  [d]
  (* d LY_M))

(defn light-years-to-astronomical-units
  "Converts the distance given in light years to astronomical units."
  [d]
  (* d LY_AU))

(defn light-years-to-parsecs
  "Converts the distance given in light-years-to-parsecs."
  [d]
  (/ d PC_LY))

(defn parsecs-to-meters
  "Converts the distance given in parsecs to light years."
  [d]
  (* d PC_M))

(defn parsecs-to-light-years
  "Converts the distance given in parsecs to astronomical units."
  [d]
  (* d PC_LY LY_AU))

(defn parsecs-to-light-years
  "Converts the distance given in parsecs to light years."
  [d]
  (* d PC_LY))

; TODO move protocols and record in a domain layer

(defprotocol Distance
  (meters [d] "Returns the distance in meters.")
  (astronomical-units [obj] "Returns the distance in astronomical units.")
  (light-years [obj] "Returns the distance in light years.")
  (parsec [obj] "Returns the distance in parallax seconds."))

(defrecord Meters [m]
  Distance
  (meters [dist]
    {:meters dist})
  (astronomical-units [dist]
    (meters-to-astronomical-units {:meters dist}))
  (light-years [dist]
    (meters-to-light-years {:meters dist}))
  (parsecs [dist]
    (meters-to-parsecs {:meters dist})))

(defrecord AstronomicalUnits [au]
  Distance
  (meters [dist]
    (astronomical-units-to-meters {:au dist}))
  (astronomical-units [dist]
    {:au dist})
  (light-years [dist]
    (astronomical-units-to-light-years {:au dist}))
  (parsecs [dist]
    (astronomical-units-to-parsecs {:au dist})))

(defrecord LightYears [ly]
  Distance
  (meters [dist]
    (light-years-to-meters {:ly dist}))
  (astronomical-units [dist]
    (light-years-to-astronomical-units {:ly dist}))
  (light-years [dist]
    {:ly dist})
  (parsecs [dist]
    (light-years-to-parsecs {:ly dist})))

(defrecord Parsecs [pc]
  Distance
  (meters [dist]
    (parsecs-to-meters {:pc dist}))
  (astronomical-units [dist]
    (parsecs-to-astronomical-units {:pc dist}))
  (light-years [dist]
    (parsecs-to-light-years {:pc dist}))
  (parsecs [dist]
    {:pc dist}))

