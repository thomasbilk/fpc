(ns aufgaben.blatt08.aufgabe01)

(defn palindrome? [inp]
  (= (reverse inp) (seq inp)))

(palindrome? "otto")
(palindrome? "udo")
