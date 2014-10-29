(ns fpc.a302)

; a
(defn square 
  "squares the parameter"
  [n]
  (* n n))
(square 6)
(square -5)
(square 0)

; b
(defn sum-of-squares 
  "summs the squares of both parameters"
  [a b]
  (+ (* a a) (* b b)))
(sum-of-squares 3 3)
(sum-of-squares 4 5)
(sum-of-squares -2 2)

; c
(defn eval-test
  "tests the evaluation of the parameters"
  [a b]
  (* a a))
(eval-test 2 4)
(eval-test (+ 3 3) (+ 4 4))
