package lld.designpatterns.structural.facade;

import lld.designpatterns.structural.facade.service.InventoryService;
import lld.designpatterns.structural.facade.service.ShippingService;
import lld.designpatterns.structural.facade.service.PaymentService;
import lld.designpatterns.structural.facade.service.NotificationService;

public class OrderFacade {

  private InventoryService inventoryService;
  private PaymentService paymentService;
  private ShippingService shippingService;
  private NotificationService notificationService;

  public OrderFacade() {
    inventoryService = new InventoryService();
    paymentService = new PaymentService();
    shippingService = new ShippingService();
    notificationService = new NotificationService();
  }

  public void placeOrder(String product, double amount) {

    if (inventoryService.checkStock(product)) {

      paymentService.processPayment(amount);

      shippingService.shipProduct(product);

      notificationService.sendConfirmation();

      System.out.println("Order placed successfully");
    } else {
      System.out.println("Product out of stock");
    }
  }
}
