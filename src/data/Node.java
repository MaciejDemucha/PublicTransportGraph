/*
 *  Program: Edytor grafu - komunikacja miejska
 *     Plik: Node.java
 *
 *  Klasa Node reprezentuje w postaci węzłów przystanki
 *  komunikacji miejskiej
 *
 *    Autor: Maciej Demucha
 *     Data:  grudzień 2021 r.
 */

/**
 * Klasa reprezentuje węzeł grafu przedstawiający przystanek komunikacji miejskiej <br>
 * Węzeł rysowany jest w postaci okręgu z nazwą wyświetlaną nad nim. <br>
 *
 * @author Maciej Demucha
 * @version 28 grudnia 2021 r.
 */

package data;

import java.awt.*;

public class Node {
    /**Nazwa przystanku*/
    protected String name;

    /** położenie koła*/
    protected int x;
    protected int y;

    /** promień koła*/
    protected int r;

    /** kolor wypełnienia*/
    private Color color;

    /** Konstruktor przystanku z jego położeniem i nazwą*/
    public Node(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.r = 10;
        this.color = Color.WHITE;
        this.name = name;
    }
    /** Metoda zwracająca wartość współrzędnej x przystanku*/
    public int getX() {
        return x;
    }
    /** Metoda ustawiająca wartość współrzędnej x przystanku*/
    public void setX(int x) {
        this.x = x;
    }
    /** Metoda zwracająca wartość współrzędnej y przystanku*/
    public int getY() {
        return y;
    }
    /** Metoda ustawiająca wartość współrzędnej y przystanku*/
    public void setY(int y) {
        this.y = y;
    }
    /** Metoda zwracająca wartość promienia przystanku*/
    public int getR() {
        return r;
    }
    /** Metoda ustawiająca wartość promienia przystanku*/
    public void setR(int r) {
        this.r = r;
    }
    /** Metoda zwracająca kolor przystanku*/
    public Color getColor() {
        return color;
    }

    /** Metoda ustawiająca kolor przystanku*/
    public void setColor(Color color) {
        this.color = color;
    }

    /** Metoda ustawiająca nazwę przystanku*/
    public void setName(String name) {
        this.name = name;
    }
    /** Metoda sprawdzająca czy kursor znajduje się nad węzłem grafu (przystankiem)*/
    public boolean isMouseOver(int mx, int my){
        return (x-mx)*(x-mx)+(y-my)*(y-my)<=r*r;
    }
    /** Metoda rysująca przystanek w postaci okręgu, wypełniająca go kolorem i wypisująca nazwę przystanku*/
    void draw(Graphics2D g) {
        // Rysowanie wypełnionego koła o środku w punkcie  (x,y)
        // i promieniu r
        g.setColor(color);
        g.fillOval(x-r, y-r, 2*r, 2*r);
        g.setColor(Color.BLACK);
        g.drawOval(x-r, y-r, 2*r, 2*r);
        g.drawString(name, x-r+10, y-r-10);
    }
    /** Przedefiniowana metoda toString zwracająca nazwę przystanku i jego współrzędne*/
    @Override
    public String toString(){
        return (name + " (" + x +", " + y + ")");
    }

}
