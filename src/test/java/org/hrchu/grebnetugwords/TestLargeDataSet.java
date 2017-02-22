package org.hrchu.grebnetugwords;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Requirement: Please test with 30-100Mb of text files as resources for the application.
 * Created by hrchu on 2016/10/10.
 */
public class TestLargeDataSet {
  GrebnetugWords words;

  private void setUp(String dirPath) throws IOException {
    List<Path> paths = new ArrayList<>();
    Path dir = Paths.get(dirPath);
    DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
    stream.forEach(paths::add);
    words = new GrebnetugWords(paths);
  }

  @Test
  public void test30M() throws IOException {
    setUp("src/test/resources/30M/");
    assertTrue(words.ask("of").isPresent());
    assertEquals(185964, words.ask("of").get().occur);
  }

  @Test
  public void test100M() throws IOException {
    setUp("src/test/resources/100M/");
    assertTrue(words.ask("of").isPresent());
    assertEquals(655707, words.ask("of").get().occur);
  }
}
