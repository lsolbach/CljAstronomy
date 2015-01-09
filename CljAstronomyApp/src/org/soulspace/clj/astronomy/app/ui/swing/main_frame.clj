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
  (:use [clojure.java.io]
        [clojure.tools.swing-utils :only [do-swing-and-wait]]
        [org.soulspace.clj.java awt]
        [org.soulspace.clj.java.awt event]
        [org.soulspace.clj.java.swing constants swinglib]
        [org.soulspace.clj.application classpath]
        [org.soulspace.clj.astronomy.app i18n]
        [org.soulspace.clj.astronomy.app.data catalogs]
        [org.soulspace.clj.astronomy.app.ui.swing chart equipment observation])
  (:import [javax.swing Action BorderFactory JFrame]))

(declare ui-frame)
(def chart-frame) ; TODO use atom or ref here

(def heading-font (font (font-names :dialog) [(font-styles :bold)] 14))

; TODO implement if neccessary
(defn open
  [file])

; TODO implement if neccessary
(defn save
  [file])

; actions
(def new-action
  (action (fn [_] ) ; TODO 
          {:name (i18n "action.file.new")
           :accelerator (key-stroke \n :ctrl)
           :mnemonic nil
           ;:icon (image-icon (system-resource-url "images/New.gif") {})
           }))

(def open-action
  (action (fn [_] (if-let [file (file-open-dialog ".")]
                    (open file)))
          {:name (i18n "action.file.load")
           :accelerator (key-stroke \o :ctrl)
           :mnemonic nil
           ;:icon (image-icon (system-resource-url "images/Open.gif") {})
           }))

(def save-action
  (action (fn [_] nil)
          {:name (i18n "action.file.save")
           :accelerator (key-stroke \s :ctrl)
           :mnemonic nil
           ;:icon (image-icon (system-resource-url "images/Save.gif") {})
           }))

(def saveas-action
  (action (fn [_] (if-let [file (file-save-dialog ".")]
                    (save file)))
          {:name (i18n "action.file.saveAs")
           :accelerator (key-stroke \a :ctrl)
           :mnemonic nil
           ;:icon (image-icon (system-resource-url "images/SaveAs.gif") {})
           }))

(def quit-action
  (action (fn [_] (System/exit 0))
          {:name (i18n "action.file.quit")
           :accelerator (key-stroke \q :ctrl)
           :mnemonic nil
           ;:icon (image-icon (system-resource-url "images/Quit.gif") {})
           }))

(def equirectangular-star-chart-action
  (action (fn [_]
            (let [chart-dialog (equirectangular-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (i18n "action.view.starchart.equirectangular")
           :accelerator (key-stroke \c :ctrl)
           :mnemonic nil}))

(def stereoscopic-star-chart-action
  (action (fn [_]
            (let [chart-dialog (stereoscopic-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (i18n "action.view.starchart.stereoscopic")
           :accelerator (key-stroke \c :ctrl)
           :mnemonic nil}))

(def orthoscopic-star-chart-action
  (action (fn [_]
            (let [chart-dialog (orthoscopic-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (i18n "action.view.starchart.orthoscopic")
           :accelerator (key-stroke \c :ctrl)
           :mnemonic nil}))

(def planetarium-action
  (action (fn [_]
            (let [chart-dialog (orthoscopic-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (i18n "action.view.planetarium")
           :accelerator (key-stroke \p :ctrl)
           :mnemonic nil}))

(def orrery-action
  (action (fn [_]
            (let [chart-dialog (orthoscopic-star-chart-dialog)]
              (.setVisible chart-dialog true)))
          {:name (i18n "action.view.orrery")
           :accelerator (key-stroke \y :ctrl)
           :mnemonic nil}))

(defn time-panel
  []
  (let []))

(defn location-panel
  []
  (let []))

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
                           (menu-item {:action stereoscopic-star-chart-action})
                           (menu-item {:action orthoscopic-star-chart-action})])
                    (menu-item {:action planetarium-action})
                    (menu-item {:action orrery-action})])
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
                               [(menu-item {:action (action (fn [_] ) ; TODO set locale to de_DE
                                                       {:name (i18n "menu.settings.locale.de_DE") :mnemonic nil})})
                                (menu-item {:action (action (fn [_] ) ; TODO set locale to en_GB
                                                       {:name (i18n "menu.settings.locale.en_GB") :mnemonic nil})})])])
             (menu {:text (i18n "menu.help")}
                   [(menu-item {:action (action (fn [_] (message-dialog (i18n "dialog.about.message")
                                                                        (i18n "dialog.about.title")
                                                                        :info))
                                                {:name (i18n "menu.help.about")
                                                 :accelerator (key-stroke \A :ctrl :alt)
                                                 :mnemonic nil})})])]))

(defn main-frame []
  (frame {:title (i18n "app.title")
          :jMenuBar (main-menu)
          :defaultCloseOperation JFrame/DISPOSE_ON_CLOSE}
         [(panel
            {:layout (mig-layout {:layoutConstraints "wrap 1"})}
            [(tool-bar {} [new-action open-action save-action saveas-action quit-action])
             (horizontal-split-pane
               {}
               [(vertical-split-pane
                  {}
                  [(panel {:layout (mig-layout {:layoutConstraints "wrap 1, insets 0, fill, top"})}
                          [(scroll-pane (j-tree {}))])
                   (panel {:layout (mig-layout {:layoutConstraints "wrap 1, insets 10, fill, top"})}
                          [])])
                (vertical-split-pane
                  {}
                  [(panel {:layout (mig-layout {:layoutConstraints "wrap 1, insets 10, fill, top"})}
                          [])
                   (panel {:layout (mig-layout {:layoutConstraints "wrap 1, insets 10, fill, top"})}
                          [])])])])]))

(defn init-ui []
  (def ui-frame (main-frame))
  (doto ui-frame
    (.pack)
    (.setVisible true)))

(defn start []
  (do-swing-and-wait (init-ui)))
