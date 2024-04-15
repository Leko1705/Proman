package mylib.swingx;

import javax.swing.*;

public abstract class RecyclerView extends JPanel {
    protected abstract void onSelect(JList<? extends RecyclerView> list, int index, boolean isSelected, boolean cellHasFocus);
}
