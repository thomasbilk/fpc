(ns aufgaben.blatt03.a301 )

; a 
((fn [name] (str "Hallo " name)) "Thomas")

; b
(#(str "Hallo "%) "Thomas")

; c
(def hallo (fn [name] (str "Hallo " name)))
(hallo "Thomas")

; d
(def hallo #(str "Hallo " %))
(hallo "Thomas")

; e
(defn hallo 
 "Greets you"
 [name]
 (str "Hallo " name))
(doc hallo)
(hallo "Thomas")
