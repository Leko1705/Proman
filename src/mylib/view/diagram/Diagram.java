package mylib.view.diagram;

import mylib.view.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public abstract class Diagram
        extends JPanel
        implements PropertyChangeListener, MouseInputListener, MouseWheelListener {

    public Diagram(){
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    protected abstract void paintComponent(Graphics g);

    @Override
    public void onChange() {
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {}

}
