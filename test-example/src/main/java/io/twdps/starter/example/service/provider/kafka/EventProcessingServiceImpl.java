package io.twdps.starter.example.service.provider.kafka;

import io.twdps.starter.boot.exception.DownstreamTimeoutException;
import io.twdps.starter.example.api.kafka.requests.CustomerEvent;
import io.twdps.starter.example.api.kafka.responses.EventKafkaMetadata;
import io.twdps.starter.example.service.spi.kafka.EventProcessingService;
import io.twdps.starter.example.service.spi.kafka.model.CustomerEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class EventProcessingServiceImpl implements EventProcessingService {

  private AtomicInteger simulatedEventId;
  private Random random;

  private CustomerEventKafkaProducer eventProducer;

  /**
   * constructor.
   *
   * @param producer autowired kafka producer bean
   */
  public EventProcessingServiceImpl(CustomerEventKafkaProducer producer) {
    this.eventProducer = producer;
    this.simulatedEventId = new AtomicInteger();
    this.simulatedEventId.set(0);
    random = new Random();
  }

  /**
   * create message and send via kafka.
   *
   * @param customerEvent message to send
   * @return metadata record of kafka receipt
   */
  @Override
  public EventKafkaMetadata createAndSendEvent(CustomerEvent customerEvent) {

    // create an entity which may contain more information to help routing processing
    // down the event chain - here we add a modified time and create a unique event id
    CustomerEventMessage customerEventMessage = createSimulatedEvent(customerEvent);

    // call the kafka producer to send and then process the result
    ListenableFuture<SendResult<Integer, CustomerEventMessage>> future =
        eventProducer.sendMessage(customerEventMessage);

    try {
      SendResult<Integer, CustomerEventMessage> sendResult = future.get();
      CustomerEventMessage cem = sendResult.getProducerRecord().value();
      RecordMetadata recordMetadata = sendResult.getRecordMetadata();
      EventKafkaMetadata eventKafkaMetadata =
          new EventKafkaMetadata(customerEvent.getCustomerId(), cem.getEventId(), recordMetadata);
      log.info(
          "successfully wrote eventId:{}, to partition:{} with offset:{}",
          eventKafkaMetadata.getEventId(),
          eventKafkaMetadata.getPartition(),
          eventKafkaMetadata.getOffset());
      return eventKafkaMetadata;
    } catch (InterruptedException e) {
      throw new DownstreamTimeoutException(
          "500", String.format("Interrupted Write to kafka. Cause: %s", e.getLocalizedMessage()));
    } catch (ExecutionException e) {
      throw new DownstreamTimeoutException(
          "500", String.format("Exception writing to kafka. Cause: %s", e.getLocalizedMessage()));
    }
  }

  /**
   * create simulated customer event record.
   *
   * @param customerEvent customer event record
   * @return customer event message
   */
  protected CustomerEventMessage createSimulatedEvent(CustomerEvent customerEvent) {

    int eventId = simulatedEventId.incrementAndGet();
    long modified = ZonedDateTime.now().toEpochSecond();
    return new CustomerEventMessage(
        eventId,
        customerEvent.getCustomerId(),
        customerEvent.getCreatedAt(),
        modified,
        customerEvent.getType());
  }
}
