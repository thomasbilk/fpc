(ns aufgaben.blatt05.aufgabe01)

(defn sum-rec [n]
  (cond
    (= n 0) 0
    :else (+ n (sum-rec (dec n)))))

(sum-rec 5)
(sum-rec 25)
(sum-rec 1800)

(defn sum-trec [n]
  (loop [cur n, acc 0]
    (if (= cur 0)
      acc
      (recur (dec cur) (+ acc cur)))))

(sum-trec 5)
(sum-trec 25)
(sum-trec 1800)

(defn sum-gauß [n]
  (/ (* n (+ n 1)) 2))

(sum-gauß 5)
(sum-gauß 25)
(sum-gauß 1800)
