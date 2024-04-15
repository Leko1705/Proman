package mylib.swingx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

public class JDragPanel extends JPanel {

    private Point initialMouseLocation;
    private Point initialComponentLocation;

    private final ComponentDragListener dragListener = new ComponentDragListener();

    private Component currentFocused = null;
    private Component lastFocused = null;

    private final HashSet<ActionListener> actionListeners = new HashSet<>();

    private boolean cameraCanMove;

    public JDragPanel(boolean cameraCanMove){
        this.cameraCanMove = cameraCanMove;
        CameraMoveListener listener = new CameraMoveListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public JDragPanel(){
        this(false);
    }

    public void setCameraCanMove(boolean cameraCanMove) {
        this.cameraCanMove = cameraCanMove;
    }

    @Override
    public Component add(Component comp) {
        setListeners(comp);
        return super.add(comp);
    }

    @Override
    public Component add(String name, Component comp) {
        setListeners(comp);
        return super.add(name, comp);
    }

    @Override
    public Component add(Component comp, int index) {
        setListeners(comp);
        return super.add(comp, index);
    }

    @Override
    public void add(Component comp, Object constraints) {
        setListeners(comp);
        super.add(comp, constraints);
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        setListeners(comp);
        super.add(comp, constraints, index);
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        super.setLayout(null);
    }

    private void setListeners(Component component){
        component.addMouseListener(dragListener);
        component.addMouseMotionListener(dragListener);
    }

    public Component getFocused(){
        return currentFocused;
    }

    public void setFocused(Component component){
        lastFocused = currentFocused;
        this.currentFocused = component;
        notifyActionListeners(-1);
    }

    public Component getLastFocused() {
        return lastFocused;
    }

    public void addActionListener(ActionListener listener){
        actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener){
        actionListeners.remove(listener);
    }

    private void notifyActionListeners(int eventID){
        ActionEvent e = new ActionEvent(this, eventID, null);
        for (ActionListener listener : actionListeners)
            listener.actionPerformed(e);
    }


    private class ComponentDragListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            initialMouseLocation = e.getLocationOnScreen();
            initialComponentLocation = e.getComponent().getLocation();
            lastFocused = currentFocused;
            currentFocused = e.getComponent();
            notifyActionListeners(MouseEvent.MOUSE_PRESSED);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point finalMouseLocation = e.getLocationOnScreen();
            int deltaX = finalMouseLocation.x - initialMouseLocation.x;
            int deltaY = finalMouseLocation.y - initialMouseLocation.y;
            Point newLocation = new Point(initialComponentLocation.x + deltaX, initialComponentLocation.y + deltaY);
            lastFocused = currentFocused;
            currentFocused = e.getComponent();
            currentFocused.setLocation(newLocation);
            notifyActionListeners(MouseEvent.MOUSE_DRAGGED);
        }
    }


    private class CameraMoveListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            lastFocused = currentFocused;
            currentFocused = null;
            notifyActionListeners(MouseEvent.MOUSE_PRESSED);
            initialMouseLocation = e.getLocationOnScreen();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (cameraCanMove) {
                for (Component c : getComponents()) {
                    Point finalMouseLocation = e.getLocationOnScreen();
                    int deltaX = finalMouseLocation.x - initialMouseLocation.x;
                    int deltaY = finalMouseLocation.y - initialMouseLocation.y;
                    Point newLocation = new Point(c.getX() + deltaX, c.getY() + deltaY);
                    c.setLocation(newLocation);
                }
                initialMouseLocation = e.getLocationOnScreen();
            }
        }
    }


}
