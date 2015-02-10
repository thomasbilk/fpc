(use '(incanter core stats charts optimize datasets latex io))

(def n 10000)
(def winDoors (sample [1 2 3] :size n))  ; Probe mit Gewinn-Türen
(def guesses (sample [1 2 3] :size n))   ; Probe mit geratenen Türen

; Simulation eines Spielers, der nicht wechselt
(defn winOnStay [guess winDoor]
  (if (= guess winDoor) 1 0))
; Simulation eines Spielers der wechselt
(defn winOnSwitch [guess winDoor]
  (if (not= guess winDoor) 1 0))

; Simulationen durchführen
(def wStay (sum (map winOnStay guesses winDoors)))
(def wSwitch (sum (map winOnSwitch guesses winDoors)))


; Diagramme
(doto
  (pie-chart ["Gewinn" "Niete"] [wStay (- n wStay)]
    :title "Ziegenproblem")
  (add-subtitle "Ohne Wechsel")
  view)
(doto
  (pie-chart ["Gewinn" "Niete"] [wSwitch (- n wSwitch)]
    :title "Ziegenproblem")
  (add-subtitle "Mit Wechsel")
  view)



(def combination (sample (range 1 50) :size 6 :replacement false))
(def guesses (for [x (range 0 100000)] (sample (range 1 50) :size 6 :replacement false)))

; ist Zahl in den 6 richtigen enthalten?
(defn inCombination? [item]
  (if (some #(= item %) combination) 1 0))

; wie viele Übereinstimmungen gibt es?
(defn nInCombination [guess]
  (int (sum (map inCombination? guess))))

(def lotteryProbe ($order :correct :asc (col-names (to-dataset (conj-rows (seq (frequencies (map nInCombination guesses))))) [:correct :count])))

(view lotteryProbe)

(time ($order :correct :asc (col-names (to-dataset (conj-rows (seq (frequencies (map nInCombination guesses))))) [:correct :count])))
