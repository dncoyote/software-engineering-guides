package javaguides.multithreading.lifecycle;

public class ThreadLifecycleDemo {

  public static void main(String[] args) throws Exception {

    Thread t = new Thread(() -> {
      try {
        System.out.println("Thread running...");
        Thread.sleep(2000); // TIMED_WAITING
        System.out.println("Thread finished");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    System.out.println("State after creation: " + t.getState()); // NEW

    t.start();
    System.out.println("State after start: " + t.getState()); // RUNNABLE

    Thread.sleep(500);
    System.out.println("State during sleep: " + t.getState()); // TIMED_WAITING

    t.join();
    System.out.println("State after completion: " + t.getState()); // TERMINATED
  }
}
