(ns aufgaben.blatt05.aufgabe04)

(defn is-prime [n]
  (loop [n n d 2]
    (cond
      (= n d) "true"
      (= (mod n d) 0) "false"
      :else (recur n (inc d)))))

(is-prime 5)
(is-prime 13)
(is-prime 12)
(is-prime 99991)
(is-prime 99993)
