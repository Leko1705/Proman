package mylib.view.diagram;

import mylib.structs.*;
import mylib.view.struct.DataValue;
import mylib.view.struct.TreeDataSet;

import java.awt.*;

public class TreeDiagram extends Diagram {
    
    private final TreePaintAlgorithm algorithm;

    private final TreeDataSet<?> treeDataSet;

    public TreeDiagram(TreeDataSet<?> treeDataSet) {
        treeDataSet.addPropertyChangeListener(this);
        this.treeDataSet = treeDataSet;
        this.algorithm = getAlgorithm(treeDataSet.getTree());
    }

    public TreeDataSet<?> getTreeDataSet() {
        return treeDataSet;
    }

    private TreePaintAlgorithm getAlgorithm(Tree<?> tree){
        if (tree instanceof BSTree<?>)
            return new BSTPaintAlgorithm();
        else if (tree instanceof AVLTree<?>)
            return new AVLTPaintAlgorithm();
        else if (tree instanceof BinaryHeap<?>)
            return new BinaryHeapPaintAlgorithm();
        else if (tree instanceof PairingHeap<?>)
            return new PairingHeapPaintAlgorithm();
        else
            throw new UnsupportedOperationException(tree.getClass().getName());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        algorithm.paintTree(g2);
    }
    
    
    private interface TreePaintAlgorithm {
        void paintTree(Graphics2D g2);
    }


    private class BSTPaintAlgorithm implements TreePaintAlgorithm {

        @Override
        public void paintTree(Graphics2D g2) {
            BSTree<?> bsTree = (BSTree<?>) treeDataSet.getTree();
            if (!bsTree.isEmpty())
                paintNode(g2, bsTree.node, 0, getWidth(), 0);
        }

        private void paintNode(Graphics2D g2, BSTree<?>.Node node, int l, int r, int depth){

            int radius = Math.max(getWidth(), getHeight()) / 100 * 3;

            int x = calcX(l, r); // center in range from l to r
            int y = calcY(depth, radius);

            if (!node.left.isEmpty())
                g2.drawLine(x+radius/2, y+radius/2, calcX(l, x)+radius/2, calcY(depth+1, radius)+radius/2);

            if (!node.right.isEmpty())
                g2.drawLine(x+radius/2, y+radius/2, calcX(x, r)+radius/2, calcY(depth+1, radius)+radius/2);


            DataValue<?> style = treeDataSet.getDataValue(node.element);

            Color defaultColor = g2.getColor();
            if (style.getColor() != null) {
                defaultColor = g2.getColor();
                g2.setColor(style.getColor());
            }
            else
                g2.setColor(getBackground());

            g2.fillOval(x, y, radius, radius);
            g2.setColor(defaultColor);
            g2.drawOval(x, y, radius, radius);

            String desc = style.getDesc();
            if (desc == null)
                desc = node.element.toString();

            int width = g2.getFontMetrics().stringWidth(desc);
            g2.drawString(desc, x+((radius-width)/2), y+((radius+10)/2));

            if (!node.left.isEmpty()) {
                paintNode(g2, node.left.node, l, x, depth+1);
            }

            if (!node.right.isEmpty()) {
                paintNode(g2, node.right.node, x, r, depth+1);
            }
        }

        private int calcX(int l, int r){
            return l + (r - l) / 2;
        }

        private int calcY(int depth, int radius){
            return 100 + 100 * depth - radius;
        }
    }

    private class AVLTPaintAlgorithm implements TreePaintAlgorithm {

        @Override
        public void paintTree(Graphics2D g2) {
            AVLTree<?> bsTree = (AVLTree<?>) treeDataSet.getTree();
            if (!bsTree.isEmpty())
                paintNode(g2, bsTree.node, 0, getWidth(), 0);
        }

        private void paintNode(Graphics2D g2, AVLTree<?>.Node node, int l, int r, int depth){

            int radius = Math.max(getWidth(), getHeight()) / 100 * 3;

            int x = calcX(l, r); // center in range from l to r
            int y = calcY(depth, radius);

            if (!node.left.isEmpty())
                g2.drawLine(x+radius/2, y+radius/2, calcX(l, x)+radius/2, calcY(depth+1, radius)+radius/2);

            if (!node.right.isEmpty())
                g2.drawLine(x+radius/2, y+radius/2, calcX(x, r)+radius/2, calcY(depth+1, radius)+radius/2);


            DataValue<?> style = treeDataSet.getDataValue(node.element);

            Color defaultColor = g2.getColor();
            if (style.getColor() != null) {
                defaultColor = g2.getColor();
                g2.setColor(style.getColor());
            }
            else
                g2.setColor(getBackground());

            g2.fillOval(x, y, radius, radius);
            g2.setColor(defaultColor);
            g2.drawOval(x, y, radius, radius);

            String desc = style.getDesc();
            if (desc == null)
                desc = node.element.toString();

            int width = g2.getFontMetrics().stringWidth(desc);
            g2.drawString(desc, x+((radius-width)/2), y+((radius+10)/2));

            if (!node.left.isEmpty()) {
                paintNode(g2, node.left.node, l, x, depth+1);
            }

            if (!node.right.isEmpty()) {
                paintNode(g2, node.right.node, x, r, depth+1);
            }
        }

