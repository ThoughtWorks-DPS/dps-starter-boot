package io.twdps.starter.example.api.kafka.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.clients.producer.RecordMetadata;

@Getter
@Setter
@NoArgsConstructor
public class EventKafkaMetadata {

  private int customerId;
  private int eventId;
  private int partition;
  private long offset;
  private String topic;

  /**
   * constructor.
   *
   * @param customerId eponymous
   * @param eventId eponymous
   * @param recordMetadata record data from Kafka message send
   */
  public EventKafkaMetadata(int customerId, int eventId, RecordMetadata recordMetadata) {
    this.customerId = customerId;
    this.eventId = eventId;
    if (recordMetadata.hasOffset()) {
      offset = recordMetadata.offset();
    } else {
      offset = -1L;
    }
    partition = recordMetadata.partition();
    topic = recordMetadata.topic();
  }
}
