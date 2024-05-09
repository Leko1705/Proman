package diagrams.utils;

import graph.GraphPanel;
import graph.TextNode;
import utils.GeomUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.geom.Line2D;

public class TextLabelNode extends TextNode implements BaseNode<TextNode, TextNode> {

    public TextLabelNode(GraphPanel owner, String initialText) {
        super(initialText);
        if (initialText.isEmpty())
            throw new IllegalStateException("text must not be empty");

        getStyledDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void removeUpdate(DocumentEvent e) {
                checkAutoRemove();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkAutoRemove();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkAutoRemove();
            }

            private void checkAutoRemove(){
                if (getText().isEmpty())
                    owner.removeNode(TextLabelNode.this);
                else
                    updateSize(getText());
            }
        });

        setOpaque(false);
        updateSize(initialText);
    }

    private void updateSize(String text){
        JLabel label = new JLabel(text);
        FontMetrics fm = label.getFontMetrics(label.getFont());
        int width = fm.stringWidth(text);
        int height = fm.getHeight() * text.split("\n").length;

        setSize(width, height);
    }

    @Override
    public Point getNearestPointOnOutline(Point q) {
        Rectangle r = getBounds();
        Point p = GeomUtils.getCenter(r);
        return GeomUtils.lineIntersectionOnRect(r, new Line2D.Double(p, q));
    }
}
