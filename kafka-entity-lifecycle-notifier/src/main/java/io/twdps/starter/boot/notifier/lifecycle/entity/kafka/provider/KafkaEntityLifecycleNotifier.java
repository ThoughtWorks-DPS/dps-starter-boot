package io.twdps.starter.boot.notifier.lifecycle.entity.kafka.provider;

import io.twdps.starter.boot.kafkaconnectors.provider.KafkaProducer;
import io.twdps.starter.boot.notifier.lifecycle.entity.kafka.config.properties.KafkaEntityLifecycleNotifierConfigProperties;
import io.twdps.starter.boot.notifier.lifecycle.entity.kafka.model.KafkaEntityLifecycleNotification;
import io.twdps.starter.boot.notifier.lifecycle.entity.model.EntityLifecycleNotification;
import io.twdps.starter.boot.notifier.lifecycle.entity.spi.EntityLifecycleNotifier;
import io.twdps.starter.boot.notifier.lifecycle.entity.spi.TimestampProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class KafkaEntityLifecycleNotifier extends KafkaProducer<KafkaEntityLifecycleNotification>
    implements EntityLifecycleNotifier {

  private TimestampProvider provider;

  private String queueName;

  /**
   * constructor.
   *
   * @param kafkaTemplate KafkaTemplate<> for sending the notifications
   * @param provider Timestamp Provider (for testing)
   */
  public KafkaEntityLifecycleNotifier(
      KafkaTemplate<Integer, KafkaEntityLifecycleNotification> kafkaTemplate,
      KafkaEntityLifecycleNotifierConfigProperties configProperties,
      TimestampProvider provider) {
    super(kafkaTemplate, configProperties);
    this.provider = provider;
    this.queueName = configProperties.getTopic().getName();
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
    log.info(
        "notify: [{}] on topic [{}:{}]",
        notification,
        getConfigProperties().getTopic().getName(),
        queueName);
    KafkaEntityLifecycleNotification kafkaMessage =
        new KafkaEntityLifecycleNotification(notification);
    send(queueName, kafkaMessage);
  }

  @Bean
  @Order(-1)
  public NewTopic createLifecycleNotifierTopic() {
    return super.createNewTopic();
  }
}
