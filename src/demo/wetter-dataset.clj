(use '(incanter core io))

;; funktion zum herunterladen von wetterdaten
(defn wetter-pro-monat [monat]
  (-> (format "http://www.wunderground.com/history/airport/EDDF/2014/%d/1/MonthlyHistory.html?format=1" monat)
      (read-dataset :header true)))

;; lade das wetter aller monate von 2014 herunter
(def wetter (->> (range 1 13) 
  (map wetter-pro-monat) (apply conj-rows)))

(view wetter)
(def data (sel wetter :cols [0 2 8 11 14 21]))
(view data)

(view ($where {(keyword "Mean TemperatureC") {:$gt 25}} data)))
(view (query-dataset data (fn [row] (> (row (keyword "Mean TemperatureC")) 25))))
(view ($where {(keyword " Events") "Rain"} data)))
(view ($order (keyword " Mean Sea Level PressurehPa") :desc data))
