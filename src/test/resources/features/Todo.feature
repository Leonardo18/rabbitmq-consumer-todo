Feature: Consume a Todo from a queue

  Scenario: Consuming a todo from a queue occurred with success
    Given a message with valid data of a todo
    When a message of todo is added to queue
    Then should remove the message from queue after 5 seconds

  Scenario: Consuming a todo from a queue occurred with error because data is invalid
    Given a message with invalid data of a todo
    When a message of todo is added to queue
    Then should add the message to dead letter queue after 5 seconds