(ns aufgaben.blatt05.aufgabe02)

(defn gcd [n m]
  (loop [a n b m]
    (if (= b 0)
      a
      (recur b (mod a b)))))

(gcd 44 12)
(gcd 872 752)
