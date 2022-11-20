package main.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerMain {
    private final DatabaseServer db;

    public ServerMain(Connection connection) throws RemoteException {
        this.db = new DatabaseServer(connection);
    }

    public void start(int port) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind("Store", db);
    }

    public static void main(String[] args) {
        try {
            String url = "Jdbc:mysql://localhost:3306/store";
            Connection connection = DriverManager.getConnection(url, "root", "mypassword");
            new ServerMain(connection).start(6666);
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
