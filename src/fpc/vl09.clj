; Funktionale Programmierung (in Clojure) Vorlesung 9
; Folgen (Sequences, kurz Seqs)
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.vl09
  (:require [clojure.repl :refer :all]))

stop

;; 1 Folgen (als Abstraktion von zusammengesetzten Datenstrukturen)

(comment
  Wir haben eine ganze Reihe von Datenstrukturen gesehen, wie Listen, Vektoren, 
  Mengen, Hash-Maps usw.
  Clojure hat eine einheitliche Art und Weise mit solchen Datenstrukturen als Ganzen
  elementweise umzugehen, die Abstraktion "sequence" (Folge), alle solche Datenstrukturen
  sind "seqable"
)  

(comment
  Seqs haben eine gewisse Ähnlichkeit zu Iteratoren, haben aber ein funktionales Konzept.
  "Seqs differ from iterators in that they are persistent and immutable, not stateful 
   cursors into a collection. As such, they are useful for much more than foreach - 
   functions can consume and produce seqs, they are thread safe, they can share structure 
   etc." Aus der Dokumentation von Clojure
)

(comment
  Das Konzept von Sequences kommt von LISP, dort aber ist die Sequence definiert auf einer
  konkreten Datenstruktur, der Liste.
  In Clojure ist die sequence ein logisches Konzept, das auf Basis verschiedener konkreter
  Datenstrukturen funktioniert.
  "A sequence is a sequential _view_ on a collection, it is not the collection itself."
  (Meikel Brandmeyer)
)  


