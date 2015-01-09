(ns org.soulspace.clj.astronomy.coordinates.projection
  (:use [org.soulspace.clj.math math java-math]))

; Snyder, John P.; Map Projections - A Working Manual; USGS Professional Paper 1395 

; lambda -> longitude
; phi    -> latitude

; TODO move to a separate module
; TODO maybe add simplifications for long-0 or lat-0 = 0 or 90 degrees

(defn stereoscopic-projection
  "Calculates the stereoscopic projection of the coordinates."
    ([R k-0 [long-0 lat-1] [long lat]]
      (stereoscopic-projection R k-0 long-0 lat-1 long lat))
    ([R k-0 long-0 lat-1 long lat]
      (let [k (/ (* 2 k-0) (+ 1 (* (sin lat-1) (sin lat)) (* (cos lat-1) (cos lat) (cos (- long long-0)))))
            x (* R k (cos lat) (sin (- long long-0)))
            y (* R k (- (* (cos lat-1) (sin lat)) (* (sin lat-1) (cos lat) (cos (- long long-0)))))
            ;h-stroke (+ (* (sin lat-1) (sin lat)) (* (cos lat-1) (cos lat) (cos (- long long-0))))
            ;k-stroke 1.0
            ]
        ;[x y h-stroke k-stroke]
        [x y])))

(defn reverse-stereoscopic-projection
  "Calculates the coordinates in a reversed stereoscopic projection."
  ([R k-0 [long-0 lat-1] [x y]]
    (reverse-stereoscopic-projection R k-0 long-0 lat-1 x y))
  ([R k-0 long-0 lat-1 x y]
    (let [rho (sqrt (+ (* x x) (* y y)))
          c (* 2 (atan (/ rho (* 2 R k-0))))
          lat (if (= rho 0)
                 lat-1
                 (asin (+ (* (cos c) (sin lat-1)) (* y (sin c) (cos (/ lat-1 rho))))))
          long (if (= rho 0)
                 long-0
                 (cond
                   (= lat-1 90.0) (+ long-0 (atan (/ x (* -1 y))))
                   (= lat-1 -90.0) (+ long-0 (atan (/ x y)))
                   :default (asin (+ (* (cos c) (sin lat-1))))))]
      [long lat])))
  
(defn stereoscopic-projector
  "Returns a function for stereoscopic projections."
  ([R]
    (partial stereoscopic-projection R))
  ([R k-0]
    (partial stereoscopic-projection R k-0))
  ([R k-0 [long-0 lat-1]]
    (partial stereoscopic-projection R k-0 long-0 lat-1))
  ([R k-0 long-0 lat-1]
    (partial stereoscopic-projection R k-0 long-0 lat-1)))

(defn reverse-stereoscopic-projector
  "Returns a function for reverse stereoscopic projections."
  ([R]
    (partial reverse-stereoscopic-projection R))
  ([R k-0]
    (partial reverse-stereoscopic-projection R k-0))
  ([R k-0 [long-0 lat-1]]
    (partial reverse-stereoscopic-projection R k-0 long-0 lat-1))
  ([R k-0 long-0 lat-1]
    (partial reverse-stereoscopic-projection R k-0 long-0 lat-1)))

(defn orthoscopic-projection
  "Calculates the orthoscopic projection of the coordinates."
  ([R [long-0 lat-1] [long lat]]
    (orthoscopic-projection R long-0 lat-1 long lat))
  ([R long-0 lat-1 long lat]
    (let [x (* R (cos lat) (sin (- long long-0))) 
          y (* R (- (* (cos lat-1) (sin lat)) (* (sin lat-1) (cos lat) (cos (- long long-0)))))
          ;h-stroke (+ (* (sin lat-1) (sin lat)) (* (cos lat-1) (cos lat) (cos (- long long-0))))
          ;k-stroke 1.0
          ]
      ;[x y h-stroke k-stroke]
      [x y])))

(defn reverse-orthoscopic-projection
  "Calculates the coordinates in a reversed orthoscopic projection."
  ([R [long-0 lat-1] [x y]]
    (reverse-orthoscopic-projection R long-0 lat-1 x y))
  ([R long-0 lat-1 x y]
    (let [rho (sqrt (+ (* x x) (* y y)))
          c (asin (/ rho R))
          lat (if (= rho 0)
                 lat-1
                 (asin (+ (* (cos c) (sin lat-1)) (* y (sin c) (cos (/ lat-1 rho))))))
          long (if (= rho 0)
                 long-0
                 (cond
                   (= lat-1 90.0) (+ long-0 (atan (/ x (* -1 y))))
                   (= lat-1 -90.0) (+ long-0 (atan (/ x y)))
                   :default (asin (+ (* (cos c) (sin lat-1))))))]
      [long lat])))

(defn orthoscopic-projector
  "Returns a function for stereoscopic projections."
  ([R]
    (partial orthoscopic-projection R))
  ([R [long-0 lat-1]]
    (partial orthoscopic-projection R long-0 lat-1))
  ([R long-0 lat-1]
    (partial orthoscopic-projection R long-0 lat-1)))

(defn reverse-orthoscopic-projector
  "Returns a function for reverse stereoscopic projections."
  ([R]
    (partial reverse-orthoscopic-projection R))
  ([R [long-0 lat-1]]
    (partial reverse-orthoscopic-projection R long-0 lat-1))
  ([R long-0 lat-1]
    (partial reverse-stereoscopic-projection R long-0 lat-1)))

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

