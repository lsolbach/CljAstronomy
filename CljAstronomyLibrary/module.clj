[
 :module "CljAstronomyLibrary"
 :project "org.soulspace.clj"
 :type :library
 :version "0.1.1"
 :description "A library of astronomical algorithms in clojure"
 :plugins ["global"
           ["org.soulspace.baumeister/ClojurePlugin"]
           ["org.soulspace.baumeister/ClojureTestPlugin"]
           ["org.soulspace.baumeister/PackagePlugin"]]
 :dependencies [["org.clojure/clojure, 1.7.0"]
                ["org.soulspace.clj/CljLibrary, 0.6.1"]
                ["org.soulspace.clj/CljMathLibrary, 0.3.0"]]
 ]