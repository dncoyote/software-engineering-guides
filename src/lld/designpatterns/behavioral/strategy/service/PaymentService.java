package lld.designpatterns.behavioral.strategy.service;

import lld.designpatterns.behavioral.strategy.strategy.PaymentStrategy;

public class PaymentService {

  private final PaymentStrategy paymentStrategy;

  public PaymentService(PaymentStrategy paymentStrategy) {
    this.paymentStrategy = paymentStrategy;
  }

  public void processPayment(double amount) {
    paymentStrategy.pay(amount);
  }
}
