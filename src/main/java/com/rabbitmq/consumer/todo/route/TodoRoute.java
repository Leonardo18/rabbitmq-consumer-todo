package com.rabbitmq.consumer.todo.route;

import com.rabbitmq.consumer.todo.config.RabbitConfig;
import com.rabbitmq.consumer.todo.model.TodoModel;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class TodoRoute extends RouteBuilder {

    @Autowired
    private RabbitConfig rabbitConfig;

    @Override
    public void configure() {
        onException(Exception.class)
                .asyncDelayedRedelivery()
                .redeliveryDelay(2000)
                .maximumRedeliveries(2)
                .useOriginalMessage()
                .log(LoggingLevel.ERROR,"Exception: Sending message to dead letter. TodoModel ${body}")
                .logRetryAttempted(true);

        from(getFaturamentoNonameRabbitUri())
                .log(LoggingLevel.INFO, "Initializing process from data queue")
                .unmarshal()
                .json(JsonLibrary.Jackson, TodoModel.class)
                .log(LoggingLevel.INFO, "Message found in the queue: TodoModel ${body}")
                .choice()
                    .when(this::notHasTodoModelData)
                        .log(LoggingLevel.INFO, "Data not found to process")
                        .throwException(new Exception("No data to process"))
                    .endChoice()
                .end()
        .end();
    }

    private boolean notHasTodoModelData(Exchange exchange) {
        return !Optional.ofNullable(exchange.getIn().getBody(TodoModel.class)).isPresent();
    }

    private String getFaturamentoNonameRabbitUri() {
        return UriComponentsBuilder
                .newInstance()
                .scheme("rabbitmq")
                .host(rabbitConfig.getRabbitUrl() + "/" + rabbitConfig.getRabbitExchange())
                .queryParam("queue", rabbitConfig.getRabbitQueue())
                .queryParam("exchangeType", "direct")
                .queryParam("username", rabbitConfig.getRabbitUser())
                .queryParam("password", rabbitConfig.getRabbitPass())
                .queryParam("autoDelete", false)
                .queryParam("autoAck", false)
                .queryParam("automaticRecoveryEnabled", true)
                .queryParam("deadLetterExchangeType", "fanout")
                .queryParam("deadLetterExchange", "todo.dead")
                .queryParam("deadLetterQueue", "dead.letter")
                .queryParam("deadLetterRoutingKey", "#")
                .build()
                .toString();
    }
}
