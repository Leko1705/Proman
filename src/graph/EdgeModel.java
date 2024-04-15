package graph;

import mvc.IModel;

public interface EdgeModel extends IModel {

    long getFromID();

    long getToID();

}
