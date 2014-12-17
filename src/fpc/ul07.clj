; Funktionale Programmierung (in Clojure) Uebungen Serie 7
; Folgen
; (c) 2014 by Burkhardt Renz, THM

(ns fpc.ul07)

stop
; Für die folgenden Aufgaben verwenden Sie bitte die Funktionen, die wir bisher
; besprochen haben. Wir werden im nächsten Übungsblatt sehen, dass vieles auch viel
; eleganter geht.

; Aufgabe 1 Vektoren

; (a) Schreiben Sie eine Funktion vsquare, die aus einem Vektor einen
; anderen Vektor erstellt, der die Quadrate der Elemente enthält

(defn vsquare
  [vec]
  (let [n (dec (count vec))]
    (loop [curr 0, result [] ]
      (let [x (nth vec curr)]
      (if (= curr n)
        (conj result (* x x))
        (recur (inc curr) (conj result (* x x))))))))

(defn vsquare
  [vec]
  (let [n (count vec)]
    (loop [curr 0, result [] ]
      (let [x (nth vec curr 0)]
        (if (= curr n)
          result
          (recur (inc curr) (conj result (* x x))))))))
(vsquare [1 2 3])
;=> [1 4 9]

; (b) Schreiben Sie eine Funktion vinc, die aus einem Vektor einen
; anderen Vektor erstellt, dessen Element gerade um 1 höher sind.

(defn vinc
  [vec]
  (let [n (dec (count vec))]
    (loop [curr 0, result [] ]
      (let [x (nth vec curr)]
      (if (= curr n)
        (conj result (inc x))
        (recur (inc curr) (conj result (inc x))))))))

; Offenbar hat diese Funktion denselben Aufbau wie doe vorherige,
; also kann man eine Funktion höherer Ordnung dafür nehmen.

(defn vecfun
  [fun vec]
  (let [n (dec (count vec))]
    (loop [curr 0, result [] ]
      (let [x (nth vec curr)]
        (if (= curr n)
          (conj result (fun x))
          (recur (inc curr) (conj result (fun x))))))))

(defn vinc
  [vec]
  (vecfun inc vec))

(vinc [1 2 3])
;=> [2 3 4]

; (c) Schreiben Sie eine Funktion vmult, die die Elemente eines
; Vektors mit einer Zahl multipliziert

(defn vmult
  [vec n]
  (let [cnt (dec (count vec))]
    (loop [curr 0, result [] ]
      (let [x (nth vec curr)]
      (if (= curr cnt)
        (conj result (* x n))
        (recur (inc curr) (conj result (* x n))))))))

; oder mit vecfun:
(defn vmult
  [vec n]
  (vecfun (partial * n) vec))

(vmult [1 2 3] 2)
;=> [2 4 6]

; (d) Schreiben Sie eine Funktion, die aus zwei Vektoren gleicher Länge 
; das Skalarprodukt berechnet

(defn vskalarprod
  [vec1 vec2]
  (let [cnt (dec (count vec1))]
    (loop [curr 0, result 0 ]
      (let [x (nth vec1 curr), y (nth vec2 curr)]
      (if (= curr cnt)
        (+ result (* x y))
        (recur (inc curr) (+ result (* x y))))))))

(vskalarprod [1 2 3] [-7 8 9])
;=> 36

; Aufgabe 2 Hash-Maps

; (a) Erstellen Sie einen Vektor, der Hashmaps von Informationen über Bücher mit
; ISBN, Autor und Titel enthält

