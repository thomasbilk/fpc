(ns incant.core
  (:use (incanter core))
  (:require
    [incanter.core :as incanter]
    [incanter.io :as incanterio]))

(defn -main [& args]
  (let [ds (incanterio/read-dataset "~/Downloads/wetter.csv" :headers true)]
    (view ds)))
