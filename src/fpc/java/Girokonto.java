package fpc.java;

public class Girokonto extends Konto {
  
  public Girokonto(int ktoNummer, String ktoInhaber, double ktoSaldo) {
    super(ktoNummer, ktoInhaber, ktoSaldo);
    // TODO Auto-generated constructor stub
  }

  @Override
  public double zins() {
    return (ktoSaldo * 0.0001);
  }

}
