(ns aufgaben.blatt04.aufgabe03)


(defn zinssatz
  [kontostand]
  (cond (< kontostand 3000) 2 :else 3))

(defn zinsen
  [kontostand]
  (* kontostand (/ (zinssatz kontostand) 100)))

