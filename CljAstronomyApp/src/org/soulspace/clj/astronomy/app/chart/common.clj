(ns org.soulspace.clj.astronomy.app.chart.common
  (:use [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt graphics]))

(def projections [:equirectangular :orthoscopic :stereoscopic])

(def simple-star-color-map
  {"O" (color 0x9bb0ff)
   "B" (color 0xaabfff)
   "A" (color 0xcad7ff)
   "F" (color 0xf8f7ff)
   "G" (color 0xfff4ea)
   "K" (color 0xffd2a1)
   "M" (color 0xffcc6f)})

;TODO use colors
(def chart-colors {:open-cluster (color 0xffffff)
                   :globular-cluster (color 0xffffff)
                   :galaxy (color 0xffffff)
                   :supernova-remnant (color 0x009999) 
                   :planetary-nebula (color 0x0077ff)
                   :emission-nebula (color 0xff0000)
                   :reflection-nebula (color 0xffff77)
                   :dark-nebula (color 0x330000)
                   :nebula (color 0x00ff00)
                   :grid (color 0x333333)
                   :black (color 0)
                   :white (color 0xffffff)})

