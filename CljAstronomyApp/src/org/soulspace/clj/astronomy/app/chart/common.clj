(ns org.soulspace.clj.astronomy.app.chart.common
  (:require [org.soulspace.clj.java.awt.core :as awt]
            [org.soulspace.clj.java.awt.graphics :as gfx]))

(def projections [:equirectangular :orthoscopic :stereoscopic])

(def simple-star-color-map
  {"O" (awt/color 0x9bb0ff)
   "B" (awt/color 0xaabfff)
   "A" (awt/color 0xcad7ff)
   "F" (awt/color 0xf8f7ff)
   "G" (awt/color 0xfff4ea)
   "K" (awt/color 0xffd2a1)
   "M" (awt/color 0xffcc6f)})

;TODO use colors
(def chart-colors {:open-cluster (awt/color 0xffffff)
                   :globular-cluster (awt/color 0xffffff)
                   :galaxy (awt/color 0xffffff)
                   :supernova-remnant (awt/color 0x009999) 
                   :planetary-nebula (awt/color 0x0077ff)
                   :emission-nebula (awt/color 0xff0000)
                   :reflection-nebula (awt/color 0xffff77)
                   :dark-nebula (awt/color 0x330000)
                   :nebula (awt/color 0x00ff00)
                   :grid (awt/color 0x333333)
                   :black (awt/color 0)
                   :white (awt/color 0xffffff)})
