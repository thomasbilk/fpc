(ns aufgaben.blatt06.aufgabe02)

(def quadrat #(* % %))
(quadrat 3)
(quadrat 5)

(defn n-fach[a x]
  (apply comp (repeat x a)))

((n-fach quadrat 3) 5)
