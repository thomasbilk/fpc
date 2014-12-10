(ns aufgaben.blatt07.aufgabe03)

; (a)

(defn rectangle "creates a rectangle" [x y]
  {:x x :y y})

(rectangle 4 3)

; (b)

(defn height "get the height of a rectangle" [rect]
  (:y rect))

(defn width "get the width of a rectangle" [rect]
  (:x rect))

(defn area "gets the area of a rectangle" [rect]
  (* (:x rect) (:y rect)))

(height (rectangle 5 7))
(width (rectangle 5 7))
(area (rectangle 5 7))

; (c)

(defn square? [rect]
  (= (:x rect) (:y rect)))

(square? (rectangle 3 3))
(square? (rectangle 3 4))

(defn contains-point? [rect point]
  (and
    (and
      (<= 0 (first point))
      (<= (first point) (:x rect)))
    (and
      (<= 0 (second point))
      (<= (second point) (:y rect)))))

(contains-point? (rectangle 5 5) [4 4])
(contains-point? (rectangle 5 5) [4 6])
(contains-point? (rectangle 5 5) [6 4])
(contains-point? (rectangle 5 5) [-1 2])
