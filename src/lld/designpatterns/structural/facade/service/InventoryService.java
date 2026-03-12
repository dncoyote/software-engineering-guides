package lld.designpatterns.structural.facade.service;

public class InventoryService {

  public boolean checkStock(String product) {
    System.out.println("Checking stock for " + product);
    return true;
  }
}
