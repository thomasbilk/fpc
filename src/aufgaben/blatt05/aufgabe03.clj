(ns aufgaben.blatt05.aufgabe03)

(defn fib1 [n]
  (cond
    (= n 0) 0
    (= n 1) 1
    :else (+ (fib1 (dec n)) (fib1 (- n 2)))))

(fib1 3)
(fib1 16)

(defn fib2 [n]
  (loop [cur n a 0N b 1N]
    (if (= cur 0) a
        (recur (dec cur) b (+ a b)))))

(fib2 3)
(fib2 16)
(fib2 42)
(fib2 70)
(fib2 100)
