package com.msi.dispatch.service;

import com.msi.dispatch.message.OrderCreated;
import com.msi.dispatch.message.OrderDispatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchService {

    private final KafkaTemplate<String , Object> kafkaTemplate;
    private static final String ORDER_DISPATCH_TOPIC = "order.dispatched";
    public void process(String key, OrderCreated orderCreated) throws Exception{
        OrderDispatch orderDispatch = OrderDispatch.builder()
                .orderId(orderCreated.getOrderId())
                .build();
//        without .get() the producer sends the event to kafka but
//        does not wait for the acknowledgment for the successful write
//        so if write failed we don't know about it.
//        by calling get() method we change it to synchronous and handle the exception in handler class
        kafkaTemplate.send(ORDER_DISPATCH_TOPIC, key, orderDispatch)
                .get();

        log.info("sent message: key: " + key + " , orderId: " + orderCreated.getOrderId() );
    }
}
