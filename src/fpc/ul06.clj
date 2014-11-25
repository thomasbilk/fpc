; Funktionale Programmierung (in Clojure) Uebungen Serie 6
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.ul06)

stop

; Aufgabe 1 (Komposition von Funktionen)

; (a) Schreiben Sie eine Funktion, die von einer Zahl n 2 abzieht, ohne dass das Zeichen
;     - im Code auftaucht.

(def minus2 (comp dec dec))

(minus2 2)
; => 0
(minus2 3)
; => 1
(minus2 0)
; => -2


; (b) Gegeben sei die Funktion (defn sq[x] (* x x)). Schreiben Sie eine Funktion sqplus,
;     die eine Zahl um 1 erhöht und dann quadriert. Im Code darf + nicht vorkommen.

(defn sq [x] (* x x))

(def sqplus (comp sq inc))

(sqplus 4)
; => 25

; Aufgabe 2

; Wenn f eine numerische Funktion und n eine positive ganze Zahl ist, 
; dann können wir die n-fach wiederholte Anwendung von f bilden, die 
; als Funktion von x mit dem Wert f(f(...(f(x))...)) definiert ist. 
; Wenn z. B. f(x) = x+1 ist, dann ist die n-fach wiederholte Anwendung 
; von f die Funktion g(x) = x+n. 
; Wenn z. B. f die Quadrierung ist, dann ist g die 2-hoch-n-te Potenzierung. 
; Schreiben Sie eine Prozedur "n-fach" mit einer Prozedur f und einer 
; positiven ganzen Zahl n als Parameter, die sich z. B. folgendermaßen verwenden läßt: 

; ((n-fach quadrat 3) 5)
; => 390625

; Hinweis: Denken Sie an comp

(defn n-fach 
  [f n]
  (loop [cur 1, result f]
    (if (= cur n)
        result
        (recur (inc cur) (comp f result)))))

((n-fach inc 3 ) 5)
;=> 8

((n-fach #(* % %) 3) 5)
;=> 390625

; Aufgabe 3

; Gegeben sei:

(defn f [g] (g 2))
  
;(a) Beispiele:

(f #(* % %))
;=> 4

(f (fn [z] (* z (inc z))))
;=> 6

; Was passiert, wenn wir (f f) auswerten lassen? Genaue Erklärung?

(f f)
;=> ClassCastException java.lang.Long cannot be cast to clojure.lang.IFn

; f wendet die übergebene Funktion auf die Zahl 2 an
; in diesem Fall ist die übergebene Funktion f selbst
; d.h. f erhält das Argument 2
; dann wird dieses Argument als Funktion interpretiert -> Fehlermeldung
