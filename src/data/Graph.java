/*
 *  Program: Edytor grafu - komunikacja miejska
 *     Plik: Graph.java
 *
 *  Klasa Graph reprezentuje schemat komunikacji miejskiej składającej się z:
 *  przystanków (Node),
 *  połączeń między przystankami (Connection)
 *
 *  Obiekty klasy Connection należące do grafu są kolorowane wg rodzaju
 *  środka komunikacji:
 *
 *  Czerwony: Bus
 *  Niebieski: Tramwaj
 *
 *    Autor: Maciej Demucha
 *    Data:  grudzień 2021 r.
 */


/**
 * Klasa reprezentuje graf przedstawiający linię komunikacji miejskiej <br>
 * Przystanki są reprezentowane przez węzły w postaci kół z odpowiednimi nazwami. <br>
 * Kolor krawędzi odpowiada rodzajowi transportu:
 * <ul>
 *     <li>Czerwony: Bus</li>
 *     <li>Niebieski: Tramwaj</li>
 * </ul>
 *
 * @author Maciej Demucha
 * @version 28 grudnia 2021 r.
 */

package data;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;


public class Graph implements Serializable {

    private static final long serialVersionUID = 1L;

    /**Numer linii*/
    private int number;
    /**Rodzaj linii (bus lub tramwaj)*/
    private TransportType transportType;
    /**Zbiór przystanków linii*/
    private Set<Node> nodes;
    /**Lista połączeń pomiędzy przystankami*/
    private List<Connection> connections;

    /**Konstruktor grafu reprezentującego pojedyńczą linię komunikacji*/
    public Graph(int number, TransportType transportType) {
        this.number = number;
        this.transportType = transportType;
        this.nodes = new HashSet<>();
        this.connections = new ArrayList<>();
    }
    /**Konstruktor wykorzystywany przy tworzeniu grafu reprezentującego wszystkie linie komunikacji miejskiej*/
    public Graph(TransportType transportType) {
        this.transportType = transportType;
        this.nodes = new HashSet<>();
        this.connections = new ArrayList<>();
    }
    /**Metoda dodająca przystanek w postaci węzła do listy węzłów grafu*/
    public void addNode(Node node){
        nodes.add(node);
    }
    /**Metoda usuwająca przystanek*/
    public void removeNode(Node node){
        nodes.remove(node);
    }
    /**Metoda zwracająca wszystkie przystanki w postaci tablicy*/
    public Node[] getNodes(){
        Node [] array = new Node[0];
        return nodes.toArray(array);
    }
    /**Metoda dodająca połączenie między przystankami do listy połączeń grafu i ustawiająca odpowiedni kolor:
     * <ul>
     *     <li>Czerwony: Bus</li>
     *     <li>Niebieski: Tramwaj</li>
     * </ul>*/
    public void addConnectionColor(Connection connection){
        connections.add(connection);
        if (this.transportType.equals(TransportType.TRAM)){
            connection.setColor(Color.BLUE);
        }
        else if (this.transportType.equals(TransportType.BUS)){
            connection.setColor(Color.RED);
        }
        else {
            connection.setColor(Color.BLACK);
        }
    }
    /**Metoda dodająca połączenie między przystankami do listy połączeń grafu*/
    public void addConnection(Connection connection){
        connections.add(connection);
    }
    /**Metoda kolorująca wszystkie połączenia w grafie*/
    public void colorConnections(Color color){
        for (Connection connection : connections) {
            connection.setColor(color);
        }
    }
    /**Metoda usuwająca połączenie między przystankami z listy*/
    public void removeConnection(Connection connection){
        connections.remove(connection);
    }
    /**Metoda tworząca połączenie między przystankami*/
    public void createConnection(Node node1, Node node2){
        Connection connection = new Connection(node1, node2);
        this.addConnectionColor(connection);
    }
    /**Metoda zwracająca wszystkie połączenia w postaci tablicy*/
    public Connection[] getConnections(){
        Connection [] array = new Connection[0];
        return connections.toArray(array);
    }
    /**Metoda zwracająca rodzaj transportu linii*/
    public TransportType getTransportType() {
        return transportType;
    }

    /**Przedefiniowana metoda toString zwracająca numer linii i rodzaj transportu*/
    @Override
    public String toString(){
        return "\nNr linii: " + number + " \nRodzaj transportu: " + transportType.toString() + "\n";
    }
    /**Metoda rysująca przystanki i linie, które zawiera graf linii komunikacji*/
    public void draw(Graphics g){
        for(Connection connection : connections){
            connection.draw((Graphics2D) g);
        }
        for(Node node : nodes){
            node.draw((Graphics2D) g);
        }
    }

}
