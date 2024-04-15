package main;

import context.BaseContext;
import context.Context;

import javax.swing.*;
import java.awt.*;

public class MainView extends BaseContext<Void, Void> {

    public MainView(Context<?, ?> context){
        super(context);
    }

    @Override
    public void onCreate(Void initializer) {
        setLayout(new BorderLayout());
        add(new ProjectSelectionView(this), BorderLayout.NORTH);
        setMenuBar();
    }

    private void setMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem newProjectMenuItem = new JMenuItem("New Project");
        newProjectMenuItem.addActionListener(e -> {
                    NewProjectView newProjectView = new NewProjectView(this);
                    getApplicationContext().open(newProjectView);
                    newProjectView.onCreate(null);
                });

        fileMenu.add(newProjectMenuItem);
        menuBar.add(fileMenu);

        ((JFrame)SwingUtilities.getWindowAncestor(this)).setJMenuBar(menuBar);
    }

    @Override
    public void closeRecentOpened() {
        setMenuBar();
        getApplicationContext().closeRecentOpened();
    }


}
