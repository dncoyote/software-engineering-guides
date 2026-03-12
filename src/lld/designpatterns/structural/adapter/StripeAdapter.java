package lld.designpatterns.structural.adapter;

public class StripeAdapter implements PaymentProcessor {

  private StripeAPI stripe;

  public StripeAdapter(StripeAPI stripe) {
    this.stripe = stripe;
  }

  @Override
  public void processPayment(double amount) {
    // Translate the call
    stripe.makePayment(amount);
  }
}
