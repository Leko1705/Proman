package mylib.io;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Mit der Klasse BufferedFile kann eine leere Datei erstellt werden, die dann gelesen oder
 * bearbeitet werden kann. Sofern die Datei noch nicht existiert wird diese neu erstellt, andernfalls
 * wird auf diese referenziert. Dabei koennen sowohl Texte, als auch Objekte in die Datei
 * geschrieben werden. Objekte, die in die Datei geladen werden sollen, muessen zwangsweise das
 * Interface java.io.Serializable implementieren.
 * Soll eine ausführbare Datei erstellt werden, muss das jeweilige auszuführende Objekt
 * zusätzlich das Interface nellytools.io.Executable implementieren.
 * Die Klasse verwendet fuer moegliche iterationen intern einen Pointer, der auf eine bestimmte
 * Zeile verweist. Methoden, die direkt auf eine Zeile zugreifen veraendern diesen Pointer
 * nicht.
 */
@SuppressWarnings("unused")
public class BuffedFile extends File implements Serializable, Closeable
{
    private String path;
    public int currentLine = 0;
    private int length;
    private final LinkedList<FileChangeDetector> detectors = new LinkedList<>();
    
    /**
     * Erzeugt oder greift auf eine Datei zu, mit dem uebergebenen Pfad
     * @param path dateipfad
     */
    public BuffedFile(String path)
    {
    	super(path);
        this.path = path;
        try{
            // sofern die Datei noch nicht existiert
            createNewFile(); 
        }catch(IOException e){
            e.printStackTrace();
        }
        this.length = this.size();
    }
    
    /**
     * Erzeugt oder greift auf eine Datei zu, mit dem uebergebenen File-objekt
     * @param file datei
     */
    public BuffedFile(File file)
    {
    	super(file.getPath());
        this.path = file.getPath();
        try{
            // sofern die Datei noch nicht existiert
            file.createNewFile(); 
        }catch(IOException e){
            e.printStackTrace();
        }
        this.length = this.size();
    }
    
