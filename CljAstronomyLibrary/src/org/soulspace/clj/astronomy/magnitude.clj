;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.magnitude
  (:use [org.soulspace.clj.math math java-math]))

(defn magnitude-difference
  "Calculates the magnitude difference from the brightness ratio."
  [ratio]
  (* 2.5 (log10 ratio)))

(defn brightness-ratio
  "Calculates the brightness ratio from the magnitudes or the magnitude difference."
  ([delta-mag]
   (pow 10 (* 0.4 delta-mag)))
  ([mag1 mag2]
   (brightness-ratio (- mag2 mag1))))

(defn contrast
  "Calculates the contrast from the magnitudes or the magnitude difference."
  ([delta-mag]
   (* -0.4 delta-mag))
  ([mag1 mag2]
   (* -0.4 (- mag1 mag2))))

(defn combined-magnitude
  "Calculates the combined magnitude of the given magnitudes."
  ([mag1]
    mag1)
  ([mag1 mag2]
   (let [x (* 0.4 (- mag2 mag1))]
     (- mag2 (* 2.5 (log10 (+ (pow 10 x) 1))))))
  ([mag1 mag2 & mags]
    (->> (cons mag1 (cons mag2 mags))
      (map #(pow 10 (* -0.4 %)))
      (reduce +)
      (log10)
      (* -2.5))))

(defn absolute-magnitude
  "Calculates the absolute magnitude from the given relative magnitude and distance in parsec."
  [dist mag]
  (+ mag 5 (* -5 (log10 dist))))

(defn mag-per-''²-to-mag-per-'²
  "Calculates the magnitudes per square arc minute from the magnitudes per square arc second."
  [mag-per-''²]
  (- mag-per-''² 8.89))

(defn mag-per-''²-to-mag-per-°²
  "Calculates the magnitudes per square arc degree from the magnitudes per square arc second."
  [mag-per-''²]
  (- mag-per-''² 17.78))

(defn mag-per-'²-to-mag-per-°²
  "Calculates the magnitudes per square arc degree from the magnitudes per square arc minute."
  [mag-per-'²]
  (- mag-per-'² 8.89))

(defn mag-per-'²-to-mag-per-''²
  "Calculates the magnitudes per square arc second from the magnitudes per square arc minute."
  [mag-per-'²]
  (+ mag-per-'² 8.89))

(defn mag-per-°²-to-mag-per-''²
  "Calculates the magnitudes per square arc second from the magnitudes per square arc degree."
  [mag-per-°²]
  (+ mag-per-°² 17.78))

(defn mag-per-°²-to-mag-per-'²
  "Calculates the magnitudes per square arc minute from the magnitudes per square arc degree."
  [mag-per-°²]
  (+ mag-per-°² 8.89))
