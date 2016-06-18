[
 :module "CljAstronomyLibrary"
 :project "org.soulspace.clj"
 :type :library
 :version "0.2.0"
 :description "A library of astronomical algorithms in clojure."
 :scm-url "https://github.com/lsolbach/CljAstronomy"
 :license ["Eclipse Public License 1.0" "http://www.eclipse.org/legal/epl-v10.html"]
 :plugins [["org.soulspace.baumeister/ClojurePlugin"]
           ["org.soulspace.baumeister/ClojureTestPlugin"]
           ["org.soulspace.baumeister/PackagePlugin"]]
 :dependencies [["org.clojure/clojure, 1.8.0"]
                ["org.soulspace.clj/CljLibrary, 0.7.0"]
                ["org.soulspace.clj/CljMathLibrary, 0.5.1"]]
 ]