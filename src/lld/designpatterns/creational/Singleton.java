package lld.designpatterns.creational;

public class Singleton {

  private static volatile Singleton instance;
  // volatile ensures visibility across threads

  private Singleton() {
  }

  public static Singleton getInstance() {
    if (instance == null) { // First check (no locking)
      synchronized (Singleton.class) {
        if (instance == null) { // Second check (with locking)
          instance = new Singleton();
        }
      }
    }
    return instance;
  }
}
