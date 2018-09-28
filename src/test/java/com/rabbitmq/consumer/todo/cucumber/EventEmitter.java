package com.rabbitmq.consumer.todo.cucumber;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

@Component
public class EventEmitter {

    @Autowired
    private RabbitConnection rabbitConnection;
    private Channel channel;
    @Value("${spring.application.name:appName}")
    private String appName;

    public String on(String exchange, String routingKey, Consumer<Object> callback) throws IOException, TimeoutException {
        return this.on(exchange, routingKey, callback, (Map)null);
    }

    public String on(String exchange, String routingKey, final Consumer<Object> callback, Map<String, Object> args) throws IOException, TimeoutException {
        String queue = String.join(".", this.appName, UUID.randomUUID().toString().replace("-", ""));
        this.getChannel().queueDeclare(queue, false, false, false, args);
        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(this.getChannel()) {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                callback.accept(message);
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        this.getChannel().queueBind(queue, exchange, routingKey);
        return this.getChannel().basicConsume(queue, true, consumer);
    }


    public void emmit(String queue, String message) throws IOException, TimeoutException {
        Channel emmitChannel = rabbitConnection.getConnection().createChannel();
        emmitChannel.basicPublish("", queue, (AMQP.BasicProperties)null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        emmitChannel.close();
    }

    public void unsubscribe(String consumerTag) throws IOException {
        this.getChannel().basicCancel(consumerTag);
    }

    private Channel getChannel() throws IOException {
        if(channel == null || !channel.isOpen()) {
            this.channel = rabbitConnection.getConnection().createChannel();
        }
        return channel;
    }
}
