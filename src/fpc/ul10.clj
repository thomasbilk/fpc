; Funktionale Programmierung (in Clojure) Uebungen Serie 10
; Identität, Zustand und Werte
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.ul10)

stop

; Aufgabe 1 Konten in Clojure

; (a) Schreiben Sie eine Funktion (make-account name no),
; die ein Konto für den Namen name und die Kontonummer no erzeugt.
; Das Konto soll so konstruiert sein, dass sichergestellt wird,
; dass der Saldo des Kontos niemals negativ wird.

(defn make-account
  "Konto mit Nummer no für Inhaber name"
  [no name]
  (let [saldo (ref 0 :validator #(>= % 0))]
    {:no no, :name name, :saldo-ref saldo}))

; Alternative Repräsentierung: Das gesamte Konte als ein Ref

(def kto12 (make-account 12 "hans"))

(:no kto12)
;=> 12

(:name kto12)
;=> "hans"

@(:saldo-ref kto12)
;=> 0


; (b) Schreiben Sie eine Funktion (deposit account amount),
; die auf dem Konto acount den Betrag amount zubucht,
; sowie eine Funktion (withdraw account amount), die ihn abbucht.

(defn deposit
  "Bucht amount auf Konto account"
  [account amount]
  (dosync
    (alter (:saldo-ref account) #(+ amount %))))

(deposit kto12 100)

@(:saldo-ref kto12)
;=> 100


(defn withdraw
  "Zieht amount von Konto account ab"
  [account amount]
  (dosync
    (alter (:saldo-ref account) #(- %  amount))))

(withdraw kto12 80)

@(:saldo-ref kto12)

; (c) Schreiben Sie eine Funktion (balance account), die den
; Saldo des Kontos ausgibt.

(defn balance
  "Saldo des Kontos"
  [account]
  @(:saldo-ref account))

(balance kto12)
;=> 20

(deposit kto12 20)

(balance kto12)
;=> 40

; (d) Schreiben Sie eine Funktion (transfer acc1 acc2 amount),
; die amount von Konto acc1 auf acc2 überweist.

(defn transfer
  "Überweisung von amount von acc1 auf acc1"
  [acc1 acc2 amount]
  (dosync
    (withdraw acc1 amount)
    (deposit  acc2 amount)))

(def kto15 (make-account 15 "gisela"))

(balance kto15)
;=> 0

(balance kto12)
;=> 40

(transfer kto12 kto15 20)

(balance kto15)
;=> 20

(balance kto12)
;=> 20


; Aufgabe 2 Eine Bank in Clojure

; (a) Repräsentieren Sie die Konten einer Bank in einer geeigneten
; Datenstruktur in Clojure. Dabei sollen die Kontonummern eindeutig sein.

; Repräsentation der Konten
; Die Bank hat Konten von der Sorte aus Aufgabe 1 in einer Menge
; von Maps mit {:ktonr n :konto acc}

(def bank
  (ref #{
    {:ktonr 12 :konto kto12}
    {:ktonr 15 :konto kto15} }))

(empty? (clojure.set/select #(= (:ktonr %) 12) @bank))
(empty? (clojure.set/select #(= (:ktonr %) 11) @bank))

; (b) Schreiben Sie eine Funktion (add-account bank account), die dem
; Bestand der Konten der Bank ein weiteres Konto hinzufügt.
; Beachten Sie die Eindeutigkeit der Kontonummer.

(defn add-account
  "Fügt dem Bestand der Bank ein Konto hinzu"
  [bank account]
  (dosync
    (let [ktonr (:no account)
          no-duplicate? (fn [bank nr] (empty? (clojure.set/select #(= (:ktonr %) nr) @bank)))]
     (if (no-duplicate? bank ktonr)
       (alter bank conj {:ktonr ktonr :konto account})))))

(def kto42 (make-account 42 "adam"))

(add-account bank kto42)


; (c) Schreiben Sie eine Funktion (bank-transfer bank no1 no2 amount),
; die in der Bank den Betrag amount vom Konto mit der Nummer no1 auf
; das mit Kontonummer no2 überweist.


(:konto (first (clojure.set/select #(= (:ktonr %) 15) @bank)))
(:konto (first (clojure.set/select #(= (:ktonr %) 42) @bank)))

; Achtung: keine Prüfung, ob es die Konten gibt!!
(defn bank-transfer
  "Überweisung in der Bank von no1 auf no2"
  [bank no1 no2 amount]
  (dosync
    (let [kto1 (:konto (first (clojure.set/select #(= (:ktonr %) no1) @bank)))
          kto2 (:konto (first (clojure.set/select #(= (:ktonr %) no2) @bank)))]
      (transfer kto1 kto2 amount))))

(bank-transfer bank 15 42 10)

(balance kto15)
;=> 10

(balance kto42)
;=> 10

; (d) Schreiben Sie eine Funktion (deposits bank), die die Summe der
; Guthaben bei der Bank ermittelt.

(defn deposits
  "Summe der Guthaben der Bank"
  [bank]
  (reduce + (map #(balance (:konto %)) @bank)))

(deposits bank)

; Aufgabe 3 Konkurrierende Zugriffe in Java

; (a) Konstruieren Sie Beispiele der Verwendung folgenden Java-Codes,
; die zeigen, dass man es so nicht machen sollte. Das Beispiel stammt aus dem Buch „Java Concur- rency in Practice“ von Brian Goetz et al.
                                                                                                                                                                                                         /**
;/** UnsafeSequence
; *
; * @author Brian Goetz and Tim Peierls
; */
;@NotThreadSafe
;public class UnsafeSequence {
; private int value;
; /**
;  * Returns a unique value.
;  */
; public int getNext() {
;   return value++;
; }
;}

; siehe Java UnsafeSequence.java

; (b) Schreiben Sie entsprechenden Code in Clojure und
; überprüfen Sie Ihre Szenarien aus (a) damit.

(defn counter
  []
  (let [value (atom 0)]
    #(swap! value inc)))

(def seq-gen (counter))

(seq-gen)


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

(wait-futures 4
  (dotimes [_ 10]
    (Thread/sleep 100)
    (println (seq-gen))))







