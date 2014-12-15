(ns aufgaben.blatt08.aufgabe03)

(defn sumfunc [f n]
  (reduce f (range (inc n))))

(defn sumadd [n] (sumfunc + n))

(sumadd 10)
(sumadd 100)
