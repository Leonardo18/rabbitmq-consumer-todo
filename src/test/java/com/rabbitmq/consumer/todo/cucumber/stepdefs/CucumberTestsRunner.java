package com.rabbitmq.consumer.todo.cucumber.stepdefs;

import com.rabbitmq.consumer.todo.RabbitmqConsumerTodoApplication;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "com.rabbitmq.consumer.todo.cucumber")
@ContextConfiguration(loader = SpringBootContextLoader.class, classes = RabbitmqConsumerTodoApplication.class)
public class CucumberTestsRunner { }
