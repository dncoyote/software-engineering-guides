package lld.designpatterns.structural.adapter;

public class AdapterDemo{

  public static void main(String[] args) {

    StripeAPI stripeAPI = new StripeAPI();

    PaymentProcessor processor = new StripeAdapter(stripeAPI);

    processor.processPayment(500);
  }
}
