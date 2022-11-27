package main.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class ServerMain {
    private final DatabaseServer db;

    public ServerMain(Connection connection) {
        this.db = new DatabaseServer(connection);
    }

    public void start(String host, String inputQueue, String outputQueue) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        com.rabbitmq.client.Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(inputQueue, false, false, false, null);
        channel.queueDeclare(outputQueue, false, false, false, null);

        RequestHandler rh = new RequestHandler(db);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String requestMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String respondMessage = rh.run(requestMessage);
            channel.basicPublish("", outputQueue, null, respondMessage.getBytes(StandardCharsets.UTF_8));
        };
        channel.basicConsume(inputQueue, true, deliverCallback, consumerTag -> { });
    }

    public static void main(String[] args) {
        try {
            String url = "Jdbc:mysql://localhost:3306/store";
            Connection connection = DriverManager.getConnection(url, "root", "mypassword");
            new ServerMain(connection).start("localhost", "to_server", "to_client");
            connection.close();
        } catch (SQLException | IOException | TimeoutException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
