package lld.designpatterns.structural.facade.service;

public class PaymentService {

  public void processPayment(double amount) {
    System.out.println("Processing payment of ₹" + amount);
  }
}
