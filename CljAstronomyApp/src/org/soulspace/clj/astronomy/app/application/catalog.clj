(ns org.soulspace.clj.astronomy.app.application.catalog
  (:use [clojure.data.csv]))

(def *hyg-file* "/home/soulman/devel/tmp/hygxyz.csv")

(defn read-hyg
  []
  (with-open [in-file *hyg-file*]
    (println (take 10 (read-csv in-file)))))
