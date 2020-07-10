;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.test.magnitude
  (:use [clojure.test]
        [org.soulspace.clj.astronomy.test]
        [org.soulspace.clj.astronomy magnitude]))

(deftest combined-magnitude-test
  (is (about-equal (combined-magnitude 1.96) 1.96))
  (is (about-equal (combined-magnitude 1.96 2.89) 1.58))
  (is (about-equal (combined-magnitude 4.73 5.22 5.60) 3.93)))

(deftest brightness-ratio-test
  (is (about-equal (brightness-ratio 0.14 2.12) 6.19)))

