package graph;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NoNodeModel implements NodeModel {

    private static NoNodeModel instance = null;

    public static NoNodeModel get(){
        if (instance == null)
            instance = new NoNodeModel();
        return instance;
    }

    private NoNodeModel(){}

    @Override
    public long getID() {
        return 0;
    }

    @Override
    public void setID(long id) { }

}
