package com.example.topic;

import com.rabbitmq.client.*;

public class OrderConsumer {

    private static final String EXCHANGE_NAME = "events";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String queueName = "order-service-queue";
        channel.queueDeclare(queueName, true, false, false, null);

        // subscribe to all order events
        channel.queueBind(queueName, EXCHANGE_NAME, "order.*");

        System.out.println("Order service waiting...");

        DeliverCallback callback = (tag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[ORDER] " + message);
        };

        channel.basicConsume(queueName, true, callback, tag -> {});
    }
}