package mylib.swingx;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class StackPanel extends JPanel {

    private final CardLayout cardLayout;
    private final Deque<OpenedPanel> trace = new ArrayDeque<>();
    private final JPanel owner;

    private int name = 0;

    public StackPanel(JPanel owner) {
        this.owner = owner;
        cardLayout = new CardLayout();
        owner.setLayout(cardLayout);
    }

    public void push(JPanel panel) {
        String name = Integer.toString(this.name++);
        owner.add(panel, name);
        cardLayout.show(owner, name);
        trace.push(new OpenedPanel(panel, name));
    }

    public void pop() {
        OpenedPanel curr = trace.pop();
        owner.remove(curr.panel);

        if (trace.isEmpty()) {
            SwingUtilities.windowForComponent(owner).dispose();
            return;
        }

        OpenedPanel prev = trace.peek();
        cardLayout.show(owner, prev.name);
    }

    public JPanel peek(){
        return trace.element().panel();
    }

    private record OpenedPanel(JPanel panel, String name){
    }
}
