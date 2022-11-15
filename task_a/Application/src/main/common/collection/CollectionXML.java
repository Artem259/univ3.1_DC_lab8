package main.common.collection;

import main.common.Album;
import main.common.Singer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class CollectionXML {
    private final Collection collection;
    private final File dtdFile;

    public CollectionXML(File dtdFile) {
        this.collection = new Collection();
        this.dtdFile = dtdFile;
    }

    public CollectionXML(Collection collection, File dtdFile) {
        this.collection = collection;
        this.dtdFile = dtdFile;
    }

    public Collection getCollection() {
        return collection;
    }

    public String toXmlString() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        assert doc != null;
        List<List<Album>> sortedAlbums = collection.getSortedAlbums();

        Element collectionElem = doc.createElement("collection");
        doc.appendChild(collectionElem);
        for (int i=0; i<collection.singers.size(); i++) {
            Singer singer = collection.singers.get(i);
            Element singerElem = doc.createElement("singer");
            singerElem.setAttribute("id", singer.getId().toString());
            singerElem.setAttribute("name", singer.getName());
            collectionElem.appendChild(singerElem);
            for (int j=0; j<sortedAlbums.get(i).size(); j++) {
                Album album = sortedAlbums.get(i).get(j);
                Element albumElem = doc.createElement("album");
                albumElem.setAttribute("id", album.getId().toString());
                albumElem.setAttribute("name", album.getName());
                albumElem.setAttribute("year", album.getYear().toString());
                albumElem.setAttribute("genre", album.getGenre());
                singerElem.appendChild(albumElem);
            }
        }

        StringWriter sw = new StringWriter();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdFile.getAbsolutePath());
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return sw.toString();
    }

    public String toXmlFormattedString() {
        StringWriter sw = new StringWriter();
        try {
            String str = toXmlString();
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Source strSource = new StreamSource(new StringReader(str));
            Result strResult = new StreamResult(sw);
            transformer.transform(strSource, strResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    public void toXmlFile(File xmlFile) {
        try {
            String str = toXmlString();
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Source strSource = new StreamSource(new StringReader(str));
            Result fileResult = new StreamResult(xmlFile.getAbsoluteFile());
            transformer.transform(strSource, fileResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void fromXmlFile(File xmlFile) {
        collection.clear();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        Document doc = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) {
                    System.out.println("Warning! " + exception.getMessage() + " Line: " + exception.getLineNumber());
                }

                @Override
                public void error(SAXParseException exception) {
                    System.out.println("Error! " + exception.getMessage() + " Line: " + exception.getLineNumber());
                }

                @Override
                public void fatalError(SAXParseException exception) {
                    System.out.println("Fatal error! " + exception.getMessage() + " Line: " + exception.getLineNumber());
                }
            });
            doc = db.parse(xmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        assert doc != null;

        Element collectionElem = doc.getDocumentElement();
        if (!collectionElem.getTagName().equals("collection")) {
            throw new RuntimeException();
        }
        NodeList singesList = collectionElem.getElementsByTagName("singer");
        for (int i=0; i<singesList.getLength(); i++) {
            Element singerElem = (Element) singesList.item(i);
            Integer singerId = Integer.valueOf(singerElem.getAttribute("id"));
            String singerName = singerElem.getAttribute("name");
            Singer singer = new Singer(singerId, singerName);
            collection.addSinger(singer);
            NodeList albumsList = singerElem.getElementsByTagName("album");
            for (int j=0; j<albumsList.getLength(); j++) {
                Element albumElem = (Element) albumsList.item(j);
                Integer albumId = Integer.valueOf(albumElem.getAttribute("id"));
                String albumName = albumElem.getAttribute("name");
                Integer albumYear = Integer.valueOf(albumElem.getAttribute("year"));
                String albumGenre = albumElem.getAttribute("genre");
                Album album = new Album(albumId, singer, albumName, albumYear, albumGenre);
                collection.addAlbum(album);
            }
        }
    }

    @Override
    public String toString() {
        return toXmlString();
    }
}
