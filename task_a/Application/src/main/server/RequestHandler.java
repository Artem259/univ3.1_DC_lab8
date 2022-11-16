package main.server;

import main.common.Album;
import main.common.Singer;
import main.common.collection.Collection;
import main.common.collection.CollectionXml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class RequestHandler implements Runnable {
    private final DatabaseServer db;
    private final BufferedReader in;
    private final PrintWriter out;
    private final char sep;
    private String[] fields;
    private Integer responseCode;
    private String responseMessage;

    public RequestHandler(Socket socket, DatabaseServer databaseServer) throws IOException {
        this.db = databaseServer;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.sep = '#';
    }

    private void clearRequest() {
        if (fields.length != 0) {
            responseCode = 1;
            return;
        }
        boolean res = db.clear();
        responseMessage = String.valueOf(res);
    }

    private void getSingerByIdRequest() {
        if (fields.length != 1) {
            responseCode = 1;
            return;
        }
        try {
            int id = Integer.parseInt(fields[0]);
            responseMessage = String.valueOf(db.getSingerById(id));
        } catch (NumberFormatException e) {
            responseCode = 3;
        }
    }

    private void getAlbumByIdRequest() {
        if (fields.length != 1) {
            responseCode = 1;
            return;
        }
        try {
            int id = Integer.parseInt(fields[0]);
            responseMessage = String.valueOf(db.getAlbumById(id));
        } catch (NumberFormatException e) {
            responseCode = 3;
        }
    }

    private void addCollectionRequest() {
        if (fields.length != 1) {
            responseCode = 1;
            return;
        }
        try {
            CollectionXml collectionXml = new CollectionXml();
            if (!collectionXml.fromXmlString(fields[0])) {
                responseCode = 3;
                return;
            }
            boolean res = db.addCollection(collectionXml.getCollection());
            responseMessage = String.valueOf(res);
        } catch (NumberFormatException e) {
            responseCode = 3;
        }
    }

    private void addSingerRequest() {
        try {
            Singer singer = new Singer(fields);
            Integer res = db.addSinger(singer);
            responseMessage = String.valueOf(res);
        } catch (IllegalArgumentException e) {
            if (fields.length != 2) {
                responseCode = 1;
                return;
            }
            responseCode = 3;
        }
    }

    private void deleteSingerByIdRequest() {
        if (fields.length != 1) {
            responseCode = 1;
            return;
        }
        try {
            int id = Integer.parseInt(fields[0]);
            boolean res = db.deleteSingerById(id);
            responseMessage = String.valueOf(res);
        } catch (IllegalArgumentException e) {
            responseCode = 3;
        }
    }

    private void addAlbumRequest() {
        try {
            int singerId = Integer.parseInt(fields[0]);
            Album album = new Album(null, Arrays.copyOfRange(fields, 1, fields.length));
            Integer res = db.addAlbum(singerId, album);
            responseMessage = String.valueOf(res);
        } catch (IllegalArgumentException e) {
            if (fields.length != 5) {
                responseCode = 1;
                return;
            }
            responseCode = 3;
        }
    }

    private void deleteAlbumByIdRequest() {
        if (fields.length != 1) {
            responseCode = 1;
            return;
        }
        try {
            int id = Integer.parseInt(fields[0]);
            boolean res = db.deleteAlbumById(id);
            responseMessage = String.valueOf(res);
        } catch (IllegalArgumentException e) {
            responseCode = 3;
        }
    }

    private void updateSingerRequest() {
        try {
            Singer singer = new Singer(fields);
            boolean res = db.updateSinger(singer);
            responseMessage = String.valueOf(res);
        } catch (IllegalArgumentException e) {
            if (fields.length != 2) {
                responseCode = 1;
                return;
            }
            responseCode = 3;
        }
    }

    private void updateAlbumRequest() {
        try {
            Album album = new Album(null, fields);
            boolean res = db.updateAlbum(album);
            responseMessage = String.valueOf(res);
        } catch (IllegalArgumentException e) {
            if (fields.length != 4) {
                responseCode = 1;
                return;
            }
            responseCode = 3;
        }
    }

    private void countAlbumsOfSingerByIdRequest() {
        if (fields.length != 1) {
            responseCode = 1;
            return;
        }
        try {
            int id = Integer.parseInt(fields[0]);
            Integer res = db.countAlbumsOfSingerById(id);
            responseMessage = String.valueOf(res);
        } catch (IllegalArgumentException e) {
            responseCode = 3;
        }
    }

    private void getAllRequest() {
        if (fields.length != 0) {
            responseCode = 1;
            return;
        }
        Collection collection = db.getAll();
        CollectionXml collectionXml = new CollectionXml(collection);
        responseMessage = String.valueOf(collectionXml);
    }

    private void getAlbumsOfSingerByIdRequest() {
        if (fields.length != 1) {
            responseCode = 1;
            return;
        }
        try {
            int id = Integer.parseInt(fields[0]);
            Collection collection = db.getAlbumsOfSingerById(id);
            CollectionXml collectionXml = new CollectionXml(collection);
            responseMessage = String.valueOf(collectionXml);
        } catch (IllegalArgumentException e) {
            responseCode = 3;
        }
    }

    private void getAllSingersRequest() {
        if (fields.length != 0) {
            responseCode = 1;
            return;
        }
        Collection collection = db.getAllSingers();
        CollectionXml collectionXml = new CollectionXml(collection);
        responseMessage = String.valueOf(collectionXml);
    }

    private void badRequest() {
        responseCode = 2;
    }

    @Override
    public void run() {
        while (true) {
            responseCode = 0;
            responseMessage = "";
            try {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                fields = message.split(String.valueOf(sep));
                int requestCode = Integer.parseInt(fields[0]);
                fields = Arrays.copyOfRange(fields, 1, fields.length);

                switch (requestCode) {
                    case 1: clearRequest();
                    case 2: getSingerByIdRequest();
                    case 3: getAlbumByIdRequest();
                    case 4: addCollectionRequest();
                    case 5: addSingerRequest();
                    case 6: deleteSingerByIdRequest();
                    case 7: addAlbumRequest();
                    case 8: deleteAlbumByIdRequest();
                    case 9: updateSingerRequest();
                    case 10: updateAlbumRequest();
                    case 11: countAlbumsOfSingerByIdRequest();
                    case 12: getAllRequest();
                    case 13: getAlbumsOfSingerByIdRequest();
                    case 14: getAllSingersRequest();
                    default: badRequest();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                responseCode = 3;
            }
            out.println(responseCode + sep + responseMessage);
        }
    }
}
