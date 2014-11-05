(ns aufgaben.blatt04.aufgabe04)

(defn mitternacht
  [a b c]
  (let [calc (fn [op] (/ (op (- b) (Math/sqrt (- (* b b) (* 4 a c)))) (* 2 a)))]
    (vector (calc +) (calc -))))

(mitternacht 4 -12 -40)
(mitternacht 1 2 -35)
(mitternacht 1 -4 4)
(mitternacht 1 12 37)
