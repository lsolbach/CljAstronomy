(ns org.soulspace.clj.astronomy.app.equipment)


(def optic-types [:monocular :binocular :achromat :apochromat :newton :dobson :maksutov :sc :rc :schiefspiegler :other])
(def barlow-reducer-types [:barlow :reducer])
(def filter-types [:color :skyglow :uhc :oiii :h-beta :line :h-alpha])
  
(defprotocol Optic
  ""
  )

;(defrecord OpticImpl [name type aperture focal-length fixed-magnification available])

(defprotocol Eyepiece
  ""
  )

(defprotocol BarlowReducer
  ""
  )

(defprotocol Filter
  ""
  )

(def optics-list (ref [{:name "Celestron Edge HD 925" :aperture 235 :focal-length 2350 :effectiveness 95 :available true}
                       {:name "8\" Dobson" :aperture 203 :focal-length 1220 :effectiveness 85  :available true}]))
(def eyepieces-list (ref [{:name "Baader Hyperion Zoom 8-24mm" :zoom true :focal-length 8  :focal-length-zoom 24 :fov 70 :available true}
                          {:name "ES 82° 4,7mm" :focal-length 4.7 :fov 82 :available true}
                          {:name "ES 82° 6,7mm" :focal-length 6.7 :fov 82 :available true}
                          {:name "ES 82° 8,8mm" :focal-length 8.8 :fov 82 :available true}]))
(def barlows-reducers-list (ref [{:name "University Optics" :type :barlow}
                                  {:name "Baader Hyperion" :type :barlow}]))
(def filters-list (ref [{:name "Baader UHC 2\"" :available true}
                        {:name "Baader OIII 2\"" :available true}
                        {:name "Baader H-beta 2\"" :available true}]))
