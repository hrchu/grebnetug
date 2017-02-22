package org.hrchu.grebnetugwords.servlet;

import org.hrchu.grebnetugwords.AskResult;
import org.hrchu.grebnetugwords.GrebnetugWords;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WordServlet extends HttpServlet {
  private GrebnetugWords words;

  public WordServlet(GrebnetugWords words) {
    this.words = words;
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String queryString = request.getQueryString();
    // TODO: replace sout to slf4j
    System.out.println("requested: " + queryString);
    AskResult result = words.ask(queryString).orElse(new AskResult(0, 0));
    response.setHeader("occur", String.valueOf(result.occur));
    response.setHeader("hit", String.valueOf(result.hit));
    response.flushBuffer();
  }
}