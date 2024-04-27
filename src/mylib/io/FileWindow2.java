package mylib.io;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileWindow2 {

    private String title;
    private String path;

    public FileWindow2(){
        title = "";
    }

    public FileWindow2 title(String title){
        this.title = title;
        return this;
    }

    public FileWindow2 directory(String path){
        this.path = path;
        return this;
    }

    public BuffedFile loadFile(){
        Loader loader = new Loader(null, title);
        if (path != null) loader.setDirectory(path);
        BuffedFile buffedFile = loader.loadFile();
        loader.dispose();
        return buffedFile;
    }

    public boolean save(File file){
        Loader loader = new Loader(null, title);
        if (path != null) loader.setDirectory(path);
        boolean b = loader.save(file);
        loader.dispose();
        return b;
    }

    private static class Loader extends FileDialog{

        public Loader(Frame parent, String title) {
            super(parent, title);
        }

        public BuffedFile loadFile(){
            setMode(FileDialog.LOAD);
            setVisible(true);
            String path = getFile();
            if(path != null) return new BuffedFile(path);
            else return null;
        }

        public boolean save(File file){
            setMode(FileDialog.SAVE);
            setFile(file.getName());
            setVisible(true);
            String path = getDirectory();

            if (path != null) {
                try {
                    Files.copy(file.toPath().toAbsolutePath(),
                            (new File(path + getFile())).toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            else return false;
        }
    }
}
