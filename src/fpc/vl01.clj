; Funktionale Programmierung (in Clojure) Vorlesung 1
; Einführung in die funktionale Programmierung
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.vl01
  (:require [clojure.repl :refer :all])
  (:require [clojure.core.logic :refer :all]))

stop

;; Folie: Das funktionale Paradigma
;; Summieren in Clojure
(reduce + (range 1 11))

;; jeder Teilausdruck entspricht einem Wert:
(range 1 11) ; ist die Folge der Zahlen 1 .. 10

;; reduce wenden die Funktion + sukzessive auf die Folge der Zahlen an
(doc reduce)
;; also
(+ (+ (+ 1 2) 3) 4) ;; etc

(defn summieren
  [anfang ende]
  (reduce + (range anfang ende)))

(summieren 1 11)
;------------------------------------------------------------------------


;; Folie: Das relationale Paradigma
;; Relationale Programmierung mit core.logic

(run* [x y s]
     (membero x [1 2 3])
     (membero y [1 2 3])
     (project [x y]
              (== s (+ x y))))

;; berechnet alle möglichen Summen, die man mit x und y aus [1 2 3] bilden kann
;; Anmerkungen:
;; x y s sind logische Variablen
;; (membero ...) legt Wertebreich für mögliche Bindungen fest
;; (project ..) "projeziert" die logischen Variablen auf die gebundenen Werte
;; (== ...) Unifikation
;------------------------------------------------------------------------


;; Folie: Das objektorientierte Paradigma
;; Stack in Java
;; und hier jetzt Stack in Clojure

;; st ist ein Symbol für den Stack [1 2]
(def st [1 2])

st

;; Clojure hat keine Funktion, die push heißt, also nennen wir 
;; die in Clojure übliche Funktion conj jetzt mal push
(def push conj)

;; wir legen 3 auf den Stack
(push st 3)
;=> [1 2 3]

;; Wir erhalten einen "neuen" Stack als Rückgabewert
;; st ist unverändert

st

;; oberstes Element des Stacks
(peek st)

;; oberstes Element "herunternehmen"
(pop st)

;; auch das ändert st nicht!
st
;------------------------------------------------------------------------


;; Folie Auswertungsart

;; Strikte Auswertung in Clojure
;; folgendes Beispiel wirft eine Exception

(count [1 (/ 2 0) 3])
;=> ArithmeticException Divide by zero  clojure.lang.Numbers.divide (Numbers.java:156)

;; aber: es gibt in Clojure auch "lazy sequences":
;; dazu kommt später mehr in der Vorlesung

(class (range 1 100))
;=> clojure.lang.LazySeq

(realized? (range 1 100))
;=> false


