package org.hrchu.grebnetugwords;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

/**
 *  Design consideration:
 *  - A hashmap as main data structure since it is just O(1) in query and hundreds of megabytes memory (according to requirement) is not a big deal in
 *    today's cloud app. Disk IO is more expensive than memory.
 *  - AtomicLong as counter so I can keep thread safe without sync block.
 *
 * Created by hrchu on 2016/10/10.
 */
public class GrebnetugWords {
  private Map<String, Tuple> stats;

  public GrebnetugWords(String path) throws IOException {
    this(Arrays.asList(Paths.get(path)));
  }

  public GrebnetugWords(List<Path> paths) throws IOException {
    init(paths);
  }

  public Optional<AskResult> ask(String word) {
    Tuple tuple = stats.get(word);
    if (tuple != null) {
      return Optional.of(new AskResult(tuple));
    } else {
      return Optional.empty();
    }
  }

  private Stream<String> uncheckedPathsGet(Path path) {
    try {
      return Files.lines(path);
    } catch (IOException e) {
      throw new RuntimeException("future.get() failed", e);
    }
  }

  private void init(List<Path> paths) throws IOException {
    stats =
        paths.stream()
            /* parallel speedup 100M test from about 15s to 6s and utilize full CPU core on my box. Whether the files are
            cached or not may hugely impact the result.
            */
            .parallel()
            .map(p -> uncheckedPathsGet(p))
            .flatMap(identity())
            .map(line -> line.split("\\s+"))
            .flatMap(Arrays::stream)
            .map(str -> str.replaceAll("[^A-Za-z]+", ""))
            .filter(str -> !str.isEmpty())
            // FIXME: do this in the same stream procedure...
            .collect(collectingAndThen(
                groupingBy(identity(), counting()),
                m -> m.entrySet().stream()
                    .collect(toMap(Entry::getKey, e -> new Tuple(e.getValue())))));

    System.out.println("Init finish. There are " + stats.size() + " distinct words in the txts.");

    // After garbage collection, HEAP in use is just about several megabytes. (screenshot: http://imgur.com/a/OtYyg)
    System.gc();
  }

  /**
   * Store the occur and the hit in the same map so that we can retain just one copy of word as key.in memory
   */
  class Tuple {
    private final long occur;
    private final AtomicLong hit;

    long hit() {
      return hit.incrementAndGet();
    }

    public long occur() {
      return occur;
    }

    Tuple(long occur) {
      this.occur = occur;
      hit = new AtomicLong(0);
    }

    @Override
    public String toString() {
      return "[occur=" + occur + ", hit=" + hit + "]";
    }
  }
}
