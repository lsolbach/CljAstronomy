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

(ns org.soulspace.clj.astronomy.app.ui.swing.application
  (:require [org.soulspace.clj.astronomy.app.data.catalogs :as cat] 
        [org.soulspace.clj.astronomy.app.ui.swing.main-frame :as ui])
  (:gen-class))

(defn -main
  "Main function to start the application"
  [& args]
  (cat/load-hyg-star-catalog)
  ;(load-messier-catalog)
  (cat/load-hyg-dso-catalog)
  (ui/start-ui))

(comment
  (-main)
  )