package com.rabbitmq.consumer.todo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitConfig {

    @Value("${rabbit.url}")
    private String rabbitUrl;

    @Value("${rabbit.exchange}")
    private String rabbitExchange;

    @Value("${rabbit.todo.queue}")
    private String rabbitQueue;

    @Value("${rabbit.user}")
    private String rabbitUser;

    @Value("${rabbit.pass}")
    private String rabbitPass;

    public String getRabbitUrl() {
        return rabbitUrl;
    }

    public String getRabbitExchange() {
        return rabbitExchange;
    }

    public String getRabbitQueue() {
        return rabbitQueue;
    }

    public String getRabbitUser() {
        return rabbitUser;
    }

    public String getRabbitPass() {
        return rabbitPass;
    }
}
