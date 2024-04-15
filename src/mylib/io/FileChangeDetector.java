package mylib.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

class FileChangeDetector extends TimerTask implements Closeable {

    private final Timer timer;
    private final File file;
    private final FileChangeListener listener;

    private long timeStamp;
    private byte[] fileContent;

    private boolean active;

    public FileChangeDetector(File file, FileChangeListener listener){
        timer = new Timer(true);
        this.file = file;
        this.listener = listener;
        startListen();
    }

    private void startListen(){
        this.timeStamp = file.lastModified();
        try {
            fileContent = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        }catch (Exception ignored){}
        active = true;
        timer.schedule( this , new Date(), 1 );
    }

    @Override
    public void run() {
        long timeStamp = file.lastModified();

        if( this.timeStamp != timeStamp ) {
            this.timeStamp = timeStamp;

            try {
                byte[] f1 = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                if (!Arrays.equals(fileContent, f1)) {
                    fileContent = f1;
                    listener.onChange();
                }
            } catch (IOException ignored) {}
        }
        if(!active) this.cancel();
    }

    @Override
    public void close(){
        timer.cancel();
        active = false;
    }
}
