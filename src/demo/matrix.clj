(use '(incanter core))

(def A (matrix [[1 2 3] [4 5 6] [7 8 9]])) ; 3x3 Matrix
(def A2 (matrix [1 2 3 4 5 6 7 8 9] 3)) ; die gleiche 3x3 matrix

(matrix 0 3 4) ; erzeugt eine 3x4 Matrix mit Nullen

(identity-matrix 4) ; erzeugt eine 4x4 Einheitsmatrix
(diag [1 2 3 4]) ; erzeugt eine 4x4 Diagonalmatrix
(diag (diag [1 2 3 4])) ; holt die Hauptdiagonale aus der Matrix

(def A (matrix [[1 2 3] [4 5 6] [7 8 9]]))
(def B (matrix [[1 2 3] [4 5 6] [7 8 9]]))
(plus A B)  ; Matrizenaddition
(mult 4 A)  ; Skalarmultiplikation
(mmult A B) ; Matrizenmultiplikation
