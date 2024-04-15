package mylib.net;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public abstract class Server implements Closeable {

    private volatile boolean serverIsActive = true;

    private final NewConnectionProcessor connectionProcessor;

    private final HashMap<Integer, ConnectionHandler> clientMap = new HashMap<>();
    
    

    public Server(int port) throws IOException {
        connectionProcessor = new NewConnectionProcessor(port);
    }

    public void send(String ip, int port, byte[] bytes) throws IOException {
        int hash = ip.hashCode() + port;
        ConnectionHandler connectionHandler = clientMap.get(hash);

        if (connectionHandler != null)
            connectionHandler.send(bytes);
    }

    public void broadcast(byte[] bytes) throws IOException {
        for (ConnectionHandler handler : clientMap.values())
            handler.send(bytes);
    }

    public void disconnect(String ip){
        int hash = ip.hashCode();
        ConnectionHandler connectionHandler = clientMap.get(hash);

        if (connectionHandler != null) {
            connectionHandler.closeConnection();
            clientMap.remove(hash);
            onClosingConnection(connectionHandler.getIpAddress(), connectionHandler.getPort());
        }
    }

    @Override
    public void close(){
        for (ConnectionHandler handler : clientMap.values()) {
            handler.closeConnection();
            onClosingConnection(handler.getIpAddress(), handler.getPort());
            handler.interrupt();
        }
        clientMap.clear();
        serverIsActive = false;
        connectionProcessor.interrupt();
    }

    public void onConnectionEstablished(String ip, int port){
    }

    public void onClosingConnection(String ip, int port){
    }

    public abstract void onReceive(String ip, int port, byte[] bytes);





    private class NewConnectionProcessor extends Thread {

        ServerSocket serverSocket;

        public NewConnectionProcessor(int port) throws IOException {
            serverSocket = new ServerSocket(port);
            start();
        }

        @Override
        public void run() {
            while (serverIsActive) {
                try {
                    Socket socket = serverSocket.accept();
                    ConnectionHandler connectionHandler = new ConnectionHandler(socket);
                    int hash = hashSocket(socket);
                    clientMap.put(hash, connectionHandler);

                    onConnectionEstablished(connectionHandler.getIpAddress(), connectionHandler.getPort());
                    
                } catch (IOException ignored) { }
            }
        }

        private int hashSocket(Socket socket){
            return socket.getInetAddress().getHostAddress().hashCode() + socket.getPort();
        }
    }
    
    


    
    private class ConnectionHandler extends Thread {

        private volatile boolean handlerIsActive = true;
        private final Socket socket;

        public ConnectionHandler(Socket socket){
            this.socket = socket;
            start();
        }

        @Override
        public void run() {
           receiveCycle();
        }

        private void receiveCycle() {
            while (serverIsActive && handlerIsActive)
            {
                try {
                    receive();
                } catch (IOException ignored) { }
            }
        }

        public void receive() throws IOException{
            DataInputStream dIn = new DataInputStream(socket.getInputStream());

            int length = dIn.readInt();                    // read length of incoming message
            if (length > 0) {
                byte[] message = new byte[length];
                dIn.readFully(message, 0, message.length); // read the message

                onReceive(getIpAddress(), getPort(), message);
            }
        }

        public void send(byte[] bytes) throws IOException {

            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

            dOut.writeInt(bytes.length); // write length of the message
            dOut.write(bytes);           // write the message
        }

        public void closeConnection() {
            handlerIsActive = false;
        }

        public String getIpAddress(){
            return socket.getInetAddress().getHostAddress();
        }

        public int getPort(){
            return socket.getPort();
        }
    }



}
