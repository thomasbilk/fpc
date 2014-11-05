(ns aufgaben.a303
  (:require [aufgaben.a302 :refer (eval-test)]))

; a 
;strikt & verzögert
(eval-test 2 3)
(* 2 2)
; 4

; b
; strikt 
(eval-test (+ 3 4) 8)
(eval-test 7 8)
(* 7 7)
49
  
; verzögert
(eval-test (+ 3 4) 8)
(* (+ 3 4) (+ 3 4))
(* 7 (+ 3 4))
(* 7 7)
49

; c
; strikt 
(eval-test 7 (* 2 4))
(eval-test 7 8)
(* 7 7)
49

; verzögert
(eval-test 7 (* 2 4))
(* 7 7)
49

; d
; strikt 
(eval-test (+ 3 4) (* 2 4))
(eval-test 7 (* 2 4))
(eval-test 7 8)
(* 7 7)
49

; verzögert
(eval-test (+ 3 4) (* 2 4))
(* (+ 3 4) (+ 3 4))
(* 7 (+ 3 4))
(* 7 7)
49
           