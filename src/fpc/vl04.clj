; Funktionale Programmierung (in Clojure) Vorlesung 4
; Beispiel: Quadratwurzeln mit dem Newton-Verfahren
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.vl04
  (:require [clojure.repl :refer :all]))

stop

;; Newton-Verfahren zur Bestimmung der Quadratwurzel

(comment
  Das Newton-Verfahren zur Bestimmung der Quadratwurzel
  
  Näherungsverfahren, das schrittweise eine präzisere
  Lösung ermittelt
  
  Die Idee besteht darin, dass man eine Nullstelle der
  Funktion x^2 - a sucht. Diese Nullstelle ist dann sqrt(a).
  
  Man startet mit einem Wert in der Nähe der Nullstelle.
  Wir nehmen 1.
  
  Nun hat man die Tangente an dieser Stelle -- deren Nullstelle 
  liegt dann näher an der gesuchten Nullstelle:

  x_{n+1} = x_n - f(x_n)/f'(x_n)

  Machen wir das mal für a = 2:  

  x_0 = 1,                     f(x_0) = -1,     f'(x_0) = 2
  
  x_1 = 1 - -1/2 = 1.5,        f(x_1) = 0.25,   f'(x_1) = 3
  
  x_2 = 1.5 - 0.25/3 = 1.4167, f(x_2) = 0.0069, f'(x_2) = 2.833
  
  x_3 = 1.4167 - 0.0069/2.833
      = 1.4142
)

;; forward declaration
(declare good-enough? improve average)

(comment
  Wir entwickeln die Funktion indem wir
  das Vorgehen des Newton-Verfahrens einfach
  in Clojure aufschreiben. Dabei tun wir einfach so,
  als ob wir die Funktionen für die Teilschritte schon hätten:
)

(defn sqrt-iter
  [guess a]
  (if (good-enough? guess a)
    guess
    (sqrt-iter (improve guess a) a)))

(comment
  Nun überlegen wir, wie wir die Teilfunktionen
  programmieren können:

  Wie ergibt sich x_{n+1} aus x_n?
  
  x_{n+1} = x_n - (x_n^2 - a)/2x_n = (x_n^2 + a)/2x_n = (x_n + a/x_n)/2
)  

(defn improve
  [guess a]
  (average guess (/ a guess)))

(defn average
  [x y]
  (/ (+ x y) 2))

(defn abs
  "Absolutbetrag von x"
  [x]
  (if (neg? x) (- x) x))

(defn square
  "Quadrat von x"
  [x]
  (* x x))

(defn good-enough?
  [guess a]
  (< (abs (- (square guess) a)) 0.001))

;; Jetzt die Hauptfunktion:

(defn sqrt
  "Quadratwurzel von a"
  [a]
  (sqrt-iter 1.0 a))
  

(sqrt 9)
;=> 3.00009155413138

(sqrt 2)
;=> 1.4142156862745097

(sqrt 16)

(comment
  Frage:
  Die Funktion, wie sie jetzt programmiert ist,
  funktioniert nicht für sehr kleine und sehr große
  Zahlen.
)

(sqrt 0.0001)
;=> 0.03230844833048122

(java.lang.Math/sqrt 0.0001)
;=> 0.01

(sqrt 1.0e50)
;=> StackOverflowError

(comment
  
  Warum? Was muss man ändern?
  
  (siehe SICP Aufgabe 1.7)
  
  Lösung:
  Die Funktion good-enough? ist absolut. 
  
  Wenn a sehr klein ist,
  dann ergibt z.B. (good-enough? 0.001 0.0000001) true, d.h.
  0.001 ginge als Wurzel aus 0.0000001 durch.
  
  Wenn a sehr groß ist, dann wird die Präzision von Doubles
  immer schlechter, schließlich so, dass der Unterschied von 0.001
  gar nicht mehr festgestellt werden kann. In diesem Fall würde
  also eine unendliche Schleife entstehen.
  
  Was muss man tun?
  Die Prüfung relativ machen:
)


