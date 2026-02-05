package javaguides.functionalInterface.usage;

import javaguides.functionalInterface.PaymentProcessor;

public class PaymentApp {
  public static void main(String[] args) {
    PaymentProcessor payment = amount -> amount <= 10000;

    int paymentAmount = 5000;
    payment.log(paymentAmount);
    boolean success = payment.process(paymentAmount);

    System.out.println("payment status ->" + success);
  }
}
