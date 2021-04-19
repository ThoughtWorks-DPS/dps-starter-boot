package io.twdps.starter.boot.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"io.twdps.starter.example", "io.twdps.starter.boot"})
//@SpringBootApplication
public class SpringKafkaTestApplication {

  private static Logger log = LoggerFactory.getLogger(SpringKafkaTestApplication.class);

  /** main function.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    new SpringApplication(SpringKafkaTestApplication.class).run(args);
    log.info("\n\n\n\n\n---------------Example API Started.----------------\n\n\n\n\n");
  }
}
