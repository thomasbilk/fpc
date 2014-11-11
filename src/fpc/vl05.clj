; Funktionale Programmierung (in Clojure) Vorlesung 5
; Rekursion
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.vl05
  (:require [clojure.repl :refer :all]))

stop

;; Rekursion

(comment
  In einer imperativen Sprache wird man die Fakultät
  durch einen Zähler in einer for-Schleife
  programmieren -- siehe Factorial.java
  
  Eine for-Schleife in dieser Form hat man in einer
  funktionalen Sprache nicht. Stattdessen:
  
  Rekursion
)

(comment
  Wie sieht die Definition der Fakultät aus?
  
        1        falls n = 0
  n! = 
        n (n-1)! falls n > 0

  Diese Definition können wir jetzt einfach abschreiben:
)

(defn fact1
  "Fakultät -- Variante 1"
  [n]
  (if (= n 0)
    1
    (* n (fact1 (- n 1)))))

(fact1 12)
;=> 479001600

(comment
  Wie geht das Prinzip bei Rekursion?
  
  1. Überlege die Abbruchsbedingung
     hier (= n 0) 
     und den Wert
     hier 1
  2. Überlege, wie man zur Abbruchsbedingung kommt
     hier n (n-1)! 
     wichtig ist, dass wir rekursiv n-1 (kleiner!) verwenden
)     

;; Alles wunderbar, aber was passiert hier?

(fact1 50)
; => ArithmeticException integer overflow  clojure.lang.Numbers.throwIntOverflow

;; Anscheinend wird die Zahl zu groß für ein long in Java
;; Also nehmen wir besser BigInteger

(fact1 50N)
;=> 30414093201713378043612608166064768844377641568960512000000000000N

;; Alles klar?

(fact1 10000N)
;=> StackOverflowError   clojure.lang.Numbers.toBigInt

(comment
  Untersuchen wir mal, wir die Berechnung geht:
  
  (fact1 6) ->
  (* 6 (fact1 5)) ->
  (* 6 (* 5 (fact1 4))) ->
  (* 6 (* 5 (* 4 (fact1 3)))) ->
  (* 6 (* 5 (* 4 (* 3 (fact1 2))))) ->
  (* 6 (* 5 (* 4 (* 3 (* 2 (fact1 1)))))) ->
  (* 6 (* 5 (* 4 (* 3 (* 2 (* 1 (fact1 0))))))) ->
  (* 6 (* 5 (* 4 (* 3 (* 2 (* 1 1)))))) ->
  (* 6 (* 5 (* 4 (* 3 (* 2 1))))) ->
  (* 6 (* 5 (* 4 (* 3 2)))) ->
  (* 6 (* 5 (* 4 6))) ->
  (* 6 (* 5 24)) ->
  (* 6 120) ->
  720
  
  Diese Art der Berechnung nennt man "linear rekursiv". 
  
  Bei jedem Schritt wird ein Ergebnis gemerkt und dann die Funktion
  rekursiv aufgerufen -> Folge: der Stack bläht sich auf!
)

;; Geht das auch anders??

(comment
  
  Statt
  (* 6 (* 5 (* 4 (* 3 (* 2 (* 1 1))))))
  
  kann man doch auch umgekehrt klammern:
  
  (* (* (* (* (* (* (* 1 6) 5) 4) 3) 2) 1) 1)
  
  Jetzt sind die vielen Klammern nicht mehr rechts, sondern links!

  Diese Art der Berechnung nennt man "linear iterativ".
  
  Was hat sich sonst noch geändert: wir starten mit (* 1 n)
  Diese 1 nennt man einen Akkumulator!!
  
  Programmieren wir das mal:
)

(defn fact2
  "Fakultät von n"
  ([n] (fact2 n 1))    
  ([n acc]
    (if (= n 0)
      acc
      (fact2 (dec n) (* acc n)))))

(fact2 6)
;=> 720


