package graph;

import mvc.IView;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class TextNode
        extends JTextPane
        implements Node<TextNode, TextNode>, NodeModel, IView {

    private long id;

    public TextNode(String text) {
        setText(text);

        StyledDocument documentStyle = getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);

        setDisabledTextColor(Color. BLACK);
        setEnabled(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    setEnabled(true);
                    requestFocusInWindow();
                }
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setEnabled(false);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    setEnabled(false);
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
                }
            }
        });

        ((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter(){
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.equals("\n")){
                    setEnabled(false);
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });

        addKeyListener(new KeyAdapter() {
            private final Set<Integer> pressedKeys = new HashSet<>();

            @Override public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                if (pressedKeys.contains(KeyEvent.VK_ENTER) && pressedKeys.contains(KeyEvent.VK_SHIFT)){
                    setText(getText() + "\n");
                }
            }
            @Override public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });
    }

    public TextNode(){
        this("");
    }

    @Override
    public TextNode getModel() {
        return this;
    }

    @Override
    public TextNode getView() {
        return this;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
