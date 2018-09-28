package com.rabbitmq.consumer.todo.cucumber;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitConnection {

    private final Connection connection;

    public RabbitConnection(
            @Value("${rabbit.url}") String url,
            @Value("${rabbit.user}") String user,
            @Value("${rabbit.pass}") String pass,
            @Value("${rabbit.port: #{0}}") Integer port
    ) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(user);
        factory.setPassword(pass);
        factory.setHost(url);
        if (port != 0) {
            factory.setPort(port);
        }
        connection = factory.newConnection();
    }

    public Connection getConnection() {
        return connection;
    }
}
