package javaguides.functionalInterface.usage;

import javaguides.functionalInterface.PaymentProcessor;

public class AnotherPaymentApp{
  public static void main(String[] args) {
    PaymentProcessor payment = amount -> amount<5000;
    int paymentAmount = 5000;
    payment.log(paymentAmount);
    boolean success = payment.process(paymentAmount);

    System.out.println("payment status ->"+ success);
  }
}

