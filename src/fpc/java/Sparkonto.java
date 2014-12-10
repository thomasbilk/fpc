package fpc.java;

public class Sparkonto extends Konto {

  public Sparkonto(int ktoNummer, String ktoInhaber, double ktoSaldo) {
    super(ktoNummer, ktoInhaber, ktoSaldo);
    // TODO Auto-generated constructor stub
  }

  @Override
  public double zins() {
    return (ktoSaldo * 0.007);
  }

}
