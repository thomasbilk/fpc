(ns aufgaben.blatt04.aufgabe02)

(defn max-quad
  [a b c]
  (cond
    (and (>= a c) (>= b c)) (+ (* a a) (* b b))
    (and (>= a b) (>= c b)) (+ (* a a) (* c c))
    :else (+ (* b b) (* c c))))

(max-quad 2 3 4)
(max-quad 2 4 3)
(max-quad 3 2 4)
(max-quad 3 4 2)
(max-quad 4 2 3)
(max-quad 4 3 2)

