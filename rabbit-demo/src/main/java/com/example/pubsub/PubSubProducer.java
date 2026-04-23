package com.example.pubsub;

import com.rabbitmq.client.*;

public class PubSubProducer {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // fanout = broadcast
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            String message = "Event: Order Created";

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

            System.out.println("Sent: " + message);
        }
    }
}