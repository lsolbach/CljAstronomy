(ns org.soulspace.clj.astronomy.app.ui.swing.application
  (:use [org.soulspace.clj.astronomy.app.data catalogs] 
        [org.soulspace.clj.astronomy.app.ui.swing main-frame])
  (:gen-class))

(defn -main
  "Main function to start the application"
  [& args]
  (load-hyg-catalog)
  (load-messier-catalog)
  (start))
