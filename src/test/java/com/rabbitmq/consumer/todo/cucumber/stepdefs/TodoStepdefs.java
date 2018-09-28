package com.rabbitmq.consumer.todo.cucumber.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.consumer.todo.TestConfig;
import com.rabbitmq.consumer.todo.config.RabbitConfig;
import com.rabbitmq.consumer.todo.cucumber.EventEmitter;
import com.rabbitmq.consumer.todo.cucumber.World;
import com.rabbitmq.consumer.todo.model.TodoModel;
import cucumber.api.java8.En;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

public class TodoStepdefs extends TestConfig implements En {

    @Autowired
    private World world;
    @Autowired
    private EventEmitter emitter;
    @Autowired
    private RabbitConfig rabbitConfig;
    @Autowired
    private ObjectMapper objectMapper;

    public TodoStepdefs () {

        Given("^a message with valid data of a todo$", () -> {
            world.todoModel = new TodoModel("Aragorn", "MyFirstTodo", 3, "Aragorn");
        });

        Given("^a message with invalid data of a todo$", () -> {
            createTodoDeadLetterListener();
            world.todoModel = null;
        });


        When("^a message of todo is added to queue$", () -> {
            emitter.emmit(rabbitConfig.getRabbitQueue(), objectMapper.writeValueAsString(world.todoModel));
        });

        Then("^should remove the message from queue after (\\d+) seconds$", (Long seconds) -> {
            world.countDownLatch.await(seconds, TimeUnit.SECONDS);
        });

        Then("^should add the message to dead letter queue after (\\d+) seconds$", (Long seconds) -> {
            world.countDownLatch.await(seconds, TimeUnit.SECONDS);
            assertEquals(0, world.countDownLatch.getCount());
            emitter.unsubscribe(world.consumerTag);
        });
    }

    private void createTodoDeadLetterListener() throws IOException, TimeoutException {
        world.countDownLatch = new CountDownLatch(1);
        world.consumerTag = emitter.on("todo.dead", "#", (message) -> {
            world.countDownLatch.countDown();
        });
    }
}