    /**
     * Erzeugt oder greift auf eine Datei zu, mit dem uebergebenen BufferedFile-objekt
     * @param bf datei
     */
    public BuffedFile(BuffedFile bf)
    {
    	super(bf.getPath());
        this.path = bf.getPath();
        try{
            // sofern die Datei noch nicht existiert
            createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
        this.length = this.size();
    }
    
    /**
     * loescht alles aus der Datei
     */
    public void clear(){
        try{
            FileWriter fw = new FileWriter(this);
            fw.write("");
            fw.close();
            this.length = 0;
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void writeln(){
        write("\n");
    }
    
    /**
     * schreibt den uebergebene text in eine neue Zeile
     * @param s text
     */
    public void writeln(String s){
        write(s + "\n");
    }
    
    /**
     * schreibt den uebergebene text in die aktuelle Zeile
     * @param s text
     */
    public void write(String s){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(this, true));
            writer.append(s);
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void appendBytes(byte... b){
        try (FileOutputStream stream = new FileOutputStream(path, true)) {
            stream.write(b);
        }
        catch (IOException ignored){
        }
    }
    
    /**
     * ueberschreibt die angegebene Zeile
     * @param line Zeile
     * @param s text
     */
    public void overwrite(int line, String s){
        ArrayList<String> content  = new ArrayList<>();
        try(Scanner scanner = new Scanner(this)){
            while(scanner.hasNextLine()){
                content.add(scanner.nextLine());
            }
            content.set(line, s);
            this.clear();
            this.write(content.get(0));
            for(int i = 1; i < content.size(); i++){
                this.writeln(content.get(i));
            }
        }catch(IOException | IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        
    }
    
    /**
     * gibt den text der angegebene zeile zurueck
     * @param line Zeile
     */
    public String read(int line){
        try(Scanner scanner = new Scanner(this)){
            int index = 0;
            while(scanner.hasNextLine()){
                String current = scanner.nextLine();
                if(index == line) return current;
                index++;
            }
            
            if(line >= this.length) throw new Exception();
        }catch(Exception e){
            throw new RuntimeException(new IOException("line does not exist"));
        }
        return "";
    }
    
    /**
     * liefert die aktuelle laenge der Datei in Zeilen
     */
    public int size() {
    	try {
    		LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(this));
    	  lineNumberReader.skip(Long.MAX_VALUE);
    	  int lines = lineNumberReader.getLineNumber();
    	  lineNumberReader.close();
    	  return lines+1;
    	}catch(Exception ignored) {}
    	return 0;
    }
    
    /**
     * liefert true zurueck, wenn die aktuelle Zeile in der Datei
     * vorhanden ist.
     * Ist dies nicht der Fall, wird false zurueck gegeben und die aktuelle Zeile
     * auf 0 gesetzt.
     * Per deafualt ist die aktuelle Zeile 0.
     */
    public boolean hasAccess(){
        boolean bool = this.currentLine < this.length;
        if(!bool) this.currentLine = 0;
        
        return bool;
    }
    
    /**
     * liefert true, wenn die aktuelle Zeile in der Datei vorhanden ist
     */
    public boolean inRange() {
    	return this.currentLine < this.length;
    }
    
    /**
     * verweis auf die naechste Zeile (Pointer wird um um 1 inkrementiert)
     */
    public void next(){
        this.currentLine++;
    }
    
    /**
     *  liefert die aktuelle Zeile und verweist auf die naechste
     *  Zeile (Pointer wird um um 1 inkrementiert)
     */
    public String nextLine(){
        String out = this.read(this.currentLine);
        this.currentLine++;
        return out;
    }
    
    /**
     * liefert die aktuelle Zeile
     * @return
     */
    public String getLine(){
        return this.read(this.currentLine);
    }
    
    /**
     * ueberschreibt die aktuelle Zeile
     */
    public void setLine(String newLine) {
    	this.overwrite(currentLine, newLine);
    }

    /**
     * laedt das uebergebene Objekt in die Datei. Es muss das Interface java.io.Serializable
     * implementieren.
     * @param obj das in die Datei zu ladene Objekt
     */
    public <T extends Serializable> void writeObject(T obj) {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(obj);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * liest das Objekt aus der Datei
     * @return das gelandene Objekt
     */
    public Object readObj() {
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            return in.readObject();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * loescht die datei
     */
    public boolean delete(){
        this.path = null;
        return super.delete();
    }

    public boolean contentMatches(BuffedFile bufferedFile){
        try {
            byte[] f1 = Files.readAllBytes(Paths.get(bufferedFile.getAbsolutePath()));
            byte[] f2 = Files.readAllBytes(Paths.get(bufferedFile.getAbsolutePath()));
            return Arrays.equals(f1, f2);
        }catch (IOException e){
            e.printStackTrace();
        }

        return false;

    }

    public void copyContentInto(BuffedFile destination){
        try {
            FileChannel src = new FileInputStream(this).getChannel();
            FileChannel dest = new FileOutputStream(destination).getChannel();
            dest.transferFrom(src, 0, src.size());
            src.close();
            dest.close();
        }catch (Exception ignored){}
    }

    public void copyContentFrom(BuffedFile source){
        try {
            FileChannel src = new FileInputStream(source).getChannel();
            FileChannel dest = new FileOutputStream(this).getChannel();
            dest.transferFrom(src, 0, src.size());
            src.close();
            dest.close();
        }catch (Exception ignored){}
    }

    public boolean renameTo(String name){
        return super.renameTo(new File(name));
    }
    
    /**
     * liefert den aktuellen Pfad
     */
    public String getPath() {
        return this.path;
    }

    public void addFileChangeListener(FileChangeListener listener){
        detectors.add(new FileChangeDetector(this, listener));
    }

    @Override
    public void close() {
        for (FileChangeDetector detector : detectors)
            detector.close();
    }
}
