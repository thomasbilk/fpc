(ns aufgaben.blatt05.aufgabe03)

(defn fib1 [n]
  (cond
    (= n 0) 0
    (= n 1) 1
    :else (+ (fib1 (dec n)) (fib1 (- n 2)))))

(fib1 3)
(fib1 16)

(defn fib2 [n]
  (loop [actn n, a 0, b 1]
    (if (= actn 0)
      a
      (recur (dec actn) b (+ a b)))))

(fib2 3)
(fib2 16)
(fib2 42)
(fib2 70M)
