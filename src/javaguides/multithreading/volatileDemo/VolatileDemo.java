package javaguides.multithreading.volatileDemo;

public class VolatileDemo {

  public static volatile boolean flag = false;

  public static void main(String[] args) {

    Thread t1 = new Thread(() -> {
      while (!flag) {
        //continue
      }
      System.out.println("Flag changed");
    });

    Thread t2 = new Thread(() -> {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      flag = true;
      System.out.println("Flag set to true");
    });

    t1.start();
    t2.start();
  }
}
