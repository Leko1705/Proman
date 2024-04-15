package graph;

public final class ConnectionProcess {

    private ConnectionProcess(){}

    private static boolean inProcess = false;

    private static Node<?, ?> last = null;

    public static void enter(){
        inProcess = true;
    }

    public static void quit(){
        inProcess = false;
        last = null;
    }

    public static void handle(Node<?, ?> node, NewConnectionHandler handler){
        if (!inProcess) return;
        if (last == null) {
            last = node;
        }
        else {
            handler.onConnect(last, node);
            quit();
        }
    }

}
