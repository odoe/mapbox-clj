(ns map-prj.main
  (:use-macros
    [dommy.macros :only [node sel sel1]])
  (:require
    [dommy.core :as dommy]
    [goog.net.XhrIo :as xhr]))

(def L (this-as ct (aget ct "L")))

(def mapbox (.-mapbox L))

(defn map-div [n]
  [:div { :id (str n) }])

(def latlng
  (array 34.0086, -118.4986))

(def zoom 9)

(defn handler [event]
  (let [response (.-target event)]
    (let [data (.getResponseJson response)]
      (dommy/append!
        (sel1 :body) (map-div (.-mapname data)))

      (let [mbmap (-> mapbox (.map (.-mapname data) (.-mapurl data))
                      (.setView latlng zoom))]

        (-> L (.marker
                (array 34.0086, -118.4986))
            (.addTo mbmap)
            (.bindPopup "<b>A point</b><br />Built with ClojureScript.")
            (.openPopup)))
      (.debug js/console data))))

(xhr/send "config.json" handler "GET")
