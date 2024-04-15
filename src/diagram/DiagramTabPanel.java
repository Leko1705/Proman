package diagram;

import javax.swing.*;
import javax.swing.plaf.metal.MetalIconFactory;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class DiagramTabPanel extends JTabbedPane {

    private final Map<String, CloseButtonTab> opened = new HashMap<>();

    public void addTab(String title, Icon icon, Component component) {
        if (opened.containsKey(title)){
            setSelectedComponent(opened.get(title).component);
            return;
        }
        super.addTab(title, icon, component);
        int count = this.getTabCount() - 1;
        CloseButtonTab tab = new CloseButtonTab(component, title, icon);
        setTabComponentAt(count, tab);
        opened.put(title, tab);
        setSelectedComponent(component);
    }


    public class CloseButtonTab extends JPanel {
        public Component component;
        public CloseButtonTab(final Component tab, String title, Icon icon) {
            this.component = tab;
            setOpaque(false);
            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 3, 3);
            setLayout(flowLayout);
            JLabel jLabel = new JLabel(title);
            jLabel.setIcon(icon);
            add(jLabel);
            JButton button = new JButton(MetalIconFactory.getInternalFrameCloseIcon(16));
            button.setMargin(new Insets(0, 0, 0, 0));
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    DiagramTabPanel.this.remove(tab);
                    opened.remove(title);
                }
            });
            add(button);
        }
    }


}
