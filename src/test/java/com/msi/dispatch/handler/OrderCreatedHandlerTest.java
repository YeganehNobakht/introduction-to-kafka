package com.msi.dispatch.handler;

import com.msi.dispatch.message.OrderCreated;
import com.msi.dispatch.service.DispatchService;
import com.msi.dispatch.util.TestEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderCreatedHandlerTest {

    private OrderCreatedHandler orderCreatedHandler;
    private DispatchService dispatchService;


    @BeforeEach
    void setUp() {
        dispatchService = Mockito.mock(DispatchService.class);
        orderCreatedHandler = new OrderCreatedHandler(dispatchService);
    }

    @Test
    void listern_Success() throws Exception {
        OrderCreated orderCreated = TestEventData.buildOrderCreatedEvent(UUID.randomUUID(), UUID.randomUUID().toString());
        orderCreatedHandler.listern(orderCreated);
        Mockito.verify(dispatchService, Mockito.times(1)).process(orderCreated);
    }

    @Test
    public void listen_ServiceThrowsException() throws Exception{
        OrderCreated orderCreated = TestEventData.buildOrderCreatedEvent(UUID.randomUUID(), UUID.randomUUID().toString());
        doThrow(new  RuntimeException("Service failure")).when(dispatchService).process(orderCreated);
        orderCreatedHandler.listern(orderCreated);
        verify(dispatchService, times(1)).process(orderCreated);
    }
}