; Funktionale Programmierung (in Clojure) Vorlesung 8
; Persistente Datenstrukturen
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.vl08
  (:require [clojure.repl :refer :all]))

;; 1 Listen

(comment
  Die Liste ist _die_ Datenstruktur eines Lisp (List Processing).
  
  Es handelt sich um eine einfach verkettete Liste, die selbst wiederum
  Listen (und in Clojure andere Datenstrukturen) enthalten kann.
  
  Dadurch kann man mit Listen auch Bäume darstellen - so etwa den Quellcode
  von Clojure-Programmen selbst.
)

;; 1.1 Literale Darstellung

(def l1 '(1 2 3))
;=> #'fpc.vl08/l1

; Wir müssen die Liste mit quote versehen, damit sie nicht
; als Code genommen und ausgewertet wird

(= (quote (1 2 3)) l1)
;=> true

l1

; Quote und Unquote
'(1 2 (- 5 2))
;=> (1 2 (- 5 2))

`(1 2 ~(- 5 2))  ;; Achtung Backtick!!
;=> (1 2 3)

(list 1 2 (- 5 2))
;=> (1 2 3)

(type l1)
;=> clojure.lang.PersistentList

(list? l1)
;=> true

;; 1.2 Erzeugen von Listen

(def l2 (list "Hallo" "Clojure"))
;=> #'fpc.vl08/l2

l2
;=> ("Hallo" "Clojure")

(def l3 '(1 (11 12 (121 122) 13 (131 132) 14) 2 3 (31))) 
;=> #'fpc.vl08/l3

(list 1 2 3 [4 5 6 7 8 9])
;=> (1 2 3 [4 5 6 7 8 9])

(list* 1 2 3 [4 5 6 7 8 9])
;=> (1 2 3 4 5 6 7 8 9)

;; 1.3 Arbeiten mit Listen

l3
(count l3)
;=> 5

(nth l3 0)
;=> 1

(nth l3 1)
;=> (11 12 (121 122) 13 (131 132) 14)

(first l3)
;=> 1

(first (rest l3))
;=> (11 12 (121 122) 13 (131 132) 14)

; Vorsicht get liefert auf sequenziellen Datenstrukturen immer nil
(get l3 1)
;=> nil
; get funktioniert bei assoziativen Datenstrukturen - der Key beim Vektor ist der Index
(get [1 2 3 4] 1)
;=> 2

; "Hinzufügen" von Elementen
l1
(conj l1 4)
;=> (4 1 2 3) d.h. eine Liste "wächst" vorne

; was ist mit der "alten" Liste l1 passiert?
l1
;=> (1 2 3)

(cons 4 l1)
;=> (4 1 2 3)

; Mit conj, pop und peek kann man Listen als Stack verwenden:

(def st ())

(def st' (conj st 1))

st'
;=> (1)

(peek st')
;=> 1

(pop st')
;=> ()

;; 1.4 Evaluierung

(def l4 '(let [p1 true p2 false] (or p1 p2)))
;=> #'fpc.vl08/l4

l4
(eval l4)
;=> true

(def l4' '(let [p1 false p2 false] (or p1 p2)))

l4'
(eval l4')

stop

;; 2 Vektoren

;; 2.1 Literale Darstellung

(def v1 [1 2 3])
;=> #'fpc.vl08/v1

v1
;=> [1 2 3]

; Vektoren können Daten unterschiedlichen Typs
; enthalten:

(def v2 [1 "Hallo" '(1 2 3)])
;=> #'fpc.vl08/v2

v2
;=> [1 "Hallo" (1 2 3)]

(type v2)
;=> clojure.lang.PersistentVector

(vector? v2)
;=> true

l1
(vector? l1)
;=> false

;; 2.2 Erzeugen von Vektoren

(vector 1 2 3)        ; Vektor aus Elementen erzeugen
;=> [1 2 3]

(= v1 (vector 1 2 3))
;=> true

l1
(vec l1)              ; Vektor aus Kollektion erzeugen
;=> [1 2 3]

(vector-of :int 1 2 3)
;=> [1 2 3]           ; verwendet primitive Typen in Java 

(= v1 (vector-of :int 1 2 3))
;=> true

;; 2.3 Arbeiten mit Vektoren

(count v1)
;=> 3

(nth v1 0)
;=> 1

(nth v1 4)
;=> IndexOutOfBoundsException 

(nth v1 4 "gibt's hier nicht")
;=> "Gibt's hier nicht"

(get v1 0)
;=> 1

(first v1)
;=> 1

(second v1)
;=> 2

; in geschachtelten Vektoren kann man einen "Indexpfad" angeben
(get-in [1 [11 12] 2 [21 22]] [1 0])
;=> 11
; 11 ist das Element mit Index 0 im Vektor am Index 1

; "Hinzufügen" von Elementen
(conj v1 4)
;=> [1 2 3 4]  ; Vektoren "wachsen" hinten

v1
;=> [1 2 3]  Der "alte" Vektor ist unverändert

; Mit conj, peek, pop kann man einen Vektor als Stack verwenden
(def st [])
;=> #'fpc.vl08/st

st
;=> []

(def st' (conj st 1))

st'
;=> [1]

(peek st')
;=> 1

(pop st')
;=> []

; "Ändern" von Elementen
(assoc v1 0 999)
;=> [999 2 3]

; auch das lässt den "alten" Vektor unberührt
v1
;=> [1 2 3]

; Weitere "Manipulationen"

(subvec v1 1)
;=> [2 3]

(subvec v1 1 2)
;=> [2]

(subvec v1 0 2)
;=> [1 2]

(replace {2 :two, 4 :four} [4 2 3 4 5 6 2])
;=> [:four :two 3 :four 5 6 :two]

; hier haben wir schon eine Map verwendet, gleich mehr darüber

;; 2.4 Werte eines Vektors als Argumente einer Funktion

; + ist variadisch
(+ 1 2 3)
;=> 6

(+ v1)
;=> ClassCastException

(apply + v1)
;=> 6

;; 3 Assoziative Datenfelder (Maps)

;; 3.1 Literale Darstellung

(def m1 {:a 1, :b 2, :c 3})
;=> #'fpc.vl08/m1

m1
;=> {:c 3, :b 2, :a 1}

{:a 1, :b 2, :c 3, :a 2}
;=> Exception

(type m1)
;=> clojure.lang.PersistentHashMap

(map? m1)
;=> true

;; 3.2 Erzeugen von Maps

(hash-map)
;=> {}

(hash-map :a 1, :b 2, :c 3, :a 2)


; Warum kann man nicht map als Name der Funktion nehmen, die eine Map erzeugt?

(hash-map :key1 1 :key2 2 :key1 0)
;=> {:key2 2, :key1 0}

; aber
{:key1 1 :key2 2 :key1 0}
;=> IllegalArgumentException Duplicate key: :key1
; warum ist das so?

(def m2 (array-map :key1 1 :key2 2))
;=>
#'fpc.vl08/m2

(type m2)
;=> clojure.lang.PersistentArrayMap
; ArrayMap versus HashMap
; ArrayMaps sind implementiert als Array von Paaren key - value - und hat damit
; eine fixe Reihenfolge der Einträge qua Konstruktion.
; Clojure garantiert aber diese Reihenfolge nur, solange kein assoc
; gemacht wird -- dann wird eventuell eine HashMap daraus.

(def m3 (sorted-map :key2 2 :key1 1))
;=> #'fpc.vl08/m3

m3
;=> {:key1 1, :key2 2}

(type m3)
;=> clojure.lang.PersistentTreeMap

(def m4 (zipmap [:a :b :c :d] [1 2 3 4]))
;=> #'fpc.vl08/m4

m4
;=> {:d 4, :c 3, :b 2, :a 1} 

(type m4)
;=> clojure.lang.PersistentArrayMap  ;; überraschend

(def m5 (zipmap [:a :b :c :d] [1 2 3 4 5]))
m5

;; 3.3 Arbeiten mit Maps

m1
(count m1)
;=> 3

(get m1 :a)
;=> 1

(first m1)
;=> [:c 3]

(select-keys m1 [:a :c])
;=> {:c 3, :a 1}

(= m2 m3)
;=> true   ; obwohl unterschiedlichen Typs

; "Hinzufügen" von Elementen
(conj m1 [:z 26])
;=> {:z 26, :c 3, :b 2, :a 1}

; die "alte" Map bleibt unberührt
m1
;=> {:c 3, :b 2, :a 1}

(conj m1 {:z 26})
;=> {:z 26, :c 3, :b 2, :a 1}

; "Ändern" einer Map

(assoc m1 :a 999)  ; Syntaktischen Unterschied zu conj beachten
;=> {:c 3, :b 2, :a 999}

(dissoc m1 :a)
;=> {:c 3, :b 2}

; Weitere Funktionen

(contains? m1 :a)
;=> true

(contains? m1 :y)
;=> false

(find m1 :a)
;=> [:a 1]

(keys m1)
;=> (:c :b :a)

(vals m1)
;=> (3 2 1)


;; 4 Mengen

;; 4.1 Literale Darstellung

(def s1 #{1 2 3 4 5 6 7 8 9})
;=> #'fpc.vl08/s1

(type s1)
;=> clojure.lang.PersistentHashSet

(set? s1)
;=> true

;; 4.2 Erzeugen von Mengen

(= s1 (hash-set 1 2 3 4 5 6 7 8 9))
;=> true

(= s1 (set [1 1 2 2 3 3 4 4 5 6 7 8 9]))
;=> true

(def s2 (sorted-set 9 1 3 2 4 5 7 6 8))
;=> #'fpc.vl08/s2

s2
;=> #{1 2 3 4 5 6 7 8 9}

(type s2)
;=> clojure.lang.PersistentTreeSet 

(= s1 s2)
;=> true

;; 4.3 Arbeiten mit Mengen

(count s1)
;=> 9

(get s1 0)
;=> nil

(get s1 1) ; Schlüssel = Wert
;=> 1

(first s1)
;=> 7
; Reihenfolge bei Menge???

(first s2)
;=> 1

(second s2)
;=> 2

(contains? s1 1)
;=> true

(contains? s1 10)
;=> false

; "Ändern" von Mengen

s1

(conj s1 0)
;=> #{0 7 1 4 6 3 2 9 5 8}

s2
; s2 ist eine sortierte "Menge"

(conj s2 0)
;=> #{0 1 2 3 4 5 6 7 8 9}

(conj s2 10)
;=> #{1 2 3 4 5 6 7 8 9 10}

(disj s2 1)
;=> #{2 3 4 5 6 7 8 9}

;; 4.4. Weitere Funktionen für Mengen

(require '[clojure.set :as set])

(def s #{1 2 3})

(def t #{3 4})

(set/union s t)
;=> #{1 4 3 2}

(set/intersection s t)
;=> #{3}

(set/difference s t)
;=> #{1 2}

(def r1 #{{:a 1 :b 1} {:a 2 :b 1} {:a 3 :b 3}})
(def r2 #{{:a 1 :c 1} {:a 1 :c 2} {:a 2 :c 3}})

; Tabellarische Darstellung
; r1            r2
; :a   :b       :a   :c
; -------       -------
;  1    1        1    1
;  2    1        1    2
;  3    3        2    3



; Natürlicher Verbund

(set/join r1 r2)
;=> #{{:c 2, :b 1, :a 1} {:c 3, :b 1, :a 2} {:c 1, :b 1, :a 1}} ; natural join

; (join r1 r2)
; :a    :b    :c
; --------------
;  1     1     1
;  1     1     2
;  2     1     3

; Projektion

(set/project r1  [:a])
;=> #{{:a 1} {:a 3} {:a 2}}

(set/project (set/join r1 r2)  [:a])
(set/project (set/join r1 r2)  [:a :b])

; Restriktion

(set/select #(= 1 (:b %)) r1)
;=> #{{:b 1, :a 2} {:b 1, :a 1}} 

; An diesem Beispiel sehen wir, dass man offenbar den Key einer Map als Funktion
; verwenden kann (:b %) -- das ist eine besondere Eigenschaft von Clojure, die wir
; in Abschnitt 6 betrachten

;; 5 Persistenz der Datenstrukturen in Clojure

#_(comment
  
  Wir haben gesehen, dass alle Datenstrukturen unveränderlich sind.
  Dies ist eine ganz wesentliche Eigenschaft für die funktionale
  Programmierung  -- denken wir an unser Substitutionsmodell

  Die Datenstrukturen in Clojure nennt man "persistent", worunter
  folgende Eigenschaften zu verstehen sind:
  
  (1) sie sind unveränderlich
  (2) sie haben auch nach "Änderung" die gleiche Laufzeitcharakteristik
 
  "We shall call a data structure persistent if it supports access to multiple versions."
  James R. Driscoll, Neil Sarnak, Daniel D. K. Sleator, and Robert E. Tarjan. Making
  data structures persistent. Journal of Computer and System Sciences, 38(1):86–
  124, February 1989
)

#_(comment
  
  Naiv kann man dies erreichen, indem man bei jeder "Änderung" eine Kopie
  macht.
  
  Das geht aber besser, wenn die verschiedenen Versionen gemeinsame Teile verwenden:
  "structure sharing".
  
  Am leichtesten zu sehen am Beispiel der Liste.

  l1 sei die Liste 1 -> 2 -> 3

  Wenn wir nun eine neue Liste erzeugen mit (conj l1 0)

  erhalten wir

  l2   l1
  |    |
  0 -> 1 -> 2 -> 3

  d.h. die Liste l1 ist ein Teil der Liste l2. Das nennt man
  "structure sharing".
  
  Clojure verwendet für Vektoren und Maps Bäume. 
  Siehe Higher Order Blog: Understanding Clojure's PersistentVector implementation
  "http://blog.higher-order.net/2009/02/01/understanding-clojures-persistentvector-implementation.html"

  Die grundlegenden Ideen stammen von
  Phil Bagwell: Ideal Hash Trees sowie
  Chris Okasaki: Purely Functional Data Structures

)

;; 6 Datenstrukturen als Funktionen

#_(comment
  
  Eine für die elegante Programmierung wichtige Eigenschaften von Clojures Datenstrukturen
  besteht darin dass man Datenstrukturen und Keywords als Funktionen
  verwenden kann.
  
  Tun wir das
)  

; Vektor als Funktion = get
v1
;=> [1 2 3]

(v1 1)
;=> 2

(["hallo" "Clojure"] 0)

(get v1 1)
;=> 2

(v1 3)
;=> IndexOutOfBoundsException

; Menge als Funktion = get
s1
;=> #{7 1 4 6 3 2 9 5 8}

(s1 1)
;=> 1

(s1 0)
;=> nil

; viel spannender ist das bei Maps
m1
;=> {:c 3, :b 2, :a 1}

(m1 :c)
;=> 3

(m1 :d)
;=> nil

; aber viel wichtiger die Verwendung von Keywords als Funktionen

(:a m1)
;=> 1

(:d m1)
;=> nil

; Diese Möglichkeit erlaubt sehr eleganten Umgang mit Maps
; Beispiel

(def kunde {:name "Schneider", :vorname "Klaus", :kndnr 123})

(:name kunde)
; => "Schneider"

(:kndnr kunde)
; => 123


;; 7 Zerlegende Variablenbindung

#_(comment
  
  In Clojure arbeitet man viel mit persistenten Datenstrukturen,
  dazu braucht man natürlich Zugriff auf ihre Bestandteile.
  Dies geht sehr elegant mit der Technik der
  
  zerlegenden Variablenbindung ("destructuring")
)  

; alle folgenden Beispiele mit let gehen ebenso mit loop und Parametern von Funktionen
; Beispiele aus Kamphausen/Kaiser S.66ff

; angenommen, wir wollen die Zahlen in einem Vektor
; als String ausgeben:
; bisher
(let [coll [1 2 3]]
  (str (coll 0) " " (coll 1) " " (coll 2)))
;=> "1 2 3"

; wie man sieht, ist es relativ mühsam, die
; Bestandteile des Vektors "auszupacken"

; Das geht in Clojure viel einfacher:
; Zerlegung bei der Bindung der Kollektion an die Parameter
(let [[a b c] [1 2 3]]
  (str a " " b " " c))
;=> "1 2 3"

; Grundidee: Man "baut" die Struktur "nach"

(let [[a b [c d e]] [1 2 [3 4 5]]]
  (list a b (list c d e)))
;=> (1 2 (3 4 5))

(let [[a b [c d e]] [1 2 [3 4 5]]]
  #{a b c d e})

(let [[a b [c d e]] [1 2 [3 4 5]]]
  (list a b c d e))
;=> (1 2 3 4 5)

; geht mit allen Datenstrukturen die nth unterstützen

(let [[a b c] '(1 2 3)]
  (str a " " b " " c))
;=> "1 2 3"

(let [[a b c] "123"]
  (str a " " b " " c))
;=> "1 2 3"

(let [[_ b c] "123"]
  (str b " " c))

(let [[a b c] "123"]
  (list  a b c))
;=> (\1 \2 \3)

; was macht man mit Maps?

(let [{a :x, b :y , c :z} {:x 1 :y 2 :z 3}]
  (str a " " b " " c))
;=> "1 2 3"

; lokale Symbole mit den Namen der Keys
(let [{:keys [x y]} {:x 1 :y 2 :z 3}]
  (str x " " y ))
;=> "1 2"

; geht auch mit Strings
(let [{:strs [x y]} {"x" 1 "y" 2 "z" 3}]
  (str x " " y ))
;=> "1 2"

; manchmal möchte man Teile und das Ganze
(let [{a :x, b :y , c :z, :as alles} {:x 1 :y 2 :z 3}]
  (list a b c alles))
;=> (1 2 3 {:y 2, :z 3, :x 1}) 

; hat man viele Argumente, braucht man die ersten und den Rest
(let [[a & rest] [1 2 3 4 5 6]]
  (list a rest))
;=> (1 (2 3 4 5 6))

; Man kann auch Defaultwerte angeben, falls welche fehlen
(let [{x :x, y :y} {:x 1}]
  (list x y))
;=> (1 nil)
; hier fehlt :y

(let [{x :x, y :y :or {x 100 y 101}} {:x 1}]
  (list x y))
;=> (1 101)
; hier hat :y einen Default-Wert

;; 8 Benutzerdefinierte Datentypen

#_(comment
  
  Es ist in Clojure durchaus üblich, dass man die vorhandenen
  Datenstrukturen als Implementierung für benutzerdefinierte
  Datentypen verwendet.
  
  So kann man z.B. Komplexe Zahlen in der Implementierung
  durch 2-stellige Vektoren [re im] repräsentieren.
  
  Was aber, wenn man auch eine Darstellung als Polarkoordinaten
  unterstützen möchte?
  
  Man kann dann Metadaten mit einem Typ verwenden.
)

(def c1 ^{:type :rectangular} [1 1])

c1
;=> [1 1]

(meta c1)
;=> {:type :rectangular}

(type [1 1])
(type c1)
;=> :rectangular

(def c2 ^{:type :polar} [1.41 45])

(type c2)
;=> :polar

#_(comment
  
  Man kann aber auch eine interne Darstellung als Map wählen:
  
  {:re 1, :im 1}
  
  Oder man kann auch direkt Java-Klassen erzeugen, die man dann wie
  diese Map verwenden kann
)  

(defrecord Complex [re im])
;=> fpc.vl08.Complex

(def c1 (Complex. 1 2))
;=> #'fpc.vl08/c1

(:re c1)
;=> 1

(:im c1)
;=> 2

(let [{:keys [re im]} c1]
  [re im])
;=> [1 2]