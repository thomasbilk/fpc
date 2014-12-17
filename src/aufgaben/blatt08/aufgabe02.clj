(ns aufgaben.blatt08.aufgabe02)

(defn char-cnt [ch inp]
  (count (filter #(= ch %) inp)))

(char-cnt \e "Hallo Freunde wie geht es euch?")
(char-cnt \x "Hallo Freunde wie geht es euch?")
