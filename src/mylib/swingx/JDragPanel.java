package mylib.swingx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class JDragPanel extends JPanel {

    private Point mousePos;
    private Point initialComponentLocation;

    private final ComponentDragListener dragListener = new ComponentDragListener();

    private boolean cameraCanMove;
    private boolean canZoom;
    private double zoomFactor = 1;
    private int zoomPointX;
    private int zoomPointY;
    private final AffineTransform zoomer = new AffineTransform();

    public JDragPanel(boolean cameraCanMove, boolean canZoom){
        this.cameraCanMove = cameraCanMove;
        this.canZoom = canZoom;
        CameraMoveListener listener = new CameraMoveListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
        ZoomListener zoomListener = new ZoomListener();
        addMouseWheelListener(zoomListener);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control R"), "zoom_reset");
        getActionMap().put("zoom_reset", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomFactor = 1;
                repaint();
            }
        });
    }

    public JDragPanel(){
        this(false, false);
    }

    public void setCameraCanMove(boolean cameraCanMove) {
        this.cameraCanMove = cameraCanMove;
    }

    public void setCanZoom(boolean canZoom) {
        this.canZoom = canZoom;
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
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        if (canZoom) {
            applyZoom(g);
        }
        super.paintComponent(g);
    }

    private void applyZoom(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        zoomer.setToIdentity();
        zoomer.translate(zoomPointX, zoomPointY);
        zoomer.scale(zoomFactor, zoomFactor);
        zoomer.translate(-zoomPointX, -zoomPointY);
        g2.transform(zoomer);
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        super.setLayout(null);
    }

    private void setListeners(Component component){
        transformInverse(component.getLocation());
        component.addMouseListener(dragListener);
        component.addMouseMotionListener(dragListener);
    }

    private class ComponentDragListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            mousePos = transformInverse(e.getLocationOnScreen());
            initialComponentLocation = e.getComponent().getLocation();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point finalMouseLocation = transformInverse(e.getLocationOnScreen());
            int deltaX = finalMouseLocation.x - mousePos.x;
            int deltaY = finalMouseLocation.y - mousePos.y;
            Point newLocation = new Point(initialComponentLocation.x + deltaX, initialComponentLocation.y + deltaY);
            e.getComponent().setLocation(newLocation);
        }
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        transformInverse(e.getLocationOnScreen());
        super.processMouseEvent(e);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        transformInverse(e.getLocationOnScreen());
        super.processMouseMotionEvent(e);
    }

    @Override
    protected void processMouseWheelEvent(MouseWheelEvent e) {
        transformInverse(e.getLocationOnScreen());
        super.processMouseWheelEvent(e);
    }

    // TODO fix random mouse-flinch-glitch
    private class CameraMoveListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            mousePos = e.getLocationOnScreen();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mousePos = e.getLocationOnScreen();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (cameraCanMove) {
                for (Component c : getComponents()) {
                    Point finalMouseLocation = e.getLocationOnScreen();
                    int deltaX = finalMouseLocation.x - mousePos.x;
                    int deltaY = finalMouseLocation.y - mousePos.y;
                    Point newLocation = new Point(c.getX() + deltaX, c.getY() + deltaY);
                    c.setLocation(newLocation);
                }
                zoomPointX = e.getX();
                zoomPointY = e.getY();
                mousePos = e.getLocationOnScreen();
            }
        }
    }

    private class ZoomListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            zoomPointX = e.getX();
            zoomPointY = e.getY();

            //Zoom in
            if (e.getWheelRotation() < 0) {
                zoomFactor += 0.1;
                repaint();
            }

            //Zoom out
            if (e.getWheelRotation() > 0) {
                zoomFactor -= 0.1;
                repaint();
            }

        }
    }


    private Point transformInverse(Point point) {
        if (zoomer == null) return point;
        try {
            zoomer.inverseTransform(point, point);
        } catch (Exception ignored) {}
        return point;

    }


}
