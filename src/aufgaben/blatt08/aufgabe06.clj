(ns aufgaben.blatt08.aufgabe06)

(defn fib-seq []
  (map first (iterate (fn [[a b]] [b (+ a b)]) [1 1])))

(take 20 (fib-seq))
