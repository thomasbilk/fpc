(use '(incanter core stats charts io))

(def votes (read-dataset "resources/iran_election_2009.csv"
                         :header true))
(view votes)

(def regions (sel votes :cols :Region))

(def ahmadinejad-votes (sel votes :cols :Ahmadinejad))
(def mousavi-votes (sel votes :cols :Mousavi))
(def rezai-votes (sel votes :cols :Rezai))
(def karrubi-votes (sel votes :cols :Karrubi))


;; erste ziffer

(def ahmadinejad (map first-digit ahmadinejad-votes))
(def mousavi (map first-digit mousavi-votes))
(def rezai (map first-digit rezai-votes))
(def karrubi (map first-digit karrubi-votes))


(defn benford-law [d] (log10 (plus 1 (div d))))
(def benford-probs (benford-law (range 1 11)))
(def benford-freq (mult benford-probs (count regions)))


(doto (xy-plot (range 1 10) (get-counts ahmadinejad)
               :legend true :series-label "Ahmadinejad"
               :y-label "First digit frequency"
               :x-label "First digit"
               :title "First digit frequency by candidate")
      (add-lines (range 1 10) (get-counts mousavi) 
                 :series-label "Mousavi")
      (add-lines (range 1 10) benford-freq 
                 :series-label "Predicted")
      (add-lines (range 1 10) (get-counts rezai) :series-label "Rezai")
      (add-lines (range 1 10) (get-counts karrubi) :series-label "Karrubi")
      clear-background
      view)


(def ahmadinejad-test 
  (chisq-test :table (get-counts ahmadinejad) 
              :probs benford-probs))
(:X-sq ahmadinejad-test) 
(:p-value ahmadinejad-test) 

(def mousavi-test 
  (chisq-test :table (get-counts mousavi) 
              :probs benford-probs))
(:X-sq mousavi-test) 
(:p-value mousavi-test) 

(def rezai-test 
  (chisq-test :table (get-counts rezai) 
              :probs benford-probs))
(:X-sq rezai-test) 
(:p-value rezai-test) 

(def karrubi-test 
  (chisq-test :table (get-counts karrubi) 
              :probs benford-probs))
(:X-sq karrubi-test) 
(:p-value karrubi-test) 


;; letzte ziffer

(defn last-digit [x] 
  (Character/digit (last (str x)) 10)) 

(def ahmadinejad-last (map last-digit ahmadinejad-votes))
(def mousavi-last (map last-digit mousavi-votes))
(def rezai-last (map last-digit rezai-votes))
(def karrubi-last (map last-digit karrubi-votes))


(defn get-zero-counts [digits] 
  (map #(get (:counts (tabulate digits)) % 0) 
       (range 0.0 10.0 1.0))) 

(doto (xy-plot (range 10) 
               (get-zero-counts ahmadinejad-last)
               :legend true 
               :series-label "Ahmadinejad"
               :y-label "First digit frequency"
               :x-label "First digit"
               :title "Last digit frequency by candidate")
      (add-lines (range 10) (get-zero-counts mousavi-last) 
                 :series-label "Mousavi")
      (add-lines (range 10) (get-zero-counts rezai-last) 
                 :series-label "Rezai")
      (add-lines (range 10) (get-zero-counts karrubi-last) 
                 :series-label "Karrubi")
      clear-background
      view)


(def ahmadinejad-test 
  (chisq-test :table (get-zero-counts ahmadinejad-last)))

(:X-sq ahmadinejad-test)
(:p-value ahmadinejad-test)

(def mousavi-test 
  (chisq-test :table (get-zero-counts mousavi-last)))
(:X-sq mousavi-test)
(:p-value mousavi-test)

(def rezai-test 
  (chisq-test :table (get-zero-counts rezai-last)))
(:X-sq rezai-test)
(:p-value rezai-test)

(def karrubi-test 
  (chisq-test :table (get-zero-counts karrubi-last)))
(:X-sq karrubi-test)
(:p-value karrubi-test)

