package diagrams.clazz;

import diagrams.clazz.graph.AttributeSpecs;
import diagrams.clazz.graph.MethodSpecs;
import diagrams.clazz.graph.node.*;
import mylib.swingx.JComponentRecycler;
import utils.DocumentAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;

public class ClassNodePropertyPanel
        extends JPanel {

    private final ClassNode classNode;


    private final JTextField titleInput = new JTextField("Untitled", 20);
    private final JComboBox<ClassType> typeSelector = new JComboBox<>(
            new ClassType[]{ClassType.CLASS, ClassType.ABSTRACT,
                    ClassType.INTERFACE, ClassType.RECORD, ClassType.ENUMERATION});

    private final JComponentRecycler attributes = new JComponentRecycler();
    private final JComponentRecycler methods = new JComponentRecycler();

    private final JTextArea description = new JTextArea(30, 20);




    public ClassNodePropertyPanel(ClassNode classNode){
        this.classNode = classNode;
        setLayout(new BorderLayout());
        initTops();
        initCenter();

        typeSelector.setSelectedItem(classNode.getClassType());
        titleInput.setText(classNode.getClassName());
        titleInput.addActionListener(e -> KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner());

        //description.setText(classNode.description);
        for (Attribute attribute : classNode.getAttributes())
            attributes.add(new AttributeSpecs(attributes, attribute, classNode));
        for (Method method : classNode.getMethods())
            methods.add(new MethodSpecs(methods, method, classNode));
    }

    private void initTops(){
        JPanel top = new JPanel(new BorderLayout());
        typeSelector.addActionListener(e -> {
            classNode.setClassType((ClassType) typeSelector.getSelectedItem());
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
        });
        top.add(typeSelector, BorderLayout.NORTH);

        titleInput.setHorizontalAlignment(SwingConstants.CENTER);
        titleInput.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 20));
        titleInput.getDocument().addDocumentListener(new TitleInputDocumentListener());

        top.add(titleInput, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);
    }

    private void initCenter(){
        JPanel attributeArea = createAttributeArea();
        JPanel methodArea = createMethodArea();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("attributes", attributeArea);
        tabbedPane.addTab("methods", methodArea);
        tabbedPane.addTab("description", new JScrollPane(description));
        description.getDocument().addDocumentListener(new DescriptionInputListener());
        description.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createAttributeArea(){
        JPanel attributeArea = new JPanel(new BorderLayout());
        attributeArea.add(new JLabel("attributes:"), BorderLayout.NORTH);
        attributeArea.add(attributes, BorderLayout.CENTER);

        JPanel inputArea = new JPanel(new BorderLayout());
        inputArea.add(new JLabel("add attribute:"), BorderLayout.NORTH);
        JTextField inputField = adaptAttributeInputField(attributes);
        inputArea.add(inputField, BorderLayout.CENTER);
        attributeArea.add(inputArea, BorderLayout.SOUTH);
        return attributeArea;
    }

    private JPanel createMethodArea(){
        JPanel methodArea = new JPanel(new BorderLayout());
        methodArea.add(new JLabel("methods:"), BorderLayout.NORTH);
        methodArea.add(methods, BorderLayout.CENTER);

        JPanel inputArea = new JPanel(new BorderLayout());
        inputArea.add(new JLabel("add method:"), BorderLayout.NORTH);
        JTextField inputField = adaptMethodInputField(methods);
        inputArea.add(inputField, BorderLayout.CENTER);
        methodArea.add(inputArea, BorderLayout.SOUTH);
        return methodArea;
    }


    private JTextField adaptAttributeInputField(JComponentRecycler attributes) {
        JTextField inputField = new JTextField(20);
        inputField.addActionListener(e -> {
            String input = inputField.getText();
            if (input.isEmpty()) return;

            Attribute attribute = new Attribute(Visibility.PUBLIC, "int", inputField.getText());

            classNode.getAttributes().add(attribute);
            attributes.add(new AttributeSpecs(attributes, attribute, classNode));

            inputField.setText("");
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
        });
        return inputField;
    }

    private JTextField adaptMethodInputField(JComponentRecycler methods) {
        JTextField inputField = new JTextField(20);
        inputField.addActionListener(e -> {
            String input = inputField.getText();
            if (input.isEmpty()) return;

            Method method = new Method(Visibility.PUBLIC, inputField.getText(), "void");
            MethodSpecs specs = new MethodSpecs(methods, method, classNode);
            classNode.getMethods().add(method);
            methods.add(specs);
            inputField.setText("");
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
        });
        return inputField;
    }


    private class TitleInputDocumentListener extends DocumentAdapter {
        @Override
        public void insertUpdate(DocumentEvent e) {
            update();
        }
        @Override
        public void removeUpdate(DocumentEvent e) {
            update();
        }
        private void update(){
            String title = titleInput.getText();
            if (title.isEmpty()) title = "Untitled";
            classNode.setClassName(title);
        }
    }

    private class DescriptionInputListener extends DocumentAdapter {
        @Override
        public void insertUpdate(DocumentEvent e) {
            update();
        }
        @Override
        public void removeUpdate(DocumentEvent e) {
            update();
        }
        private void update(){
        String desc = description.getText();
        // TODO update description
        }
    }

}
