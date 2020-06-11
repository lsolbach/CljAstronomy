(defproject org.soulspace.clj/CljAstronomyLibrary "0.2.0"
  :description "A library of astronomical algorithms in clojure"
  :url "https://github.com/lsolbach/CljAstronomy"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.soulspace.clj/CljLibrary "0.7.0"]
                 [org.soulspace.clj/CljMathLibrary "0.5.1"]
                 [proto-repl "0.3.1"]]
  :test-paths ["unittest"])
