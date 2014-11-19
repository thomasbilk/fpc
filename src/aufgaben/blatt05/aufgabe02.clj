(ns aufgaben.blatt05.aufgabe02)

(defn gcd [n m]
  (loop [a n b m]
    (if (= b 0)
      a
      (recur b (mod a b)))))

(defn gcd [n m]
  (if (zero? m)
    n
    (recur m (mod n m))))

(gcd 44 12)
(gcd 872 752)
