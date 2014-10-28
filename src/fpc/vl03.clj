; Funktionale Programmierung (in Clojure) Vorlesung 3
; Wahrheitswerte und bedingte Ausdrücke
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.vl03
  (:require [clojure.repl :refer :all]))

stop

(comment
  Wir untersuchen jetzt das Substitutionsmodell etwas genauer
  und werden sehen, dass man beim Auswerten unterschiedliche
  Reihenfolgen verwenden kann
)

; Wir brauchen wieder die Funktionen aus der letzten Vorlesung

(defn square
  "Quadriert x"
  [x]
  (* x x))

(defn sum-of-squares
  "Summiert die Quadrate von x und y"
  [x y]
  (+ (square x) (square y)))

(defn f
  "Summiert das Quadrat von a+1 mit dem Quadrat von 2a"
  [a]
  (sum-of-squares (+ a 1) (* a 2)))

(comment
  Applikative/strikte Auswertung:
  "erst alle Argumente auswerten, dann die Funktion anwenden"
  
  (f 5)
  
  (sum-of-squares (+ 5 1) (* 5 2))
                  
  (sum-of-squares 6 10)
  
  (+ (square 6) (square 10))
     
  (+ (* 6 6) (* 10 10))
  
  (+ 36 100)
  
  136
)

(comment
  Normale/verzögerte Auswertung:
  "erst alles expandieren, dann reduzieren"
  
  (f 5)
  
  (sum-of-squares (+ 5 1) (* 5 2))
  
  (+ (square (+ 5 1)) (square (* 5 2)))
  
  (+ (* (+ 5 1) (+ 5 1)) (* (* 5 2) (* 5 2)))
  
  ; jetzt wird reduziert
  
  (+ (* 6 6) (* 10 10))
  
  (+ 36 100)
  
  136
)  

(comment
  Bemerkungen:
  
  1. Clojure verwendet strikte Auswertung -- es sei denn, wir wollen
     bewusst verzögerte Folgen einsetzen - dazu später mehr
     
  2. Bei verzögerter Auswertung werden eventuell gleiche Ausdrücke mehrfach
     berechnet - siehe oben: (+ 5 1) und (* 5 2) 
     Haskell verwendet verzögerte Auswertung und vermeidet dies!
     
  3. Im Prinzip führen beide Verfahren zu demselben Ergebnis:
     Wenn beide Verfahren terminieren, dann haben sie denselben Wert
     (Satz von Church-Rosser aus dem Lambda-Kalkül)
     
  4. Es kann sein, dass strikte Auswertung nicht terminiert, obwohl
     verzögerte Auswertung dies tun würde.
     Aufgabe 1.5 in SICP
     
  5. Ich bin etwas nachlässig mit den Begriffen applikativ etc.
  
     "Puristen unterscheiden noch feiner das Verhalten auf Funktions– und auf Sprachebene:
      
      - Strikte / nicht strikte Funktion: Da Sprachen denkbar sind, bei denen der Programmierer 
      festlegen kann, ob eine einzelne Funktion ihre Argumente vor dem Betreten des Rumpfes 
      auswertet oder nicht, macht es Sinn von strikten und und nicht strikten Funktionen zu reden.
      
      - Sprache mit normaler / appliktiver Auswertungsstrategie: Liegt das Verhalten dagegen auf 
      Sprachebene fest, dann spricht man gelegentlich von Sprachen mit einer normalen (normal order) 
      oder einer applikativen (applicative oder) Auswertungstrategie. 
      In den Sprachen mit applikativer Auswertungsstartegie sind alle Funktionen strikt und umgekehrt.

      Nicht strikte Auswertung eröffnet die Möglichkeit mit unendlichen Strukturen zu arbeiten. 
      Doch davon später. " (Thomas Letschert, Skript Nichtprozedurale Programmierung S.33)
)
     
;; Bedingte Ausdrücke und Prädikate

; Wir brauchen die Möglichkeit, in unseren Funktionen Alternativen abhängig von bestimmten
; Tests zu formulieren.

(comment
  Beispiel Absolut-Betrag:
  
          x falls x > 0 
  |x| =   0 falls x = 0
         -x falls x < 0
         
  Diese Definition aus der Mathematik können wir unmittelbar übernehmen:
)

(defn abs
  "Absolut-Betrag von x"
  [x]
  (cond
    (> x 0) x
    (= x 0) 0
    (< x 0) (- x)))

