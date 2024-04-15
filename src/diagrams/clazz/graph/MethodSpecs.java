package diagrams.clazz.graph;

import diagrams.clazz.graph.node.ClassNode;
import diagrams.clazz.graph.node.Method;
import diagrams.clazz.graph.node.Parameter;
import diagrams.clazz.graph.node.Visibility;
import mylib.swingx.JComponentRecycler;
import utils.DocumentAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;

public class MethodSpecs extends JPanel {

    private final JComponentRecycler recycler;

    private final JTextField nameField = new JTextField(10);
    private final JTextField typeField = new JTextField(10);

    private JRadioButton pub, priv, prot, def, staticBtn, abstractBtn;

    private final JComponentRecycler parameters = new JComponentRecycler();

    private final Method method;

    private final ClassNode owner;

    public MethodSpecs(JComponentRecycler recycler, Method method, ClassNode owner){
        this.recycler = recycler;
        this.method = method;
        this.owner = owner;
        setLayout(new BorderLayout());
        JPanel orientation = new JPanel(new FlowLayout(FlowLayout.LEFT));
        setUpTextFieldsAndRemove(orientation, method.getName(), method.getType());
        setupVisibilitySelection(orientation, method);
        setUpParameterRecycler(this);
        for (Parameter parameter : method.getParameters())
            this.parameters.add(new ParamSpecs(parameters, parameter, method));
        if (method.isStatic())
            staticBtn.setSelected(true);
        if (method.isAbstract())
            abstractBtn.setSelected(true);
        add(orientation, BorderLayout.NORTH);
        setMaximumSize(new Dimension(getMaximumSize().width, 300));
    }


    private void setUpTextFieldsAndRemove(JPanel orientation, String name, String type){
        Box box = new Box(BoxLayout.Y_AXIS);

        JPanel threshold = new JPanel();
        nameField.setText(name);
        nameField.getDocument().addDocumentListener(new NameChangeListener());
        nameField.addActionListener(e -> KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner());
        threshold.add(new JLabel("name:"));
        threshold.add(nameField);
        box.add(threshold);

        threshold = new JPanel();
        typeField.setText(type);
        typeField.getDocument().addDocumentListener(new TypeChangeListener());
        typeField.addActionListener(e -> KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner());
        threshold.add(new JLabel("type:"));
        threshold.add(typeField);
        box.add(threshold);

        JButton removeButton = new JButton("remove method");
        removeButton.addActionListener(e -> {
            owner.getMethods().remove(method);
            recycler.remove(this);
        });
        box.add(removeButton);

        orientation.add(box);
    }

    private void setupVisibilitySelection(JPanel orientation, Method method){
        Box box = new Box(BoxLayout.Y_AXIS);
        pub = new JRadioButton("public");
        pub.addActionListener(e -> method.setVisibility(Visibility.PUBLIC));
        box.add(pub);
        priv = new JRadioButton("private");
        priv.addActionListener(e -> method.setVisibility(Visibility.PRIVATE));
        box.add(priv);
        prot = new JRadioButton("protected");
        prot.addActionListener(e -> method.setVisibility(Visibility.PROTECTED));
        box.add(prot);
        def = new JRadioButton("default");
        def.addActionListener(e -> method.setVisibility(Visibility.DEFAULT));
        box.add(def);

        ButtonGroup group = new ButtonGroup();
        group.add(pub);
        group.add(priv);
        group.add(prot);
        group.add(def);

        switch (method.getVisibility()){
            case PUBLIC -> group.setSelected(pub.getModel(), true);
            case PRIVATE -> group.setSelected(priv.getModel(), true);
            case PROTECTED -> group.setSelected(prot.getModel(), true);
            case DEFAULT -> group.setSelected(def.getModel(), true);
        }

        box.add((new JSeparator()));

        staticBtn = new JRadioButton("static");
        staticBtn.addActionListener(e -> method.setStatic(!method.isStatic()));
        box.add(staticBtn);

        abstractBtn = new JRadioButton("abstract");
        abstractBtn.addActionListener(e -> method.setAbstract(!method.isAbstract()));
        box.add(abstractBtn);

        orientation.add(box);
    }

    private void setUpParameterRecycler(JPanel orientation){
        JPanel parameterArea = new JPanel(new BorderLayout());
        parameterArea.add(new JLabel("parameters:"), BorderLayout.NORTH);
        parameterArea.add(parameters, BorderLayout.CENTER);

        JPanel inputArea = new JPanel(new BorderLayout());
        inputArea.add(new JLabel("add parameter:"), BorderLayout.WEST);
        JTextField paramInputField = createParameterInputField();
        inputArea.add(paramInputField, BorderLayout.CENTER);
        inputArea.add(new JSeparator(), BorderLayout.SOUTH);

        parameterArea.add(inputArea, BorderLayout.SOUTH);
        orientation.add(parameterArea, BorderLayout.CENTER);
    }

    private JTextField createParameterInputField() {
        JTextField paramInputField = new JTextField(15);
        paramInputField.addActionListener(e -> {
            String input = paramInputField.getText();

            if (input.isEmpty() || hasParameter(input)) return;

            Parameter parameter = new Parameter("int", input);
            ParamSpecs specs = new ParamSpecs(parameters, parameter, method);
            method.getParameters().add(specs.getParameter());
            parameters.add(specs);
            paramInputField.setText("");
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
        });
        return paramInputField;
    }

    private boolean hasParameter(String input){
        for (Parameter parameter : method.getParameters())
            if (parameter.getName().equals(input))
                return true;
        return false;
    }


    private class NameChangeListener extends DocumentAdapter {
        @Override
        public void insertUpdate(DocumentEvent e) {
            method.setName(nameField.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            method.setName(nameField.getText());
        }
    }


    private class TypeChangeListener extends DocumentAdapter {
        @Override
        public void insertUpdate(DocumentEvent e) {
            method.setType(typeField.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            method.setType(typeField.getText());
        }
    }


}
