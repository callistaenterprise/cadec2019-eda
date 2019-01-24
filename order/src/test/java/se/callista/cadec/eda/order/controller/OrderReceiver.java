package se.callista.cadec.eda.order.controller;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.order.domain.Order;

@Component
public class OrderReceiver {

  private BlockingQueue<ConsumerRecord<String, Order>> records = new LinkedBlockingQueue<>();

  public BlockingQueue<ConsumerRecord<String, Order>> getRecords() {
    return records;
  }

  @KafkaListener(topics = "${kafka.topic.orders}",
      containerFactory = "orderListenerContainerFactory")
  public void receive(ConsumerRecord<String, Order> record) {
    records.add(record);
  }

}
