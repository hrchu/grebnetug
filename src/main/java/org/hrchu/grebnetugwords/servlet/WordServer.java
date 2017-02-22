package org.hrchu.grebnetugwords.servlet;

import org.hrchu.grebnetugwords.GrebnetugWords;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by hrchu on 2016/10/10.
 */
public class WordServer {
  public static void main(String[] args) throws Exception {
    // TODO: Tune server according to real workload
    Server server = new Server(8080);
    WebAppContext handler = new WebAppContext();
    handler.setResourceBase("/");
    handler.setContextPath("/");

    // TODO: use Spring to manage singleton GutenbergWords
    GrebnetugWords words = new GrebnetugWords("src/test/resources/TestGutenbergWords.txt");
    handler.addServlet(new ServletHolder(new WordServlet(words)), "/*");

    server.setHandler(handler);
    server.start();
  }
}
