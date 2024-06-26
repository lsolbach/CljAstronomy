(defproject org.soulspace.clj/CljAstronomyApp "0.3.0-SNAPSHOT"
  :description "An Astronomical Application written in Clojure."
  :url "https://github.com/lsolbach/CljAstronomy"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/spec.alpha "0.3.218"]
                 [org.clojure/core.async "1.6.673"]
                 [org.clojure/data.csv "1.0.1"]
                 [integrant/integrant "0.8.0"]
                 [reagi/reagi "0.10.1" :exclusions [org.clojure/clojure]]
                 [org.soulspace.clj/astronomy.core "0.4.3"]
                 [org.soulspace.clj/clj.swing "0.8.3"]]
  :test-paths ["test"]
  :profiles {:dev {:dependencies [[djblue/portal "0.37.1"]
                                  [criterium "0.4.6"]]
                   :global-vars {*warn-on-reflection* true}}}
  :main org.soulspace.astronomy.app.ui.swing.application)