(defn good-enough?
  [guess a]
  (< (/ (abs (- (square guess) a)) a) 0.001))

(sqrt 0.0001)
;=> 0.010000714038711746

(sqrt 1.0e50)
;=> 1.0000003807575104E25


;; Neues Thema: Lokale Werte und Funktionen

(comment
  Wie sieht die Aufrufstruktur von sqrt aus?
  
                       sqrt    --
                        |     |  |
                     sqrt-iter --
                     /     \
             good-enough?  improve
                  / \          |
            square  abs       average
            
   Eigentlich ist für den Verwender von sqrt dieser interne Aufbau
   nicht von Belang: prozedurale Abstraktion - versteckt Interna
   gegenüber dem Verwender.

   Tun wir das:
 )           

(comment
  Lokale Namen
  
  Parameter sind für eine Funktion lokal, d.h.
  
  (defn square [x] (* x x))
  
  und
  
  (defn square [y] (* y y))
  
  sind identisch. 
  
  Parameter sind gebundene Variablen, ihre Namen sind deshalb ohne
  Belang und austauschbar -> alpha-Konversion des Lambda-Kalküls

  Nebenbei: Substitutionsmodell -> beta-Reduktion des Lambda-Kalküls
)

(comment
  Freie Variablen
  
  sind nicht gebunden.
  
  Beispiel: In good-enough? sind
  
  guess, a gebundene Variablen,
  <, -, /, abs, square freie Variablen
  
  D.h. good-enough? ist abhängig von der Bedeutung der freien Variablen
)

;; Blockstruktur

;; Wir wollen nun die Interna von sqrt vor dem Verwender verstecken

(defn sqrt
  "Quadratwurzel von a"
  [a]
  (let [good-enough? (fn [guess a] (< (/ (abs (- (square guess) a)) a) 0.001))
        improve      (fn [guess a] (average guess (/ a guess)))
        sqrt-iter    (fn sqrt-iter [guess a] 
                       (if (good-enough? guess a)
                         guess
                         (sqrt-iter (improve guess a) a)))]
    (sqrt-iter 1.0 a)))

(sqrt 9) 

(sqrt 2)                         

(sqrt 0.0001)

(sqrt 1E60)

(comment
  Hinweis:
  In Scheme kann man hier define statt let verwenden. Define innerhalb eines
  defines definiert in Scheme eine lokale Funktion, in Clojure ist das nicht so:
  def erzeugt _immer_ eine Var in der globalen Umgebung - deshalb muss man hier
  let nehmen!
)  

(def i 1)
i

(defn my-func
  []
  (def i 2)
  i)
(my-func)
i

(def i 1)
i
(defn my-func'
  []
  (let  [i 2]
    i))

(my-func')
i

(comment
  Man beobachte, wie in den Funktionen der Parameter a die Variable a von sqrt
  "überschattet".
  
  Deshalb ist es nicht notwendig, diesen Parameter explizit zu verwenden,
  man kann in den inneren Funktionen auf die Variable aus der Umgebung zugreifen.
  
  Das nennt man "lexical scoping": Bei der Definition von z.B. improve können wir
  Variablen aus der Umgebung verwenden - sie sind dann frei in der Funktionsdefinition
  und werden beim Erzeugen der Funktion gebunden mit der Variablen der Umgebung.
  
  Dies nennt man in der Lisp-Gemeinde auch "Closure" -- der Begriff wird in SICP
  nicht verwendet, weil er etwas ganz anderes bedeutet als der Abschluss (Closure) im
  Sinne der Mathematik -- siehe Fußnote 6 SICP p.98
 ) 

(defn sqrt
  "Quadratwurzel von a"
  [a]
  (let [good-enough? (fn [guess] (< (/ (abs (- (square guess) a)) a) 0.001))
        improve      (fn [guess] (average guess (/ a guess)))
        sqrt-iter    (fn sqrt-iter [guess] 
                       (if (good-enough? guess)
                         guess
                         (sqrt-iter (improve guess))))]
    (sqrt-iter 1.0)))

(sqrt 9) 

(sqrt 2)                         
  
  