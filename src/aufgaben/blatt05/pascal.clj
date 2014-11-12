(ns aufgaben.blatt05.pascal)

(defn pascal1 [z,s]
  (cond
    (or (< z 1) (< s 1) (< z s)) 0
    (or (= z 1) (= z s)) 1
    :else (+ (pascal1 (dec z) (dec s)) (pascal1 (dec z) s))))

(pascal1 1 1)
(pascal1 4 3)
(pascal1 5 3)
(pascal1 8 4)
(pascal1 46 27)

(defn pascal2 [z,s]
  (let [n (dec z), k (dec s)]
  (loop [cn n, ck k, acc 1]
    (cond
      (or (< z 1) (< z s) (< s 1)) 0
      (or (= cn 0) (= ck 0) (= ck cn)) acc
      :else (recur (dec cn) (dec ck) (/ (* acc cn) ck))))))

(pascal2 8 4)
(pascal2 46 27)
(pascal2 100 50)
