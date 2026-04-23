package com.example.topic;

import com.rabbitmq.client.*;

public class AnalyticsConsumer {

    private static final String EXCHANGE_NAME = "events";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String queueName = "analytics-queue";
        channel.queueDeclare(queueName, true, false, false, null);

        // receive EVERYTHING
        channel.queueBind(queueName, EXCHANGE_NAME, "#");

        System.out.println("Analytics waiting...");

        channel.basicConsume(queueName, true, (tag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[ANALYTICS] " + message);
        }, tag -> {});
    }
}