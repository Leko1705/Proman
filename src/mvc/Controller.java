package mvc;

/**
 *
 * @param <M> the model
 * @param <V> the view
 */
public interface Controller<M extends IModel, V extends IView> {

    M getModel();

    V getView();

}
