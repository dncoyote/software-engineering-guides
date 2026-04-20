package lld.designpatterns.behavioral.observer;

import lld.designpatterns.behavioral.observer.service.OrderService;
import lld.designpatterns.behavioral.observer.observerimplementation.*;

public class Main {

  public static void main(String[] args) {

    OrderService orderService = new OrderService();

    orderService.addObserver(new EmailService());
    orderService.addObserver(new SmsService());
    orderService.addObserver(new AnalyticsService());

    orderService.placeOrder();
  }
}
