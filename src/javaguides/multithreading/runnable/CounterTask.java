package javaguides.multithreading.runnable;

class CounterTask implements Runnable {

  private int count = 0;

  @Override
  public void run() {
    count++;
    System.out.println(
        Thread.currentThread().getName() +
            " -> count = " + count);
  }
}
