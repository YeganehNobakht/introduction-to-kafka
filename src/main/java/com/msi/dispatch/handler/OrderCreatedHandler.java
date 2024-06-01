package com.msi.dispatch.handler;

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
            groupId = "dispatch.order.created.consumer"
    )
    public void listern(String payload){
        log.info("Received message: payload: " + payload);
        dispatchService.process(payload);
    }
}
