package main;

import context.ApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    public MainFrame(){
        super("Proman");
        init();
        setVisible(true);
    }

    private void init(){
        setIconImage(Resources.getApplicationIcon());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        setSize(width, height);

        MainPanel mainPanel = new MainPanel();
        setContentPane(mainPanel);
        mainPanel.init();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    public static class MainPanel extends JPanel  {
        public void init(){
            setLayout(new BorderLayout());
            ApplicationContext appContext = new ApplicationContext(this);
            add(appContext, BorderLayout.CENTER);
            MainView mainView = new MainView(appContext);
            appContext.open(mainView);
            mainView.onCreate(null);
        }
    }

}
