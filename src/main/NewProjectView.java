package main;

import context.BaseContext;
import context.Context;
import mylib.io.BuffedFile;
import utils.FileManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NewProjectView extends BaseContext<Void, Void> {

    protected NewProjectView(Context<?, ?> parent) {
        super(parent);
    }

    @Override
    public void onCreate(Void view) {
        setMenuBar();

        setLayout(new FlowLayout());
        JTextField nameField = new JTextField(20);
        nameField.addActionListener(
                e -> KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner());
        add(nameField);

        JButton createButton = new JButton("create");
        createButton.addActionListener(e -> {
            FileManager manager = FileManager.getManager();
            Path path = manager.getRoot().resolve(nameField.getText());
            try {
                Files.createDirectory(path);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            close();
        });
        add(createButton);
    }

    private void setMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenuItem backItem = new JMenuItem("back");
        backItem.addActionListener(e -> close());
        menuBar.add(backItem);

        ((JFrame)SwingUtilities.getWindowAncestor(this)).setJMenuBar(menuBar);
    }

    @Override
    public void closeRecentOpened() {
        throw new UnsupportedOperationException("closeRecentOpened");
    }
}
