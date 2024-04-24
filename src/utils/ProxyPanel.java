package utils;

import context.BaseContext;
import context.Context;

import javax.swing.*;
import java.awt.*;

public class ProxyPanel extends BaseContext<JPanel, JPanel> {

    private JPanel current;

    public ProxyPanel(Context<?, ?> context){
        this(context, null);
    }

    public ProxyPanel(Context<?, ?> context, JPanel panel) {
        super(context);
        onCreate(panel);
    }

    public void set(JPanel panel){
        if (current != null) remove(current);
        if (panel != null)
            add(current = panel, BorderLayout.CENTER);
    }

    public JPanel get(){
        return current;
    }

    @Override
    public void onCreate(JPanel initializer) {
        setLayout(new BorderLayout());
        set(initializer);
    }

    @Override
    public void closeRecentOpened() { }

}
