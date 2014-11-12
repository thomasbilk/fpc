; Funktionale Programmierung (in Clojure) Uebungen Serie 4
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.ul04
  (:require [clojure.repl :refer :all]))

stop

; Aufgabe 1

; (a) Schreiben Sie eine Funktion \verb=minimum=, die als Ergebnis
; die kleiner der beiden Zahlen hat. (Verwenden Sie dabei nicht die Clojure-Funktion \verb=min=.)
; [Klaeren/Sperber Aufgabe 3.1]


(defn minimum
  "Minimum von a1 und a2"
  [a1 a2]
  (if (< a1 a2)
    a1
    a2))

; (b) Vergleichen Sie ihre Funktion mit der Clojure-Funktion \verb=min=.

(doc min)

;=> 
clojure.core/min
([x] [x y] [x y & more])
  Returns the least of the nums.

; Aufgabe 2

; Schreiben Sie eine Funktion \verb=max-quad=, die drei Zahlen als Argumente
; akzeptiert und die Summe der Quadrate der beiden größeren
; Zahlen zurückgibt.
; [Klaeren/Sperber Aufgabe 3.2]

(defn square [x] (* x x))

(defn max-quad
  "Summiert die Quadrate der beiden größeren Argumente"
  [a1 a2 a3]
  (cond
    (and (>= a1 a3) (>= a2 a3)) (+ (square a1) (square a2)) 
    (and (>= a1 a2) (>= a3 a2)) (+ (square a1) (square a3)) 
    (and (>= a2 a1) (>= a3 a1)) (+ (square a2) (square a3)) ))

(max-quad 1 1 1)

(max-quad 2 1 1)

(max-quad 2 1 3)

(defn max-quad1
  "Summiert die Quadrate der beiden größeren Argumente"
  [a1 a2 a3]
  (cond
    (and (>= a1 a3) (>= a2 a3)) (+ (square a1) (square a2)) 
    (and (>= a1 a2) (>= a3 a2)) (+ (square a1) (square a3)) 
    :else                       (+ (square a2) (square a3)) ))

(max-quad1 2 1 3)

(defn max-quad3
  [x y z]
  (let [sum-of-squares (fn [a b] (+ (square a) (square b)))]
    (apply sum-of-squares (drop-last (sort > [x y z])))))

(max-quad3 2 1 3)

(defn max-quad5
  [x y z]
  (let [sorted-vec (sort >= [x y z])
        s-o-s #(+ (square %1) (square %2))]
    (s-o-s (first sorted-vec) (second sorted-vec))))

(max-quad5 2 1 3)

(defn max-quad4
  [x y z]
  (let [[v1 v2] (sort >= [x y z])
        s-o-s #(+ (square %1) (square %2))]
    (s-o-s v1 v2)))

(max-quad4 2 1 3)
; siehe auch Vorlesung 3


; Aufgabe 3

; (a)
; Ein Tagesgeldkonto wirft 2% Zinsen bei einem Kontostand unter 3000 Euro
; ab und darüber 3%. Schreiben Sie eine Funktion, die abhängig vom
; Kontostand den Zinssatz zurückgibt.

(defn zinssatz
  [saldo]
  (if (< saldo 3000)
    0.02
    0.03))

(zinssatz 2999)

(zinssatz 3000)

; (b)
; Schreiben Sie darauf aufbauend eine Funktion, die den Zinsbetrag 
; bei jährlicher Zinszahlung ermittelt.

(defn zinsbetrag
  [saldo]
  (* saldo (zinssatz saldo)))

(zinsbetrag 1000)
;=> 20.0

(zinsbetrag 4000)
;=> 120.0


; Aufgabe 4

; (a) Schreiben Sie eine Funktion, die die reellen Lösungen der quadratischen Gleichung
; \[ a x^2 + b x + c = 0 \] ermittelt.
; (Sie können in Clojure die Lösungen als Vektor zurückgeben.)

(defn m-formel
  [a b c]
  (let [diskriminante (- (square b) (* 4 a c))]
    (if (neg? diskriminante)
      nil
      (let [wurzel (Math/sqrt diskriminante)
            nenner (* 2 a)]
        [(/ (+ (- b) wurzel) nenner) (/ (- (- b) wurzel) nenner)]))))


(m-formel 4 -12 -40)
;=> [5.0 -2.0]

(m-formel 1 1 1)
;=> nil

(m-formel 1 -4 4)
;=> [2.0 2.0]
  

; (b) Schreiben Sie eine Funktion, die die Zahl der reellen Lösungen 
; der "Mitternachtsformel" aus (a) ermittelt.

(defn zahl-loesungen
  [a b c]
  (let [diskriminante (- (square b) (* 4 a c))]
    (cond
      (zero? diskriminante) 1
      (pos?  diskriminante) 2
      (neg?  diskriminante) 0)))

(zahl-loesungen 4 -12 -40)
;=> 2

(zahl-loesungen 1 1 1)
;=> 0

(zahl-loesungen 1 -4 4)
;=> 1

; Man kann natürlich auch die Funktion aus (a) nehmen und das Ergebnis auswerten.