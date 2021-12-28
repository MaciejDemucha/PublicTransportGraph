/*
 *  Program: Edytor grafu - komunikacja miejska
 *     Plik: Connection.java
 *
 *  Klasa Connection reprezentuje połączenie
 *  między przystankami w postaci krawędzi
 *  łączącej węzły grafu.
 *
 *    Autor: Maciej Demucha
 *     Data:  grudzień 2021 r.
 */

/**
 * Klasa reprezentuje połączenie między przystankami w postaci krawędzi łączącej węzły grafu. <br>
 * Kolor połączenia jest zależny od rodzaju transportu i jest ustawiany w <code>Graph</code>
 *
 * @author Maciej Demucha
 * @version 28 grudnia 2021 r.
 */

package data;

import java.awt.*;
import java.awt.geom.Line2D;

public class Connection {
    /** Referencja do pierwszego grafu*/
    protected Node node1;
    /** Referencja do drugiego grafu*/
    protected Node node2;
    /** Kolor krawędzi*/
    private Color color;
    /** Konstruktor z dwoma węzłami jako parametrami*/
    public Connection(Node node1, Node node2) {
        this.node1 = node1;
        this.node2 = node2;
    }
    /** Metoda ustawiająca kolor krawędzi*/
    public void setColor(Color color) {
        this.color = color;
    }
    /** Metoda zwracająca pierwszy węzeł grafu*/
    public Node getNode1(){
        return node1;
    }
    /** Metoda zwracająca drugi węzeł grafu*/
    public Node getNode2(){
        return node2;
    }
    /** Metoda rysująca połączenie o odpowiednim kolorze od grafu pierwszego do drugiego*/
    void draw(Graphics2D g){
       g.setColor(this.color);
       g.setStroke(new BasicStroke(3));
       g.draw(new Line2D.Float(node1.getX(), node1.getY(),node2.getX(), node2.getY()));
       g.setStroke(new BasicStroke(1));
   }
    /** Metoda sprawdzająca czy kursor znajduje się nad połączeniem*/
    public boolean isMouseOver(int mx, int my){
        int x = (int) Math.pow((node1.getX()-node2.getX()), 2);
        int y = (int) Math.pow((node1.getY()-node2.getY()), 2);
        int z = (int) Math.sqrt(x+y);

        int x1 = (int) Math.pow((mx-node1.getX()), 2);
        int y1 = (int) Math.pow((my-node1.getY()), 2);
        int x2 = (int) Math.pow((mx-node2.getX()), 2);
        int y2 = (int) Math.pow((my-node2.getY()), 2);

        int z1 = (int) Math.sqrt(x1+y1);
        int z2 = (int) Math.sqrt(x2+y2);

        return z1+z2 <= z;
    }
    /** Przedefiniowana metoda toString zwracająca nazwy połączonych przystanków i ich współrzędne*/
    @Override
    public String toString(){
        return (node1.name + " (" + node1.getX() +", " + node1.getY() + ")" + "   ->   "
                + node2.name + " (" + node2.getX() +", " + node2.getY() + ")");
    }
}
