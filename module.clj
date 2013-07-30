[
 :module "CljAstronomyLibrary"
 :project "org.soulspace.clj"
 :type :library
 :version "0.1.0"
 :description "A library of astronomical algorithms in clojure"
 :plugins ["global" "sdeps" "clojure" "package"]
 :dependencies [[["org.clojure" "clojure" "1.5.1"]]
                [["org.soulspace.clj" "CljLibrary" "0.3.0"]]
                [["org.soulspace.clj" "CljMathLibrary" "0.2.0"]]]
 ]