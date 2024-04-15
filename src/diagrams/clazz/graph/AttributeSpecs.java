package diagrams.clazz.graph;

import diagrams.clazz.graph.node.Attribute;
import diagrams.clazz.graph.node.ClassNode;
import diagrams.clazz.graph.node.Visibility;
import mylib.swingx.JComponentRecycler;
import utils.DocumentAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;

public class AttributeSpecs extends JPanel {

    private final JComponentRecycler recycler;

    private final JTextField nameField = new JTextField(10);
    private final JTextField typeField = new JTextField(10);

    private JRadioButton pub, priv, prot, def, staticBtn, getterBtn, setterBtn;

    private final Attribute attribute;

    private final ClassNode owner;


    public AttributeSpecs(JComponentRecycler recycler, Attribute attribute,ClassNode owner){
        this.recycler = recycler;
        this.attribute = attribute;
        this.owner = owner;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setUpTextFieldsAndRemove(attribute);
        setupVisibilitySelection(attribute);
        setMaximumSize(new Dimension(getMaximumSize().width, 170));
    }


    private void setUpTextFieldsAndRemove(Attribute attribute){
        Box box = new Box(BoxLayout.Y_AXIS);

        JPanel threshold = new JPanel();
        nameField.setText(attribute.getName());
        nameField.getDocument().addDocumentListener(new NameChangeListener());
        nameField.addActionListener(e -> KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner());
        threshold.add(new JLabel("name:"));
        threshold.add(nameField);
        box.add(threshold);

        threshold = new JPanel();
        typeField.setText(attribute.getType());
        typeField.getDocument().addDocumentListener(new TypeChangeListener());
        typeField.addActionListener(e -> KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner());
        threshold.add(new JLabel("type:"));
        threshold.add(typeField);
        box.add(threshold);

        JButton removeButton = new JButton("remove attribute");
        removeButton.addActionListener(e -> {
            owner.getAttributes().remove(attribute);
            recycler.remove(this);
            recycler.repaint();
        });
        box.add(removeButton);

        add(box);
    }

    private void setupVisibilitySelection(Attribute attribute){
        Box box = new Box(BoxLayout.Y_AXIS);
        pub = new JRadioButton("public");
        pub.addActionListener(e -> attribute.setVisibility(Visibility.PUBLIC));
        box.add(pub);
        priv = new JRadioButton("private");
        priv.addActionListener(e -> attribute.setVisibility(Visibility.PRIVATE));
        box.add(priv);
        prot = new JRadioButton("protected");
        prot.addActionListener(e -> attribute.setVisibility(Visibility.PROTECTED));
        box.add(prot);
        def = new JRadioButton("default");
        def.addActionListener(e -> attribute.setVisibility(Visibility.DEFAULT));
        box.add(def);

        ButtonGroup group = new ButtonGroup();
        group.add(pub);
        group.add(priv);
        group.add(prot);
        group.add(def);
        group.setSelected(pub.getModel(), true);

        switch (attribute.getVisibility()){
            case PUBLIC -> group.setSelected(pub.getModel(), true);
            case PRIVATE -> group.setSelected(priv.getModel(), true);
            case PROTECTED -> group.setSelected(prot.getModel(), true);
            case DEFAULT -> group.setSelected(def.getModel(), true);
        }

        box.add((new JSeparator()));

        staticBtn = new JRadioButton("static");
        staticBtn.addActionListener(e -> attribute.setStatic(!attribute.isStatic()));
        box.add(staticBtn);

        getterBtn = new JRadioButton("getter");
        getterBtn.addActionListener(e -> attribute.setGetter(!attribute.hasGetter()));
        box.add(getterBtn);

        setterBtn = new JRadioButton("setter");
        setterBtn.addActionListener(e -> attribute.setSetter(!attribute.hasSetter()));
        box.add(setterBtn);

        if (attribute.hasGetter())
            getterBtn.setSelected(true);
        if (attribute.hasSetter())
            setterBtn.setSelected(true);


        add(box);
    }



    private class NameChangeListener extends DocumentAdapter {
        @Override
        public void insertUpdate(DocumentEvent e) {
            attribute.setName(nameField.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            attribute.setName(nameField.getText());
        }
    }


    private class TypeChangeListener extends DocumentAdapter {
        @Override
        public void insertUpdate(DocumentEvent e) {
            attribute.setType(typeField.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            attribute.setType(typeField.getText());
        }
    }


}
