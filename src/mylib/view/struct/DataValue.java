package mylib.view.struct;

import java.awt.*;

public class DataValue<E> {

    private E element;
    private String desc;
    private Color color;

    public DataValue(E element, String desc, Color color){
        this.element = element;
        this.desc = desc;
        this.color = color;
    }

    public E getElement() {
        return element;
    }

    public String getDesc() {
        return desc;
    }

    public Color getColor() {
        return color;
    }

    public void setValue(E element) {
        this.element = element;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
