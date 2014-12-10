(ns aufgaben.blatt07.aufgabe02)

; (a)

(def books [{:isbn 3550080417 :title "Buch 1" :author "Bob"}
            {:isbn 3833936509 :title "Buch 2" :author "Bill"}
            {:isbn 3426199190 :title "Buch 3" :author "Bert"}
            {:isbn 3426199230 :title "Buch 4" :author "Bob"}
            ])


; (b)

(defn search-author [books name]
  (let [cnt (count books)]
    (loop [n 0 result []]
      (if (= n cnt) result
        (let [book (nth books n)
              author (:author book)]
          (recur (inc n) (if (= author name) (conj result book) result)))))))

(search-author books "Bob")

; (c)

(defn get-author-set [books]
  (let [cnt (count books)]
    (loop [n 0 result #{}]
      (if (= n cnt) result
        (let [book (nth books n)]
          (recur (inc n) (conj result (:author book))))))))

(get-author-set books)

; (d)

(apply sorted-set (get-author-set books))
