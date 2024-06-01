package com.msi.dispatch.handler;

import com.msi.dispatch.service.DispatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class OrderCreatedHandlerTest {

    private OrderCreatedHandler orderCreatedHandler;
    private DispatchService dispatchService;


    @BeforeEach
    void setUp() {
        dispatchService = Mockito.mock(DispatchService.class);
        orderCreatedHandler = new OrderCreatedHandler(dispatchService);
    }

    @Test
    void listern() {
        orderCreatedHandler.listern("payload");
        Mockito.verify(dispatchService, Mockito.times(1)).process("payload");
    }
}