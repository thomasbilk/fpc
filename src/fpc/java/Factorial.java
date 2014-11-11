package fpc.java;

import javax.swing.JOptionPane;

public class Factorial {

  public static void main(String[] args) {

    int result = 1;
    int n = 0;
    
    // Input
    String in = JOptionPane.showInputDialog("Zahl eingeben:");
    n = Integer.parseInt(in);
    
    // Berechnung in einer iterativen Schleife
    for ( int i = 1; i <= n; i++ )
      result = result * i;
    
    System.out.println( "Factorial of " + n + ": " + result );
  }

}
