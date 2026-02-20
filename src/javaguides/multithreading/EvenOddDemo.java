package javaguides.multithreading;

public class EvenOddDemo {

  public static void main(String[] args) {
    NumberPrinter np = new NumberPrinter(10);

    Thread oddThread  = new Thread(()->np.printOdd());
    Thread evenThread  = new Thread(()->np.printEven());

    oddThread.start();
    evenThread.start();
  }
}
