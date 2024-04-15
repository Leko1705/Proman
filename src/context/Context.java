package context;

import utils.PostConstructor;

import javax.swing.*;

public abstract class Context<I, V> extends JPanel implements PostConstructor<I> {

    public abstract void onCreate(I initializer);

    public void open(V toOpen){
        throw new UnsupportedOperationException("open");
    }

    public abstract void close();

    public abstract void closeRecentOpened();

    public abstract ApplicationContext getApplicationContext();

    public void onResume(){ }

}
