package lld.designpatterns.creational.factorymethod;

public class FactoryMethodDemo {

  public static void main(String[] args) {

    Payment payment = PaymentFactory.createPayment("UPI");

    payment.processPayment(1000);
  }
}
