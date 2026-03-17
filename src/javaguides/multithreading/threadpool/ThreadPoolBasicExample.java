package javaguides.multithreading.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolBasicExample {

  public static void main(String[] args) {

    // Create a thread pool with 3 threads
    ExecutorService executor = Executors.newFixedThreadPool(3);

    // Submit 6 tasks
    for (int i = 1; i <= 6; i++) {
      int taskId = i;

      executor.submit(() -> {
        System.out.println(
            "Task " + taskId +
                " started by " + Thread.currentThread().getName());

        try {
          Thread.sleep(2000); // simulate work
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }

        System.out.println(
            "Task " + taskId +
                " completed by " + Thread.currentThread().getName());
      });
    }

    executor.shutdown();
  }
}
