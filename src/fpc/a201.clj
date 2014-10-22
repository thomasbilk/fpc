(ns
  ^{:author tom}
  fpc.a201)

; a)    +
;      / \
;     +  1
;    / \
;   2  3

; b)    +
;      / \
;     *   4
;    / \
;   2   3

; c)    *
;      / \
;     2   +
;        / \
;       3   4

; d)        or
;          /  \
;        and   true
;       /   \
;   true     false

; e)      -
;        / \
;   square  7
;     |
;     7

; f)     or
;      /    \
;     =    not=
;    / \   /  \
;   1   1 1    1

; g)      *
;       /   \
;      +     +
;     / \   / \
;    3   4 2   5
