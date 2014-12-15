(ns aufgaben.blatt08.aufgabe04)

; (a)

(defn vsquare [v] (vec (map #(* % %) v)))

(vsquare [2 3 4 5])

; (b)

(defn vinc [v] (vec (map inc v)))

(vinc [9 99 999])

; (c)

(defn vmult [v n] (vec (map #(* % n) v)))

(vmult [3 5 7] 2)

; (d)

(defn vskalarprod [v1 v2] (apply + (map * v1 v2)))

(vskalarprod [1 2 3] [-7 8 9])
