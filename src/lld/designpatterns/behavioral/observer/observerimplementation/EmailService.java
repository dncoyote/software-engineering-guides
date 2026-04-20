package lld.designpatterns.behavioral.observer.observerimplementation;

import lld.designpatterns.behavioral.observer.observer.Observer;

public class EmailService implements Observer {

  @Override
  public void update(String event) {
    System.out.println("Email sent: " + event);
  }
}
