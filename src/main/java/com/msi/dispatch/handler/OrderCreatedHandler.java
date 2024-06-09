package com.msi.dispatch.handler;

import com.msi.dispatch.message.OrderCreated;
import com.msi.dispatch.service.DispatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedHandler {

    private final DispatchService dispatchService;

    @KafkaListener(
            id = "orderCunsumerClient",
            topics = "order.created",
            groupId = "dispatch.order.created.consumer",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listern(OrderCreated payload){
        log.info("Received message: payload: " + payload);
        try {
            dispatchService.process(payload);
        } catch (Exception e) {
            log.error("*processing failure: " + e);
        }
    }
}
