package main.client;

import main.common.Database;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ClientMain {
    Database db;
    public void start(String host, String inputQueue, String outputQueue) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            this.db = new DatabaseClient(channel, outputQueue, inputQueue);
            new ClientTest(db).start();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new ClientMain().start("localhost",  "to_client", "to_server");
    }
}
