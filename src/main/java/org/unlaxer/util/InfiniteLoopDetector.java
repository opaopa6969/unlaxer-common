package org.unlaxer.util;

import java.io.Closeable;

public class InfiniteLoopDetector implements Closeable{
  
  ThreadLocal<Integer> counter = new ThreadLocal<>() {

    @Override
    protected Integer initialValue() {
      return 0;
    }
  };
  
  public void reset() {
    counter.set(0);
  }
  
  public int incrementsAndGet() {
    Integer integer = counter.get();
    counter.set(integer+1);
    return integer+1;
  }
  
  public boolean incrementsAndTest(int test) {
    Integer integer = counter.get();
    counter.set(integer+1);
    return integer+1>= test;
  }
  
  public void incrementsAndThrow(int test) {
    if(incrementsAndTest(test)) {
      throw new InfiniteLoopException();
    }
  }
  
  static class InfiniteLoopException extends RuntimeException{}
  
  public String toString() {
    return String.valueOf(counter.get());
  }

  @Override
  public void close() {
    reset();
  }
}
