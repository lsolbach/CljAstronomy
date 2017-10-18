(defproject org.soulspace.clj/CljAstronomyApp "0.2.0"
  :description "An Astronomical Application written in Clojure."
  :url "https://github.com/lsolbach/CljAstronomy"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.374"]
                 [org.clojure/data.csv "0.1.2"]
                 [reagi/reagi "0.10.1"]
                 [org.soulspace.clj/CljAstronomyLibrary "0.2.0"]
                 [org.soulspace.clj/CljSwingLibrary "0.5.1"]
                 [org.soulspace.clj/CljApplicationLibrary "0.6.0"]]
  :test-paths ["unittest"])
