package com.example.topic;

import com.rabbitmq.client.*;

public class PaymentConsumer {

    private static final String EXCHANGE_NAME = "events";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String queueName = "payment-service-queue";
        channel.queueDeclare(queueName, true, false, false, null);

        // only payment events
        channel.queueBind(queueName, EXCHANGE_NAME, "payment.*");

        System.out.println("Payment service waiting...");

        channel.basicConsume(queueName, true, (tag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[PAYMENT] " + message);
        }, tag -> {});
    }
}