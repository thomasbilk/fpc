(ns aufgaben.blatt04.aufgabe01)

(defn minimum
  [a b]
  (cond
    (< a b) a
    :else b))

(minimum 13 5)
(minimum 0 0)
(minimum 11 27)

(doc min)
(min 13 5)
(min 0 0)
(min 11 27)
