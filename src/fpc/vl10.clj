; Funktionale Programmierung (in Clojure) Vorlesung 10
; Polymorphismus in Clojure (speziell multimethods)
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.vl10
  (:require [clojure.repl :refer :all]))

stop

; 1 Polymorphismus in objektorientierten Sprachen

(comment
  In objektorientierten Sprachen ist ein wichtiges Konzept das
  des _Polymorphismus_:
  Die zur Laufzeit ausgeführte Funktion eines Objekts hängt von seinem
  aktuellen Typ ab, auch wenn es eine Variable eines Obertyps ist.
  
  Beispiel:
  - abstrakte Klasse Konto mit der Methode zins
  - abgeleitete Klasse Girokonto berechnet den zins mit 0,01%
  - abgeleitete Klasse Sparkonto berechnet den zins mit 0,7%
  siehe Java Klassen zu Konto
)  
  

(comment
  Die Auswahl der konkret aufgerufenen Funktion hängt am Typ des
  Objekts.
  Was aber, wenn wir weitere Kriterien für die Berechnung des Zinses
  haben, wie etwa eine bestimmte Höhe des Kontostands?
  
  Dann kann man kaum etwas anderes tun, also in die Methode zins()
  eine entsprechende Abfrage einzubauen.
)

; 2 Polymorphismus in Clojure

; Wir ahmen erstmal das obige Beispiel nach

(def k1 ^{:type ::giro} {:ktoNummer 100, :ktoInhaber "Hans", :ktoStand 100.0})

(def k2 ^{:type ::spar}, {:ktoNummer 101, :ktoInhaber "Peter", :ktoStand 100.0})

k1

k2

(meta k1)
; Man kann dies auch in eine Funktion make-girokonto, make spar-konto verpacken, wenn man dies möchte

; Warum ::giro und nicht :giro? ::giro hat einen namespace, um Kollisionen zu vermeiden

; Wir wollen eine polymorphe Funktion, die sich unterscheidet je nach Typ des
; Kontos

(defmulti zins type)

(defmethod zins ::giro [kto]
  (* (:ktoStand kto) 0.0001))

(defmethod zins ::spar [kto]
  (* (:ktoStand kto) 0.007))

(type k1)

(zins k1)
;=> 0.01

(zins k2)
;=> 0.7000000000000001

; dadurch haben wir genau dasselbe erreicht.

(comment
  Wie funktioniert das in Clojure?
  
  defmulti geben wir den Namen der Funktion und eine Dispatch-Funktion.
   Die Dispatch-Funktion wird verwendet, um festzustellen, welche der
   mit defmethod definierten Funktionen aufgerufen wird.
)
  
; An unserem Beispiel:
; die Dispatch-Funktion ist die unäre Funktion type

(type k1)
;=> :fpc.vl10/giro

(type k2)
;=> :fpc.vl10/spar

; Wenn eine Multimethode aufgerufen wird, wird zunächst die Dispatch-Methode
; aufgerufen und dann die Funktion, die als Angabe bei
; defmethod den Wert hat, den die Dispatch-Methode zurückliefert.

(comment
  Was ist der Unterschied zu unserem Beispiel in Java?
  
  In Java gibt es genau eine Möglichkeit, die aufzurufende Funktion
  zu bestimmen, nämlich den Typ des Objekts -- die Dispatch-Funktion
  ist gewissermaßen in die Sprache eingebaut.
  
  In Clojure kann man eine beliebige Funktion zur Wahl der Methode
  verwenden. (Das hat einen Preis: Performance.)
)  

; Nun wollen wir die Berechnung der Zinsen auch vom Kontostand abhängig
; machen

(ns-unmap *ns* 'zins)
; defmulti wird nicht immer wieder neu übersetzt, deshalb müssen wir es mal
; vom Namensraum entkoppeln

(defmulti zins (fn [x] (if (< (:ktoStand x) 1000) [(type x) :wenig] [(type x) :viel])))

(defmethod zins [::giro :viel] [kto]
  (* (:ktoStand kto) 0.0001))
  
(defmethod zins [::spar :wenig] [kto]
  (* (:ktoStand kto) 0.007))

(defmethod zins [::spar :viel] [kto]
  (* (:ktoStand kto) 0.01))

(defmethod zins :default [kto]
  0)
  
(def k1w ^{:type ::giro} {:ktoNummer 100, :ktoInhaber "Hans", :ktoStand 100.0})
(def k1v ^{:type ::giro} {:ktoNummer 102, :ktoInhaber "Hans", :ktoStand 10000.0})

(def k2w ^{:type ::spar}, {:ktoNummer 101, :ktoInhaber "Peter", :ktoStand 100.0})
(def k2v ^{:type ::spar} {:ktoNummer 103, :ktoInhaber "Peter", :ktoStand 10000.0})


(zins k1w)
;=> 0

(zins k1v)
;=> 1.0

(zins k2w)
;=> 0.7000000000000001 

(zins k2v)
;=> 100.0

(comment
  Es gibt in Clojure einen Mechanismus mit dem man Hierarchien von Typen
  deklarieren kann und der Dispatch-Mechanismus von defmulti verwendet
  diese Hierarchien,
  
  siehe http://clojure.org/multimethods
)

(comment
  Man kann in Clojure auch den objektorientierten Mechanismus von Java verwenden,
  denn mit defrecord bzw. deftype kann man Java-Klassen definieren und
  mit defprotocol kann man Interfaces für sie definieren.
  
  Dies ist jedoch nicht Thema in dieser Veranstaltung.
)  
