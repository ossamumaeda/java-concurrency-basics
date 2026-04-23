package com.example.pubsub;
import com.rabbitmq.client.*;

public class PubSubConsumer {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        // temporary queue (auto-generated)
        String queueName = channel.queueDeclare().getQueue();

        // bind queue to exchange
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("Waiting for events...");

        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received: " + message);
        };

        channel.basicConsume(queueName, true, callback, consumerTag -> {});
    }
}