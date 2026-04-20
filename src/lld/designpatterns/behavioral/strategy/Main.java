package lld.designpatterns.behavioral.strategy;

import lld.designpatterns.behavioral.strategy.service.PaymentService;
import lld.designpatterns.behavioral.strategy.strategy.PaymentStrategy;
import lld.designpatterns.behavioral.strategy.strategyimplementations.UpiPaymentStrategy;

public class Main {

  public static void main(String[] args) {

    PaymentStrategy strategy = new UpiPaymentStrategy(); // runtime decision

    PaymentService service = new PaymentService(strategy);
    service.processPayment(1000);
  }
}
