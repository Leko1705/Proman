package diagrams.clazz.graph.node;

import mylib.structs.ObservedList;

public interface ClassChangeListener extends ObservedList.ListChangeListener {

    @Override
    default void onListChanged(ObservedList<?> list){
        onChange();
    }

    void onChange();

}
