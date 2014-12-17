; Funktionale Programmierung (in Clojure) Vorlesung 11
; Zustand und Zustandsaenderung in Clojure
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.vl11
  (:require [clojure.repl :as repl :refer [doc]]))

stop

; Atoms -----------------------------------------------------------
; Atoms enthalten Referenzen auf Werte, sie sind die Identität, der
; Wert dieser Identität kann sich im Laufe der Zeit ändern.
; Die Änderung erfolgt synchron und unkoordiniert, d.h. nur die
; Änderung eines einzelnen Atoms wird überwacht

(def age (atom 25))

age

(deref age)
; => 25

@age
; => 25

;swap! ändert das Atom mit der übergebenen Funktion
(swap! age inc)
; => 26 

@age
; => 26

; dabei wird überprüft, ob das Atom noch den erwarteten
; Wert hat
(compare-and-set! age 26 27)
; => true

(compare-and-set! age 26 27)
; => false -- denn jetzt ist age ja schon 27

; reset! setzt Wert
(reset! age 30)
; => 30

@age
; => 30

; Makros zur Demonstration vom MVCC aus Clojure Programming S.173
; müssen wir jetzt nicht unbedingt verstehen

(defmacro futures
  "erzeugt n futures für jeden übergebenen Ausdruck"
  [n & exprs]
  (->> (for [expr exprs]
         `(future ~expr))
    (repeat n)
    (mapcat identity)
    vec))

(defmacro wait-futures
  "erzeugt n futures für jeden übergebenen Ausdruck und blockiert bis alle fertig sind"
  [& args]
  `(doseq [f# (futures ~@args)]
     @f#))

; Beispiel Clojure Programming S.174
; Was passiert, wenn zwei Threads gleichzeitig ein Atom ändern wollen

(def xs (atom #{1 2 3}))

(defn f4 [v]
  (Thread/sleep 250)
  (println "trying 4")
  (conj v 4))

(defn f5 [v]
  (Thread/sleep 500)
  (println "trying 5")
  (conj v 5))

(wait-futures 1 (swap! xs f4)
                (swap! xs f5))

@xs
; trying 4
; trying 5
; trying 5

; Woran liegt das?

; Beide Threads starten etwa gleichzeitig und wollen xs ändern
; f4 macht zuerst die Änderung, f5 versucht die Änderung, stellt
; aber fest, dass f4 den Wert verändert hat.
; f5 wird abgebrochen und neu gestartet. Jetzt ist f4 fertgi und es tritt
; kein Konflikt mehr auf

(comment
  Clojure verwendet Software Transactional Memory STM - dies
  implementiert MVCC Multiversion Concurrency Control

  Beim Lesen erhält jede Transaktion einen Snapshot des Atoms,
  beim Schreiben wird die "disjoint write property" überwacht.

  Tritt ein Konflikt auf, wird eine Transaktion abgebrochen und
  vom STM automatisch neu gestartet.
)

; Atom mit Validator
; Man kann Werte kontrollieren, die ein Atom annehmen kann
(def age (atom 19 :validator #(>= % 18)))

@age

(swap! age dec)
; => 18

(swap! age dec)
; => IllegalStateException Invalid reference state

; Watches
; wie ein Trigger in einer Datenbank
(defn echo-watch
  [key ref old new]
  (println key old "=>" new))

(add-watch age :echo echo-watch)

(swap! age inc)

(reset! age 25)
; Man sieht, dass in beiden Fällen die Funktion aufgerufen wird

; Atoms in closures -------------------------------------------------------------
; Man kann atoms auch in closures verwenden -> closures sind 
; äquivalent zu Objekten in OO-Sprachen
;; Counter in Clojure

(defn make-counter [init-val]
  (let [cnt (atom init-val)]
    #(swap! cnt inc)))

(defn make-counter2 [init-val]
  (let [cnt (atom init-val)]
    {:inc #(swap! cnt inc)
     :reset #(reset! cnt 0)}))

(def c3 (make-counter2 0))

((:inc c3))

((:reset c3))

(def counter (make-counter 0))

(counter)
; zählt hoch

(def c2 (make-counter 0))

(c2)
; zählen unabhängig voneinander

; Sobald wir in Closures Referenzen verwenden, die ihren Zustand verändern können,
; können wir das Substitutionsmodell nicht mehr einsetzen.
; Die Funktion c2 liefert bei jedem Aufruf einen anderen Wert.

; Was man jetzt zur Interpretation braucht, ist das sogenannte
; "Umgebungsmodell" -
; Jede Closure definiert eine Umgebung.
; Für die Berechnung des Werts der Funktionen müssen wir nun alle Referenzen
; berücksichtigen, die im jeweiligen Scope verwendet werden und die Referenz
; durch den je aktuell gültigen Wert ersetzen


; Refs mit Konto --------------------------------------------------------------------

; Record für Konto
(defrecord Account [id balance])

; Zwei Refs für Konto 1 und 2 mit Saldo 100
(def a1-ref (ref (Account. "1" 100)))
(def a2-ref (ref (Account. "2" 100)))

@a1-ref

; Ein paar Operationen mit Konten

(defn withdraw
  "Withdraws an amount of money from an account"
  [account amount]
  (assoc account :balance (- (:balance account) amount))) 

(def a1 (Account. "1" 100))
(withdraw a1 20)
; => #fpc.vl11.state.Account{:id "1", :balance 80}

(defn deposit
  "Deposits an amount of money to an account"
  [account amount]
  (assoc account :balance (+ (:balance account) amount))) 

(deposit a1 20)
; => #fpc.vl11.state.Account{:id "1", :balance 120}

(defn transfer
  "Transfers an amount of money from one account to another"
  [from to amount]
  (alter from withdraw amount)
  (alter to deposit amount))

(def a2 (Account. "2" 100))

; Nun wollen wir mal eine Überweisung machen
(transfer a1 a2 20)
; => ClassCastException fpc.vl11.state.Account cannot be cast to clojure.lang.Ref
; alter geht nur mit Refs und in einer Transaktion

; Die Überweisung erfordert die atomare Änderung zweier Referenzen.
; Auch hier kommt jetzt das STM in Einsatz:
; Synchron und koordiniert

; Die Anwendung muss mit (dosync ...) die Grenzen der Transaktion festlegen

(defn account-info [a-ref]
  (println "id" (:id @a-ref) "balance" (:balance @a-ref)))

(account-info a1-ref)
  
; Also brauchen wir Transaktionen für unseren beiden Refs von oben

(defn go-withdraw! [account amount ms]
  (dosync
    (do
      (Thread/sleep ms)
      (println (.getName (Thread/currentThread)) "attempts to withdraw")
      (alter account withdraw amount)
      (println (.getName (Thread/currentThread)) "tries to commit"))))
      
(defn go-transfer! [from to amount ms]
  (dosync
    (do
      (Thread/sleep ms)
      (println (.getName (Thread/currentThread)) "attempts to transfer")
      (transfer from to amount)
      (println (.getName (Thread/currentThread)) "tries to commit"))))
      

(let [t1 (Thread. #(go-transfer! a1-ref a2-ref 100 150) "Thread 1")
      t2 (Thread. #(go-withdraw! a2-ref 50 100) "Thread 2")]
  (.start t1)
  (.start t2)
  (.join t1)
  (.join t2)
  (account-info a1-ref)
  (account-info a2-ref))
  
; In der Konsole kann man sehen, dass t1 zweimal versucht zu überweisen.
; Auch hier tritt wieder ein Schreibkonflikt auf.

; STM bricht eine der beiden Transaktionen, hier t1 ab und startet sie
; dann neu.

; Beispiel mit commute
(defn go-withdraw! [account amount ms]
  (dosync
    (do
      (Thread/sleep ms)
      (println (.getName (Thread/currentThread)) "attempts to withdraw")
      (commute account withdraw amount)
      (account-info account)
      (println (.getName (Thread/currentThread)) "tries to commit"))))

; zurücksetzen
(dosync
  (ref-set a1-ref (Account. "1" 100)))
  
(let [t1 (Thread. #(go-withdraw! a1-ref 30 100) "Thread 1")
      t2 (Thread. #(go-withdraw! a1-ref 20 100) "Thread 2")]
  (.start t1)
  (.start t2)
  (.join t1)
  (.join t2)
  (account-info a1-ref))

(account-info a1-ref)

; wenn man Glück hat, sieht man, dass einmal der Saldo 80 und einmal 70 ist,
; erst ganz am Ende stimmt wieder alles.

; commute -- der Name der Funktion kommt von kommutativ
; in der Transaktion spielt die Reihenfolge der Aktionen keine Rolle
; a + b = b + a (Kommutativgesetz)

; deshalb wird die erste Transaktion nicht wirklich abgebrochen und neu
; gestartet, sondern wenn ein Schreibkonflikt entdeckt wird, wird einfach
; die Aktion beim commit wiederholt.

; dadurch bessere Performance

; Agents -----------------------------------------------------------------------------

; Dieser Typ von Referenz ist asynchron und unkoordiniert.

(def a (agent 42))

(deref a)
; => 42

(send a (fn [x] (dotimes [_ 10000000000] x) (inc x)))
(deref a)
; => 42

(deref a)
; => 43 -- etwas verzögert!

; Der Agent repräsentiert den Wert einer Berechnung, den er
; schließlich zur Verfügung stellt


; Beispiel für die Verwendung von Agents zur Parallelisierung

; Passwörter knacken, inspiriert durch
; http://travis-whitton.blogspot.de/2009/06/clojures-agents-scientists-monkeys-and_18.html

; Wir wollen Passwörter, die aus 4 Buchstaben bestehen knacken

; Zuerst erzeugen wir alle möglichen Klartexte
; lazy seq aller möglicher Klartexte
(def words
  (let [chars (map char (range (int \a) (inc (int \z))))]
    (for [l1 chars, l2 chars, l3 chars, l4 chars]
      (str l1 l2 l3 l4))))

; Sehen wir mal die ersten 5 an
(take 5 words)

; Wir wissen, dass die Passwörter durch den md5-Hash-Algorithmus verschlüsselt
; sind

; md5sum
(import '(java.security NoSuchAlgorithmException MessageDigest))

(defn md5sum
  "calculates MD5 hashes"
  [^String str]
  (let [alg (doto (MessageDigest/getInstance "MD5")
            (.reset)
            (.update (.getBytes str)))]
  (try
    (.toString (new BigInteger 1 (.digest alg)) 16)
    (catch NoSuchAlgorithmException e
      (throw (new RuntimeException e))))))

(md5sum "abcd")
; => "e2fc714c4727ee9395f324cd2e7f331f"

; Memoize merkt sich einmal berechnete Werte
; Da wir immer wieder die Hashwerte berechnen müssen, sind wir durch
; memoize schneller
(def md5-memo (memoize md5sum))


; wir entschlüsseln ein Passwort, indem wir es einfach mit den
; Hashwerten aller möglichen Passworte vergleichen und damit den
; Klartext finden -- brute force

(defn decode-md5 [md5]
  (first (filter #(= md5 (md5-memo %)) words)))

(decode-md5 "e2fc714c4727ee9395f324cd2e7f331f")
; => "abcd"
(decode-md5 (md5sum "zzzz"))

; Diese Funktion entschlüsselt gleich einen ganzen Haufen von Passwörtern
(defn decode-bucket [bucket]
  (map decode-md5 bucket))

; Nun wollen wir mal ausprobieren:

; produziert size zufällige kodierte Passwörter
(defn random-bucket [size]
  (vec (repeatedly size #(md5-memo (rand-nth words)))))

; Zum Ausprobieren nehmen wir einen Haufen von 40 Passwörtern
(def large-bucket (random-bucket 40))

large-bucket


; Nun messen wir mal, wie lange das Knacken der 40 dauert:
(time 
  (println (decode-bucket large-bucket)))

; => 8 sec oder so

; Können wir das beschleunigen, in dem wir Agenten einsetzen?
; Verteilen auf Agenten

; Zahl der Agenten
(def num-agents 10)
 
; Aufteilen der Arbeit
(def work-buckets (partition (int (/ (count large-bucket)
                                   num-agents)) large-bucket))

work-buckets
; Jeder Haufen für jeden Agten hat nun 4 Passwörter

; Pro Bucket wird ein Agent gestartet
(defn spawn-agents [agents buckets]
  (if (empty? buckets)
    agents
    (recur (conj agents (agent (first buckets)))
         (rest buckets))))

(comment
  (def agents (spawn-agents [] work-buckets))

  ; Jeder Agent kriegt die Dekodierfunktion für seinen Teil
  (doseq [agent agents]
    (send agent decode-bucket))

  ; Warten bis die Agenten fertig sind
  (apply await agents)

  ; Ergebnis
  (doseq [agent agents]
    (doseq [result @agent] (println result)))

  ; Aufräumen
  (shutdown-agents)
)

; Messung
(time
  (let [agents (spawn-agents [] work-buckets)]
  (doseq [agent agents]
	    (send agent decode-bucket))
	  (apply await agents)
    (doseq [agent agents]
      (println @agent))))
; => auch um die 7 sec

; Ergebnis: die Aufteilung auf Agenten bringt nicht viel
; Man bräuchte wahrscheinlich einen Rechner mit mehr Kernen,
; außerdem kostet die Ausgabe wohl viel Geld

; Immerhin sieht man, dass die Ergebnisse in Flgen zu 4 Stück
; von den Agenten geliefert werden.

; Vars ---------------------------------------------------------------------------------

; Vars sind Referenzen, mit denen ein Wert an ein Symbol gebunden wird.

(def ^:private intim 42)

intim
; => 42

; Namensraum wechseln
(ns other)

intim
; =>java.lang.RuntimeException: Unable to resolve symbol: intim in this context


; Ctrl-Shift-N wechselt in den Namensraum der Datei zurück

; docstring
(def a
  "Die ultimative Zahl"
  42)

(doc a)

(meta (var a))

; constants
(def ^:const pi (Math/PI))

(comment
  Welche Wirkung hat ^:const?

  Normalerweise wird der Wert eines Symbols in der Root-Umgebung gemerkt
  und bei jeder Verwendung wird der Wert dort ermittelt.

  Wenn man eine Var mit ^:const versieht, kann der Compiler Code erzeugen,
  bei dem der Wert direkt (inline) eingesetzt wird.

  Das bringt eine Beschleunigung.
)

; Delays --------------------------------------------------------------------------------

; Ein Delay ist eine asynchrone Berechnung, die genau einmal ausgeführt wird,
; nämlich beim ersten Dereferenzieren des Delays

; Kann man für eine Berechnung verwenden, die man evtl. später braucht
; Wird nur einmal ausgeführt

(def d (delay (println "Delay wird ausgeführt")
              :fertig))

(realized? d)
; => false

@d
; (force d) führt auch zur Auswertung

(realized? d)
; => true

@d
; Beim zweiten Aufruf wird println nicht mehr ausgeführt

; Futures ------------------------------------------------------------------------------

; Ein Future ist das Aufmachen eines Threads mit einer Berechnung, die man später
; (in der Zukunft) benötigt.

; Clojure verwendet einen Thread-Pool für die Futures

(def f (future (apply + (range 1e8))))

(realized? f)
; => false, wenn man nicht zu lange wartet

(deref f)
; => 4999999950000000

; Promises ------------------------------------------------------------------------------

; Ähnlich wie ein Delay, aber der Wert kommt nicht durch eine Berechnung zustande,
; sondern wird mit deliver gesetzt, z.B. in einem anderen Thread.

(def p (promise))

(realized? p)
; => false

(deliver p 42)

(realized? p)
; => true

@p
;=> 42

; Beispiel für Datenfluss-Steuerung Clojure Programming S.164

; Wir definieren 3 Promises und verwenden Sie um Datenfluss zu steuern
(def a (promise))
(def b (promise))
(def c (promise))

; In einem neuen Thread soll c als Wert die Summe der Promises a und b
; bekommen
(future
  (deliver c (+ @a @b))
  (println "c hat jetzt einen Wert"))

(realized? c)
;=> false

(deliver a 21)
(realized? c)
;=> false

(deliver b 21)
(realized? c)
;=> true

(deref c)