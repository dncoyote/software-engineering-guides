package lld.designpatterns.creational.factorymethod;

public class CardPayment implements Payment {

  @Override
  public void processPayment(double amount) {
    System.out.println("Processing card payment of ₹" + amount);
  }
}
