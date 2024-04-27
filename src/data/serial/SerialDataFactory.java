package data.serial;

import data.Data;
import data.DataFactory;
import mylib.io.BuffedFile;

import java.io.*;
import java.nio.file.Path;

public class SerialDataFactory implements DataFactory {

    @Override
    public void createFile(Path path, String kind, int version) {
        try (BuffedFile bf = new BuffedFile(path.toFile())) {
            SerialData sd = new SerialData(kind, version);
            bf.writeObject(sd);
        }

    }

    @Override
    public Data load(InputStream in) {
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            return (Data) ois.readObject();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Data data, OutputStream out) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(data);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
