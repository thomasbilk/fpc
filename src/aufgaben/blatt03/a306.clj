(ns aufgaben.blatt03.a306)

(defn p [] (p))
(defn test
  [x y]
  (cond
    (= x 0) 0
    :else y))

(test 0 (p))
