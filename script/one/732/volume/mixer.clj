(ns one.732.volume.mixer
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as str]))

(defn pamixer
  [& args]
  (-> (apply sh "pamixer" args)
      :out
      str/trim-newline))

(defn raise
  [n]
  (pamixer "--increase" (str n)))

(defn lower
  [n]
  (pamixer "--decrease" (str n)))

(defn toggle-mute
  []
  (pamixer "--toggle-mute"))

(defn muted?
  []
  (Boolean/valueOf (pamixer "--get-mute")))

(defn current-volume
  []
  (Integer/parseInt (pamixer "--get-volume")))

(defn current-status
  []
  (if-not (muted?)
    (current-volume)
    false))

(defn adjust
  [{:keys [up down mute]}]
  (cond
    up   (raise up)
    down (lower down)
    mute (toggle-mute))
  (current-status))
