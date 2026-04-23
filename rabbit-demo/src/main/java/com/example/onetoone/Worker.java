package com.example.onetoone;

import com.rabbitmq.client.*;
/*
Worker publishes the tasks and closes
mvn compile exec:java -Dexec.mainClass="Producer"
*/
public class Worker {

    private static final String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // process one message at a time (backpressure!)
        channel.basicQos(1);

        System.out.println("Waiting for messages...");

        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println("Received: " + message);

            try {
                // simulate work
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Done: " + message);

                // manual ACK (important!)
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        // autoAck = false → we control reliability
        channel.basicConsume(QUEUE_NAME, false, callback, consumerTag -> {
        });
    }
}