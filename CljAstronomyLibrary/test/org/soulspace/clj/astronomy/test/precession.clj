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
(ns org.soulspace.clj.astronomy.test.precession
  (:use
    [clojure.test]
    [org.soulspace.clj.astronomy.test]
    [org.soulspace.clj.astronomy angle precession]))

(deftest calc-m-test
  (is (= (calc-m 0) 3.075)))

(deftest calc-n-test
  (is (= (calc-n 0) 1.336)))

(deftest annual-precession-low-accuracy-test
  (is (= (annual-precession-low-accuracy 0 [(hms-to-rad "10h08m22.3s") (dms-to-rad "11°58'02\"")])
         {:delta-ra 3.208 :delta-dec -17.71})))
