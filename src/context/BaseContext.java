package context;

public abstract class BaseContext<I, V> extends Context<I, V> {

    private final Context<?, ?> parent;

    protected BaseContext(Context<?, ?> parent) {
        this.parent = parent;
    }


    public abstract void closeRecentOpened();

    @Override
    public void close() {
        parent.closeRecentOpened();
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return parent.getApplicationContext();
    }

    public Context<?, ?> getParentContext(){
        return parent;
    }
}
