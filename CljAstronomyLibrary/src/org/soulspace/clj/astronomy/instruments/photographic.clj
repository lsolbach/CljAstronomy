(ns org.soulspace.clj.astronomy.instruments.photographic
  (:use [org.soulspace.clj.math math java-math]))
;
; photographic
;
(def pixel-size-eos600d (/ 0.0222 5184))

(defn exposure-value
 "Calculates the exposure value for the given f-stop and shutter speed."
 ([f-stop shutter-speed]
  (log2 (/ (* f-stop f-stop) shutter-speed)))
 ([f-stop shutter-speed iso]
  (log2 (/ (* 100 f-stop f-stop) (* iso shutter-speed)))))

(defn f-stop
  "Calculates the aperture for the given exposure value and shutter speed (with the optional iso value)."
  ([exposure-value shutter-speed]
   (sqrt (* shutter-speed (pow 2 exposure-value))))
  ([exposure-value shutter-speed iso]
   (sqrt (/ (* shutter-speed (pow 2 exposure-value) iso) 100))))

(defn shutter-speed
  "Calculates the aperture for the given exposure value and f-stop (with the optional iso value)."
  ([exposure-value f-stop]
   (/ (* f-stop f-stop) (pow 2 exposure-value)))
  ([exposure-value f-stop iso]
   (/ (* 100 f-stop f-stop) (* (pow 2 exposure-value) iso))))

(defn iso
  "Calculates the iso value for the given exposure value, f-stop and shutter speed."
  [exposure-value f-stop shutter-speed]
  (/ (* 100 f-stop f-stop) (* (pow 2 exposure-value) shutter-speed)))

(defn hyperfocal-distance
  "Calculates the hyperfocal distance."
  ([focal-length f-stop]
   (hyperfocal-distance focal-length f-stop 1/1500))
  ([focal-length f-stop circle-of-confusion]
   (+ (/ (* focal-length focal-length) (* f-stop circle-of-confusion)) focal-length)))

(defn hyperfocal-distance-approximation
  "Calculates the hyperfocal distance."
  ([focal-length f-stop]
   (hyperfocal-distance-approximation focal-length f-stop 1/1500))
  ([focal-length f-stop circle-of-confusion]
   (/ (* focal-length focal-length) (* f-stop circle-of-confusion))))

(defn angle-of-view
  "Calculates the angle of view of an image for the given focal length and sensor size."
  [focal-length size]
  (* 2 (atan (/ size (* 2 focal-length)))))

(defn diagonal
  "Calculates the diagonal of a rectangle."
  ([a]
   (sqrt (* 2 a a)))
  ([a b]
   (sqrt (+ (* a a) (* b b)))))

(defn panoramic-image-count
  "Calculates the number of images needed for a panorama given the focal length, sensor size, panorama arc and overlap."
  ([focal-length size]
   (panoramic-image-count focal-length size (* 2 pi) 0))
  ([focal-length size overlap]
   (panoramic-image-count focal-length size (* 2 pi) overlap))
  ([focal-length size arc-size overlap]
   (/ arc-size
      (* (angle-of-view focal-length size) (- 1 overlap)))))
