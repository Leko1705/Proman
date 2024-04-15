package graph;

import mvc.Controller;
import mvc.IModel;
import mvc.IView;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public interface Node<M extends NodeModel, V extends Component & IView>
        extends Controller<M, V> {

}
