[
 :module "CljAstronomyApp"
 :project "org.soulspace.clj.astronomy"
 :type :appfrontend
 :version "0.2.0"
 :description "The CljAstronomyApp is an astronomical application written in Clojure"
 :license ["Eclipse Public License 1.0" "http://www.eclipse.org/legal/epl-v10.html"]
 :scm-url "https://github.com/lsolbach/CljAstronomy"
 :main "org.soulspace.clj.astronomy.app.application"
 :plugins [["org.soulspace.baumeister/ClojurePlugin"]
           ["org.soulspace.baumeister/PackagePlugin"]]
 :dependencies [["org.clojure/clojure, 1.10.0"]
                ["org.clojure/core.async, 0.2.374"]
                ["org.clojure/data.csv, 0.1.2"]
                ["reagi/reagi, 0.10.1"]
                ["org.soulspace.clj/CljAstronomyLibrary, 0.2.0"]
                ["org.soulspace.clj/CljSwingLibrary, 0.5.1"]
                ["org.soulspace.clj/CljApplicationLibrary, 0.6.0"]]]
