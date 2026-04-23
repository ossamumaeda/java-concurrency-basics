package com.example.topic;

import com.rabbitmq.client.*;

public class TopicProducer {

    private static final String EXCHANGE_NAME = "events";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String routingKey = "order.created";
            String message = "Order 123 created";

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());

            System.out.println("Sent: " + routingKey + " → " + message);
        }
    }
}