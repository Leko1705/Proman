package context;

import mylib.swingx.StackPanel;

import javax.swing.*;
import java.awt.*;

public final class ApplicationContext extends Context<Void, JPanel> {

    private final StackPanel stackPanel;

    public ApplicationContext(JPanel root){
        stackPanel = new StackPanel(root);
        setLayout(new BorderLayout());
        add(stackPanel, BorderLayout.CENTER);
    }

    @Override
    public void open(JPanel panel) {
        stackPanel.push(panel);
    }

    @Override
    public void onCreate(Void initializer) {
        throw new UnsupportedOperationException("onCreate");
    }

    @Override
    public void close() {
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    @Override
    public void closeRecentOpened() {
        stackPanel.pop();
        if (stackPanel.peek() instanceof Context<?, ?> c)
            c.onResume();
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return this;
    }

}
