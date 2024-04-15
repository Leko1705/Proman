package mylib.net;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class Client implements Closeable {

    private final Socket socket;

    private volatile boolean active = true;

    private final Thread receiveListener;

    public Client(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);

        receiveListener = new Thread(this::receiveCycle);

        receiveListener.start();
    }

    public void send(byte[] bytes) throws IOException {
        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

        dOut.writeInt(bytes.length); // write length of the message
        dOut.write(bytes);           // write the message
    }

    private void receiveCycle() {
        while (active)
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
            onReceive(message);
        }
    }

    public abstract void onReceive(byte[] bytes);

    @Override
    public void close() throws IOException{
        active = false;
        socket.close();
        receiveListener.interrupt();
    }
}
