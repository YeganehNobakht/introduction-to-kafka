package com.msi.dispatch.util;

import com.msi.dispatch.message.OrderCreated;

import java.util.UUID;

public class TestEventData {
    public static OrderCreated buildOrderCreatedEvent(UUID uuid, String item){
        return OrderCreated.builder()
                .orderId(uuid)
                .item(item)
                .build();
    }
}
