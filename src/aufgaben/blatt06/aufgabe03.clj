(ns aufgaben.blatt06.aufgabe03)

(defn f [g] (g 2))

; (a)
(f #(* % %))

(f (fn [z] (* z (inc z))))

; (b)

(f f)
