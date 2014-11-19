(ns aufgaben.blatt04.aufgabe04)

(defn mitternacht
  [a b c]
  (let [calc (fn [op] (/ (op (- b) (Math/sqrt (- (* b b) (* 4 a c)))) (* 2 a)))]
    (hash-set (calc +) (calc -))))

(mitternacht 4 -12 -40)
(mitternacht 1 2 -35)
(mitternacht 1 -4 4)
(mitternacht 1 12 37)

(defn mitternachlösungen [a b c]
  (let [result (mitternacht a b c)]
    (if (Double/isNaN (first result))
      0
      (count result))))

(mitternachlösungen 4 -12 -40)
(mitternachlösungen 1 -4 4)
(mitternachlösungen 1 12 37)
