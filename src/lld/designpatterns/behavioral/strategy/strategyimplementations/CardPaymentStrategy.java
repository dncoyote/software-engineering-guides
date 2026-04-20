package lld.designpatterns.behavioral.strategy.strategyimplementations;

import lld.designpatterns.behavioral.strategy.strategy.PaymentStrategy;

public class CardPaymentStrategy implements PaymentStrategy {

  @Override
  public void pay(double amount) {
    System.out.println("Processing Card payment: " + amount);
  }
}
