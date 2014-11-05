(ns aufgaben.blatt03.a304)

; a
(defn fa
  [x]
  (+ (* 4 x) 2))
(map fa [0 2 -2])

; b
(defn fb
  [x]
  (- (+ (* 9 x x x) (* x x) (* 7 x)) 3))
(map fb [0 2 -2])

; c
(defn fc
  [x]
  (+ (- (* -3 x x) (* 4 x)) 1))
(map fc [0 2 -2])
