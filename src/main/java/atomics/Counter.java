package atomics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {
//  private static /*volatile*/ long count = 0;
//  private static ReentrantLock lock = new ReentrantLock();

//  private static AtomicLong count = new AtomicLong(0);

  private static LongAccumulator count = new LongAccumulator((a, b) -> a + b, 0L);

  public static void main(String[] args) throws Throwable {
    Runnable r = () -> {
      for (int i = 0; i < 100_000; i++) {
        count.accumulate(1L);
//        count.incrementAndGet();
//        synchronized (Counter.class) {
//        lock.lock();
//        try {
//          count++;
//        } finally {
//          lock.unlock();
//        }
      }
    };

//    System.out.println("count before " + count);
    System.out.println("count before " + count.get());

    long start = System.nanoTime();

    List<Thread> lt = new ArrayList<>();
    for (int i = 0; i < 2_000; i++) {
      Thread t = new Thread(r);
      lt.add(t);
      t.start();
    }

    for (Thread t : lt) {
      t.join();
    }
    long time = System.nanoTime() - start;
//    System.out.println("count after " + count);
    System.out.println("count after " + count.get());
    System.out.println("time was " + (time / 1_000_000_000.0));
  }
}
