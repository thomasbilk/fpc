(ns test-vl03
  (:require [clojure.test :refer :all]
            [fpc.vl03 :refer (sum-of-squares sum-of-squares-of-two-largest s2)]))

(deftest sum-of-squares-of-two-largest-test
  (is (= (sum-of-squares-of-two-largest 2 2 2) 8))
  (is (= (sum-of-squares-of-two-largest 2 3 4) 25))
  (is (= (sum-of-squares-of-two-largest 2 4 3) 25))  
  (is (= (sum-of-squares-of-two-largest 3 2 4) 25))
  (is (= (sum-of-squares-of-two-largest 3 4 2) 25))
  (is (= (sum-of-squares-of-two-largest 4 2 3) 25))
  (is (= (sum-of-squares-of-two-largest 4 3 2) 25))
  (is (= (sum-of-squares-of-two-largest 2 2 3) 13))
  (is (= (sum-of-squares-of-two-largest 2 3 2) 13))
  (is (= (sum-of-squares-of-two-largest 3 2 2) 13)))

(deftest s2-test
  (is (= (s2 2 2 2) 8))
  (is (= (s2 2 3 4) 25))
  (is (= (s2 2 4 3) 25))  
  (is (= (s2 3 2 4) 25))
  (is (= (s2 3 4 2) 25))
  (is (= (s2 4 2 3) 25))
  (is (= (s2 4 3 2) 25))
  (is (= (s2 2 2 3) 13))
  (is (= (s2 2 3 2) 13))
  (is (= (s2 3 2 2) 13)))