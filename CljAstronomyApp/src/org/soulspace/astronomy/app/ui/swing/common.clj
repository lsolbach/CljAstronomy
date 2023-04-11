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

(ns org.soulspace.astronomy.app.ui.swing.common
  (:require [org.soulspace.clj.java.awt.core :as awt]))

(def heading-font (awt/font (awt/font-names :dialog) [(awt/font-styles :bold)] 14))

; TODO use global state atom with get-in/update-in

(def ui-state (atom {:observer {}
                     :observer-view {:location {}
                                     :time 0}
                     :object-view {:objects []
                                   :object-filter {}
                                   :selected-object {}}
                     :observations-view {:observations []
                                         :observation-filter {}
                                         :selected-observation {}}
                     :equipment-view {:optics []
                                      :optics-filter {}
                                      :selected-optics {}
                                      :eyepieces []
                                      ::eyepiece-filter {}
                                      :selected-eyepiece {}
                                      :filters []
                                      :filter-filter {}
                                      :selected-filter {}
                                      :reducers []
                                      :reducer-filter {}
                                      :selected-reducer {}
                                      :cameras []
                                      :camera-filter {}
                                      :selected-camera {}}}))

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
                :magnitude {:max -30 :min 6}}
   :selected-object nil}
  
  )