(ns map-prj.main
  (:use-macros
    [dommy.macros :only [node sel sel1]])
  (:require
    [dommy.core :as dommy]
    [goog.net.XhrIo :as xhr]))

(def L (this-as ct (aget ct "L")))

(def mapbox (.-mapbox L))

(def locateOpts {:setView true
                 :maxZoom 16})

(defn map-div [n]
  [:div { :id (str n) }])

(defn parseResponse [e]
  (.getResponseJson
    (.-target e)))

(defn rad [n]
  (/ n 2))

(defn onLocation [m]
  (fn [e]
    (.debug js/console "event: \n" e)
    (let [radius (rad(.-accuracy e))]
      (-> L (.marker
              (.-latlng e))
          (.addTo m)
          (.bindPopup "Welcome to the jungle!")
          (.openPopup))
      (-> L (.circle (.-latlng e) radius)
          (.addTo m))
      )))

(defn handler [event]
  (let [data (parseResponse event)]
    (dommy/append!
      (sel1 :body) (map-div (.-mapname data)))

    (let [mbmap (-> mapbox (.map (.-mapname data) (.-mapurl data))
                    (.setView (.-center data) (.-zoom data)))]

      (.on mbmap "locationfound" (onLocation mbmap))
      (.locate mbmap (clj->js locateOpts)))

    (.debug js/console data)))

(xhr/send "config.json" handler "GET")
