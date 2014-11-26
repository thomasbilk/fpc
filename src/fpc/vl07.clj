; Funktionale Programmierung (in Clojure) Vorlesung 7
; Einfache Datentypen
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.vl07
  (:require [clojure.repl :refer :all]))

stop

;; 1 Numerische Werte

;; 1.1 Ganze Zahlen

;; 1.1.1 Ganze Zahlen sind in Clojure longs von Java
12
;=> 12

(type 12)
;=> java.lang.Long ;;

; im Unterschied zu Java ergibt Overflow eine Exception:

(+ Long/MAX_VALUE 1)
;=> ArithmeticException integer overflow 

; 1.1.2 Rechnen mit beliebiger Präzision:

12N
;=> 12N

(type 12N)
;=> clojure.lang.BigInt

; damit ist beliebige Präzision möglich

(+ Long/MAX_VALUE 1N)
;=> 9223372036854775808N

; Man kann auch beliebige Präzision erreichen, indem man spezielle Operatoren verwendet:
; +', -', *', inc', dec'

(+' Long/MAX_VALUE 1)
;=> 9223372036854775808N

(inc Long/MAX_VALUE)
;=> ArithmeticException integer overflow 

(inc' Long/MAX_VALUE)
;=> 9223372036854775808N

;; 1.1.3 Rechnen mit longs wie in Java

(unchecked-add Long/MAX_VALUE 1)
;=>-9223372036854775808 

(unchecked-multiply Long/MAX_VALUE 2)
;=> -2

;; 1.1.4 Funktionen mit ganzen Zahlen:

; Addition
(+ 12 30)
;=> 42

(+ 12 10 10 10)
;=> 42

; Subtraktion
(- 50 8)
;=> 42

(- 50 1 1 1 1 1 1 1 1)
;=> 42

; Multiplikation
(* 7 6)
;=> 42

(* 7 2 3)
;=> 42

; Division

(/ 84 2)
;=> 42

(/ 168 2 2)
;=> 42

; aber: siehe weiter unten rationale Zahlen
(/ 7 3)
;=> 7/3

; Inkrement

(inc 41)
;=> 42

; Dekrement

(dec 43)
;=> 42

; Ganzzahliger Quotient

(quot 84 2)
;=> 42

(quot 7 3)
;=> 2

; Rest

(rem 84 2)
;=> 0

(rem 7 3)
;=> 1

; Achtung -- wie in Java
(rem -5 2)
;=> -1

; Würde man also odd? definieren wie folgt
(defn my-odd? 
  [x]
  (= 1 (rem x 2)))

; ergäbe
(my-odd? -5)
;=> false

; siehe: Joshua Bloch und Neal Gafter: Java Puzzlers Puzzle 1 Oddity

; wie löst Clojure das Problem?
(source odd?)

;(defn odd?
;  "Returns true if n is odd, throws an exception if n is not an integer"
;  {:added "1.0"
;   :static true}
;  [n] (not (even? n)))

(source even?)
;(defn even?
;  "Returns true if n is even, throws an exception if n is not an integer"
;  {:added "1.0"
;   :static true}
;   [n] (if (integer? n)
;        (zero? (bit-and (clojure.lang.RT/uncheckedLongCast n) 1))
;        (throw (IllegalArgumentException. (str "Argument must be an integer: " n)))))

; (n & 1) == 0  ist gerade!

; Minimum und Maximum

(min 72 46 123456 42)
;=> 42

(max -23 12 8 11 42 35 27 -12345678)
;=> 42

(max -23 12 8 11 42 35 27 Long/MIN_VALUE)
;=> 42


;; 1.2 Rationale Zahlen

; Clojure hat rationale Zahlen eingebaut, warum?
(+ 0.1 0.1 0.1)
;=> 0.30000000000000004

(+ 1/10 1/10 1/10)
;=> 3/10

(+ 1 1/2)
;=> 3/2

(/ 1/2 1/2)
;=> 1N

(/ 1/2 2)
;=> 1/4

(* 1/2 2)
;=> 1N

(* 1/2 2/1)
;=> 1N

9/15
;=> 3/5

(inc 1/2)
;=> 3/2

(dec 1/2)
;=> -1/2

(quot 1/2 2)
;=> 0N

(quot 11/2 2)
;=> 2N

(rem 1/2 2)
;=> 1/2

(rem 5/2 2)
;=> 1/2

; Zähler
(numerator 41/7)
;=> 41

; Nenner
(denominator 41/7)
;=> 7

(rationalize 1.5)
;=> 3/2

(rationalize 1.33)
;=> 133/100

Math/PI
;=> 3.141592653589793

; Kann man pi zu einer rationalen Zahl machen?!
(rationalize Math/PI)
;=> 3141592653589793/1000000000000000

;; 1.3 Reelle Zahlen

; 1.3.1 Reelle Zahlen sind Java doubles
1.2
;=> 1.2

(class 1.2)
;=> java.lang.Double

(/ 1.2 3)
;=> 0.39999999999999997 

(* 1.2 0.3)
;=> 0.36

; 1.3.2 Beliebige Präzision

(/ 1.2M 3)
;=> 0.4M

(/ 22M 7)
;=> ArithmeticException Non-terminating decimal expansion; no exact representable decimal result.  
; Exception stammt von java.math.BigDecimal

(type 1.2M)

; kann man in Clojure leicht umschiffen:
(with-precision 10 (/ 22M 7))
;=> 3.142857143M

; Man kann die gewünschte Präzision und Rundungsart global setzen
(set! *math-context* (java.math.MathContext. 10 java.math.RoundingMode/FLOOR))
;=> #<MathContext precision=10 roundingMode=FLOOR>
(/ 22M 7)
;=> 3.142857142M

; 1.4 Gleichheit bei numerischen Werten

; 1.4.1 Gleichheit in derselben Kategorie
(= 1 1N)
;=> true

(= 1 (short 1))
;=> true

(= 7 (inc 6))
;=> true

; aber:
(= 1 1.0)
;=> false

(= 0.5 1/2)
;=> false

; Warum? = soll schnell sein, deshalb funktioniert es nur innerhalb derselben "Kategorie"
; von Zahlen

; 1.4.2 Numerische Gleichheit

(== 1 1.0)
;=> true

(== 0.5 1/2)
;=> true

; == geht nur mit numerischen Werten

(== 1 "foo")
;=> ClassCastException java.lang.String cannot be cast to java.lang.Number

(= 1 "foo")
;=> false

;; 2 Wahrheitswerte

; In Clojure sind false und nil false, alles andere ist true

(if "irgendwas" :t :f)
;=> :t

(if true :t :f)
;=> :t

(if (= 1 1) :t :f)
;=> :t

(if (= 1 0) :t :f)
;=> :f

(if false :t :f)
;=> :f

(if '() :t :f)
;=> :t

(if (next '()) :t :f)
;=> :f

(if nil :t :f)
;=> :f

; Es gibt auch Funktionen true? false?
; Sie testen auf die entsprechenden Java-Werte - also anders als if -- Vorsicht

(true? "irgendwas")
;=> false

(false? "irgendwas")
;=> false

(true? nil)
;=> false

(false? nil)
;=> false

(true? (= 1 1))
;=> true

(false? (= 1 1))
;=> false

;; 3 Zeichen und Zeichenketten

;; 3.1 Zeichen

; Literale Darstellung
\a
;=> \a

; Clojure char sind Java char
(class \a)
;=> java.lang.Character

(char 65)
;=> \A

(char \u2227)
;=> \∧ (logisches und)

(char-name-string \newline)
;=> "newline"

(char-escape-string \newline)
;=> "\\n"

(char 10)
;=> \newline

;; 3.2 Zeichenketten

; 3.2.1 Literale Darstellung von Strings
"irgendwas"

; Clojure Strings sind Java Strings
(class "irgendwas")
;=>  java.lang.String

; 3.2.2 Funktionen für Strings aus clojure.core

(str "Teil1" "Teil2" "Teil3")
;=> "Teil1Teil2Teil3"

(str 4 2)
;=> "42" 

(str)
;=> ""

(str nil)
;=> ""

(format "Hallo %s" "Clojure")
;=> "Hallo Clojure"

(string? "abc")
;=> true

(string? 42)
;=> false

(string? (str 4 2))
;=> true

; pr-str macht aus dem Argument einen String
(pr-str "hallo")
;=> "\"hallo\""

(pr-str 42)
;=> "42"

(pr-str 42 43)
;=> "42 43"

(prn-str 42)
;=> "42\n"

(prn-str 42 43 44)
;=> "42 43 44\n"

(print-str 42 43 44)
;=> "42 43 44"

(print-str "Hallo")
;=> "Hallo"

; 3.2.3 Funktionen aus clojure.string
(require '[clojure.string :as str])

(str/blank? "     ")
;=> true

(clojure.string/blank? "    ")

(str/capitalize "hallo")
;=> "Hallo"

(str/capitalize "ärger")
;=> "Ärger"

(str/join ["Hallo" " " "Clojure"])
;=> "Hallo Clojure" 

(str/lower-case "HALLO")
;=> "hallo"

(str/upper-case (str/lower-case "HALLo"))
;=> "HALLO"

(str/reverse "hallo")
;=> "ollah"

(str/trim "     in der Mitte      ")
;=> "in der Mitte"

(str/replace "Original" #"O" "A")
;=> Ariginal

(str/replace "Das Auto ist gelb." #"gelb" "rot")
;=> "Das Auto ist rot."

(str/replace "123456123456123456" #"1|3|5" "x")
;=> "x2x4x6x2x4x6x2x4x6"

; #"1|3|5" ist ein regulärer Ausdruck, Clojure unterstützt die komplette
; Java-Funktionalität für reguläre Ausdrücke

(str/replace "123456123456123456" #"1|3|5" {"1" "a" "3" "c" "5" "e"})
;=>"a2c4e6a2c4e6a2c4e6" 

; Wie das funktioniert ist ein bisschen ein Rätsel, das wir in der
; nächsten Vorlesung lösen werden

;; 4 Symbole

; Symbole stehen für etwas
(def s 12)
;=> #'fpc.vl07/s 

; Sie werten zu dem aus, wofür sie stehen
s

(def s')

s'
;=> Unbound

(def f #(+ %1 %2))
;=> #'fpc.vl07/f

f
;=> #<vl07$f fpc.vl07$f@18902ab>

(f 1 41)
;=>  42

; Symbols must begin with a non-numeric character, and can contain *, +, !, -, _,
; and ? in addition to any alphanumeric characters. Symbols that contain a slash (/)
; denote a namespaced symbol and will evaluate to the named value in the specified
; namespace

; Man kann die Auswertung von Symbolen verhindern, indem man sie quotiert
(quote (f 1 41))
;=> (f 1 41)

(quote s)
;=> s

's
;=> s

'(f 1 41)

(symbol "s'")
;=> s'

(symbol? 's)
;=> true

(symbol? 's')
;=> true

; gensym erzeugt neues Symbol mit einem eindeutigen Namen im aktuellen Namensraum
(gensym)
;=> G__1823

;; 5 Schlüsselworte

; Schlüsselworte sind symbolische Identifizerer, die zu sich selbst auswerten
; man kann sie vergleichen mit enums in Java
; Sie können einen Namensraum haben

:key
;=> key

::my-key
;=> :fpc.vl07/my-key

(keyword? "hallo")
;=> false

(def k :key)
;=> #'fpc.vl07/k

(keyword? k)
;=> true

(name k)
;=> "key"

(name ::my-key)
;=> "my-key"

(namespace k)
;=> nil

(namespace ::my-key)
;=> "fpc.vl07"

; Wir werden im kommenden Abschnitt sehen, wie man keywords einsetzt.