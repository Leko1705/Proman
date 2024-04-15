package mylib.io;

import java.io.*;

public final class Serializer {

    private Serializer(){
    }

    public static byte[] serialize(Serializable s) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(s);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] stream) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(stream);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();

    }

}
