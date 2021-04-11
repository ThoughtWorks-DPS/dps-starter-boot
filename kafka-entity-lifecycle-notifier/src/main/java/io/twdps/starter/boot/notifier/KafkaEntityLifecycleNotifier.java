package io.twdps.starter.boot.notifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
public class KafkaEntityLifecycleNotifier implements EntityLifecycleNotifier {

  private TimestampProvider provider;
  private final KafkaTemplate<String, EntityLifecycleNotification> kafkaTemplate;

  private String queueName;

  /**
   * constructor.
   *
   * @param kafkaTemplate KafkaTemplate<> for sending the notifications
   * @param provider      Timestamp Provider (for testing)
   * @param queueName     name of the kafka queue
   */
  public KafkaEntityLifecycleNotifier(
      KafkaTemplate<String, EntityLifecycleNotification> kafkaTemplate,
      TimestampProvider provider,
      @Value("${starter.boot.kafka-lifecycle-notifier.queue-name:entity-lifecycle-notifier}")
          String queueName) {
    this.kafkaTemplate = kafkaTemplate;
    this.queueName = queueName;
    this.provider = provider;
  }

  public TimestampProvider getTimestampProvider() {
    return provider;
  }

  /**
   * perform the notification.
   *
   * @param notification lifecycle notification message
   */
  public void notify(EntityLifecycleNotification notification) {
    log.info("notify: [{}] on topic [{}]", notification, queueName);
    kafkaTemplate.send(queueName,
        notification.getEntityDescriptor()
            .getTypename(),
        notification);
  }

}
