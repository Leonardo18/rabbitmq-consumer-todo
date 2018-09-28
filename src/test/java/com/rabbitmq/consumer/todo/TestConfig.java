package com.rabbitmq.consumer.todo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

@TestConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = RabbitmqConsumerTodoApplication.class)
@ComponentScan(basePackages = {"com.rabbitmq.consumer.todo"})
public class TestConfig { }
