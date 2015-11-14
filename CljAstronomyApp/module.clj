[
 :module "CljAstronomyApp"
 :project "org.soulspace.clj.astronomy"
 :type :appfrontend
 :version "0.1.1"
 :description "Astronomical Application written in Clojure."
 :main "org.soulspace.clj.astronomy.app.application"
 :plugins ["global"
           ["org.soulspace.baumeister/ClojurePlugin"]
           ["org.soulspace.baumeister/PackagePlugin"]]
 :dependencies [["org.clojure/clojure, 1.7.0"]
                ["org.clojure/core.async, 0.2.374"]
                ["org.clojure/data.csv, 0.1.2"]
                ["org.soulspace.clj/CljAstronomyLibrary, 0.1.1"]
                ["org.soulspace.clj/CljSwingLibrary, 0.4.3"]
                ["org.soulspace.clj/CljApplicationLibrary, 0.5.2"]]
 ]
