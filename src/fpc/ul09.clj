; Funktionale Programmierung (in Clojure) Uebungen Serie 9
; Polymorphismus
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.ul09
  (:require [clojure.inspector :refer :all]))

stop

; Aufgabe 1

; (a)
; Programmieren Sie in Java eine abstrakte Klasse Tier mit einer Methode spricht.
; Bilden Sie Subklassen Katze und Hund, die bei der Methode spricht "Wauwau" bzw.
; "miau" ausgeben.
; Erzeugen Sie ein paar Objekte und lassen sie sprechen

; (b)
; Programmieren Sie dieselbe Funktionalität in Clojure mit der Verwendung von
; Multimethoden

; Aufgabe 2

; (a)
; Machen Sie sich mit der Bibliothek clojure.inspector vertraut und machen Sie einige
; Beispiele:

(inspect-tree '(and p (or q r)))

(inspect-tree [1 2 3 [4 5 6 [7 8 9]]])

(inspect-tree {:clojure {:rev "1.6.0" :java "SE 6"}})

(inspect-table [[:a :b :c] [1 2 3] ["a" "b" "c"]])


; (b)
; Analysieren Sie den Quellcode in Clojure.inspector.clj und erläutern Sie, wie
; Multimethoden eingesetzt werden.

; Aufgabe 3

; (a)
; Programmieren Sie Funktion make-rect und make-circle, die Hash-Maps für
; Rechtecke und Kreise erzeugen.

(defn make-rect
  "Erzeugt Rechteck mit den Seiten a und b."
  [a b]
  ^{:type ::rect} {:a a, :b b})

(defn make-circle
  "Erzeugt Kreis mit Radius r."
  [r]
  ^{:type ::circle} {:r r})

; (b)
; Programmieren Sie eine Multimethode (area ...), die die Fläche eines
; Rechtecks oder eines Kreises berechnet.

(defmulti area
  "Berechnet die Fläche einer geometrischen Form."
  type)

(defmethod area ::rect [shape]
  (* (:a shape) (:b shape)))

(defmethod area ::circle [shape]
   (* Math/PI (* (:r shape) (:r shape))))

(def r1 (make-rect 2 3))

(area r1)
;=> 6

(def c1 (make-circle 1))

(area c1)
;=> 3.141592653589793

; Aufgabe 4

; Lösen Sie Aufgabe 3 mittels (defrecord ...) und (defprotocol ...)

(defprotocol Area 
  (area [this]))
;=> Area 

(defrecord Rect [a b]
  Area
  (area [this] (* (:a this) (:b this))))
;=> fpc.ul08.Rect

(defrecord Circle [r]
  Area
  (area [this] (* Math/PI (* (:r this) (:r this)))))
;=> fpc.ul08.Circle

(def r2 (Rect. 2 3))

(area r2)
;=> 6

(def c2 (Circle. 1))

(area c2)
;=> 3.141592653589793



