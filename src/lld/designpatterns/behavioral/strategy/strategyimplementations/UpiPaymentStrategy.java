package lld.designpatterns.behavioral.strategy.strategyimplementations;

import lld.designpatterns.behavioral.strategy.strategy.PaymentStrategy;

public class UpiPaymentStrategy implements PaymentStrategy {

  @Override
  public void pay(double amount) {
    System.out.println("Processing UPI payment: " + amount);
  }
}
