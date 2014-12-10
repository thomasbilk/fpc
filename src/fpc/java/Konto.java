package fpc.java;

public abstract class Konto {
  
  int    ktoNummer;
  String ktoInhaber;
  double ktoSaldo;

  public Konto(int ktoNummer, String ktoInhaber, double ktoSaldo) {
    super();
    this.ktoNummer = ktoNummer;
    this.ktoInhaber = ktoInhaber;
    this.ktoSaldo = ktoSaldo;
  }
  
  abstract public double zins();


  public static void main(String[] args) {
    
    // Wir erzeugen zwei Konten:
    Konto k1 = new Girokonto( 100, "Hans", 100);
    Konto k2 = new Sparkonto( 101, "Peter", 100);
    
    // Welche Zinsen gibt es?
    System.out.println( String.format("Zinsen von Konto %d: %.2f Euro" , k1.ktoNummer, k1.zins()));
    System.out.println( String.format("Zinsen von Konto %d: %.2f Euro" , k2.ktoNummer, k2.zins()));
  }

}
