(ns aufgaben.blatt08.aufgabe05)

(defn euler1 [n]
  (apply + (filter #(or (= (mod % 3) 0) (= (mod % 5) 0)) (range 1 n))))

(euler1 10)
(euler1 100)
(euler1 1000)
