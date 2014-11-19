(ns aufgaben.blatt05.aufgabe04)

(defn is-prime [n]
  (loop [actn n d 2]
    (cond
      (= actn d) true
      (= (mod actn d) 0) false
      :else (recur actn (inc d)))))

(is-prime 5)
(is-prime 13)
(is-prime 12)
(is-prime 99991)
(is-prime 99993)

(defn prime? [n]
  (cond
    (= 2 n) true
    (even? n) false
    :else (let [von 3
                bis (inc (Math/sqrt n))
                pred #(zero? (mod n %))]
            (empty? (filter pred (range von bis 2))))))

(prime? 99991)
(prime? 99993)
