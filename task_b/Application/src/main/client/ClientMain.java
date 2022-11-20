package main.client;

import main.common.Database;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientMain {
    Database db;
    public void start(String ip, int port) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "//" + ip + ":" + port + "/Store";
        this.db = (Database) Naming.lookup(url);

        new ClientTest(db).start();
    }

    public static void main(String[] args) {
        try {
            new ClientMain().start("localhost", 6666);
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
