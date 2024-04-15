package mylib.swingx;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

@SuppressWarnings("unused")
public class JRecycler extends JList<RecyclerView> implements Iterable<RecyclerView> {

    private final DefaultListModel<RecyclerView> model;

    public JRecycler(RecyclerView... views){
        model = new DefaultListModel<>();
        for (RecyclerView view : views)
            addComponent(view);
        setModel(model);
        setCellRenderer(new CellRendererImpl());
    }

    public void addComponent(RecyclerView view){
        if (view == null)
            throw new NullPointerException("view must not be null");
        model.addElement(view);
    }

    public void remove(RecyclerView view){
        model.removeElement(view);
    }

    public void remove(int idx){
        if (idx < length())
            model.remove(idx);
    }

    public void removeSelected(){
        int index = getSelectedIndex();
        if (index != -1)
            remove(getSelectedIndex());
    }

    public void clear(){
        model.clear();
    }

    public int length(){
        return model.size();
    }

    public RecyclerView get(int idx){
        return model.get(idx);
    }

    @Override
    public Iterator<RecyclerView> iterator() {
        return new Itr();
    }

    private static class CellRendererImpl implements ListCellRenderer<RecyclerView> {
        @Override
        public Component getListCellRendererComponent(JList<? extends RecyclerView> list, RecyclerView value, int index, boolean isSelected, boolean cellHasFocus) {
            value.onSelect(list, index, isSelected, cellHasFocus);
            return value;
        }
    }

    private class Itr implements Iterator<RecyclerView> {
        int index = 0;
        public boolean hasNext() {
            return index < length();
        }
        public RecyclerView next() {
            return model.get(index++);
        }
    }

}