; seq macht aus einer konkreten Datenstruktur eine sequence
(seq '(1 2 3 4 5))
;=> (1 2 3 4 5)

(seq? (seq '(1 2 3 4 5)))
;=> true

(seq [1 2 3 4 5])
;=> (1 2 3 4 5)

(seq? [1 2 3 4 5])
;=> false

(seq? (seq [1 2 3 4 5]))
;=> true

(instance? clojure.lang.Seqable (seq [1 2 3 4 5]))
;=> true

(seq #{1 2 3 4 5})
;=> (1 4 3 2 5) 

(seq {:a 1, :b 2, :c 3, :d 4, :e 5})
;=> ([:e 5] [:c 3] [:b 2] [:d 4] [:a 1])

; seq geht auch mit Java!
(seq "12345")
;=> (\1 \2 \3 \4 \5)
; Ein String ist eine Folge von Buchstaben

(comment
  Man muss beachten, dass die einheitliche Behandlung der konkreten Datenstrukturen
  einen Preis hat. Verwendet man einen Vektor als Folge, dann hat man hinterher eine
  Folge in der Hand, nicht mehr den Vektor, will man daraus wieder einen Vektor machen,
  dann muss man das explizit tun.

  Wir werden später sehen, dass viele Funktionen "automatisch" konkrete Datenstrukturen
  in Folgen verwandeln, damit man Funktionen schachteln kann. Das funktioniert nur,
  weil alle diese Funktionen mit sequences funktionieren. Deshalb kann man nicht unbedingt
  erwarten, dass der ursprüngliche Typ erhalten bleibt.
 )

(instance? clojure.lang.PersistentVector (seq [1 2]))
;=> false

(instance? clojure.lang.PersistentVector (vector (seq [1 2])))
;=> true

;; 2 Verzögerte und unendliche Folgen

(comment
  Das Auswertungsmodell, das wir bisher bei Clojure kennengelernt haben, ist _strikte_ Auswertung,
  d.h. zuerst werden die Parameter ausgewertet, dann die Werte an die Funktion
  übergeben. Man nennt diese Auswertungsstrategie auch "eager".
  
  Dem steht gegenüber die verzögerte Auswertung ("lazy"), die einen Wert erst dann erzeugt,
  wenn er auch wirklich gebraucht wird.
  
  Clojure verwendet diese Strategie bei den sogenannten "lazy sequences", wobei eine Sequenz
  nicht wirklich realisiert wird. Man muss sich das so vorstellen: Bei der Definition der
  Folge wird nicht die Folge selbst erzeugt, sondern eine Funktion, die die Folge erzeugen
  kann. Wird ein Element der Folge benötigt, dann wird diese Funktion aufgerufen 
  (Clojure tut dies nicht einzeln pro Element, sondern in Blöcken (chunks)).
)

; Beispiel für eine verzögerte Folge

(range 10)
;=> (0 1 2 3 4 5 6 7 8 9) 

(type (range 10))
;=> clojure.lang.LazySeq 

; In der REPL wird die verzögerte Folge ausgegeben, deshalb muss sie realisiert werden
; aber wenn wir sie in einer Var merken, dann ist sie nicht realisiert

(def r (range  10))
;=> #'fpc.vl09/r

(realized? r)
;=> false

r
;=> (0 1 2 3 4 5 6 7 8 9)

(realized? r)
;=> true

; Wir wollen nun an einem Beispiel eine lazy sequence selbst programmieren und
; ihr Verhalten untersuchen (aus Clojure Programming S. 93)

(defn random-ints
  "Erzeugt eine verzögerte Folge von Zufallszahlen und protokolliert die Erzeugung"
  [limit]
  (lazy-seq
    (println "ich erzeuge eine Zufallszahl")
    (cons (rand-int limit) (random-ints limit))))
;=> #'fpc.vl09/random-ints

(doc cons)
;-------------------------
;clojure.core/cons
;([x seq])
;  Returns a new seq where x is the first element and seq is
;    the rest. 

;=> Vorsicht: dauert auf meinem neuen Mac sehr lange!!
(random-ints 49)
;=> OutOfMemoryError Java heap space  java.util.Arrays.copyOf (:-1) 

; Dieser Aufruf war keine gute Idee: random-ints erzeugt Zufallszahlen ohne Ende!

; Zwei Möglichkeiten:
; Die Funktion (take n ...) gibt die ersten n Elemente der Folge aus
; Die dynamische Var *print-length* sorgt dafür, dass die REPL nach dieser Zahl aufhört

(take 10 (random-ints 49))

(set! *print-length* 10)
(random-ints 49)

; Nun wollen wir beobachten, wie die Zufallszahlen erzeugt werden:

(def rands (random-ints 49))
;=> #'fpc.vl09/rands

(realized? rands)

; es gibt keine Meldung, dass eine Zufallszahl erzeugt wurde
; wir sehen also, dass keine Auswertung der Funktion erfolgt ist

(first rands)
;=> ich erzeuge eine Zufallszahl
;=> 24

; eine Zufallszahl wurde erzeugt

(nth rands 3)
;=> ich erzeuge eine Zufallszahl
;=> ich erzeuge eine Zufallszahl
;=> ich erzeuge eine Zufallszahl
;=> 10

; man sieht wie die verzögerte Auswertung erfolgt


(comment
  Eine schöne Eigenschaft der verzögerten Auswertung haben wir an diesem
  Beispiel schon gesehen:
  Verzögerte Folgen erlaube es _unendliche_ Folgen zu programmieren.
) 

; Das Musterbeispiel - unendliche Folge von ganzen Zahlen beginnend bei start

(defn numbers
  [start]
  (lazy-seq 
    (cons start (numbers (inc start)))))
;=> #'fpc.vl09/numbers

(numbers 0)
;=> (0 1 2 3 4 5 6 7 8 9 ...)
;=> die ... stehen da, weil wir die Zahl der Werte für die Ausgabe beschränkt haben

(numbers 1)
;=>  (1 2 3 4 5 6 7 8 9 10 ...)

(class (numbers 0))
;=> clojure.lang.LazySeq

; Nun aber wollen wir endlich sehen, was man mit Folgen alles tun kann:

;; 3 Funktionen mit und für Folgen

; Die Abstraktion "sequence" hat folgende Funktionen:
; seq erzeugt eine Folge aus einer konkreten Datenstruktur
; first, rest und next verwendet man um die Folge zu durchlaufen
; lazy-seq erzeugt eine verzögerte Folge

; seq und lazy-seq haben wir schon gesehen

(def bis3 (seq [1 2 3]))

(first bis3)
;=> 1

(defn durchlauf
  [seq]
  (if (= seq nil)
    (println "Folge ist zuende")
    (do
      (println (first seq))
      (durchlauf (next seq)))))

(durchlauf bis3)
;=> 1
;=> 2
;=> 3
;=> Folge ist zuende

; Was ist der Unterschied zwischen rest und next?

(next [1 2])
;=> (2)

(next [1])
;=> nil

(next [])
;=> nil

(rest [1 2])
;=> (2)

(rest [1])
;=> ()

(rest [])
;=> ()

; next realisiert das nächste Element der Folge, wenn keines mehr da ist ergibt sich nil

; rest realisiert das nächste Element _nicht_, gibt immer eine leere Folge zurück
; Grund: rest erlaubt "mehr" Verzögerung

; Man kann den Unterschied mit random-ints sehen
(def r (next (random-ints 49)))
;=> ich erzeuge eine Zufallszahl 
;=> ich erzeuge eine Zufallszahl 
; Die Meldung kommt zweimal: 1. für das erste Element und 2. für den Anfang der restlichen Folge

(def r (rest (random-ints 49)))
;=> ich erzeuge eine Zufallszahl 
; Die Meldung kommt nur einmal für das erste Element, die restliche Folge wird nicht ausgewertet

; wie bricht man mit "rest" ab?
(empty? (rest [1]))
;=> true

(empty? (rest [1 2]))
;=> false

;; Folgen sind die zentrale Abstraktion in Clojure und es gibt eine Menge Funktionen, die mit
;; Folgen arbeiten

;; Funktionen, die Folgen erzeugen -----------------------------------

; Folge aus konkreter Datenstruktur
(seq [1 2 3 4])
;=> (1 2 3 4)

; Folge in umgekehrter Ordnung aus geordneter Datenstruktur
(rseq [1 2 3 4])
;=> (4 3 2 1) 

; Folge der Werte einer Hash-Map
(vals {:a 1, :b 2})
;=> (2 1)

; Folge der Schlüssel einer Hash-Map
(keys {:a 1, :b 2})
;=> (:b :a)

; Folge durch die Iteration einer Funktion
(iterate inc 0)
;=> (0 1 2 3 4 5 6 7 8 9 ...) 
; Vergleiche mit numbers oben

; Folge durch Wiederholung einer Funktion
(repeatedly #(rand-int 49))
;=> (28 10 18 44 4 29 24 40 46 39 ...)
; Vergleiche mit random-ints oben

; Folge aus Konstanten
(repeat 1)
;=> (1 1 1 1 1 1 1 1 1 1 ...)

; Folge aus Zahlenbereich
(range)
;=> (0 1 2 3 4 5 6 7 8 9 ...)

(range 5)
;=> (0 1 2 3 4)

(range 2 5)
;=> (2 3 4)

(range 2 10 2)
;=> (2 4 6 8)

; Folge durch zyklische Wiederholung von Werten
(cycle [1 2 3])
;=> (1 2 3 1 2 3 1 2 3 1 ...) 

; Folgen durch aneinanderhängen
(concat [1 2] [3 4] [5 6])
;=> (1 2 3 4 5 6) 

; Folge durch Mischen von Folgen
(interleave [:a :b :c] [1 2 3 4])
;=> (:a 1 :b 2 :c 3) 


;; Funktionen, die Folgen manipulieren --------------------------------------------------

; Folge filtern
(filter even? (range 20))
;=> (0 2 4 6 8 10 12 14 16 18) 

; geht auch andersrum
(remove even? (range 20))
;=> (1 3 5 7 9 11 13 15 17 19)

(filter odd? (range 20))

; Folge elementweise "ändern"
(map inc (range 20))
;=> (1 2 3 4 5 6 7 8 9 10 ...)

(map + (range 20) (range 20))

; Folge sukzessive zusammenfalten
(reduce + [1 2 3 4])
;=> 10

; Man kann sich auch alle Zwischenergebnisse ansehen
(reductions + [1 2 3 4])
;=> (1 3 6 10) 

; alle diese Funktionen kann man kombinieren und dadurch erreicht man extrem eleganten Stil,
; wie wir gleich sehen werden.

; Es gibt "tons of functions for seqs", siehe Dokumentation von Clojure

;; 4 Rekursion mit Folgen neu gesehen

; wir haben schon Beispiele gesehen

; Erzeugen von Zufallszahlen
(defn random-ints
  [limit]
  (lazy-seq
    (cons (rand-int limit) (random-ints limit))))

(random-ints 49)
;=> (23 28 19 32 32 1 18 23 28 48 ...)

; geht schöner mit Folge
(defn random-ints'
  [limit]
  (repeatedly #(rand-int limit)))

(random-ints' 49)
;=> (6 1 26 35 23 25 41 31 28 6 ...)

; noch ein Beispiel Berechnung der Fakultät
; so sah factorial mit Endrekursion aus
(defn factorial
  "Fakultät von n"
  [n]
  (loop [cur n, acc 1]
    (if (= cur 0)
      acc
      (recur (dec cur) (* acc cur)))))

(factorial 12)
;=> 479001600

; geht das auch einfacher??
(defn factorial'
  "Fakultät von n"
  [n]
  (reduce * (range 1 (inc n))))
   
(factorial' 12)
;=> 479001600

; Folgen statt Rekursion wollen wir im Praktikum ausprobieren
