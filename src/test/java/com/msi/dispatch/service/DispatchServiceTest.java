package com.msi.dispatch.service;

import com.msi.dispatch.client.StockServiceClient;
import
        com.msi.dispatch.message.OrderCreated;
import com.msi.dispatch.message.OrderDispatch;
import com.msi.dispatch.util.TestEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DispatchServiceTest {

    private DispatchService dispatchService;
    private KafkaTemplate kafkaTemplate;

    private StockServiceClient stockServiceClient;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(kafkaTemplate);
        stockServiceClient = mock(StockServiceClient.class);
        dispatchService = new DispatchService(kafkaTemplate, stockServiceClient);
    }

    @Test
    void process_Success() throws Exception{
        String key = UUID.randomUUID().toString();
        when(kafkaTemplate.send(anyString(), anyString(), any(OrderCreated.class))).thenReturn(mock(CompletableFuture.class));
        when(stockServiceClient.checkAvailability(anyString())).thenReturn(String.valueOf(true));
        OrderCreated orderCreated = TestEventData.buildOrderCreatedEvent(UUID.randomUUID(), UUID.randomUUID().toString());

        dispatchService.process(key, orderCreated);
        verify(kafkaTemplate, times(1)).send(eq ("order.dispatched"), eq(key), any(OrderDispatch.class));
        verify(stockServiceClient, times(1)).checkAvailability(orderCreated.getItem());
    }

    @Test
    public void process_ProducerThrowsException(){
        when(kafkaTemplate.send(anyString(), anyString(), any(OrderCreated.class))).thenReturn(mock(CompletableFuture.class));
        String key = UUID.randomUUID().toString();
        OrderCreated orderCreated = TestEventData.buildOrderCreatedEvent(UUID.randomUUID(), UUID.randomUUID().toString());
        doThrow(new  RuntimeException("Service failure")).when(kafkaTemplate).send(eq(("order.dispatched")), any(OrderDispatch.class));
         Exception exception = assertThrows(RuntimeException.class, () -> dispatchService.process(key, orderCreated));
         verify(kafkaTemplate, times(1)).send(eq(("order.dispatched")), any(OrderDispatch.class));
        verify(stockServiceClient, times(1)).checkAvailability(orderCreated.getItem());
        assertThat(exception.getMessage(), equalTo("Producer failure"));

    }
}