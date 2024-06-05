package com.msi.dispatch.service;

import com.msi.dispatch.message.OrderCreated;
import com.msi.dispatch.util.TestEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DispatchServiceTest {

    private DispatchService dispatchService;

    @BeforeEach
    void setUp() {
        dispatchService = new DispatchService();
    }

    @Test
    void process() {
        OrderCreated orderCreated = TestEventData.buildOrderCreatedEvent(UUID.randomUUID(), UUID.randomUUID().toString());

        dispatchService.process(orderCreated);
    }
}