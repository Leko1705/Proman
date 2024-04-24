package diagrams.clazz.graph.node;

import graph.Node;
import graph.NodeModel;
import mvc.IView;
import mylib.structs.ObservedList;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassNode
        extends JPanel
        implements Node<ClassNode, ClassNode>, IView, NodeModel {

    private long id;

    private boolean hasFocus = false;


    private ClassType classType;

    private String name;

    private final ObservedList<Generic> generics = new ObservedList<>(new ArrayList<>());

    private final ObservedList<Attribute> attributes = new ObservedList<>(new ArrayList<>());

    private final ObservedList<Method> methods = new ObservedList<>(new ArrayList<>());

    private String description = "";


    public ClassNode(ClassType classType, String name){
        this.classType = Objects.requireNonNull(classType);
        this.name = Objects.requireNonNull(name);

        setOpaque(true);

        ObservedList.ListChangeListener classChangeListener = list -> {

            for (Object o : list){
                ClassElement element = (ClassElement) o;
                element.setClassChangeListener(this::repaint);
            }

            Container container = getParent();
            if (container != null)
                container.repaint();
            repaint();
        };

        generics.addListChangeListener(classChangeListener);
        attributes.addListChangeListener(classChangeListener);
        methods.addListChangeListener(classChangeListener);

        resize();
    }

    public boolean isFocused(){
        return hasFocus;
    }

    public void setFocused(boolean hasFocus) {
        this.hasFocus = hasFocus;
        repaint();
    }

    public String getClassName(){
        return name;
    }

    public void setClassName(String name) {
        this.name = Objects.requireNonNull(name);
        repaint();
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = Objects.requireNonNull(classType);
        repaint();
    }


    public List<Generic> getGenerics() {
        return generics;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public ClassNode getModel() {
        return this;
    }

    @Override
    public ClassNode getView() {
        return this;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        resize();

        Color prev = g.getColor();
        Color bg = hasFocus
                ? new Color(255, 234, 157)
                : new Color(174, 233, 255);
        g.setColor(bg);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(prev);

        drawCenteredString(g, classType.getTextFormat(), new Rectangle(0, 0, getWidth(), 20), new JLabel().getFont());
        drawCenteredString(g, name, new Rectangle(0, 0, getWidth(), 50), new JLabel().getFont());

        int height = 17 * 2 - 1;
        g.drawRect(0, 0, getWidth(), height);

        for (Attribute attribute : attributes) {
            height += 17;
            String string = attribute.getText();
            g.drawString(string, 5, height);
            if (attribute.isStatic())
                g.drawLine(4, height+1, 6+getMinimumWidth(string), height+1);
        }

        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        height += 15;

        g.drawRect(0, 0, getWidth(), height);

        for (Method method : methods) {
            height += 17;
            String string = method.getText();

            Font prevFont = g.getFont();
            if (method.isAbstract())
                g.setFont(new Font(prevFont.getName(), Font.ITALIC | Font.BOLD, prevFont.getSize()));

            g.drawString(string, 5, height);
            if (method.isStatic())
                g.drawLine(4, height+1, 6+getMinimumWidth(string), height+1);

            g.setFont(prevFont);
        }
    }

    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }

    private void resize(){
        String longestString = getClassType().getTextFormat();
        longestString = max(longestString, getClassName());

        int height = 17 * 2 - 1;

        for (Attribute attribute : getAttributes()){
            String text = attribute.getText();
            longestString = max(longestString, text);
            height += 17;
        }

        height += 15;

        for (Method method : getMethods()){
            longestString = max(longestString, method.getText());
            height += 17;
        }

        height += 15;

        setSize(getMinimumWidth(longestString) + 50, height);
    }

    private int getMinimumWidth(String s){
        JLabel label = new JLabel(s);
        FontMetrics fm = label.getFontMetrics(label.getFont());
        return fm.stringWidth(s);
    }

    private static String max(String s1, String s2){
        if (s1 == null) return s2;
        if (s2 == null) return s1;
        return s1.length() > s2.length() ? s1 : s2;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }

}
