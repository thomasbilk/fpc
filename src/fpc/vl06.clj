; Funktionale Programmierung (in Clojure) Vorlesung 6
; Funktionen höherer Ordnung
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.vl06
  (:import (javax.lang.model.util Elements))
  (:require [clojure.repl :refer :all]))

stop

;; Thema: Funktionen höherer Ordnung

(comment

  In funktionalen Sprachen kann man Funktionen wie Werte verwenden,
  Funktionen sind "Bürger erster Klasse".
  
  Man spricht von Funktionen höherer Ordnung:
  
  - Funktionen, die Funktionen als Argumente haben
  - Funktionen, die Funktionen als Ergebnis liefern
  - Funktionen können in Datenstrukturen vorkommen
  
  dies wollen wir jetzt näher untersuchen
 )

; Eine Funktion, die die Summe eines Bereichs von Zahlen berechnet:

(defn sum-integers
  "Summiert die ganzen Zahlen im Intervall [a b]"
  [a b]
  (if (> a b)
    0
    (+ a (sum-integers (inc a) b))))

(sum-integers 1 10)
;=> 55

; Eine Funktion, die die Summe der Kubikzahlen eines Bereichs berechnet:

; Wir brauchen erst eine Funktion, die x^3 berechnet:
(defn cube
  [x]
  "x^3"
  (* x x x))

(cube 2)
;=> 8

; Nun die Summe der Kubikzahlen
(defn sum-cubes
  "Summiert die Kubikzahlen der ganzen Zahlen in [a b]"
  [a b]
  (if (> a b)
    0
    (+ (cube a) (sum-cubes (inc a) b))))
  

(sum-cubes 1 10)
;=> 3025

; Noch eine Funktion:
; 1/1*3 + 1/5*7 + 1/9*11 + ... konvergiert nach pi/8

(defn pi-sum
  "Summiert 1/x * (x+2) für x im Bereich [a b]"
  [a b]
  (if (> a b)
    0
    (+ (/ 1.0 (* a (+ a 2))) (pi-sum (+ a 4) b))))
  

(* 8 (pi-sum 1 100))
;=> 3.1215946525910105 

(comment
  
  Man sieht dass alle drei Funktionen dasselbe Muster haben:
  
  (defn <name>
    [a b]
    (if (> a b)
      0
      (+ (<term> a) (<name> (<next> a) b))))
  
  entspricht mathematisch
  
  sum_{n=a}^b f(n), also
  f(a) + f(a+1) + ... + f(b)

  und genau hier kann man ansetzen:
 )


;; Verallgemeinerung der Beispiele

(defn sum
  [term a next b]
  (if (> a b)
    0
    (+ (term a) (sum term (next a) next b)))) 
    

; sum-integers
(defn sum-integers
  [a b] (sum identity a inc b))

(sum-integers 1 10)
;=> 55

; sum-cubes
(defn sum-cubes
  [a b] (sum cube a inc b))
  

(sum-cubes 1 10)
;=> 3025

(defn pi-sum
  [a b]
  (let [pi-term (fn [x] (/ 1.0 (* x (+ x 2))))
        pi-next (fn [x] (+ x 4))]
    (sum pi-term a pi-next b)))

(* 8 (pi-sum 1 100))
;=> 3.1215946525910105

(comment
 
  Diskussion: welchem aus der Objektorientierung bekannten Muster
  entspricht dieses Vorgehen?

  Wir verwenden eine Funktion, der wir die Strategie der Berechnung
  der Terme der Summation und der Berechnung des nächsten Elements
  übergeben.

  "Define a family of algorithms, encapsulate each one, and make them
  interchangeable. Strategy lets the algorithm vary independently from
  clients that use it." (Gamma, Helm, Johnson, Vlissides: Design Patterns)
  
)  


;; Currying und partial on Clojure

(comment
  
  Wenn man eine Funktion f(x, y) mit zwei Argumenten hat, dann
  kann man die Funktion aufrufen, indem man nur 1 Argument angibt,
  das an die Stelle von x tritt und jetzt ein Funktion zurückgibt,
  die dann nur noch ein Argument hat.
  
  Beispiel:
  f(x, y) = x + y
  
  f(x, y) (2) = (2 + y) ergibt die Funktion g(y) = 2 + y
  
  Diese Technik nennt man Currying, nach Haskell Brooks Curry 1900 - 1982.
  Ursprünglich stammt sie von Moses Schönfinkel 1989 - 1942 -- man müsste
  als eigentlich von "Schönfinkeln" sprechen.
  
  Funktionale Sprachen kennen diese Technik.
  
  Sehen wir mal, wie es sich in Clojure verhält:
)

(defn adder
  [x y]
  "addiert x und y"
  (+ x y))

(adder 2)
;=> ArityException Wrong number of args (1) passed to: vl06/adder

; Currying auf direkte Weise geht nicht. Das liegt daran, dass
; Clojure variadische Funktionen kennt - die also verschiedene
; Zahl von Argumenten haben können.

; Trotzdem kann man aus einer Funktion mit 2 Parametern eine Funktion mit
; 1 Parameter erzeugen:

(def add-two (partial adder 2))

(add-two 5)
;=> 7

(defn suber
  [x y]
  "subtrahiert y von x"
  (- x y))

(def sub-from (partial suber 5))

(sub-from 2)
;=> 3


;; Dies ist eines der Beispiele von Funktionen in Clojure, die
;; Funktionen erzeugen. Weiter von dieser Sorte sind

;; comp Komposition von Funktionen

;  Angenommen f(x) = x + 1 und g(x) = x^2 --

;  was ergibt dann die Komposition
  
;     (g ○ f)(x) = g(f(x)) = (x + 1)^2  spreche "g nach f"


(defn square [x] (* x x))

(def g-f (comp square inc))


(g-f 2)
;=> 9

; andersrum
((comp inc square) 2)
;=> 5

(g-f 6)
;= 49

((comp inc square) 6)
;=> 37

;; constantly

(comment
  
  (constantly x) ergibt eine Funktion, die stets x zurückgibt
  
)

(def func4 (constantly 4))

(func4)
;=> 4

(func4 5)
;=> 4

(func4 2 3 4 5 6)
;=> 4


;; complement

(comment
  
  (complement f) ergibt eine Funktion, die den umgekehrten
  Wahrheitswert liefert wie f
)

(pos? 2)
;=> true

((complement pos?) 2)
;=> false

;; juxt
;; juxtaposition = Nebeneinanderstellung

(comment
  
  (juxt f g h ...) ergibt eine Funktion, die f g h auf Argument(e)
  anwendet und einen Vektor mit dem Ergebnis zurückliefert
)  

(def eins-zwei-drei (juxt inc #(+ 2 %) #(+ 3 %)))

; Was macht diese Funktion??

(eins-zwei-drei 2)
;=> [3 4 5]

(eins-zwei-drei 10)
;=> [11 12 13]

(comment
  Soweit zu Funktionen höherer Ordnung. 
  Es gibt dazu viel mehr zu sagen.
  Das wird aber erst so richtig spannend, wenn wir uns Datentypen
  und insbesondere Datenstrukturen in Clojure angesehen haben.
)