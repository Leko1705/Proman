package mylib.structs;

import java.util.*;

@SuppressWarnings("unused")
public class Graph<E> extends AbstractSet<E> {

    private final HashMap<E, HashMap<E, Double>> graphMap = new HashMap<>();

    private int negEdgeCount = 0;


    public Graph(){
    }

    public Graph(Collection<? extends E> collection){
        addAll(collection);
    }

    @Override
    public int size() {
        return graphMap.size();
    }

    public void connect(E e1, E e2, double w){
        if (w < 0) negEdgeCount++;
        if (!graphMap.containsKey(e1)) graphMap.put(e1, new HashMap<>());
        if (!graphMap.containsKey(e2)) graphMap.put(e2, new HashMap<>());
        graphMap.get(e1).put(e2, w);
    }

    public void disconnect(E e1, E e2){
        if (graphMap.containsKey(e1)) {
            double w = graphMap.get(e1).remove(e2);
            if (w < 0) negEdgeCount--;
        }
    }

    @Override
    public boolean add(E e) {
        if (!graphMap.containsKey(e)) {
            graphMap.put(e, new HashMap<>());
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean contains(Object o) {
        return graphMap.containsKey(o);
    }

    @Override
    public boolean remove(Object o) {
        return graphMap.remove(o) != null;
    }



    public E[] path(E from, E to){
        if (negEdgeCount == 0)
            return new Dijkstra().findPath(from, to);
        throw new UnsupportedOperationException("can not handle negative edges");
    }

    public HashMap<E, Double> getConnections(Object v){
        return graphMap.get(v);
    }


    @Override
    public Iterator<E> iterator() {
        return graphMap.keySet().iterator();
    }

    @SuppressWarnings("unchecked")
    public Iterator<E> DFSIterator() {
        return new DFSIterator((E) graphMap.keySet().toArray()[0]);
    }

    public Iterator<E> DFSIterator(E e){
        return new DFSIterator(e);
    }

    @SuppressWarnings("unchecked")
    public Iterator<E> BFSIterator() {
        return BFSIterator((E) graphMap.keySet().toArray()[0]);
    }

    public Iterator<E> BFSIterator(E e) {
        return new BFSIterator(e);
    }


    private class DFSIterator implements Iterator<E> {
        private E last;
        private final HashSet<E> marked = new HashSet<>();
        private final ArrayDeque<Iterator<E>> stack = new ArrayDeque<>();

        DFSIterator(E startElement){
            if (!contains(startElement))
                throw new IllegalArgumentException(startElement + " does not exist in graph");
            last = startElement;
            stack.push(graphMap.get(last).keySet().iterator());
        }

        @Override
        public boolean hasNext() {
            return last != null;
        }

        @Override
        public E next() {
            E res = last;
            boolean foundNext = false;
            boolean pushedNew = false;

            marked.add(res);

            while (!stack.isEmpty() && !foundNext){
                Iterator<E> itr = stack.peek();

                while (itr.hasNext()) {
                    E neighbour = itr.next();

                    if (!marked.contains(neighbour)) {
                        last = neighbour;
                        stack.push(graphMap.get(neighbour).keySet().iterator());
                        foundNext = true;
                        pushedNew = true;
                        break;
                    }
                }

                if (!itr.hasNext() && !pushedNew)
                    stack.pop();
            }

            if (stack.isEmpty()) {
                E unvisited = findNonSearchedNode();
                if (unvisited != null){
                    last = unvisited;
                    stack.push(graphMap.get(unvisited).keySet().iterator());
                }
                else {
                    last = null;
                }
            }

            return res;
        }

        private E findNonSearchedNode(){
            for (E nodeElement : graphMap.keySet())
                if (!marked.contains(nodeElement))
                    return nodeElement;
            return null;
        }
    }

    private class BFSIterator implements Iterator<E> {

        private final HashSet<E> marked = new HashSet<>();
        private final Queue<E> queue = new ArrayDeque<>();

        BFSIterator(E startElement){
            if (!contains(startElement))
                throw new IllegalArgumentException(startElement + " does not exist in graph");
            queue.add(startElement);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public E next() {
            E element = queue.poll();
            marked.add(element);
            Set<E> neighbours = graphMap.get(element).keySet();
            for (E neighbour : neighbours)
                if (!marked.contains(neighbour))
                    queue.add(neighbour);

            if (queue.isEmpty()){
                E unvisited = findNonSearchedNode();
                if (unvisited != null)
                    queue.add(unvisited);
            }

            return element;
        }

        private E findNonSearchedNode(){
            for (E nodeElement : graphMap.keySet())
                if (!marked.contains(nodeElement))
                    return nodeElement;
            return null;
        }
    }




    public class Dijkstra {

        private final HashMap<E, E> parents = new HashMap<>();

        private final HashMap<E, Double> vertexWights = new HashMap<>();

        private final HashSet<E> marked = new HashSet<>();

        public E[] findPath(E from, E to){
            reset();
            vertexWights.put(from, 0d);

            PriorityQueue<E> pq = new PriorityQueue<>((o1, o2) -> Double.compare(vertexWights.get(o1), vertexWights.get(o2)));
            pq.add(from);

            while (!pq.isEmpty()){
                E v = pq.poll();
                if (!marked.contains(v))
                    relaxire(v, pq);
            }

            return build(from, to);
        }

        private void relaxire(E v, PriorityQueue<E> pq){
            double currentValue = vertexWights.get(v);

            HashMap<E, Double> edges = Graph.this.getConnections(v);

            for (E w : edges.keySet()){

                double wight = edges.get(w);
                double neighbourValue = vertexWights.get(w);

                if (currentValue + wight < neighbourValue){
                    parents.put(w, v);
                    vertexWights.put(w, currentValue + wight);
                }
                if (!marked.contains(w))
                    pq.add(w);
            }

            marked.add(v);
        }

        @SuppressWarnings("all")
        private E[] build(E from, E to){
            LinkedList<E> path = new LinkedList<>();
            E current = to;
            while (current != from && current != null){
                path.addFirst(current);
                current = parents.get(current);
            }
            if (current == from){
                path.addFirst(from);
                return (E[]) path.toArray();
            }
            return null;
        }

        private void reset(){
            for (E e : Graph.this){
                vertexWights.put(e, Double.POSITIVE_INFINITY);
                parents.put(e, null);
            }
        }

    }


}
