package lld.designpatterns.behavioral.observer.service;

import lld.designpatterns.behavioral.observer.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

  private final List<Observer> observers = new ArrayList<>();

  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  public void placeOrder() {
    System.out.println("Order placed");

    notifyObservers("Order placed");
  }

  private void notifyObservers(String event) {
    for (Observer observer : observers) {
      observer.update(event);
    }
  }
}
