/*
 *  Program: Edytor grafu - komunikacja miejska
 *     Plik: GraphEditor.java
 *
 *  Klasa GraphEditor implementuje okno główne
 *  dla edytora linii komunikacji miejskiej.
 *
 *    Autor: Maciej Demucha
 *     Data:  grudzień 2021 r.
 */

package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

import data.Connection;
import data.Graph;
import data.Node;
import data.TransportType;


public class GraphEditor extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    int i = 0;

    Timer timer;

    private static final String APP_AUTHOR = "Autor: Maciej Demucha\n  Data: grudzień 2021";
    private static final String APP_TITLE = "Komunikacja miejska";

    private static final String SAVED_GRAPHS = "SAVED_GRAPHS.BIN";

    private static final String APP_INSTRUCTION =
            "                  O P I S   P R O G R A M U \n\n" +
                    "Poszczególne grafy składające się z kół (przystanków) i krawędzi (połączeń) przedstawiają linie komunikacji miejskiej\n " +
                    "Kolor połączenia określa rodzaj środka transportu: \n" +
                    "Czerwony: Bus\n" +
                    "Niebieski: Tramwaj\n\n"+
                    "Aktywna klawisze:\n" +
                    "   strzałki ==> przesuwanie wszystkich kół\n" +
                    "   SHIFT + strzałki ==> szybkie przesuwanie wszystkich kół\n\n" +
                    "ponadto gdy kursor wskazuje koło:\n" +
                    "   DEL   ==> kasowanie koła\n" +
                    "   +, -   ==> powiększanie, pomniejszanie koła\n" +
                    "   r,g,b ==> zmiana koloru koła\n\n" +
                    "Operacje myszka:\n" +
                    "   przeciąganie ==> przesuwanie wszystkich kół\n" +
                    "   PPM ==> tworzenie nowego koła w miejscu kursora\n" +
                    "ponadto gdy kursor wskazuje koło:\n" +
                    "   przeciąganie ==> przesuwanie koła\n" +
                    "   PPM ==> zmiana koloru, imienia koła,\n" +
                    "                   usuwanie koła lub utworzenie połączenia\n\n" +
                    "gdy kursor wskazuje krawędź:\n" +
                    "PPM ==> usunięcie połączenia\n\n" +
                    "Opcje menu:\n" +
                    "Nowa linia: Utworzenie nowej linii komunikacji\n" +
                    "Następna linia: Przeglądanie i edycja poszczególnych linii\n" +
                    "Wszystkie linie: Wyświetlenie wszystkich linii jednocześnie (nie należy edytować linii w tym widoku)\n" +
                    "Wyodrębnij wybraną linię: Koloruje wybrana linię" +
                    "Lista przystanków: Wyświetlenie listy przystanków\n" +
                    "Lista połączeń: Wyświetlenie listy połączeń\n" +
                    "Lista linii: Wyświetlenie listy linii komunikacji\n" +
                    "Wczytaj z pliku: Odczyt grafów z pliku binarnego\n" +
                    "Zapisz do pliku: Zapis grafów do pliku binarnego\n";


    public static void main(String[] args) {
        new GraphEditor();
    }


    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuGraph = new JMenu("Graf");
    private JMenu menuHelp = new JMenu("Pomoc");
    private JMenuItem menuNew = new JMenuItem("Nowa linia", KeyEvent.VK_N);
    private JMenuItem menuNext = new JMenuItem("Następna linia");
    private JMenuItem menuAllLines = new JMenuItem("Wszystkie linie", KeyEvent.VK_X);
    private JMenuItem menuColorLine = new JMenuItem("Wyodrębnij wybraną linię", KeyEvent.VK_X);
    private JMenuItem menuExit = new JMenuItem("Wyjście", KeyEvent.VK_E);
    private JMenuItem menuListOfNodes = new JMenuItem("Lista przystanków", KeyEvent.VK_N);
    private JMenuItem menuListOfConnections = new JMenuItem("Lista połączeń", KeyEvent.VK_N);
    private JMenuItem menuListOfLines = new JMenuItem("Lista linii", KeyEvent.VK_N);
    private JMenuItem menuLoadFromFile = new JMenuItem("Wczytaj z pliku");
    private JMenuItem menuSaveToFile = new JMenuItem("Zapisz do pliku");
    private JMenuItem menuAuthor = new JMenuItem("Autor", KeyEvent.VK_A);
    private JMenuItem menuInstruction = new JMenuItem("Instrukcja", KeyEvent.VK_I);

    private GraphPanel panel = new GraphPanel();


    public GraphEditor() {
        super(APP_TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,600);
        setLocationRelativeTo(null);
        setContentPane(panel);
        createMenu();
        createLines();
        panel.setGraphs(panel.getGraphs());
        setVisible(true);
    }

    private void showListOfNodes(Graph graph) {
        Node[] nodes = graph.getNodes();
        int i = 0;
        StringBuilder message = new StringBuilder("Liczba przystanków: " + nodes.length + "\n");
        for (Node node : nodes) {
            message.append(node).append("    ");
            if (++i % 5 == 0)
                message.append("\n");
        }
        JOptionPane.showMessageDialog(this, message, APP_TITLE + " - Lista przystanków", JOptionPane.PLAIN_MESSAGE);
    }

    private void showListOfConnections(Graph graph) {
        Connection[] connections = graph.getConnections();
        int i = 0;
        StringBuilder message = new StringBuilder("Liczba połączeń: " + connections.length + "\n");
        for (Connection connection : connections) {
            message.append(connection).append("    ");
            if (++i % 5 == 0)
                message.append("\n");
        }
        JOptionPane.showMessageDialog(this, message, APP_TITLE + " - Lista połączeń", JOptionPane.PLAIN_MESSAGE);
    }

    private void showListOfLines() {
        int i = 0;
        StringBuilder message = new StringBuilder("Liczba linii: " + panel.getGraphsNumber() + "\n");
        for (Graph graph : panel.getGraphs()) {
            message.append(graph.toString()).append("    ");
            if (++i % 5 == 0)
                message.append("\n");
        }
        JOptionPane.showMessageDialog(this, message, APP_TITLE + " - Lista linii", JOptionPane.PLAIN_MESSAGE);
    }

    private void createMenu() {
        menuNew.addActionListener(this);
        menuNext.addActionListener(this);
        menuAllLines.addActionListener(this);
        menuColorLine.addActionListener(this);
        menuExit.addActionListener(this);
        menuListOfNodes.addActionListener(this);
        menuListOfConnections.addActionListener(this);
        menuListOfLines.addActionListener(this);
        menuLoadFromFile.addActionListener(this);
        menuSaveToFile.addActionListener(this);
        menuAuthor.addActionListener(this);
        menuInstruction.addActionListener(this);

        menuGraph.setMnemonic(KeyEvent.VK_G);
        menuGraph.add(menuNew);
        menuGraph.add(menuNext);
        menuGraph.add(menuAllLines);
        menuGraph.add(menuColorLine);
        menuGraph.addSeparator();
        menuGraph.add(menuListOfNodes);
        menuGraph.add(menuListOfConnections);
        menuGraph.add(menuListOfLines);
        menuGraph.addSeparator();
        menuGraph.add(menuLoadFromFile);
        menuGraph.add(menuSaveToFile);
        menuGraph.addSeparator();
        menuGraph.add(menuExit);

        menuHelp.setMnemonic(KeyEvent.VK_H);
        menuHelp.add(menuInstruction);
        menuHelp.add(menuAuthor);

        menuBar.add(menuGraph);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        menuGraph.remove(menuColorLine);

        if (source == menuNew) {
            try{
                String numberString = JOptionPane.showInputDialog("Enter line number");
                int number = Integer.parseInt(numberString);

                TransportType[] transportTypeValues = new TransportType[2];
                transportTypeValues[0] = TransportType.TRAM;
                transportTypeValues[1] = TransportType.BUS;

            TransportType transportType = (TransportType) JOptionPane.showInputDialog(null,
                    "Wybierz rodzaj transportu:",
                    "Wybierz rodzaj transportu", JOptionPane.QUESTION_MESSAGE,
                    null, transportTypeValues, TransportType.TRAM);

            panel.addGraph(new Graph(number, transportType));

            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(this, "Musisz wprowadzić liczbę calkowitą!");
                return;
            }
        }
        if (source == menuNext) {
            if(i+1 == panel.getGraphsNumber()){
                i = 0;
            }
            else {
                i++;
            }
            panel.setGraph(i);
            System.out.println("Graph number: " + i);
        }
        if (source == menuAllLines) {
            panel.setGraphs(panel.getGraphs());
            menuGraph.add(menuColorLine);
            repaint();
        }
        if (source == menuColorLine) {
            try {
                Graph graphToColor = (Graph) JOptionPane.showInputDialog(null,
                        "Wybierz linię:",
                        "Wybierz linię", JOptionPane.QUESTION_MESSAGE,
                        null, panel.getGraphsArray(), panel.getGraphsArray()[0]);

                Color previousColor = null;

                if(graphToColor.getTransportType().equals(TransportType.TRAM)){
                    previousColor = Color.BLUE;
                }
                else if (graphToColor.getTransportType().equals(TransportType.BUS)){
                    previousColor = Color.RED;
                }

                JOptionPane.showMessageDialog(this, "Wyodrębniono wybrana linię.");
                graphToColor.colorConnections(Color.green);
                TimeUnit.SECONDS.sleep(5);
                graphToColor.colorConnections(previousColor);
                menuGraph.add(menuColorLine);
                repaint();
            }
            catch (NullPointerException | InterruptedException e){
                return;
            }
        }
        if (source == menuListOfNodes) {
            showListOfNodes(panel.getGraph());
        }
        if (source == menuListOfConnections) {
            showListOfConnections(panel.getGraph());
        }
        if(source == menuListOfLines){
            showListOfLines();
        }
        if(source == menuSaveToFile){
            try {
                saveToFile(SAVED_GRAPHS);
                JOptionPane.showMessageDialog(null, "Dane zostały zapisane do pliku " + SAVED_GRAPHS);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(source == menuLoadFromFile){
            try {
                loadFromFile(SAVED_GRAPHS);
                JOptionPane.showMessageDialog(null, "Dane zostały wczytane z pliku " + SAVED_GRAPHS);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (source == menuAuthor) {
            JOptionPane.showMessageDialog(this, APP_AUTHOR, APP_TITLE, JOptionPane.INFORMATION_MESSAGE);
        }
        if (source == menuInstruction) {
            JOptionPane.showMessageDialog(this, APP_INSTRUCTION, APP_TITLE, JOptionPane.PLAIN_MESSAGE);
        }
        if (source == menuExit) {
            System.exit(0);
        }
        if (source == timer) {
            System.exit(0);
        }
    }

    //wczytanie grafów z pliku
    void loadFromFile(String file_name) throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file_name))) {
            panel.setGraphs((ArrayList<Graph>)in.readObject());
            repaint();
        } catch (FileNotFoundException e) {
            throw new Exception("Nie odnaleziono pliku " + file_name);
        } catch (Exception e) {
            throw new Exception("Wystąpił błąd podczas odczytu danych z pliku.");
        }
    }

    //Zapis grafów do pliku
    void saveToFile(String file_name) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file_name))) {
            out.writeObject(panel.getGraphs());
        } catch (FileNotFoundException e) {
            throw new Exception("Nie odnaleziono pliku " + file_name);
        } catch (IOException e) {
            throw new Exception("Wystąpił błąd podczas zapisu danych do pliku.");
        }
    }

    private void createLines() {
        Graph linia1 = new Graph(1, TransportType.TRAM);
        Graph linia146 = new Graph(146, TransportType.BUS);

        Node pg = new Node(103, 38, "Plac Grunwaldzki");
        Node pgBus = new Node(208, 42, "Plac Grunwaldzki");
        Node kliniki = new Node(133, 108, "Kliniki");
        Node klinikiBus = new Node(198, 117, "Kliniki");
        Node hs = new Node(137, 184, "Hala Stulecia");
        Node hsBus = new Node(225, 184, "Hala Stulecia");
        Node zoo = new Node(166, 245, "Zoo");
        Node tramwajowaBus = new Node(208, 318, "Tramwajowa");
        Node tramwajowa = new Node(284, 313, "Tramwajowa");

        Connection tramwajowaZoo = new Connection(tramwajowa, zoo);
        Connection zooHs = new Connection(zoo, hs);
        Connection hsKliniki = new Connection(hs, kliniki);
        Connection klinikiPg = new Connection(kliniki, pg);

        Connection tramwajowaHsBus = new Connection(tramwajowaBus, hsBus);
        Connection hsKlinikiBus = new Connection(hsBus, klinikiBus);
        Connection klinikiPgBus = new Connection(klinikiBus, pgBus);

        linia1.addConnectionColor(tramwajowaZoo);
        linia1.addConnectionColor(zooHs);
        linia1.addConnectionColor(hsKliniki);
        linia1.addConnectionColor(klinikiPg);

        linia1.addNode(tramwajowa);
        linia1.addNode(zoo);
        linia1.addNode(hs);
        linia1.addNode(kliniki);
        linia1.addNode(pg);

        linia146.addConnectionColor(tramwajowaHsBus);
        linia146.addConnectionColor(hsKlinikiBus);
        linia146.addConnectionColor(klinikiPgBus);

        linia146.addNode(tramwajowaBus);
        linia146.addNode(hsBus);
        linia146.addNode(klinikiBus);
        linia146.addNode(pgBus);

        panel.addGraph(linia1);

        panel.addGraph(linia146);
    }
}
