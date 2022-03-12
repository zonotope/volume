(ns one.732.volume.notify
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as str]))

(def sound-path
  "/usr/share/sounds/freedesktop/stereo/audio-volume-change.oga")

(defn play-sound
  []
  (sh "mpv" "--no-terminal" sound-path))

(def timeout-ms 2000)

(def timeout
  (str/join "=" ["--timeout" timeout-ms]))

(defn hint-arg
  [components]
  (let [hint (str/join ":" components)]
    (str/join "=" ["--hints" hint])))

(def tag
  (hint-arg ["string" "x-dunst-stack-tag" "volume"]))

(defn status->progress-hint
  [status]
  (let [n (if-not status 0 status)]
    (hint-arg ["int" "value" n])))

(defn status->text
  [status]
  (if-not status
    "Muted"
    (str "Volume: " status "%")))

(defn status->icon-name
  [status]
  (if (false? status)
    "audio-volume-muted"
    (cond
      (< status 33)     "audio-volume-low"
      (<= 33 status 66) "audio-volume-medium"
      (< 66 status)     "audio-volume-high")))

(defn status->icon-arg
  [status]
  (let [icon (status->icon-name status)]
    (str/join "=" ["--icon" icon])))

(defn show
  [status]
  (let [icon     (status->icon-arg status)
        progress (status->progress-hint status)
        title    (status->text status)]
    (sh "dunstify" tag icon progress timeout title)))

(defn play-and-show
  [status]
  (show status)
  (when status
    (play-sound))
  nil)
