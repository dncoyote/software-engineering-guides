package lld.designpatterns.structural.adapter;

public class StripeAPI {

  public void makePayment(double amount) {
    System.out.println("Stripe payment processed: " + amount);
  }
}
