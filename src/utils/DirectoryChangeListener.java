package utils;

import java.io.File;

public interface DirectoryChangeListener {

    void fileCreated(File file);

    void fileModified(File file);

    void fileDeleted(File file);

    default void onOverflow(File file) { }

}
