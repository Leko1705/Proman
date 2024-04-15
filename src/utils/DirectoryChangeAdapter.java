package utils;

import java.io.File;

public abstract class DirectoryChangeAdapter implements DirectoryChangeListener {

    public void fileCreated(File file) { }

    public void fileModified(File file) { }

    public void fileDeleted(File file) { }

}
