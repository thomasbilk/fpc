(ns fpc.a304)

; a
(defn fa
  [x]
  (+ (* 4 x) 2))
(fa 0)
(fa 2)
(fa -2)

; b
(defn fb
  [x]
  (- (+ (* 9 x x x) (* x x) (* 7 x)) 3))
(fb 0)
(fb 2)
(fb -2)

; c
(defn fc
  [x]
  (+ (- (* -3 x x) (* 4 x)) 1))
(fc 0)
(fc 2)
(fc -2)