(def books
  [{:isbn "3-257-21755-2", :title "Schwarzrock", :author "Brian Moore"}
   {:isbn "3-257-21931-8", :title "Die Große Viktorianische Sammlung", :author "Brian Moore"}
	 {:isbn "3-518-37459-1", :title "Der Hauptmann und sein Frauenbataillon", :author "Mario Vargas Llosa"}
	 {:isbn "3-518-38020-6", :title "Tante Julia und der Kunstschreiber", :author "Mario Vargas Llosa"}
   {:isbn "3-499-22410-0", :title "Geschichte machen", :author "Stephen Fry"}
   {:isbn "3-257-06209-5", :title "Entwurf einer Liebe auf den ersten Blick", :author "Erich Hackl"}
   {:isbn "3-422-72318-3", :title "Längengrad", :author "Dana Zobel"}
   {:isbn "3-596-13399-8", :title "Glück Glanz Ruhm", :author "Robert Gernhardt"}
	 {:isbn "3-8031-3112-X", :title "Die nackten Masken", :author "Luigi Malerba"}
	 {:isbn "3-446-18298-5", :title "Erklärt Pereira", :author "Antonio Tabucchi"}
	 {:isbn "3-492-24118-2", :title "Mit brennender Geduld", :author "Antonio Skarmeta"}])

; (b) Schreiben Sie eine Funktion search-author, die in der Bücherliste nach Büchern eines Autors sucht

; Das geht natürlich viel einfacher, wenn wir Funktionen für seqs verwenden
; siehe nächstes Übungsblatt

(defn search-author
  [books name]
  (let [cnt (count books)]
    (loop [n 0, result []]
        (if (= n cnt) result
        (let [book (nth books n)
              author (:author book)]
          (recur (inc n) (if (= author name) (conj result book) result)))))))

(search-author books "Brian Moore")

; (c) Schreiben Sie eine Funktion get-author-set, die die Menge der Autoren der Bücher ausgibt

(defn get-author-set
  [books]
  (let [cnt (count books)]
    (loop [n 0, result #{}]
        (if (= n cnt) result
          (let [book (nth books n)]
          (recur (inc n) (conj result (:author book))))))))

(get-author-set books)

; (d) Sortieren Sie die Menge der Autoren

(sort (get-author-set books))

(apply sorted-set (get-author-set books))

; Was ist der Unterschied??

; Aufgabe 3 Rechtecke

#_(comment
  Wir betrachten Rechtecke in der euklidischen Ebene, deren linke untere Ecke am Punkt (0,0) liegt.
  Ein solches Rechteck wird durch die rechte obere Ecke (x,y) eindeutig bestimmt.
)

; (a) Wie kann man Rechtecke in Clojure repräsentieren?
;     Schreiben Sie eine Funktion (rectangle x y), die ein Rechteck "erzeugt".

(defn rectangle
  [x y]
  {:height y, :width x})
; Man könnte eber genauso einen Vektor nehmen

(def r1 (rectangle 1.1 2))

r1
;=> {:height 2, :width 1.1} 

; (b) Gegeben ein Rechteck rectangle, schreiben Sie Funktionen für die Bestimmung
; - der Höhe (height rectangle)
; - der Breite (width rectangle)
; - der Fläche (area rectangle)

(defn height 
  [rectangle]
  (:height rectangle))

(height r1)

(defn width
  [rectangle]
  (:width rectangle))

(width r1)

(defn area 
  [{:keys [height width]}]
  (* height width))

(area r1)

; (c) Schreiben Sie Funktionen für die Untersuchung von Rechtecken:

; - Ist es ein Quadrat? (square? rectangle)
; - Enthält es eines bestimmten Punkt? (contains-point? rectangle point)
;   Der Punkt sei repräsentiert durch einen Vektor mit x- und y-Koordinate
; - Liegt ein Rechteck innerhalb eines anderen? (contains-rectangle? outer inner)

(defn square?
  [{:keys [height width]}]
  (= height width))

(square? r1)
;=> false

(square? (rectangle 3 3))
;=> true

(defn contains-point?
  [{:keys [height width]} [x y]]
  (and (<= 0 x width) (<= 0 y height)))

(contains-point? r1 [-1 1])
;=> false

(contains-point? r1 [1 1])
;=> true

(contains-point? r1 [1.1 1])
;=> true
    
(contains-point? r1 [1.1 3])
;=> false

(defn contains-rectangle?
  [outer {:keys [height width]}]
  (contains-point? outer [width height]))

(contains-rectangle? r1 (rectangle 0.5 0.5))
;=> true

(contains-rectangle? r1 (rectangle 2.5 0.5))
;=> false
    