package concurrent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class Loader implements Runnable {
  private int start;
  private Map<Integer, String> mis;
  public Loader(int start, Map<Integer, String> mis) {
    this.start = start;
    this.mis = mis;
  }

  @Override
  public void run() {
//    for (int i = start; i < start + 1_000; i++) {
    for (int i = start; i < start + 32_768_000; i++) {
      mis.put(i, "value " + i);
    }
    System.out.println("Loader at " + start + " finished");
  }
}

public class UseAMap {
  public static void main(String[] args) throws Throwable {
//    final int THREAD_COUNT = 32768;
//    final int THREAD_COUNT = 1024;
    final int THREAD_COUNT = 1;

//    Map<Integer, String> mis = new HashMap<>();
//    Map<Integer, String> mis = Collections.synchronizedMap(new HashMap<>());
    Map<Integer, String> mis = new ConcurrentHashMap<>();

    long start = System.nanoTime();

    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < THREAD_COUNT; i++) {
      Thread t = new Thread(new Loader(1_000 * i, mis));
      threads.add(t);
      t.start();
    }
    System.out.println("all started");
    for (Thread t : threads) {
      t.join();
    }
    long time = System.nanoTime() - start;
    System.out.println("elapsed time " + (time / 1_000_000_000.0));
//    mis.put(-1, "value bad");
    boolean good = mis.entrySet().stream()
        .filter(e -> !e.getValue().equals("value " + e.getKey()))
        .noneMatch(e -> true);
    System.out.println("Map size is " + mis.size());
    System.out.println("Validated contents? " + good);
  }
}
