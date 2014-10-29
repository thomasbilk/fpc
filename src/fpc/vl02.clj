; Funktionale Programmierung (in Clojure) Vorlesung 2
; Funktionen und ihre Auswertung
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.vl02
  (:require [clojure.repl :refer :all]))

stop

(comment
  
- Die funktionale Programmiersprache, die wir verwenden werden ist Clojure
- Clojure ist ein Lisp-Dialekt
- Clojure verwendet die Java Virtual Machine

- Lisp kommt von List Processing
- John McCarthy (1927 - 2011): 
  "Recursive Functions of Symbolic Expressions and 
   Their Computation by Machine" (1960)
)

(comment
  
- Lisp verwendet Präfix-Syntax

- Folgender Ausdruck ist in Infix-Syntax, die wir alle kennen:
  (2 + 3) * square(7)
  
- Wie sieht der (abstrakte) Syntaxbaum dieses Ausdrucks aus?
  -> Tafel
  
- Wie kann man diesen Baum "linearisieren"?
  -> Tafel
  (* (+ 2 3) (square 7))

  vorausgesetzt wir haben die Funktion square definiert:
)                
  
(defn square
  "Quadriert x"
  [x]
  (* x x))

(* (+ 2 3) (square 7))
; => 245

(comment
  Grundlegende Mechanismen einer Programmiersprache:
  
  - Einfache (primitive) Ausdrücke
  - Mittel solche Ausdrücke zu kombinieren
  - Abstraktion = solche Kombinationen mit Namen zu versehen und
    als Einheit verwenden zu können.
    
  Wir verwenden Funktionen und Daten, dementsprechend werden wir
  ansehen:
  
  - wie manipuliert man Daten mit Funktionen?
  - wie definiert man Funktionen?
  - wie kombiniert man Funktionen?
  
  - welche einfachen Datentypen kennt Clojure?
  - welche zusammengesetzten Datentypen kennt Clojure?
  - wie erzeugen und verwenden wir anwendungsspezifische Daten?
)

(comment
  Die REPL
  
  Read-Evaluate-Print Loop
  
  Read     -- liest Quellcode (forms) und parst ihn zu Clojure-Datenstrukturen
  
  Evaluate -- der Compiler untersucht einen Ausdruck, 
              compiliert ihn evtl. zu Java-Bytecode,
              evaluiert ihn zu einem Wert
 
  Print    -- gibt den Wert der Auswertung aus 
  
  Loop     -- und zurück: erwartet die nächste Eingabe
)

;; verwenden wir die REPL

486
;=> 486

(+ 137 349)
;=> 486

(- 1000 334)
;=> 666

(comment
  Wie muss man einen solchen Ausdruck sehen?

  (- 1000 334) entspricht (func arg1 arg2)
  
  - ist die Funktion, der Operator
  1000 und 334 sind die Arguente der Funktion, die Operanden
  der gesamte Ausdruck entspricht dem Wert, der sich durch das Anwenden der
  Funktion ergibt - also 666 in diesem Beispiel
)

;; Drei Vorteile der Präfix-Notation

(+ 21 35 12 7)
;=> 75 
; Vorteil 1: Funktionen können beliebig viele Argumente haben


(+ (* 3 5) (- 10 6))
;=> 19 

; Vorteil 2: Es kann keine Zweifel über Operator-Präzedenz geben
;            aber: eventuell viele Klammern

