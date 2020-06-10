;
;   Copyright (c) Ludger Solbach. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file license.txt at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;
(ns org.soulspace.clj.astronomy.app.ui.swing.main-frame
  (:require [reagi.core :as r])
  (:use [clojure.java.io]
        [clojure.tools.swing-utils :only [do-swing-and-wait]]
        [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.application classpath]
        [org.soulspace.clj.astronomy.time instant time]
        [org.soulspace.clj.astronomy.app common i18n]
        [org.soulspace.clj.astronomy.app.data catalogs]
        [org.soulspace.clj.astronomy.app.ui.swing common equipment observation]
        [org.soulspace.clj.astronomy.app.ui.swing.equipment barlow-reducer eyepieces filters optics]
        [org.soulspace.clj.astronomy.app.ui.swing.objects object-info object-list]
        [org.soulspace.clj.astronomy.app.ui.swing.charts equirectangular stereographic orthographic])
  (:import [javax.swing Action BorderFactory JFrame]
           [org.soulspace.clj.astronomy.time.instant JulianDay]))

(declare ui-frame)
(def chart-frame) ; TODO use atom or ref here

; TODO implement if neccessary
(defn open
  [file])

; TODO implement if neccessary
(defn save
  [file])

; actions
(def new-action
  (action (fn [_]) ; TODO
          {:name (i18n "action.file.new")
           :accelerator (key-stroke \n :ctrl)
           :mnemonic nil}))
           ;:icon (image-icon (system-resource-url "images/New.gif") {})

(def open-action
  (action (fn [_] (if-let [file (file-open-dialog ".")]
                    (open file)))
          {:name (i18n "action.file.load")
           :accelerator (key-stroke \o :ctrl)
           :mnemonic nil}))
           ;:icon (image-icon (system-resource-url "images/Open.gif") {})

(def save-action
  (action (fn [_] nil)
          {:name (i18n "action.file.save")
           :accelerator (key-stroke \s :ctrl)
           :mnemonic nil}))
           ;:icon (image-icon (system-resource-url "images/Save.gif") {})

(def saveas-action
  (action (fn [_] (if-let [file (file-save-dialog ".")]
                    (save file)))
          {:name (i18n "action.file.saveAs")
           :accelerator (key-stroke \a :ctrl)
           :mnemonic nil}))
           ;:icon (image-icon (system-resource-url "images/SaveAs.gif") {})

(def quit-action
  (action (fn [_] (System/exit 0))
          {:name (i18n "action.file.quit")
           :accelerator (key-stroke \q :ctrl)
           :mnemonic nil}))
           ;:icon (image-icon (system-resource-url "images/Quit.gif") {})

(def planetarium-action
  (action (fn [_]
            (let [chart-dialog (orthographic-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (i18n "action.view.planetarium")
           :accelerator (key-stroke \p :ctrl)
           :mnemonic nil}))

(def orrery-action
  (action (fn [_]
            (let [chart-dialog (orthographic-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (i18n "action.view.orrery")
           :accelerator (key-stroke \y :ctrl)
           :mnemonic nil}))

(defn location-panel
  []
  (let [f-name (text-field {:columns 15 :editable false :text (:name @current-location)})
        f-long (text-field {:columns 15 :editable false :text (:longitude @current-location)})
        f-lat (text-field {:columns 15 :editable false :text (:latitude @current-location)})]
    (defn update-location-panel
      [location])

    (defn clear-location-panel
      [])
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                     :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.location.title") :font heading-font}) "left, wrap 1"
             (label {:text (i18n "label.location.name")}) f-name
             (label {:text (i18n "label.location.longitude")}) f-long
             (label {:text (i18n "label.location.latitude")}) f-lat]])))

(defn time-panel
  []
  (let [f-local-time (text-field {:columns 15 :text "" :editable false})
        f-universal-time (text-field {:columns 15 :text "" :editable false})
        f-julian-day (text-field {:columns 15 :text (str (as-julian-day @current-time)) :editable false})]
    (defn update-time-panel
      [time]
      (.setText f-julian-day (str (as-julian-day time))))
    (defn clear-time-panel
      [])
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.time.title") :font heading-font}) "left, wrap 1"
             (label {:text (i18n "label.time.local-time")}) f-local-time
             (label {:text (i18n "label.time.universal-time")}) f-universal-time
             (label {:text (i18n "label.time.julian-day")}) f-julian-day]])))

