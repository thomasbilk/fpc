; Funktionale Programmierung (in Clojure) Uebungen Serie 2
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.ul02)

; Aufgabe 1 und 2

; (a)
; Addition ist linksassoziativ
(+ (+ 2 3) 1)

(+ 2 3 1)

; (b)

(+ (* 2 3) 4)

; (c)

(* 2 (+ 3 4))

; (d) 
; and bindet stärker
(or (and true false) true)

; (e)

(defn square [x] (* x x))

(- (square 7) 7)

; (f)

(or (= 1 1) (not= 1 1))

; (g)

(* (+ 3 4) (+ 2 5))

; Aufgabe 3

10
; => 10

(+ 5 3 4)
; => 12

(- 9 1)
; => 8

(/ 6 2)
; => 3

(/ 7 2)
;= 7/2

(/ 8 6)
;=> 4/3

(+ (* 2 4) (- 4 6))
;=> 6

(def a 3)
;=> #'fpc.ul02/a

(def b (+ a 1))
;=> #'fpc.ul02/b

(+ a b (* a b))
;=> 19

(= a b)
;=> false

(if (and (> b a) (< b (* a b))) 
  b 
  a)
;=> 4

(cond
  (= a 4)  6
  (= b 6)  (+ 6 7 a)
  :else    25)
;=> 25

; Aufgabe 4
; (a)

'(+ 2 3)

;=> (+ 2 3)

(quote (+ 2 3))
;=> (+ 2 3)

(list + 2 3)
;=> (#<core$_PLUS_ clojure.core$_PLUS_@8d2085> 2 3)
; Woher kommt der Unterschied?

; (b)

(eval '(+ 2 3))
;=> 5

(eval (list + 2 3))
;=> 5
