package lld.designpatterns.creational.factorymethod;

public class UpiPayment implements Payment {

  @Override
  public void processPayment(double amount) {
    System.out.println("Processing UPI payment of ₹" + amount);
  }
}
