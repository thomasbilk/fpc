(ns aufgaben.blatt08.aufgabe06)

(defn fib [n]
  (loop [cur n a 0N b 1N]
    (if (= cur 0) a
      (recur (dec cur) b (+ a b)))))
