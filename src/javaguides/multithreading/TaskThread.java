package javaguides.multithreading;

class TaskThread extends Thread {

  private final String taskName;

  TaskThread(String taskName) {
    this.taskName = taskName;
  }

  @Override
  public void run() {
    // This code runs on a NEW thread when start() is called
    for (int i = 1; i <= 5; i++) {
      System.out.printf("Task %s - step %d (thread=%s)%n",
          taskName, i, Thread.currentThread().getName());
      sleepSafely(200);
    }
  }

  private void sleepSafely(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
