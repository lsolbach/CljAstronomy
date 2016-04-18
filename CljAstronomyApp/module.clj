[
 :module "CljAstronomyApp"
 :project "org.soulspace.clj.astronomy"
 :type :appfrontend
 :version "0.2.0"
 :description "An Astronomical Application written in Clojure."
 :main "org.soulspace.clj.astronomy.app.application"
 :plugins ["global"
           ["org.soulspace.baumeister/ClojurePlugin"]
           ["org.soulspace.baumeister/PackagePlugin"]]
 :dependencies [["org.clojure/clojure, 1.7.0"]
                ["org.clojure/core.async, 0.2.374"]
                ["org.clojure/data.csv, 0.1.2"]
                ["reagi/reagi, 0.10.1"]
                ["org.soulspace.clj/CljAstronomyLibrary, 0.1.2"]
                ["org.soulspace.clj/CljSwingLibrary, 0.4.4"]
                ["org.soulspace.clj/CljApplicationLibrary, 0.5.2"]]
 :log-level :info
 ]
