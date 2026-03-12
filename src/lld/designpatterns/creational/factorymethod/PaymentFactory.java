package lld.designpatterns.creational.factorymethod;

public class PaymentFactory {

  public static Payment createPayment(String type) {

    if (type.equalsIgnoreCase("CARD")) {
      return new CardPayment();
    }

    if (type.equalsIgnoreCase("UPI")) {
      return new UpiPayment();
    }

    if (type.equalsIgnoreCase("NETBANKING")) {
      return new NetBankingPayment();
    }

    throw new IllegalArgumentException("Invalid payment type");
  }
}
