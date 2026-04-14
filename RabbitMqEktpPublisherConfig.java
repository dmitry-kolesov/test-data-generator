package ru.tomsknipi.track_gateway_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqEktpPublisherConfig {
    @Value("${spring.rabbitmq.ektp_sender.host}")
    String host;

    @Value("${spring.rabbitmq.ektp_sender.username}")
    String username;

    @Value("${spring.rabbitmq.ektp_sender.password}")
    String password;

    @Bean(name = "ektpSenderConnectionFactory")
    public ConnectionFactory senderConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(host);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

    // видимо необязательно указывать так как у бина и будет имя по умолчанию "senderRabbitTemplate", но так нагляднее
    @Bean(name = "ektpSenderRabbitTemplate")
    public RabbitTemplate senderRabbitTemplate(@Qualifier("ektpSenderConnectionFactory") ConnectionFactory senderConnectionFactory) {
        return new RabbitTemplate(senderConnectionFactory);
    }

    // 2. Создаем RabbitAdmin именно для этого соединения
    @Bean(name = "ektpSenderAdmin")
    public RabbitAdmin senderAdmin(@Qualifier("ektpSenderConnectionFactory") ConnectionFactory senderConnectionFactory) {
        return new RabbitAdmin(senderConnectionFactory);
    }
}
