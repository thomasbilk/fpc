; Funktionale Programmierung (in Clojure) Uebungen Serie 5
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.ul05)

stop

; Aufgabe 1

; Berechnen Sie die Summe $\sum_{i=1}^n i$:

; a) durch Rekursion

(defn sum-a [n]
  (if (= 1 n)
    1
    (+ n (sum-a (dec n)))))

(sum-a 100)

; b) durch Endrekursion

(defn sum-b [n]
  (loop [cur n, acc 1]
    (if (= 1 cur)
      acc
      (recur (dec cur) (+ cur acc)))))

(sum-b 100)

; c) und noch ganz anders ohne Schleife

(defn gauss [n]
  (/ (* n (inc n)) 2))

(gauss 100)

; Aufgabe 2 Euklidischer Algorithmus

; Programmieren Sie eine Funktion (gcd n m), die mit dem Euklidischen Algorithmus
; den größten gemeinsamen Teiler der natürlichen Zahlen n und m berechnet.

(defn gcd [n m]
  (if (zero? m)
    n
    (recur m (mod n m))))

(gcd 15 8)

(gcd 3 120)

; Aufgabe 3

; Die Fibonacci-Zahlen sind definiert als
;               0                  falls n = 0
; Fib(n) =      1                  falls n = 1
;              Fib(n-1)+Fib(n-2)  sonst
; Setzen Sie diese Definition um in eine rekursive Prozedur, die Fib(n) berechnet.

(defn fib-r [n]
  (if (<= n 1)
    n
    (+ (fib-r (- n 1)) (fib-r (- n 2)))))

(fib-r 0)

(fib-r 1)

(fib-r 2)

(fib-r 3)

(fib-r 4)

; Formulieren Sie eine iterative Variante nach folgender Idee: 
; Initialisiere a = 1, b = 0.
; Wiederhole gleichzeitig die Transformationen (a = a + b, b = a) n-mal.

(defn fib [n]
  (if (<= n 1)
    n
    (loop [cur 1, a 1, b 0]
			(if (= cur n)
			  a
			  (recur (inc cur) (+ a b) a)))))
			
(fib 0)

(fib 1)

(fib 2)

(fib 3)

(fib 9)

; Aufgabe 4 Primzahltest


;	Schreiben Sie eine Funktion prime?, die für eine positive
;	Zahl n testet, ob sie eine Primzahl ist.

(defn prime? 
  "Ist n eine Primzahl?"
  [n]
  (if (even? n) 
    false
    (loop [i 3]
        (if (> (* i i) n) true
          (if (zero? (mod n i)) false
            (recur (+ i 2)))))))

(prime? 4)
;=> false

(prime? 9)
;=> false

(prime? 7)
;=> true

(prime? 97)
;=> true

(prime? 99991)
;=> true

(prime? 99993)
;=> false

