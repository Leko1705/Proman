package utils;

import diagram.store.DataFactory;
import diagram.store.XMLDataFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static FileManager manager = null;

    private final DataFactory dataFactory = new XMLDataFactory();


    public static FileManager getManager(){
        if (manager == null)
            manager = new FileManager();
        return manager;
    }

    private FileManager(){
        init();
    }

    private Path root;

    private void init(){
        File file = new File("proman");
        if (!file.exists() || !file.isDirectory())
            file.mkdirs();
        root = file.toPath();
    }

    public Path getRoot(){
        return root;
    }

    public List<Path> getAllContent(Path path){
        return loadContent(path, file -> true);
    }

    public List<Path> getSubDirectories(Path path){
        return loadContent(path, File::isDirectory);
    }

    public List<Path> getFiles(Path path){
        return loadContent(path, File::isFile);
    }

    private interface FileChooser {
        boolean choose(File file);
    }

    private List<Path> loadContent(Path path, FileChooser chooser){
        List<Path> paths = new ArrayList<>();
        String[] subPaths =
                path.toFile().list((current, name) -> chooser.choose(new File(current, name)));
        if (subPaths == null) return List.of();
        for (String subPath : subPaths) {
            paths.add(Path.of(path.toAbsolutePath() + File.separator + subPath));
        }
        return paths;
    }

    public String getFileName(Path path, boolean withExtension){
        if (withExtension)
            return path.toFile().getName();
        else
            return getFileNameWithoutExtension(path);
    }

    private String getFileNameWithoutExtension(Path path){
        String fileName = path.toAbsolutePath().toString();
        int from = fileName.lastIndexOf(File.separator) + 1;

        int to = fileName.lastIndexOf(".");
        if (to == -1) to = fileName.length();

        return fileName.substring(from, to);
    }

    public void remove(Path path){
        try {
            Files.delete(path);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void move(Path oldPath, Path newPath) {
        try {
            Files.move(oldPath, newPath);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public DataFactory getDataFactory(){
        return dataFactory;
    }

}
