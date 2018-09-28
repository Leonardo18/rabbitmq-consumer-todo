package com.rabbitmq.consumer.todo.cucumber;

import com.rabbitmq.consumer.todo.model.TodoModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;


@Component
@Scope("cucumber-glue")
public class World {
    public TodoModel todoModel;
    public CountDownLatch countDownLatch = new CountDownLatch(1);
    public String consumerTag;
}