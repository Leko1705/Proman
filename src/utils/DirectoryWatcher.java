package utils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

public class DirectoryWatcher extends SwingWorker<Void, Void> {

    private final WatchService watchService = FileSystems.getDefault().newWatchService();
    private final Set<DirectoryChangeListener> listeners = new HashSet<>();

    public DirectoryWatcher(Path directory, DirectoryChangeListener listener) throws IOException {
        listeners.add(listener);
        directory.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.OVERFLOW);
    }

    public DirectoryWatcher start(){
        execute();
        return this;
    }

    @Override
    protected Void doInBackground() {
        while (true) {
            WatchKey key;
            try {
                key = watchService.take();
                // Poll all the events queued for the key
                for (WatchEvent<?> event : key.pollEvents()) {
                    @SuppressWarnings("unchecked")
                    Path filePath = ((WatchEvent<Path>) event).context();
                    File file = filePath.toFile();
                    switch (event.kind().name()) {
                        case "ENTRY_CREATE":
                            notifyFileCreated(file);
                            break;
                        //
                        case "ENTRY_MODIFY":
                            notifyFileModified(file);
                            break;
                        //
                        case "ENTRY_DELETE":
                            notifyFileDeleted(file);
                            break;

                        case "OVERFLOW":
                            notifyOverflowOccurred(file);
                    }

                }
                // reset is invoked to put the key back to ready state
                key.reset();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //return null;
    }


    private void notifyFileCreated(File file){
        for (DirectoryChangeListener listener : listeners)
            listener.fileCreated(file);
    }

    private void notifyFileModified(File file){
        for (DirectoryChangeListener listener : listeners)
            listener.fileModified(file);
    }

    private void notifyFileDeleted(File file){
        for (DirectoryChangeListener listener : listeners)
            listener.fileDeleted(file);
    }

    private void notifyOverflowOccurred(File file){
        for (DirectoryChangeListener listener : listeners)
            listener.onOverflow(file);
    }

}