(abs 2)
;=> 2

(abs (- 2 2))
;=> 0

(abs -2)
;=> 2

(abs (- 2))
;=> 2

(doc cond)
;-------------------------
;clojure.core/cond
;([& clauses])
;Macro
;  Takes a set of test/expr pairs. It evaluates each test one at a
;  time.  If a test returns logical true, cond evaluates and returns
;  the value of the corresponding expr and doesn't evaluate any of the
;  other tests or exprs. (cond) returns nil.;

(comment
  Bedingte Ausdrücke sind Ausdrücke
  
  In imperativen Sprachen sind switch, if etc Kontrollstrukturen, die
  selbst keinen Wert repräsentieren
  
  In funktionalen Sprachen ist ein bedingter Ausdruck ein Ausdruck,
  der für einen Wert steht.
  
  wie in int a = (x < 0) ? -x : x in Java
)

(comment
  Allgemeine Form (Schablone) von cond:
  
  (cond <pred1> <expr1>
        <pred2> <expr2>
        ...
        <predn> <exprn>)
   
  entspricht dem expr des ersten zutreffenden Prädikats, nil sonst.

  Ein Prädikat ist ein Ausdruck, der einen Wahrheitswert hat.
)  
  
; Andere Schreibweise

(defn abs
  [x]
  (cond (< x  0) (- x)
        :else       x))
  
(abs -3)
;=> 3

(abs 0)
;=> 0

; cond ist ein Makro, das auf if aufbaut

(defn abs
  [x]
  (if (< x 0) (- x) x))

(abs -5)
;=> 5

(abs 5)
;=> 5

(comment
  Struktur (Schablone) von if:
  
  (if <pred> 
    <consequent> 
    <alternative>)
)

(comment
  Wahrheitswerte in Clojure
  
  true  -- alles was nicht false oder nil ist wird in einem if als true betrachtet
           Beispiel: das Keyword :else oben
          
  false -- ist explizit der Wahrheitswert false
  
  nil   -- steht für eine leere Folge (seq)
           wird verwendet für das Abbrechen von Iteration oder Rekursion über Folgen
           (später mehr)
 )          

;; Logische Vergleiche in Clojure

(and true true)
;=> true

(and 1 2)
;=> 2 

; Warum?

(doc and)
;-------------------------
;clojure.core/and
;
;([] [x] [x & next])
;Macro
;  Evaluates exprs one at a time, from left to right. If a form
;  returns logical false (nil or false), and returns that value and
;  doesn't evaluate any of the other expressions, otherwise it returns
;  the value of the last expr. (and) returns true.


(or 1 2)
;=> 1

(doc or)

(not 1)
;=> false

(not false)
;=> true

(not nil)
;=> true


(def x 7)
(and (> x 5) (< x 10))

(comment
  Beispiel des bisher gelernten: Aufgabe 1.3 aus SICP
  
  "Define a procedure that takes three numbers as arguments 
   and returns the sum of the squares of the two larger numbers."
  
  Gemeinsames Erarbeiten in der Vorlesung
  
  Testfälle entwickeln:
  Alle Permutationen von drei verschiedenen Zahlen
  (2 3 4) (2 4 3) (3 2 4) (3 4 2) (4 2 3) (4 3 2)
  Mit zwei gleichen Zahlen
  (2 2 4) (4 4 2)
  Mit drei gleichen Zahlen
  (2 2 2)
)

(defn sum-of-squares-of-two-largest
"Sums the squares of the two larger numbers of three arguments."
  [x y z]
(cond
  (and (>= x z) (>= y z)) (sum-of-squares x y)
  (and (>= x y) (>= z y)) (sum-of-squares x z)
   :else                  (sum-of-squares y z)))
  
; siehe auch Übungen

; Testfälle?
; siehe oben Tests durchführen

(def f sum-of-squares-of-two-largest)

(f 2 3 4)
;=> 25

(f 2 4 3)
(f 3 2 4)
(f 3 4 2)
(f 4 2 3)
(f 4 3 2)

(f 2 2 2)
;=> 8

(f 4 4 2)
;=> 32

(f 2 2 2)
;=> 8

; Andere Implementierung?

(defn s2
  [x y z]
  (let [sorted-vec (sort >= [x y z])]
    (sum-of-squares (first sorted-vec) (second sorted-vec))))

(s2 2 3 4)
;=> 25