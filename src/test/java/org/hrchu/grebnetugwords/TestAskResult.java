package org.hrchu.grebnetugwords;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by hrchu on 2016/10/10.
 */
public class TestAskResult {
  @Test
  public void testEqual() {
    assertEquals(new AskResult(9, 9), new AskResult(9, 9));
    assertNotEquals(new AskResult(9, 9), new AskResult(9, 1));
    assertNotEquals(new AskResult(9, 9), new AskResult(1, 9));
  }
}
