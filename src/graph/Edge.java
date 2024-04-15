package graph;

import mvc.Controller;

import java.util.Objects;

public class Edge<M extends EdgeModel>
        implements Controller<M, EdgeView<M>> {

    private final M model;

    private final EdgeView<M> view;

    protected Edge(M model, EdgeView<M> view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public M getModel() {
        return model;
    }

    @Override
    public EdgeView<M> getView() {
        return view;
    }
}
