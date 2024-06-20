package com.msi.dispatch.handler;

import com.msi.dispatch.message.OrderCreated;
import com.msi.dispatch.service.DispatchService;
import com.msi.dispatch.util.TestEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
        String key = UUID.randomUUID().toString();
        OrderCreated orderCreated = TestEventData.buildOrderCreatedEvent(UUID.randomUUID(), UUID.randomUUID().toString());
        orderCreatedHandler.listern(0, key, orderCreated);
        Mockito.verify(dispatchService, Mockito.times(1)).process(key, orderCreated);
    }

    @Test
    public void listen_ServiceThrowsException() throws Exception{
        String key = UUID.randomUUID().toString();
        OrderCreated orderCreated = TestEventData.buildOrderCreatedEvent(UUID.randomUUID(), UUID.randomUUID().toString());
        doThrow(new  RuntimeException("Service failure")).when(dispatchService).process(key, orderCreated);
        Exception exception = assertThrows(RuntimeException.class, () -> orderCreatedHandler.listern(0,key, orderCreated));
        assertThat(exception.getMessage(), equalTo("Service failure"));
        verify(dispatchService, times(1)).process(key, orderCreated);
    }
}