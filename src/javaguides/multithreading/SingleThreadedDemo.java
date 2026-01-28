package javaguides.multithreading;

public class SingleThreadedDemo {

  public static void main(String[] args) {
    System.out.println("Main started on: " + Thread.currentThread().getName());

    task("A");
    task("B");

    System.out.println("Main finished on: " + Thread.currentThread().getName());
  }

  static void task(String name) {
    for (int i = 1; i <= 5; i++) {
      System.out.printf("Task %s - step %d (thread=%s)%n",
          name, i, Thread.currentThread().getName());
      sleep(200);
    }
  }

  static void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
