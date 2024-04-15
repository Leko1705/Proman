package mylib.swingx;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class JComponentRecycler extends JScrollPane implements Iterable<Component> {

    private final Box viewBox = new Box(BoxLayout.Y_AXIS);

    private final ArrayList<Component> components = new ArrayList<>();

    public JComponentRecycler(){
        setViewportView(viewBox);
    }

    public Component addAndAdapt(Component comp){
        comp.setPreferredSize(new Dimension(getWidth(), comp.getPreferredSize().height));
        return add(comp);
    }

    @Override
    public Component add(Component comp) {
        components.add(comp);
        Component r = viewBox.add(comp);
        viewBox.revalidate();
        return r;
    }

    @Override
    public void remove(Component comp) {
        components.remove(comp);
        viewBox.remove(comp);
        viewBox.revalidate();
    }

    @Override
    public void remove(int index) {
        Component c = components.remove(index);
        viewBox.remove(c);
        viewBox.revalidate();
    }

    public void clear(){
        components.clear();
        viewBox.removeAll();
        viewBox.revalidate();
    }

    public int amount(){
        return components.size();
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }
}
