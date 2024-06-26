package com.msi.dispatch;

import com.msi.dispatch.exceptions.NotRetryableException;
import com.msi.dispatch.exceptions.RetryableException;
import com.msi.dispatch.message.OrderCreated;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.web.client.RestTemplate;

import javax.swing.plaf.PanelUI;
import java.util.HashMap;
import java.util.Map;

@ComponentScan(basePackages = {"com.msi"})
@Configuration
public class DispatchConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String , Object> kafkaListenerContainerFactory(ConsumerFactory<String , Object> consumerFactory){
        ConcurrentKafkaListenerContainerFactory<String , Object> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(100L, 3));
        errorHandler.addRetryableExceptions(RetryableException.class);
        errorHandler.addNotRetryableExceptions(NotRetryableException.class);
        factory.setCommonErrorHandler(errorHandler);
        return factory;

    }

    @Bean
    public ConsumerFactory<String , Object> consumerFactory(@Value("${kafka.bootstrap-servers}") String bootstrapServers){
        Map<String , Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, OrderCreated.class.getCanonicalName());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String , Object> kafkaTemplate (ProducerFactory<String , Object> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String , Object> producerFactory(@Value("${kafka.bootstrap-servers}") String servers){
       Map<String , Object> config = new HashMap<>();
       config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
       config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
       config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
       return new DefaultKafkaProducerFactory<>(config);
    }
}
