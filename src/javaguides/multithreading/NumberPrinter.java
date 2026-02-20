package javaguides.multithreading;

public class NumberPrinter {

  private int number = 1;
  private final int max;

  NumberPrinter(int max) {
    this.max = max;
  }

  public synchronized void printOdd() {
    while (number <= max) {
      while (number % 2 == 0) {
        try {
          wait();
        } catch (InterruptedException e) {
          System.out.println(e);
        }
      }
      if (number <= max) {
        System.out.println("Odd Thread - " + number);
        number++;
        notify();
      }
    }
  }

  public synchronized void printEven() {
    while (number <= max) {
      while (number % 2 != 0) {
        try {
          wait();
        } catch (InterruptedException e) {
          System.out.println(e);
        }
      }
      if (number <= max) {
        System.out.println("Even Thread - " + number);
        number++;
        notify();
      }
    }
  }
}