(defn location-time-panel
  []
  (let [date (julian-day-to-date (as-julian-day @current-time))
        year (:year date)
        month (:month date)
        day (:day date)
        f-name (text-field {:columns 20 :editable false :text (:name @current-location)})
        f-long (text-field {:columns 20 :editable false :text (:longitude @current-location)})
        f-lat (text-field {:columns 20 :editable false :text (:latitude @current-location)})
        f-local-time (text-field {:text (str (int year) "/" (int month) "/" (int day)) :columns 20 :editable false})
;        f-local-time (text-field {:text "" :columns 20 :editable false})
        f-universal-time (text-field {:text "" :columns 20 :editable false})
        f-julian-day (text-field {:text (str (as-julian-day @current-time)) :columns 20 :editable false})]
    (println (as-date @current-time))
    (defn update-time-panel
      [time]
      (.setText f-julian-day (str (as-julian-day time))))
    (defn clear-time-panel
      [])
    (panel {:layout (mig-layout {:layoutConstraints "insets 10, wrap 2, fill"
                                 :columnConstraints "[left|grow]"})}
           [[(label {:text (i18n "label.location.title") :font heading-font}) "left, wrap 1"]
            (label {:text (i18n "label.location.name")}) f-name
            (label {:text (i18n "label.location.longitude")}) f-long
            (label {:text (i18n "label.location.latitude")}) f-lat
            [(label {:text (i18n "label.time.title") :font heading-font}) "left, wrap 1"]
            (label {:text (i18n "label.time.local-time")}) f-local-time
            (label {:text (i18n "label.time.universal-time")}) f-universal-time
            (label {:text (i18n "label.time.julian-day")}) f-julian-day])))

(defn main-menu
  []
  (menu-bar {}
            [(menu {:text (i18n "menu.file")}
                   [(menu-item {:action new-action})
                    (menu-item {:action open-action})
                    (menu-item {:action save-action})
                    (menu-item {:action saveas-action})
                    (separator {})
                    (menu-item {:action quit-action})])
             (menu {:text (i18n "menu.views")}
                   [(menu {:text (i18n "menu.view.starchart")}
                          [(menu-item {:action equirectangular-star-chart-action})
                           (menu-item {:action stereographic-star-chart-action})
                           (menu-item {:action orthographic-star-chart-action})])
                    (menu-item {:action planetarium-action})
                    (menu-item {:action orrery-action})
                    (menu-item {:action object-list-action})])
             (menu {:text (i18n "menu.equipment")}
                   [(menu-item {:action optics-action})
                    (menu-item {:action eyepieces-action})
                    (menu-item {:action barlows-reducers-action})
                    (menu-item {:action filters-action})])
             (menu {:text (i18n "menu.observation")}
                   [(menu-item {:action observations-action})])
             (menu {:text (i18n "menu.settings")}
                   [(menu {:text (i18n "menu.settings.layout")}
                          [(menu-item {:action (action (fn [_] (set-look-and-feel ui-frame :metal))
                                                       {:name (i18n "menu.settings.layout.metal") :mnemonic nil})})
                           (menu-item {:action (action (fn [_] (set-look-and-feel ui-frame :nimbus))
                                                       {:name (i18n "menu.settings.layout.nimbus") :mnemonic nil})})
                           (menu-item {:action (action (fn [_] (set-look-and-feel ui-frame :synth))
                                                       {:name (i18n "menu.settings.layout.synth") :mnemonic nil})})
                           (menu-item {:action (action (fn [_] (set-look-and-feel ui-frame :gtk))
                                                       {:name (i18n "menu.settings.layout.gtk") :mnemonic nil})})])
                    (menu {:text (i18n "menu.settings.locale")} ; TODO add other locales
                          [(menu-item {:action (action (fn [_]) ; TODO set locale to de_DE
                                                  {:name (i18n "menu.settings.locale.de_DE") :mnemonic nil})})
                           (menu-item {:action (action (fn [_]) ; TODO set locale to en_GB
                                                  {:name (i18n "menu.settings.locale.en_GB") :mnemonic nil})})
                           (menu-item {:action (action (fn [_]) ; TODO set locale to en_US
                                                  {:name (i18n "menu.settings.locale.en_US") :mnemonic nil})})])])
             (menu {:text (i18n "menu.help")}
                   [(menu-item {:action (action (fn [_] (message-dialog (i18n "label.about.message")
                                                                        (i18n "label.about.title")
                                                                        :info))
                                                {:name (i18n "menu.help.about")
                                                 :accelerator (key-stroke \A :ctrl :alt)
                                                 :mnemonic nil})})])]))

(defn main-frame []
  (frame {:title (i18n "label.app.title")
          :jMenuBar (main-menu)
          :defaultCloseOperation JFrame/DISPOSE_ON_CLOSE}
         [(panel
            {:layout (mig-layout {:layoutConstraints "wrap 1"})}
            [(tool-bar {} [equirectangular-star-chart-action stereographic-star-chart-action orthographic-star-chart-action quit-action])
             (panel {:layout (mig-layout {:layoutConstraints "wrap 1, insets 10, fill, top"})}
                    [(location-time-panel)])])]))

(defn init-ui []
  (def ui-frame (main-frame))
  (doto ui-frame
    (.pack)
    (.setVisible true)))

(defn start-ui []
  (do-swing-and-wait (init-ui)))
