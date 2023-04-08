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

(ns org.soulspace.astronomy.app.ui.swing.main-frame
  (:require [clojure.java.io :as io]
            [clojure.tools.swing-utils :refer [do-swing-and-wait]]
            [reagi.core :as r]
            [org.soulspace.clj.java.awt.core :as awt]
            [org.soulspace.clj.java.awt.events :as aevt]
            [org.soulspace.clj.java.swing.core :as swing]
            [org.soulspace.astronomy.time :as time]
            [org.soulspace.astronomy.app.common :as app]
            [org.soulspace.astronomy.app.ui.swing.common :as swc]
            [org.soulspace.astronomy.app.ui.swing.equipment :as sweq]
            [org.soulspace.astronomy.app.ui.swing.observation :as swobs]
            [org.soulspace.astronomy.app.ui.swing.objects :as swobj]
            [org.soulspace.astronomy.app.ui.swing.charts :as swch])
  (:import [javax.swing Action BorderFactory JFrame JDialog]
           [org.soulspace.astronomy.time JulianDay]))

(declare ui-frame)

; TODO use global state atom with get-in/update-in

(def chart-frame) ; TODO use atom or ref here

; TODO implement if neccessary
(defn open
  [file])

; TODO implement if neccessary
(defn save
  [file])

;;
;; Actions
;;

(def new-action
  (swing/action (fn [_]) ; TODO
          {:name (app/i18n "action.file.new")
           :accelerator (swing/key-stroke \n :ctrl)
           :mnemonic nil}))
           ;:icon (image-icon (system-resource-url "images/New.gif") {})

(def open-action
  (swing/action (fn [_] (if-let [file (swing/file-open-dialog ".")]
                    (open file)))
          {:name (app/i18n "action.file.load")
           :accelerator (swing/key-stroke \o :ctrl)
           :mnemonic nil}))
           ;:icon (image-icon (system-resource-url "images/Open.gif") {})

(def save-action
  (swing/action (fn [_] nil)
          {:name (app/i18n "action.file.save")
           :accelerator (swing/key-stroke \s :ctrl)
           :mnemonic nil}))
           ;:icon (image-icon (system-resource-url "images/Save.gif") {})

(def saveas-action
  (swing/action (fn [_] (if-let [file (swing/file-save-dialog ".")]
                    (save file)))
          {:name (app/i18n "action.file.saveAs")
           :accelerator (swing/key-stroke \a :ctrl)
           :mnemonic nil}))
           ;:icon (image-icon (system-resource-url "images/SaveAs.gif") {})

(def quit-action
  (swing/action (fn [_] (System/exit 0))
          {:name (app/i18n "action.file.quit")
           :accelerator (swing/key-stroke \q :ctrl)
           :mnemonic nil}))
           ;:icon (image-icon (system-resource-url "images/Quit.gif") {})

