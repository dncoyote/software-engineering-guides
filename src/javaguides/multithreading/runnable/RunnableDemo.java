package javaguides.multithreading.runnable;

public class RunnableDemo {

  public static void main(String[] args) {

    CounterTask task = new CounterTask();

    Thread t1 = new Thread(task);
    Thread t2 = new Thread(task);
    Thread t3 = new Thread(task);

    t1.start();
    t2.start();
    t3.start();
  }
}
