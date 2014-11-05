(ns aufgaben.blatt04.aufgabe02)

(defn max-quad
  [a b c]
  (let [sos #(+ (* %1 %1) (* %2 %2))]
  (cond
    (and (>= a c) (>= b c)) (sos a b)
    (and (>= a b) (>= c b)) (sos a c)
    :else (sos b c))))

(max-quad 2 3 4)
(max-quad 2 4 3)
(max-quad 3 2 4)
(max-quad 3 4 2)
(max-quad 4 2 3)
(max-quad 4 3 2)
