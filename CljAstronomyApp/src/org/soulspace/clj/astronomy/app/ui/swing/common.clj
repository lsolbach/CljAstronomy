;;;;
;;;;   Copyright (c) Ludger Solbach. All rights reserved.
;;;;
;;;;   The use and distribution terms for this software are covered by the
;;;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;;;   which can be found in the file license.txt at the root of this distribution.
;;;;   By using this software in any fashion, you are agreeing to be bound by
;;;;   the terms of this license.
;;;;
;;;;   You must not remove this notice, or any other, from this software.
;;;;

(ns org.soulspace.clj.astronomy.app.ui.swing.common
  (:require [org.soulspace.clj.java.awt.core :as awt]))

(def heading-font (awt/font (awt/font-names :dialog) [(awt/font-styles :bold)] 14))

(comment
  ; UI state?
  {:observer {:location ""
              :time ""}
   :star-chart {:projection :equirectangular
                :center ""
                :size-x 1440
                :size-y 720
                :zoom-level 0
                :object-types :all ; #{:galaxy :star} ; set of objct types to display
                :magnitudes {:max -30 :min 6}}
   :selected-object nil}
  
  )