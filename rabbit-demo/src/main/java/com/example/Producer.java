import com.rabbitmq.client.*;

/*
Producer keeps listening to messages
*/
public class Producer {

    private static final String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // durable queue
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            for (int i = 1; i <= 10; i++) {
                String message = "Requiring " + i;

                channel.basicPublish(
                        "",
                        QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes()
                );

                System.out.println("Sent: " + message);
            }
        }
    }
}