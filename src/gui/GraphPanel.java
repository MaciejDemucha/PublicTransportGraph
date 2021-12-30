/*
 *  Program: Edytor grafu - komunikacja miejska
 *     Plik: GraphPanel.java
 *
 *  Klasa GraphPanel implementuje podstawowe funkcjonalności
 *  graficznego edytora grafu nieskierowanego reprezentującego linię komunikacji miejskiej.
 *  Klasa umożliwia:
 *     - rysowanie grafu w oknie,
 *     - obsługę zdarzeń generowanych przez myszkę,
 *     - tworzenie i obsługę menu kontekstowych
 *       umożliwiających wykonywanie operacji edycyjnych.
 *
 *    Autor: Maciej Demucha
 *     Data:  grudzień 2021 r.
 */

package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

import data.Connection;
import data.Graph;
import data.Node;
import data.TransportType;


public class GraphPanel extends JPanel
        implements MouseListener, MouseMotionListener, KeyListener {

    private static final long serialVersionUID = 1L;

    protected Graph graph;
    protected ArrayList<Graph> graphs = new ArrayList<Graph>();


    private int mouseX = 0;
    private int mouseY = 0;
    private boolean mouseButtonLeft = false;
    @SuppressWarnings("unused")
    private boolean mouseButtonRight = false;
    protected int mouseCursor = Cursor.DEFAULT_CURSOR;

    protected Node nodeUnderCursor = null;
    protected Connection connectionUnderCursor = null;


    public GraphPanel() {
        this.addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setGraph(int i) {
        this.graph = graphs.get(i);
        repaint();
    }

    public void setGraphs(ArrayList<Graph> graphs){
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Connection> connections = new ArrayList<>();
        for (Graph graph: graphs) {
            nodes.addAll(Arrays.asList(graph.getNodes()));
            connections.addAll(Arrays.asList(graph.getConnections()));
        }
        Graph graph = new Graph(TransportType.BOTH);

        for (Node node: nodes) {
            graph.addNode(node);
        }
        for (Connection connection: connections) {
            graph.addConnection(connection);
        }

        this.graph = graph;
    }

    public int getGraphsNumber(){
        return graphs.size();
    }

    public void addGraph(Graph graph){
        graphs.add(graph);
    }

    public ArrayList<Graph> getGraphs(){
       return graphs;
    }

    public Graph[] getGraphsArray(){
        Graph[] graphArray = new Graph[graphs.size()];
       return graphs.toArray(graphArray);
    }

    private Node findNode(int mx, int my){
        for(Node node: graph.getNodes()){
            if (node.isMouseOver(mx, my)){
                return node;
            }
        }
        return null;
    }

    private Node findNode(MouseEvent event){
        return findNode(event.getX(), event.getY());
    }

    private Connection findConnection(int mx, int my){
        for(Connection connection: graph.getConnections()){
            if (connection.isMouseOver(mx, my)){
                return connection;
            }
        }
        return null;
    }

    private Connection findConnection(MouseEvent event){
        return findConnection(event.getX(), event.getY());
    }

    protected void setMouseCursor(MouseEvent event) {
        nodeUnderCursor = findNode(event);
        connectionUnderCursor = findConnection(event);

        if (nodeUnderCursor != null) {
            mouseCursor = Cursor.HAND_CURSOR;
        } else if (mouseButtonLeft) {
            mouseCursor = Cursor.MOVE_CURSOR;
        } else if(connectionUnderCursor != null){
            mouseCursor = Cursor.CROSSHAIR_CURSOR;
        } else {
            mouseCursor = Cursor.DEFAULT_CURSOR;
        }

        setCursor(Cursor.getPredefinedCursor(mouseCursor));
        mouseX = event.getX();
        mouseY = event.getY();
    }

    protected void setMouseCursor() {
        nodeUnderCursor = findNode(mouseX, mouseY);
        connectionUnderCursor = findConnection(mouseX, mouseY);
        if (nodeUnderCursor != null) {
            mouseCursor = Cursor.HAND_CURSOR;
        } else if (mouseButtonLeft) {
            mouseCursor = Cursor.MOVE_CURSOR;
        } else if(connectionUnderCursor != null){
            mouseCursor = Cursor.CROSSHAIR_CURSOR;
        } else {
            mouseCursor = Cursor.DEFAULT_CURSOR;
        }
        setCursor(Cursor.getPredefinedCursor(mouseCursor));
    }


    private void moveNode(int dx, int dy, Node node){
        node.setX(node.getX()+dx);
        node.setY(node.getY()+dy);
    }


    private void moveAllNodes(int dx, int dy) {
        for (Node node : graph.getNodes()) {
            moveNode(dx, dy, node);
        }
    }

    private void moveConnection(int dx, int dy, Connection connection){
        Node node1 = connection.getNode1();
        Node node2 = connection.getNode2();
        node1.setX(node1.getX()+dx);
        node1.setY(node1.getY()+dy);
        node2.setX(node2.getX()+dx);
        node2.setY(node2.getY()+dy);
    }

    /*
     * UWAGA: ta metoda będzie wywoływana automatycznie przy każdej potrzebie
     * odrysowania na ekranie zawartości panelu
     *
     * W tej metodzie NIE WOLNO !!! wywoływać metody repaint()
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graph==null) return;
        graph.draw(g);

    }



    /*
     * Implementacja interfejsu MouseListener - obsługa zdarzeń generowanych przez myszkę
     * gdy kursor myszki jest na tym panelu
     */
    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getButton()==1) mouseButtonLeft = true;
        if (event.getButton()==3) mouseButtonRight = true;
        setMouseCursor(event);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (event.getButton() == 1)
            mouseButtonLeft = false;
        if (event.getButton() == 3)
            mouseButtonRight = false;
        setMouseCursor(event);
        if (event.getButton() == 3) {
            if (nodeUnderCursor != null) {
                createPopupMenu(event, nodeUnderCursor);
            }
            else if(connectionUnderCursor != null){
                createPopupMenu(event, connectionUnderCursor);
            }
            else {
                createPopupMenu(event);
            }
        }
    }


    /*
     * Implementacja interfejsu MouseMotionListener
     *  - obsługa zdarzeń generowanych przez ruch myszki
     * gdy kursor myszki jest na tym panelu
     */
    @Override
    public void mouseDragged(MouseEvent event) {
        if (mouseButtonLeft) {
            if (nodeUnderCursor != null) {
                moveNode(event.getX() - mouseX, event.getY() - mouseY, nodeUnderCursor);
            }
            else if (connectionUnderCursor != null) {
                moveConnection(event.getX() - mouseX, event.getY() - mouseY, connectionUnderCursor);
            }
            else {
                moveAllNodes(event.getX() - mouseX, event.getY() - mouseY);
            }
        }
        mouseX = event.getX();
        mouseY = event.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        setMouseCursor(event);
    }


    /*
     *  Impelentacja interfejsu KeyListener - obsługa zdarzeń generowanych
     *  przez klawiaturę gdy focus jest ustawiony na ten obiekt.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        {  int dist;
            if (event.isShiftDown()) dist = 10;
            else dist = 1;
            switch (event.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    moveAllNodes(-dist, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    moveAllNodes(dist, 0);
                    break;
                case KeyEvent.VK_UP:
                    moveAllNodes(0, -dist);
                    break;
                case KeyEvent.VK_DOWN:
                    moveAllNodes(0, dist);
                    break;
                case KeyEvent.VK_DELETE:
                    if (nodeUnderCursor != null) {
                        graph.removeNode(nodeUnderCursor);
                        nodeUnderCursor = null;
                    }
                    break;
            }
        }
        repaint();
        setMouseCursor();
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    @Override
    public void keyTyped(KeyEvent event) {
        char znak=event.getKeyChar();
        if (nodeUnderCursor!=null){
            switch (znak) {
                case 'r':
                    nodeUnderCursor.setColor(Color.RED);
                    break;
                case 'g':
                    nodeUnderCursor.setColor(Color.GREEN);
                    break;
                case 'b':
                    nodeUnderCursor.setColor(Color.BLUE);
                    break;
                case '+':
                    int r = nodeUnderCursor.getR()+10;
                    nodeUnderCursor.setR(r);
                    break;
                case '-':
                    r = nodeUnderCursor.getR()-10;
                    if (r>=10) nodeUnderCursor.setR(r);
                    break;
            }
            repaint();
            setMouseCursor();
        }
    }


    /*
     *  Tworzenie i obsługa menu kontekstowego uruchanianego
     *  poprzez kliknięcie prawym przyciskiem myszki
     */
    protected void createPopupMenu(MouseEvent event) {
        JMenuItem menuItem;

        //Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Create new node");

        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((action) -> {
            String name = JOptionPane.showInputDialog("Enter node name");
            graph.addNode(new Node(event.getX(), event.getY(), name));
            repaint();
        });

        popup.add(menuItem);
        popup.show(event.getComponent(), event.getX(), event.getY());
    }

    protected void createPopupMenu(MouseEvent event, Node node) {
        JMenuItem menuItem;

        // Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Change node color");

        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((a) -> {
            Color newColor = JColorChooser.showDialog(
                    this,
                    "Choose Background Color",
                    node.getColor());
            if (newColor!=null){
                node.setColor(newColor);
            }
            repaint();
        });


        popup.add(menuItem);
        menuItem = new JMenuItem("Remove this node");

        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((action) -> {
            graph.removeNode(node);
            repaint();
        });

        popup.add(menuItem);

        menuItem = new JMenuItem("Change node name");

        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((action) -> {
            String name = JOptionPane.showInputDialog("Fill node name");
            node.setName(name);
            repaint();
        });

        popup.add(menuItem);

        menuItem = new JMenuItem("Create new connection from this node");

        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((action) -> {
            Node node2;

                Node nodeTemp = (Node) JOptionPane.showInputDialog(null,
                        "Wybierz drugi przystanek:",
                        "Wybierz drugi przystanek", JOptionPane.QUESTION_MESSAGE,
                        null, graph.getNodes(), graph.getNodes()[0]);

                if(nodeTemp != null) node2 = nodeTemp;

                else return;

                graph.createConnection(node, node2);
                repaint();
        });

        popup.add(menuItem);
        popup.show(event.getComponent(), event.getX(), event.getY());
    }

    protected void createPopupMenu(MouseEvent event, Connection connection){
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem;
        menuItem = new JMenuItem("Remove this connection");

        // Implementacja słuchacza zdarzeń za pomocą wyrażenia Lambda
        menuItem.addActionListener((action) -> {
            graph.removeConnection(connection);
            repaint();
        });

        popup.add(menuItem);
        popup.show(event.getComponent(), event.getX(), event.getY());
    }

}
