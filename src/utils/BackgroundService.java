package utils;

import javax.swing.*;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class BackgroundService extends SwingWorker<Void, Void> {

    private static BackgroundService updater = null;



    private final Deque<Runnable> tasks = new LinkedBlockingDeque<>();

    private volatile boolean running = false;

    private BackgroundService(){
    }

    public static void startService(){
        updater = new BackgroundService();
        updater.running = true;
        updater.execute();
    }

    public static void cancelService(){
        updater.running = false;
        updater = null;
    }

    public static void performTask(Runnable task){
        if (updater == null) startService();
        updater.tasks.addFirst(task);
    }

    @Override
    protected Void doInBackground() {
        while (running){
            while (running && tasks.isEmpty()) Thread.onSpinWait();
            if (running) {
                Runnable task = tasks.removeLast();
                try {
                    task.run();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



}
