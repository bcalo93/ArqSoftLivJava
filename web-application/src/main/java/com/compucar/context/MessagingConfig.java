package com.compucar.context;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@ImportResource("classpath:application-context-messaging.xml")
public class MessagingConfig {

    @Value("${queue.connection.expiry-timeout}")
    private long expiryTimeout;

    @Value("${queue.broker.url}")
    private String brokerURL;

    @Bean
    public PooledConnectionFactory connectionFactory() {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(this.directConnectionFactory());
        pooledConnectionFactory.setExpiryTimeout(expiryTimeout);
        return pooledConnectionFactory;
    }

    @Bean
    public ActiveMQConnectionFactory directConnectionFactory() {
        ActiveMQConnectionFactory directConnectionFactory = new ActiveMQConnectionFactory();
        directConnectionFactory.setBrokerURL(brokerURL);
        return directConnectionFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
