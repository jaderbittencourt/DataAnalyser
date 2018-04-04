package br.com.dataanalyser.jaderbittencourt.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class DataAnalyser {

    private boolean processing = false;
    private final String DATA_IN = System.getProperty("user.home") + File.separator + "data" + File.separator + "in" + File.separator;

    private FileProcessor fileProcessor;

    public DataAnalyser() {
        this.fileProcessor = new FileProcessor(DATA_IN);
    }

    public void run() throws IOException, InterruptedException {
        // compute initial files
        analyseDataFiles();

        Path path = Paths.get(DATA_IN);

        WatchService watchService =  path.getFileSystem().newWatchService();
        path.register(watchService, ENTRY_CREATE);

        WatchKey watchKey = null;
        while (true) {
            watchKey = watchService.poll(10, TimeUnit.MINUTES);
            if(watchKey != null) {
                watchKey.pollEvents().stream().forEach(event -> analyseDataFiles());
            }
            watchKey.reset();
        }
    }

    /**
     * check if system is idle and start processing files
     */
    protected void analyseDataFiles() {
        if (isIdle()) {
            // flag the system as processing
            startProcessing();
            // build files that probably already are in the data in directory
            fileProcessor.buildFilesList();

            // process files and delete each one after processed
            fileProcessor.processFiles();

            // flag the system as idle
            stopProcessing();
        }
    }

    protected boolean isIdle() {
        return !processing;
    }

    protected void startProcessing() {
        processing = true;
    }

    protected void stopProcessing() {
        processing = false;
    }
}
