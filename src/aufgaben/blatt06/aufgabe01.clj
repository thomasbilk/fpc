(ns aufgaben.blatt06.aufgabe01)

; (a)
(def dec2 (comp dec dec))

(dec2 5)
(dec2 2)


; (b)
(defn sq[x] (* x x))
(def sqplus (comp sq inc))

(sqplus 2)
(sqplus 5)
