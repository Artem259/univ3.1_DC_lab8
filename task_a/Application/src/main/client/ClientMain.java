package main.client;

import main.common.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMain {
    Database db;
    public void start(String ip, int port) {
        try (Socket socket = new Socket(ip, port)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            this.db = new DatabaseClient(in, out);
            new ClientTest(db).start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new ClientMain().start("localhost", 6666);
    }
}