        private int calcX(int l, int r){
            return l + (r - l) / 2;
        }

        private int calcY(int depth, int radius){
            return 100 + 100 * depth - radius;
        }
    }


    private class BinaryHeapPaintAlgorithm implements TreePaintAlgorithm {

        private final BinaryHeap<?> heap = (BinaryHeap<?>) treeDataSet.getTree();

        @Override
        public void paintTree(Graphics2D g2) {
            if (!heap.isEmpty())
                paintNode(g2, 1, 0, getWidth(), 0);
        }

        private void paintNode(Graphics2D g2, int idx, int l, int r, int depth){
            int radius = Math.max(getWidth(), getHeight()) / 100 * 3;

            int x = calcX(l, r); // center in range from l to r
            int y = calcY(depth, radius);

            if (idx*2-1 < heap.size())
                g2.drawLine(x+radius/2, y+radius/2, calcX(l, x)+radius/2, calcY(depth+1, radius)+radius/2);

            if (idx*2 < heap.size())
                g2.drawLine(x+radius/2, y+radius/2, calcX(x, r)+radius/2, calcY(depth+1, radius)+radius/2);

            DataValue<?> style = treeDataSet.getDataValue(heap.get(idx-1));

            Color defaultColor = g2.getColor();
            if (style.getColor() != null) {
                defaultColor = g2.getColor();
                g2.setColor(style.getColor());
            }
            else
                g2.setColor(getBackground());

            g2.fillOval(x, y, radius, radius);
            g2.setColor(defaultColor);
            g2.drawOval(x, y, radius, radius);

            String desc = style.getDesc();
            if (desc == null)
                desc = heap.get(idx-1).toString();

            int width = g2.getFontMetrics().stringWidth(desc);
            g2.drawString(desc, x+((radius-width)/2), y+((radius+10)/2));

            if (idx*2-1 < heap.size()) {
                paintNode(g2, idx*2, l, x, depth+1);
            }

            if (idx*2 < heap.size()) {
                paintNode(g2, idx*2+1, x, r, depth+1);
            }
        }


        private int calcX(int l, int r){
            return l + (r - l) / 2;
        }

        private int calcY(int depth, int radius){
            return 100 + 100 * depth - radius;
        }

    }

    private class PairingHeapPaintAlgorithm implements TreePaintAlgorithm {

        @Override
        public void paintTree(Graphics2D g2) {
            PairingHeap<?> heap = (PairingHeap<?>) treeDataSet.getTree();
            if (!heap.isEmpty())
                paintNode(g2, heap.root, 0, getWidth(), 0);
        }

        private void paintNode(Graphics2D g2, PairingHeap<?>.Node node, int l, int r, int depth){

            int radius = Math.max(getWidth(), getHeight()) / 100 * 3;

            int x = calcX(l, r); // center in range from l to r
            int y = calcY(depth, radius);

            if (!node.children.isEmpty()) {
                int stepSize = (r - l) / node.children.size();
                int curr = l;
                for (PairingHeap<?>.Node child : node.children){
                    g2.drawLine(x+radius/2, y+radius/2, calcX(curr, curr + stepSize)+radius/2, calcY(depth+1, radius)+radius/2);
                    paintNode(g2, child, curr, curr + stepSize, depth+1);
                    curr += stepSize;
                }
            }

            DataValue<?> style = treeDataSet.getDataValue(node.element);

            Color defaultColor = g2.getColor();
            if (style.getColor() != null) {
                defaultColor = g2.getColor();
                g2.setColor(style.getColor());
            }
            else
                g2.setColor(getBackground());

            g2.fillOval(x, y, radius, radius);
            g2.setColor(defaultColor);
            g2.drawOval(x, y, radius, radius);

            String desc = style.getDesc();
            if (desc == null)
                desc = node.element.toString();

            int width = g2.getFontMetrics().stringWidth(desc);
            g2.drawString(desc, x+((radius-width)/2), y+((radius+10)/2));

        }


        private int calcX(int l, int r){
            return l + (r - l) / 2;
        }

        private int calcY(int depth, int radius){
            return 100 + 100 * depth - radius;
        }

    }
    
}
