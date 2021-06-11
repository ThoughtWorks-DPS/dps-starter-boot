package io.twdps.starter.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"io.twdps.starter.example", "io.twdps.starter.boot"})
public class TestApplication {
  private static Logger log = LoggerFactory.getLogger(TestApplication.class);

  /**
   * main function.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    new SpringApplication(TestApplication.class).run(args);
    log.info("\n\n\n\n\n---------------Example API Started.----------------\n\n\n\n\n");
  }
}
