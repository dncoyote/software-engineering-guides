package lld.designpatterns.behavioral.strategy.strategyimplementations;

import lld.designpatterns.behavioral.strategy.strategy.PaymentStrategy;

public class WalletPaymentStrategy implements PaymentStrategy {

  @Override
  public void pay(double amount) {
    System.out.println("Processing Wallet payment: " + amount);
  }
}