(comment
  Wie sieht nun die Berechnung aus?
  
  (fact2 6) ->
  (fact2 6 1) ->
  (fact2 5 6) ->
  (fact2 4 30) ->
  (fact2 3 120) ->
  (fact2 2 360) ->
  (fact2 1 720) ->
  (fact2 0 720) ->
  720
  
  Wie man sieht kann jetzt die Berechnung durchgeführt
  werden, ohne dass sich der Stack aufbläht.
  
  Woran liegt das?
  
  Der rekursive Aufruf ist die letzte Anweisung
  der Funktion, er geht nicht mehr in eine weitere 
  Berechnung ein.

  -> Endrekursion  engl. tail recursion
  
)

;; Also jetzt mit fact2 alles klar?

(fact2 10000N)
;=> StackOverflowError   clojure.lang.Numbers.toBigInt

;; Wir brauchen Endrekursion (tail recursion) dies macht aber Clojure nicht automatisch

(defn factorial
  "Fakultät von n"
  ([n] (factorial n 1))
  ([n acc]
    (if (= n 0)
      acc
      (recur (dec n) (* acc n)))))
  
(factorial 6)
;=> 720

(factorial 1000N)
;=> eine sehr große Zahl!!

;; Noch eine andere Variante der Implementierung der Endrekursion
;; Clojure hat ein spezielles Konstrukt für Endrekursion:

(defn factorial
  "Fakultät von n"
  [n]
  (loop [cur n, acc 1]
    (if (= cur 0)
      acc
      (recur (dec cur) (* acc cur)))))

(factorial 6)
;=> 720

(factorial 1000N)
;=> eine sehr große Zahl!!


;; Jetzt erarbeiten wir uns gemeinsam ein Beispiel

(comment
  
  Das folgende Zahlenmuster heißt das Pascal'sche Dreieck
  
                            1
                          1   1
                        1   2   1
                      1   3   3   1
                    1   4   6   4   1
                           ...
                           
  Wir wollen eine Funktion (pascal zeile spalte) schreiben, 
  die die entsprechende Pascal'sche Zahl berechnet, dabei fängt
  die Zählung von Zeile und Spalte jeweilse bei 1 an.
  
  Beispiele
  (pascal 5 1) ;=> 1
  (pascal 5 2) ;=> 4
  (pascal 5 3) ;=> 6 
)

;; Überlegen wir zunächst mal, wie man die Pascal'sche Zahl
;; (pascal zeile spalte) berechnet

(comment
                  0 falls z < 1 oder s > z

  pascal(z s)  =  1 falls z = 1 oder z = s  

                  pascal(z-1, s-1) + pascal(z-1, s)  sonst
                          
)

;; Das kann man nun quasi direkt übernehmen:

(defn pascal1
  [z s]
  "Pascal'sche Zahl an Zeile z und Spalte s."
  (cond
    (or (< z 1) (< z s) (< s 1)) 0
    (or (= s 1) (= s z)) 1
    :else (+ (pascal1 (dec z) (dec s)) (pascal1 (dec z) s))))

;; Beispiele

(pascal1 5 1)
;=> 1

(pascal1 5 2)
;=> 4

(pascal1 5 3)
;=> 6

(pascal1 5 4)
;=> 4

(pascal1 5 5)
;=> 1

(pascal1 5 0)
;=> 0

(pascal1 5 6)
;=> 0


;; Aber: diese Lösung ist nicht endrekursiv.
;; Können wir die Berechnung endrekursiv machen?

(comment
  
  Es gibt einen Zusammenhang zwischen den Pascal'schen Zahlen 
  und den Binomialkoeffizienten n über k:
  
  pascal(1 1) = 0 über 0
  
  pascal(2 1) = 1 über 0
  
  pascal(2 2) = 1 über 1
  
  usw. 
  
  Für die Binomialkoeffizienten gibt es eine
  Formel:
  
    n
  (   ) = Produkt i = 1 bis k  (n - k + i) / i
    k

  Beispiel:

                   4
  pascal (5 3) = (   )  und das ist nach obiger Formel
                   2

  (4-2+1)/1 * (4-2+2)/2 = 3/1 * 4/2 = 3 * 2 = 6                 
  
  oder von hinten her aufziehen:
  
  ((1 * 4)/2 * 3)/1    n und k entsprechend runterzählen
  
  Können wir das verwenden?  
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
