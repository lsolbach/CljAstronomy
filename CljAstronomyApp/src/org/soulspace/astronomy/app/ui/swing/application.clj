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

(ns org.soulspace.astronomy.app.ui.swing.application
  (:require [org.soulspace.astronomy.app.ui.swing.main-frame :as ui]
            [org.soulspace.astronomy.app.data.hyg-dso-catalog :as chdc]
            [org.soulspace.astronomy.app.data.hyg-star-catalog :as chsc]
            [org.soulspace.astronomy.app.data.messier-catalog :as cmes])
  (:gen-class))


(defn swing-application
  ""
  [& opts]
  )

(defn -main
  "Main function to start the application"
  [& args]
  (chsc/load-hyg-star-catalog)
  (cmes/load-messier-catalog)
  (chdc/load-hyg-dso-catalog)
  (ui/start-ui))

(comment
  (-main)
  )