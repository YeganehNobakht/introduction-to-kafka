package com.msi.dispatch.handler;

import com.msi.dispatch.exceptions.NotRetryableException;
import com.msi.dispatch.exceptions.RetryableException;
import com.msi.dispatch.message.OrderCreated;
import com.msi.dispatch.service.DispatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
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
    public void listern(@Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        @Payload OrderCreated payload){
        log.info("Received message: partition: "+ partition + " , key: " +key + " , payload: " + payload);
        try {
            dispatchService.process(key, payload);
        } catch (RetryableException e){
            log.warn("Retryable exception: " + e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error("NotRetryable exception: " + e.getMessage());
            throw new NotRetryableException(e);
        }
    }
}
