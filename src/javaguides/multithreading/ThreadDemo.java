package javaguides.multithreading;

public class ThreadDemo {

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Main started on: " + Thread.currentThread().getName());

    TaskThread tA = new TaskThread("A");
    TaskThread tB = new TaskThread("B");

    // Start NEW threads
    tA.start();
    tB.start();

    // Main waits until both threads finish
    tA.join();
    tB.join();

    System.out.println("Main finished on: " + Thread.currentThread().getName());
  }
}
