(ns org.soulspace.clj.astronomy.app.observation)

(defprotocol Observation
  ""
  )

(defprotocol DeepSkyObservation
  ""
  )

(defprotocol PlanetObservation
  ""
  )

(defprotocol SunObservation
  ""
  )


(def observation-list (ref []))