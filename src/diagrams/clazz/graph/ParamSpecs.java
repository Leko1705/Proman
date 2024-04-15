package diagrams.clazz.graph;

import diagrams.clazz.graph.node.Method;
import diagrams.clazz.graph.node.Parameter;
import mylib.swingx.JComponentRecycler;
import utils.DocumentAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;

public class ParamSpecs extends JPanel {

    private final JComponentRecycler recycler;


    private JTextField nameField;

    private JTextField typeField;

    private Parameter parameter;


    public ParamSpecs(JComponentRecycler recycler, Parameter parameter, Method parent){
        this.recycler = recycler;
        init(parameter.getName(), parameter.getType(), parameter, parent);
    }

    private void init(String name, String type, Parameter parameter, Method parent){
        this.nameField = new JTextField(name, 12);
        nameField.addActionListener(e -> KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner());
        this.typeField = new JTextField(type, 12);
        typeField.addActionListener(e -> KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner());
        this.parameter = parameter;

        nameField.getDocument().addDocumentListener(new ParameterNameListener());
        typeField.getDocument().addDocumentListener(new ParameterTypeListener());

        JButton removeBtn = new JButton("remove");
        removeBtn.addActionListener(e -> {
            parent.getParameters().remove(parameter);
            recycler.remove(this);

        });

        add(nameField);
        add(typeField);
        add(removeBtn);
    }

    public Parameter getParameter(){
        return parameter;
    }


    private class ParameterNameListener extends DocumentAdapter {
        @Override
        public void insertUpdate(DocumentEvent e) {
            onChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onChange();
        }

        private void onChange(){
            parameter.setName(nameField.getText());
        }
    }

    private class ParameterTypeListener extends DocumentAdapter {
        @Override
        public void insertUpdate(DocumentEvent e) {
            onChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onChange();
        }

        private void onChange(){
            parameter.setType(typeField.getText());
        }
    }
}
