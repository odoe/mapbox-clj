(ns map-prj.main
  (:use-macros
    [dommy.macros :only [node sel sel1]])
  (:require
    [dommy.core :as dommy]))

(def map-name "map-div")

(def map-div
  [:div { :id (str map-name) }])

(dommy/append!
  (sel1 :body) map-div)

(def L (this-as ct (aget ct "L")))

(def map-url "examples.map-9ijuk24y")

(def latlng
  (array 34.0086, -118.4986))

(def zoom 9)

(let [mbmap (-> L (.mapbox.map map-name map-url)
                (.setView latlng zoom))]

  (-> L (.marker
          (array 34.0086, -118.4986))
      (.addTo mbmap)
      (.bindPopup "<b>A point</b><br />Built with ClojureScript.")
      (.openPopup)))