(def planetarium-action
  (swing/action (fn [_]
            (let [^JDialog chart-dialog (swch/orthographic-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (app/i18n "action.view.planetarium")
           :accelerator (swing/key-stroke \p :ctrl)
           :mnemonic nil}))

(def orrery-action
  (swing/action (fn [_]
            (let [chart-dialog (swch/orthographic-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (app/i18n "action.view.orrery")
           :accelerator (swing/key-stroke \y :ctrl)
           :mnemonic nil}))

;;
;; Panels
;;

(defn location-panel
  "Creates a panel for the location."
  []
  (println "observer-panel")
  (let [f-name (swing/text-field {:columns 15 :editable false :text (:name @app/current-location)})
        f-long (swing/text-field {:columns 15 :editable false :text (:longitude @app/current-location)})
        f-lat (swing/text-field {:columns 15 :editable false :text (:latitude @app/current-location)})]
    (defn update-location-panel
      [location])

    (defn clear-location-panel
      [])
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                             :columnConstraints "[left|grow]"})}
                 [[(swing/label {:text (app/i18n "label.location.title") :font swc/heading-font}) "left, wrap 1"
                   (swing/label {:text (app/i18n "label.location.name")}) f-name
                   (swing/label {:text (app/i18n "label.location.longitude")}) f-long
                   (swing/label {:text (app/i18n "label.location.latitude")}) f-lat]])))

(defn time-panel
  "Creates a panel for the time."
  []
  (println "time-panel")
  (let [f-local-time (swing/text-field {:columns 15 :text "" :editable false})
        f-universal-time (swing/text-field {:columns 15 :text "" :editable false})
        f-julian-day (swing/text-field {:columns 15 :text (str (time/as-julian-day @app/current-time)) :editable false})]
    (defn update-time-panel
      [time]
      (.setText f-julian-day (str (time/as-julian-day time))))
    (defn clear-time-panel
      [])
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                             :columnConstraints "[left|grow]"})}
                 [[(swing/label {:text (app/i18n "label.time.title") :font swc/heading-font}) "left, wrap 1"
                   (swing/label {:text (app/i18n "label.time.local-time")}) f-local-time
                   (swing/label {:text (app/i18n "label.time.universal-time")}) f-universal-time
                   (swing/label {:text (app/i18n "label.time.julian-day")}) f-julian-day]])))

(defn observer-panel
  "Creates the observer panel to set location and time."
  []
  (println "observer-panel")
  (let [date (time/julian-day-to-date (time/as-julian-day @app/current-time))
        year (:year date)
        month (:month date)
        day (:day date)
        f-name (swing/text-field {:columns 20 :editable false :text (:name @app/current-location)})
        f-long (swing/text-field {:columns 20 :editable false :text (:longitude @app/current-location)})
        f-lat (swing/text-field {:columns 20 :editable false :text (:latitude @app/current-location)})
        f-local-time (swing/text-field {:text (str (int year) "/" (int month) "/" (int day)) :columns 20 :editable false})
;        f-local-time (text-field {:text "" :columns 20 :editable false})
        f-universal-time (swing/text-field {:text "" :columns 20 :editable false})
        f-julian-day (swing/text-field {:text (str (time/as-julian-day @app/current-time)) :columns 20 :editable false})]
    (println (time/as-date @app/current-time))
    (defn update-time-panel
      [time]
      (.setText f-julian-day (str (time/as-julian-day time))))
    (defn clear-time-panel
      [])
    (swing/panel {:layout (swing/mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(swing/label {:text (app/i18n "label.location.title") :font swc/heading-font}) "left, wrap 1"]
             (swing/label {:text (app/i18n "label.location.name")}) f-name
             (swing/label {:text (app/i18n "label.location.longitude")}) f-long
             (swing/label {:text (app/i18n "label.location.latitude")}) f-lat
            [(swing/label {:text (app/i18n "label.time.title") :font swc/heading-font}) "left, wrap 1"]
             (swing/label {:text (app/i18n "label.time.local-time")}) f-local-time
             (swing/label {:text (app/i18n "label.time.universal-time")}) f-universal-time
             (swing/label {:text (app/i18n "label.time.julian-day")}) f-julian-day])))

