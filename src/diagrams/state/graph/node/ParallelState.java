package diagrams.state.graph.node;

import diagrams.utils.BaseNode;
import graph.NodeModel;
import mvc.IView;
import utils.GeomUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class ParallelState
        extends JInternalFrame
        implements BaseNode<ParallelState, ParallelState>, NodeModel, IView, SwingConstants {

    private long id;

    private final List<ModuleNode> streams = new ArrayList<>();

    private int alignment = VERTICAL;

    private ModuleNode maxed;


    public ParallelState(String title, int parallels){
        super(title, true, false, true, false);

        for (int i = 0; i < parallels; i++)
            addStreamImpl();

        alignStreams();
    }

    public void addStream(){
        addStreamImpl();
        alignStreams();
    }


    private void addStreamImpl(){
        int i = streams.size();
        ModuleNode state = new ModuleNode("stream " + (i + 1));

        for (MouseListener listener : state.getNorthPane().getMouseListeners())
            state.getNorthPane().removeMouseListener(listener);

        state.addPropertyChangeListener(e -> {
            if (JInternalFrame.IS_MAXIMUM_PROPERTY.equals(e.getPropertyName())) {
                boolean isMaximized = (boolean) e.getNewValue();
                if (isMaximized){
                    maxed = state;
                    for (ModuleNode stream : streams) {
                        if (stream == state) continue;
                        stream.setVisible(false);
                    }
                }
                else {
                    maxed = null;
                    for (ModuleNode stream : streams) {
                        if (stream == state) continue;
                        stream.setVisible(true);
                    }
                    repaint();
                }
            }
        });

        state.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    new StreamPopUpMenu(state).show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        state.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (maxed == null) return;
                maxed.setBounds(0, 0, getWidth(), getHeight());
            }
        });

        streams.add(state);
        add(state);
        setVisible(true);
    }

    public void setAlignment(int alignment){
        if (alignment != HORIZONTAL && alignment != VERTICAL)
            throw new IllegalArgumentException("alignment must be SwingConstants.HORIZONTAL or SwingConstants.VERTICAL");
        this.alignment = alignment;
        alignStreams();
    }

    private void alignStreams(){
        if (maxed != null) return;

        int count = streams.size();
        if (alignment == HORIZONTAL) {
            for (int i = 0; i < count; i++) {
                ModuleNode stream = streams.get(i);
                stream.setBounds(i * (getWidth() / count), 0, (getWidth() / count), getHeight());
            }
        }
        else {
            for (int i = 0; i < count; i++) {
                ModuleNode stream = streams.get(i);
                stream.setBounds(0, i * (getHeight() / count), getWidth(), (getHeight() / count));

            }
        }
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
    public ParallelState getModel() {
        return this;
    }

    @Override
    public ParallelState getView() {
        return this;
    }

    @Override
    public void paintComponent(Graphics g) {
        int width = getWidth()/streams.size();
        for (int i = 0; i < streams.size(); i++) {
            JInternalFrame stream = streams.get(i);
            stream.setBounds(i * width, 0, width, getHeight());
        }

        super.paintComponent(g);
    }

    @Override
    public Point getNearestPointOnOutline(Point q) {
        Rectangle r = getBounds();
        Point p = GeomUtils.getCenter(r);
        return GeomUtils.lineIntersectionOnRect(r, new Line2D.Double(p, q));
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        if (getNorthPane() != null)
            getNorthPane().addMouseListener(l);
    }

    @Override
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        super.addMouseMotionListener(l);
        if (getNorthPane() != null)
            getNorthPane().addMouseMotionListener(l);
    }

    @Override
    public synchronized void addMouseWheelListener(MouseWheelListener l) {
        super.addMouseWheelListener(l);
        if (getNorthPane() != null)
            getNorthPane().addMouseWheelListener(l);
    }

    @Override
    public synchronized void addComponentListener(ComponentListener l) {
        super.addComponentListener(l);
        if (getNorthPane() != null)
            getNorthPane().addComponentListener(l);
    }

    @Override
    public synchronized void addFocusListener(FocusListener l) {
        super.addFocusListener(l);
        if (getNorthPane() != null)
            getNorthPane().addFocusListener(l);
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        super.addKeyListener(l);
        if (getNorthPane() != null)
            getNorthPane().addKeyListener(l);
    }

    private JComponent getNorthPane(){
        return ((BasicInternalFrameUI) getUI()).getNorthPane();
    }



    private class StreamPopUpMenu extends JPopupMenu {

        public StreamPopUpMenu(ModuleNode stream) {
            JMenuItem renameItem = new JMenuItem("rename");
            renameItem.addActionListener(e -> {
                String newTitle = JOptionPane.showInputDialog(
                        SwingUtilities.getWindowAncestor(this),
                        null,
                        "Rename",
                        JOptionPane.PLAIN_MESSAGE);
                if (newTitle != null)
                    stream.setTitle(newTitle);
            });
            add(renameItem);

            JMenuItem removeStreamItem = new JMenuItem("remove stream");
            removeStreamItem.addActionListener(e -> {
                getContentPane().remove(stream);
                streams.remove(stream);
                alignStreams();
            });
            add(removeStreamItem);
        }
    }

}
