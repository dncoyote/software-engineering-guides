package lld.designpatterns.structural.facade;

public class FacadeDemo{

  public static void main(String[] args) {

    OrderFacade orderFacade = new OrderFacade();

    orderFacade.placeOrder("Laptop", 75000);
  }
}