(list + 21 35 12 7)
;=> (#<core$_PLUS_ clojure.core$_PLUS_@aa0758> 21 35 12 7)
; Eine Liste mit einer Funktion und 4 Werten (Daten!!)
; diese Liste kann man zur Laufzeit erzeugen und dann auswerten (Code!!)

(eval (list + 21 35 12 7))
;=> 75

(+ 21 35 12 7)
; Vorteil 3: Daten = Code und Code = Daten, genannt Homoikonizität

; Wie wechselt man zwischen Code und Daten hin und her?

; Von Code zu Liste
(+ 1 2 3 4 5)   ; Code
'(+ 1 2 3 4 5)  ; ' Quote "zitiert" Code als Liste
(quote (+ 1 2 3 4 5))

; Von Liste zu Code
(def l '(+ 1 2 3 4 5))
l    ; hinter dem Symbol 'l' verbirgt sich eine Liste
(eval l)  ; wertet die Liste als Code aus

(def l' '(1 2 3 4 5))
l'
(eval (conj l' '+))

(list + 1 2)
'(+ 1 2)

(list + (+ 2 1) 3)

'(+ (+ 2 1) 3)

(comment
  Wir brauchen Namen für Objekte - Funktionen und Daten - unserer Sprache:
  Dazu gibt es in Clojure Vars, die einen Namen haben und an die Objekte
  gebunden werden können.
)

(def size 2)
;=> #'fpc.vl01/size
; es wurde eine Var namens size im Namensraum fpc.vl01 erzeugt
; Vars in Clojure leben stets in einem Namensraum

size
;=> 2
; Der Wert 2, der an das Symbol "size" gebunden ist
; Man nennt das "root binding" -> globale Umgebung

(* 5 size)
;=> 10

(def pi 3.14159)
;=> #'fpc.vl01/pi

(def radius 10)
;=> #'fpc.vl01/radius

(def circumference (* 2 pi radius))

circumference
;=> 62.8318

(comment
  Substitutionsmodell:
  
  Auswerten eines Ausdrucks:
  
  - Werte die Subausdrücke aus
  - Wende die äußerste Funktion auf die Argumente an
  
  Beachten: Definition beinhaltet Rekursion
  
  Beispiel:
  (* (+ 2 (* 4 6)) (+ 3 5 7))
  
  hat folgenden Syntaxbaum mit Zwischenergebnissen:
 
                                * (390)
                              /    \
                           + (26)    + (15)
                         /  \       / | \ 
                       2    * (24) 3  5  7
                            /\
                           4  6
) 

(comment
  (Fundamentale) Regeln der Auswertung:

  1. Werte werden zu sich selbst ausgewertet
  2. Symbole werten aus zu dem Ausdruck, den sie repräsentieren
  3. Listen werten aus zu einem Funktionsaufruf      
     dabei ist das erste Element der Liste die Funktion
  4. Bestimmte (eingebaute) Funktionen werden speziell behandelt: special forms
     Beispiel:
        (def size 2) -- darf natürlich size nicht auswerten!
)

(comment
  Definition von Funktionen
  
  - anonyme Funktion
  - Binden einer Funktion an ein Symbol
  - Definition einer Funktion mit Dokumentation
)

; anynome Funktion - hat keinen Namen - die Definition selbst ist der "Name":
((fn [x] (* x x)) 3)
; Funktion        Argument   
;=> 9

; geht auch kürzer - mit dem Reader-Makro #:
(#(* % %) 3)
; Fkt     Arg

; bei mehreren Argumenten
(#(+ %1 %2) 3 3)
;=> 6

; Binden einer Funktion an ein Symbol
(def sq (fn [x] (* x x)))
;=> #'fpc.vl01/sq

(sq 3)
;=> 9

; Schöner:
(defn square
  "Quadriert x -- hier steht die Doku"
  [x]
  (* x x))

(doc square)
(doc list)
;-------------------------
;fpc.vl01/square
;([x])
;  Quadriert 

(square 21)
;=> 441

(square (+ 2 5))
;=> 49

(square (square 3))
;=> 81

; Man kann nun square verwenden, um komplexere Funktionen zu bauen:

(defn sum-of-squares
  "Summiert die Quadrate von x und y"
  [x y]
  (+ (square x) (square y)))

(sum-of-squares 3 4)
;=> 25

(sum-of-squares 3)
;=> ArityException Wrong number of args (1) passed to: vl01/sum-of-squares  clojure.lang.AFn.throwArity (AFn.java:429)

; Das Zusammenbauen von Funktionen kann man immer weiter treiben
(defn f
  "Was macht folgende Funktion?"
  [a]
  (sum-of-squares (+ a 1) (* a 2)))

(f 5)
;=> 136
