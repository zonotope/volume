(ns one.732.volume
  (:require [one.732.volume.mixer :as mixer]
            [one.732.volume.notify :as notify]
            [clojure.tools.cli :as cli]))

(def cli-opts
  [["-d" "--down AMOUNT" "Percentage"
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 100) "Must be a number between 0 and 100"]]
   ["-m" "--mute"]
   ["-u" "--up AMOUNT" "Percentage"
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 100) "Must be a number between 0 and 100"]]])

(defn run
  [& args]
  (-> args
      (cli/parse-opts cli-opts)
      :options
      mixer/adjust
      notify/play-and-show))
