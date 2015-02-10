(use '(incanter core stats charts optimize datasets latex io))

(def plot (function-plot sin -10 10))
(view plot)

(defn twoXSqaure [x] (* 2 (* x x )))
(view (function-plot twoXSqaure -5 5))

(derivative twoXSqaure)
(doto (function-plot twoXSqaure -5 5)
  (add-function (derivative twoXSqaure) -5 5)
  view)

(def simpleXYDataset (dataset ["x" "y"]
                       [[1 2] [3 8] [4 4] [8 2] [9 7]]))
(view(xy-plot :x :y :data simpleXYDataset))

(with-data simpleXYDataset
  (doto (xy-plot :x :y)
    (add-function #(* 10 (sin %)) 1 9)
    view))

(view (line-chart :x :y :data simpleXYDataset))


; Blumen dataset
(def iris (get-dataset :iris))

; Mittlere Kelchblatt-Breite
(def meanSepalWidth ($rollup mean  :Sepal.Width :Species iris))

; Diagramm
(view (bar-chart :Species :Sepal.Width :data meanSepalWidth))

(doto (bar-chart :Species :Sepal.Width :data meanSepalWidth)
  (add-categories :Species :Petal.Width :data ($rollup mean :Petal.Width :Species iris))
  view)


; Achsenbeschriftung und Titel
(def plotV (function-plot twoXSqaure -5 5
             :x-label "X-Wert"
             :y-label "Y-Wert"
             :title "Plot von drei Funktionen"))
(add-function plotV (derivative twoXSqaure) -5 5)
(add-function plotV #(* 50 (sin %)) -5 5)

; Text einfügen
(add-text plotV 0 0 "Nullpunkt")

; Pfeil einfügen
(add-pointer plotV 1 2 :text "Punkt (1,2)" :angle :se)

; Polygon einfügen
(add-polygon plotV [[-3.5 -8] [-2.5 -8] [-2.5 -15] [-3.5 -15]])


; Latex code
(def eq (str "f(x)=\\frac{\\sin(x^2)-x^3 \\exp(x)}{0.5}"))

; Funktion plotten und latex einfügen
(doto (function-plot #(/ (- (sin (* % %)) (* % % % (exp %))) 0.5) -5 1
        :y-label "Y"
        :x-label "X")
  (add-latex -2 -2 eq)
  (add-latex-subtitle (str "\\LaTeX \\text{ in Incanter}"))
  view)


; als PDF
(save-pdf plotExp "./pdf-chart.pdf")

; als PNG
(save plotExp "./png-chart.png" :height 700 :width 700)


; Funktion definieren: f(x) = ax^2 + bx
(defn fx [x a b] (+ (* x x a) (* x b)) )

; Dynamischer Plot
(let [x (range -3 3 0.1)]
  (def tPlot (dynamic-xy-plot [a (range -10 10 0.5)    ; Slider für a
                               b (range -10 10 0.5)]   ; Slider für b
               (for [xi x] [xi (fx xi a b)])           ; Ausdruck für Diagrammwerte
               :title "Dynamisches Diagramm"
               ))
  (doto tPlot
    (add-latex-subtitle (str "f(x)=ax^2+bx"))
    (set-x-label "X-Wert")
    (set-y-label "X-Wert")
    view))

(save tPlot "./png-chart-exp.png" :height 700 :width 700)
