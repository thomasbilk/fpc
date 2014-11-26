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
  (cond
    (= 2 n) true
    (even? n) false
    :else (loop [i 3]
            (if (> (* i i) n) true
              (if (zero? (mod n i)) false
                (recur (+ i 2)))))))

(defn prime?
  [n]
  (cond 
    (= 2 n) true
    (even? n) false
    :else
    (let [von 3
              bis (inc (Math/sqrt n))
              pred #(zero? (mod n %))]
          (empty? (filter pred (range von bis 2))))))

; besser
(defn prime?
  [n]
  (cond
    (= n 2) true
    (zero? (mod n 2)) false
    :else  (let [von 3
                 bis (inc (Math/sqrt n))
                 pred (fn [i] (pos? (mod n i)))
                 ;pred (fn [i] (if (> i 4) (/ i 0) (pos? (mod n i))))
                 ]
             (empty? (drop-while pred (range von bis 2))))))

; bei dieser Lösung wird das Prädikat nur solange ausgewertet
; bis der erste Teiler gefunden wird.

; Man sieht das daran, wenn man das Prädikat so ändert:
; wie oben als auskommentierte Variante
; dann entsteht bei 99993 keine Exception, weil 3 als Teiler gefunden wird

(prime? 2)

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

; Aufgabe 6 Pascal'sche Zahlen
; Rekursive Funktion (pascal row index) zur Berechnung
; der Pascalschen Zahl

; In der Vorlesung vl05 möglicherwiese bereits gemeinsam
; erarbeitet

(defn pascal
  "Value in Pascal's triangle in row r at index i (counting beginning with 1)."
  [r i]
  {:pre [(integer? r) (integer? i) (<= 1 i r)]}
  (if (or (= i 1) (= i r))
    1
    (+ (pascal (dec r) (dec i)) (pascal (dec r) i))))


(pascal 1 1)
;=> 1

(pascal 0 1)
;=> AssertionError Assert failed: (<= 1 i r)

(pascal 2 1)
;=>  1

(pascal 2 2)
;=> 1

(pascal 2 3)
;=> AssertionError Assert failed: (<= 1 i r)

(pascal 3 1)
;=> 1

(pascal 3 2)
;=> 2

(pascal 3 3)
;=> 1

(pascal 4 2)
;=> 3

(pascal 5 3)
;=> 6

;; Endrekursive Variante
(comment
  Für die Binomialkoeffizienten gibt es eine Formel:

    n
  (   ) = Produkt i = 1 bis k  (n - k + i) / i
    k

  Beispiel:

                   4
  pascal (5 3) = (   )  und das ist nach obiger Formel
                   2

;  (4-2+1)/1 * (4-2+2)/2 = 3/1 * 4/2 = 3 * 2 = 6

  oder von hinten her aufziehen:

;  ((1 * 4)/2 * 3)/1    n und k entsprechend runterzählen

  Das können wir verwenden:
)

(defn pascal
  [z s]
  "Endrekursive Variante für die Berechnung der Pascal'schen Zahl
   an Zeile z und Spalte s."
   (let [n (dec z) k (dec s)]
     (loop [current-n n current-k k acc 1]
       (cond
         (or (< z 1) (< z s) (< s 1)) 0
         (or (= current-n 0) (= current-k 0) (= current-k current-n)) acc
         :else (recur (dec current-n) (dec current-k) (/ (* acc current-n) current-k))))))

(pascal 1 1)
;=> 1

(pascal 5 1)
;=> 1

(pascal 5 2)
;=> 4

(pascal 5 3)
;=> 6

(pascal 5 4)
;=> 4

(pascal 5 5)
;=> 1

(pascal 5 0)
;=> 0

(pascal 5 6)
;=> 0

