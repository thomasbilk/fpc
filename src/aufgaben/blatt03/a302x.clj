(ns aufgaben.blatt03.a0302x)

(def fa (fn [x] ((fn [x] (+ x 1)) (+ x 2))))

(fa 3)

(def fb (fn [x] ((fn [y] (+ y 1)) (+ x 2))))

(fb 3)
