(ns org.soulspace.clj.astronomy.coordinates.projection
  (:use [org.soulspace.clj.math math java-math]))

; Snyder, John P.; Map Projections - A Working Manual; USGS Professional Paper 1395 

; symbol mapping
; lambda -> longitude
; phi    -> latitude

; implementations of spherical projections, no ellipsoid projections implemented yet

; TODO move to a separate module
; TODO maybe add simplifications for long-0 or lat-0 = 0 or 90 degrees

(defn stereoscopic-projection
  "Calculates the stereoscopic projection of the coordinates."
    ([R k-0 [long-0 lat-1] [long lat]]
      (stereoscopic-projection R k-0 long-0 lat-1 long lat))
    ([R k-0 long-0 lat-1 long lat]
      (let [k (/ (* 2 k-0)
                 (+ 1
                    (* (sin lat-1) (sin lat))
                    (* (cos lat-1) (cos lat) (cos (- long long-0)))))
            x (* R k (cos lat) (sin (- long long-0)))
            y (* R k (- (* (cos lat-1) (sin lat))
                        (* (sin lat-1) (cos lat) (cos (- long long-0)))))
            ;h-stroke (+ (* (sin lat-1) (sin lat)) (* (cos lat-1) (cos lat) (cos (- long long-0)))) ; scale
            ;k-stroke 1.0 ; scale
            ]
        ;[x y h-stroke k-stroke]
        [x y])))

; TODO fix quadrant problem in atans; maybe implement atan2 
(defn reverse-stereoscopic-projection
  "Calculates the coordinates in a reversed stereoscopic projection."
  ([R k-0 [long-0 lat-1] [x y]]
    (reverse-stereoscopic-projection R k-0 long-0 lat-1 x y))
  ([R k-0 long-0 lat-1 x y]
    (let [rho (sqrt (+ (* x x) (* y y)))
          c (* 2 (atan2 rho (* 2 R k-0)))
          ; c (* 2 (atan (/ rho (* 2 R k-0))))
          lat (if (= rho 0.0)
                 lat-1
                 (asin (+ (* (cos c) (sin lat-1))
                          (/ (* y (sin c) (cos lat-1))
                             rho))))
          long (cond
                 (= rho 0.0) long-0
                 ; (= lat-1 (/ pi 2)) (+ long-0 (atan (/ x (* -1 y))))
                 (= lat-1 (/ pi 2)) (+ long-0 (atan2 x (* -1 y)))
                 ; (= lat-1 (/ pi -2)) (+ long-0 (atan (/ x y)))
                 (= lat-1 (/ pi -2)) (+ long-0 (atan2 x y))
                 :default (+ long-0 (atan (* x (sin (/ c 
                                                       (- (* rho (cos lat-1) (cos c))
                                                          (* y (sin lat-1) (sin c)))))))))]
      [long lat])))
  
(defn stereoscopic-projector
  "Returns a function for stereoscopic projections."
  ([R]
    (partial stereoscopic-projection R))
  ([R k-0]
    (partial stereoscopic-projection R k-0))
  ([R k-0 [long-0 lat-1]]
    (partial stereoscopic-projection R k-0 [long-0 lat-1]))
  ([R k-0 long-0 lat-1]
    (partial stereoscopic-projection R k-0 long-0 lat-1)))

(defn reverse-stereoscopic-projector
  "Returns a function for reverse stereoscopic projections."
  ([R]
    (partial reverse-stereoscopic-projection R))
  ([R k-0]
    (partial reverse-stereoscopic-projection R k-0))
  ([R k-0 [long-0 lat-1]]
    (partial reverse-stereoscopic-projection R k-0 [long-0 lat-1]))
  ([R k-0 long-0 lat-1]
    (partial reverse-stereoscopic-projection R k-0 long-0 lat-1)))

(defn orthoscopic-projection
  "Calculates the orthoscopic projection of the coordinates."
  ([R [long-0 lat-1] [long lat]]
    (orthoscopic-projection R long-0 lat-1 long lat))
  ([R long-0 lat-1 long lat]
    (let [x (* R (cos lat) (sin (- long long-0))) 
          y (* R (- (* (cos lat-1) (sin lat))
                    (* (sin lat-1) (cos lat) (cos (- long long-0)))))
          ;h-stroke (+ (* (sin lat-1) (sin lat)) (* (cos lat-1) (cos lat) (cos (- long long-0))))
          ;k-stroke 1.0
          ]
      ;[x y h-stroke k-stroke]
      [x y])))

; TODO fix quadrant problem in atans
(defn reverse-orthoscopic-projection
  "Calculates the coordinates in a reversed orthoscopic projection."
  ([R [long-0 lat-1] [x y]]
    (reverse-orthoscopic-projection R long-0 lat-1 x y))
  ([R long-0 lat-1 x y]
    (let [rho (sqrt (+ (* x x) (* y y)))
          c (asin (/ rho R))
          lat (if (= rho 0.0)
                 lat-1
                 (asin (+ (* (cos c) (sin lat-1))
                          (/ (* y (sin c) (cos lat-1))
                             rho))))
          long (cond
                 (= rho 0.0) long-0
                 ; (= lat-1 (/ pi 2)) (+ long-0 (atan (/ x (* -1 y))))
                 (= lat-1 (/ pi 2)) (+ long-0 (atan2 x (* -1 y)))
                 ; (= lat-1 (/ pi -2)) (+ long-0 (atan (/ x y)))
                 (= lat-1 (/ pi -2)) (+ long-0 (atan2 x y))
                 :default (+ long-0 (atan (* x (sin (/ c
                                                       (- (* rho (cos lat-1) (cos c))
                                                          (* y (sin lat-1) (sin c)))))))))]
      [long lat])))

(defn orthoscopic-projector
  "Returns a function for stereoscopic projections."
  ([R]
    (partial orthoscopic-projection R))
  ([R [long-0 lat-1]]
    (partial orthoscopic-projection R [long-0 lat-1]))
  ([R long-0 lat-1]
    (partial orthoscopic-projection R long-0 lat-1)))

(defn reverse-orthoscopic-projector
  "Returns a function for reverse stereoscopic projections."
  ([R]
    (partial reverse-orthoscopic-projection R))
  ([R [long-0 lat-1]]
    (partial reverse-orthoscopic-projection R [long-0 lat-1]))
  ([R long-0 lat-1]
    (partial reverse-stereoscopic-projection R long-0 lat-1)))


;TODO implement other projections
(defn mercator-projection
  "Calculates the mercator projection of the coordinates."
  []
  (let []
    ))

(defn reverse-mercator-projection
  "Calculates the coordinates in a reversed mercator projection."
  []
  (let []
    ))
