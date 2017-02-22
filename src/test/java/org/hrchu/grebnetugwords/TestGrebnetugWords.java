package org.hrchu.grebnetugwords;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.*;

/**
 * Created by hrchu on 2016/10/10.
 */
public class TestGrebnetugWords {
  GrebnetugWords words;

  @Before
  public void setUp() throws IOException {
    words = new GrebnetugWords("src/test/resources/TestGrebnetugWords.txt");
  }

  /**
   * Spec: the other one is how many times this word (input parameter) occurs in a set of text files that the application has as its internal resources
   */
  @Test
  public void testWordCount() throws IOException {
    // basic
    assertTrue(words.ask("of").isPresent());
    assertEquals(3, words.ask("of").get().occur);
    assertEquals(2, words.ask("Word").get().occur);
    // punctuation
    assertFalse(words.ask("scasc,").isPresent());
    assertTrue(words.ask("scasc").isPresent());
    // non-english word
    assertFalse(words.ask("Word#$#$%").isPresent());
    assertFalse(words.ask("功成正").isPresent());
    assertFalse(words.ask("プロ野球 - スポーツナビ").isPresent());
    assertFalse(words.ask("1234").isPresent());
    assertFalse(words.ask("").isPresent());
  }

  /**
   * Spec: the first one is how many time the endpoint has been called with the same value of this parameter. For example, 20 request asking about
   * "Jordan", the answer should be 20
   */
  @Test
  public void testHitCount() throws IOException {
    // basic
    for (int i = 1; i <= 20; i++) {
      assertEquals(i, words.ask("Jordan").get().hit);
    }
    // non-exist
    assertFalse(words.ask("iphone8").isPresent());
  }

  /**
   * Thread safe: two requests should always have the same word-count but never have the same number of  'how many time the endpoint was called ”
   */
  final int CONCURRENCY = 4; // for my Dell four core box
  final int TIMES = 1_000_000;

  @Test
  public void testConcurrentHit() throws ExecutionException, InterruptedException {
    Callable<Long> hitJob = () -> words.ask("country").get().hit;
    List<Callable<Long>> hitJobs = new ArrayList<>(Collections.nCopies(CONCURRENCY * TIMES, hitJob));
    ExecutorService executorService = Executors.newFixedThreadPool(CONCURRENCY);

    List<Future<Long>> results = executorService.invokeAll(hitJobs);

    Long count = results.stream()
        .map(f -> (uncheckedFutureGet(f)))
        .distinct()
        .count();

    assertEquals(Long.valueOf(CONCURRENCY * TIMES), count);
  }

  private <T> T uncheckedFutureGet(Future<T> future) {
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException("future.get() failed", e);
    }
  }
}
