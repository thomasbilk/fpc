; Funktionale Programmierung (in Clojure) Uebungen Serie 3
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.ul03)

stop

;: 1. Funktionen definieren und verwenden

;; (a)
((fn [name] (str "Hallo " name)) "Thomas")

;; (b)
(#(str "Hallo " %) "Thomas")

;; (c)
(def hallo (fn [name] (str "Hallo " name)))

(hallo "Thomas")

;; (d)
(def hallo #(str "Hallo " %))

(hallo "Thomas")

;; (e)
(defn hallo
  "hallo heisst name willkommen."
  [name]
  (str "Hallo " name))

(hallo "Thomas")

;; 2. Lexikalische Bindung

;; (a) Betrachten Sie folgende Schachtelung anonymer Funktionen
((fn [x] ((fn [x] (+ x 1)) (+ x 2))) 13)
;; Das Symbol x kommt zweimal vor. Welches x gehört zu welcher Funktion?

; fn [x]
; __________________________
; |
; | fn [x]
; | ----------------------
; | |
; | | (+ x 1)
; | |
; | ----------------------
; |
; | ; ergibt Wert fuer x
; |
; | (+ x 2)
; |
; --------------------------
((fn [x] ((fn [x] (+ x 1)) (+ x 2))) 1)


;; (b) Wodurch unterscheidet sich die folgende Schachtelung und in welcher
;; Beziehung steht sie zu Aufgabe (a)?
((fn [x] ((fn [y] (+ y 1)) (+ x 2))) 13)

; Der Unterschied liegt in der Benennung der Variable 
; im innersten Block. Die Funktion an sich ist jedoch identisch.
; fn [x]
; ________________________
; |
; | fn [y]
; | --------------------
; | |
; | | (+ y 1)
; | |
; | --------------------
; |
; | ; ergibt Wert fuer y
; |
; | (+ x 2)
; |
; ------------------------


;; 3. Funktionen definieren

;; (a)
(defn square
  "quadriert x"
  [x]
  (*  x x))

(square 2)
;=> 4

(square 5)
;=> 25

;; (b)
(defn sum-of-squares
  "addiert die Summe der Quadrate von x und y"
  [x y]
  (+ (* x x) (* y y )))

(sum-of-squares 2 3)
;= >9

(sum-of-squares 3 3)
;=> 18

;; (c)
(defn eval-test
  "quadriert x und ignoriert y"
  [x y]
  (* x x)) 

(eval-test 4 5)
;=> 16

(eval-test 5 4)
;=> 25

;; 4. Strikte und verzögerte Auswertung

;; (a) (eval-test 2 3)

(a) 
	strikt:
	(eval-test 2 3) ->
	(* 2 2) ->
	4
	also 2 Schritte
	
	verzögert:
	(eval-test 2 3) ->
	(* 2 2) ->
	4
	also auch 2 Schritte


;; (b) (eval-test (+ 3 4) 8)

(b)
	strikt:
	(eval-test (+ 3 4) 8) ->
	(eval-test 7 8) ->
	(* 7 7) ->
	49
	also 3 Schritte

	verzögert:
	(eval-test (+ 3 4) 8) ->
	(* (+ 3 4) (+ 3 4)) ->
	(* 7 (+ 3 4)) ->
	(* 7 7) ->
	49
	also 4 Schritte


;; (c) (eval-test 7 (* 2 4))

(c)
  strikt:
	(eval-test 7 (* 2 4)) ->
	(eval-test 7 8) ->
	(* 7 7) ->
	49
	also 3 Schritte

	verzögert:
	(eval-test 7 (* 2 4)) ->
	(* 7 7) ->
	49
	also 2 Schritte

;; (d) (eval-test (+ 3 4) (* 2 4))

(d)
	strikt:
	(eval-test (+ 3 4) (* 2 4)) ->
	(eval-test 7 (* 2 4)) ->
	(eval-test 7 8) ->
	(* 7 7) ->
	49
	also 4 Schritte

	verzögert:
	(eval-test (+ 3 4) (* 2 4)) ->
	(* (+ 3 4) (+ 3 4)) ->
	(* 7 (+ 3 4)) ->
	(* 7 7) ->
	49
	also auch 4 Schritte

;; 5. Polynomiale Funktionen
 
;; (a) f(x) = 4 x + 2
 
(defn fa [x]
  (+ (* 4 x) 2))

(map fa [0 2 -2])
;=> (2 10 -6)
; => -6

;; (b) f(x) = 9 x^3 + x^2 + 7 x - 3
	
(defn fb [x]
  (+ (* 9 x x x) (* x x) (* 7 x) -3))

(map fb [0 2 -2])
;=> (-3 87 -85)

(fb 1)

;; (c) f(x) = -3 x^2 -4 x + 1		

(defn fc [x]
  (+ (* -3 x x) (* -4 x) 1))

(map fc [0 2 -2])
;=> (1 -19 -3)

;; Aufgabe 6 (SICP Exercise 1.5)

(defn loop [] (loop))

(defn test [x y]
  (if (= x 0) 0 y))

(test 1 2)
;=> 2

(test 0 1)
;=> 0

(test 0 (loop))
;=> StackOverflowError   fpc.ul03/loop 


; In strikter Auswertung führt loop zu einer unendlichen Schleife,
; die endet, weil der Stack überläuft: jeder neue Aufruf braucht einen Stack frame
