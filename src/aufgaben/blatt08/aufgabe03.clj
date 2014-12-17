(ns aufgaben.blatt08.aufgabe03)

(defn sumfunc [f n]
  (reduce + (map f (range (inc n)))))

(defn sum [n] (sumfunc identity n))

(sum 10)
(sum 100)
(sum 555)
