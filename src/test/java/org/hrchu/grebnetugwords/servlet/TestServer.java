package org.hrchu.grebnetugwords.servlet;

import org.hrchu.grebnetugwords.AskResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Created by hrchu on 2016/10/10.
 */
public class TestServer {

  @Test
  public void testAsk() throws IOException {
    System.out.println("Plz run the server before start test");
    // basic
    Assert.assertEquals(new AskResult(3, 1), ask("of"));
    assertEquals(new AskResult(3, 2), ask("of"));
    // non exist
    assertEquals(new AskResult(0, 0), ask("iphone8"));
    assertEquals(new AskResult(0, 0), ask("iphone8"));
  }

  private AskResult ask(String word) throws IOException {
    URL url = new URL("http://localhost:8080/?" + word);
    HttpURLConnection httpConnection = null;
    AskResult result;
    try {
      httpConnection = (HttpURLConnection) url.openConnection();
      assertEquals(HttpURLConnection.HTTP_OK, httpConnection.getResponseCode());
      result = new AskResult(Long.parseLong(httpConnection.getHeaderField("occur")),
          Long.parseLong(httpConnection.getHeaderField("hit")));
    } finally {
      if (httpConnection != null) {
        httpConnection.disconnect();
      }
    }
    return result;
  }
}
