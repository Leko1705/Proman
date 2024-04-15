package mylib.view;

import mylib.view.diagram.Diagram;
import mylib.view.diagram.NoDiagram;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Display {

    private final ArrayList<Diagram> diagrams = new ArrayList<>();
    private JFrame frame;

    public Display(){
        this(null);
    }

    public Display(Diagram diagram){
        if (diagram == null) diagram = NoDiagram.getInstance();
        diagrams.add(diagram);
    }

    public void show(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        createAndShowGUI((int) dimension.getWidth(), (int) dimension.getHeight());
    }

    public void show(int width, int height){
        createAndShowGUI(width, height);
    }


    private void createAndShowGUI(int width, int height){
        initJFrame(width, height);
        adaptDiagrams();
        frame.setVisible(true);
    }

    private void initJFrame(int width, int height){
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    private void adaptDiagrams(){
        frame.getContentPane().setLayout(new BorderLayout());
        frame.add(diagrams.get(0), BorderLayout.CENTER);
    }

}
