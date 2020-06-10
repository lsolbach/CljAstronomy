(ns org.soulspace.clj.astronomy.app.instruments.equipment)

(def optic-types [:monocular :binocular :achromat :apochromat :newton :dobson :maksutov :sc :rc :schiefspiegler :other])
(def barlow-reducer-types [:barlow :reducer :flattener :flattener-reducer])
(def filter-types [:color :skyglow :uhc :oiii :h-alpha :h-beta :sii :nii :line :infrared])

(def optics-list (ref [{:name "Celestron C925 Edge HD" :aperture 235 :focal-length 2350 :effectiveness 95 :available true}
                       {:name "GSO Dobson 10\"" :aperture 254 :focal-length 1250 :effectiveness 85  :available true}]))
(def eyepieces-list (ref [{:name "Baader Hyperion Zoom 8-24mm" :zoom true :focal-length 8  :focal-length-zoom 24 :fov 50 :available true}
                          {:name "Baader Hyperion Aspheric 36mm" :zoom false :focal-length 36 :fov 72 :available true}
                          {:name "ES 82° 4,7mm" :zoom false :focal-length 4.7 :fov 82 :available true}
                          {:name "ES 82° 6,7mm" :zoom false :focal-length 6.7 :fov 82 :available true}
                          {:name "ES 82° 8,8mm" :zoom false :focal-length 8.8 :fov 82 :available true}]))
(def barlows-reducers-list (ref [{:name "University Optics" :type :barlow}
                                 {:name "Baader Hyperion" :type :barlow}]))
(def filters-list (ref [{:name "Baader UHC 2\"" :available true}
                        {:name "Baader OIII 2\"" :available true}
                        {:name "Baader H-beta 2\"" :available true}]))