(defn main-menu
  []
  (println "main-menu")
  (println "menu-bar")
  (swing/menu-bar {}
                  [
                   (swing/menu {:text (app/i18n "menu.file")}
                               [(swing/menu-item {:action new-action})
                                (swing/menu-item {:action open-action})
                                (swing/menu-item {:action save-action})
                                (swing/menu-item {:action saveas-action})
                                (swing/separator {})
                                (swing/menu-item {:action quit-action})])
                   (swing/menu {:text (app/i18n "menu.views")}
                               [(swing/menu {:text (app/i18n "menu.view.starchart")}
                                            [(swing/menu-item {:action swch/equirectangular-star-chart-action})
                                             (swing/menu-item {:action swch/stereographic-star-chart-action})
                                             (swing/menu-item {:action swch/orthographic-star-chart-action})])
                                (swing/menu-item {:action planetarium-action})
                                (swing/menu-item {:action orrery-action})
                                (swing/menu-item {:action swobj/object-list-action})])
                   (swing/menu {:text (app/i18n "menu.equipment")}
                               [(swing/menu-item {:action sweq/optics-action})
                                (swing/menu-item {:action sweq/eyepieces-action})
                                (swing/menu-item {:action sweq/barlows-reducers-action})
                                (swing/menu-item {:action sweq/filters-action})])
                   (swing/menu {:text (app/i18n "menu.observation")}
                               [(swing/menu-item {:action swobs/observations-action})])
                   (swing/menu {:text (app/i18n "menu.settings")}
                               [(swing/menu {:text (app/i18n "menu.settings.layout")}
                                            [(swing/menu-item {:action (swing/action
                                                                        (fn [_]
                                                                            (swing/set-look-and-feel ui-frame :metal))
                                                                        {:name (app/i18n "menu.settings.layout.metal") :mnemonic nil})})
                                             (swing/menu-item {:action (swing/action
                                                                        (fn [_]
                                                                            (swing/set-look-and-feel ui-frame :nimbus))
                                                                        {:name (app/i18n "menu.settings.layout.nimbus") :mnemonic nil})})
                                             (swing/menu-item {:action (swing/action
                                                                        (fn [_]
                                                                            (swing/set-look-and-feel ui-frame :synth))
                                                                        {:name (app/i18n "menu.settings.layout.synth") :mnemonic nil})})
                                             (swing/menu-item {:action (swing/action
                                                                        (fn [_]
                                                                            (swing/set-look-and-feel ui-frame :gtk))
                                                                        {:name (app/i18n "menu.settings.layout.gtk") :mnemonic nil})})])
                                (swing/menu {:text (app/i18n "menu.settings.locale")} ; TODO add other locales
                                            [(swing/menu-item {:action (swing/action
                                                                        (fn [_]) ; TODO set locale to de_DE
                                                                        {:name (app/i18n "menu.settings.locale.de_DE") :mnemonic nil})})
                                             (swing/menu-item {:action (swing/action
                                                                        (fn [_]) ; TODO set locale to en_GB
                                                                        {:name (app/i18n "menu.settings.locale.en_GB") :mnemonic nil})})
                                             (swing/menu-item {:action (swing/action
                                                                        (fn [_]) ; TODO set locale to en_US
                                                                        {:name (app/i18n "menu.settings.locale.en_US") :mnemonic nil})})])])
                   (swing/menu {:text (app/i18n "menu.help")}
                               [(swing/menu-item {:action (swing/action
                                                           (fn
                                                             [_]
                                                             (swing/message-dialog
                                                              (app/i18n "label.about.message")
                                                              (app/i18n "label.about.title")
                                                              :info))
                                                           {:name (app/i18n "menu.help.about")
                                                            :accelerator (swing/key-stroke \A :ctrl :alt)
                                                            :mnemonic nil})})])
                   ]))

(defn main-frame
  "Creates the main frame of the Swing UI."
  []
  (println "main-frame")
  (swing/frame {:title (app/i18n "label.app.title")
          :jMenuBar (main-menu)
          :defaultCloseOperation JFrame/DISPOSE_ON_CLOSE}
         [(swing/panel
            {:layout (swing/mig-layout {:layoutConstraints "wrap 1"})}
            [(swing/tool-bar {}
                             [swch/equirectangular-star-chart-action
                              swch/stereographic-star-chart-action
                              swch/orthographic-star-chart-action
                              quit-action])
             (swing/panel {:layout (swing/mig-layout {:layoutConstraints "wrap 1, insets 10, fill, top"})}
                    [(observer-panel)])])]))

(defn init-ui
  "Creates and initializes the Swing UI."
  []
  (println "init-ui")
  (def ui-frame (main-frame))
  (doto ui-frame
    (.pack)
    (.setVisible true)))

(defn start-ui
  "Starts the Swing event loop on the UI."
  []
  (do-swing-and-wait (init-ui)))
