package lld.designpatterns.creational.factorymethod;

public class NetBankingPayment implements Payment {

  @Override
  public void processPayment(double amount) {
    System.out.println("Processing Net Banking payment of ₹" + amount);
  }
}
