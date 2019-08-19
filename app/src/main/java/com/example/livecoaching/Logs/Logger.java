package com.example.livecoaching.Logs;

import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.util.Log;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Logger {

    private final String TAG = "Logger";

    protected ArrayList<Location> logs;
    protected File logsFile;
    protected final String fileName = "trainingLogs.txt";
    protected final String filePath = "LogsDirectory";
    protected final String separator = ";";
    protected final String coordinatesSeparator = ",";
    protected Context context;

    public Logger(Context context) {
        this.context = context;
        logs = new ArrayList<Location>();
        initFile();
    }

    protected void initFile() {
        // Log.d(TAG, "external storage is available for read and write : " + isExternalStorageWritable());
        // Log.d(TAG, " external storage is Available : " + isExternalStorageAvailable());
        logsFile = new File(context.getExternalFilesDir(filePath), fileName);
        if (logsFile.exists()) {
            Log.d(TAG, "file exists !");
        } else {
            Log.e(TAG, "file does not exist... creating it");
            try {
                logsFile.createNewFile();
                writeToLogFile("TrainingLogs", false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Location> getLogsArray() {
        return logs;
    }

    public void setLogsArray(ArrayList<Location> array) {
        logs = array;
    }

    public void initNewLog(String ID, String interactionType, int difficulty) {
        String res = "\r\n" + ID  + separator + interactionType + separator + difficulty + separator;
        writeToLogFile(res, true);
    }

    public void flushLogArray() {
        StringBuilder logString = new StringBuilder();
        for (Location loc : logs) {
            logString.append(loc.getLatitude());
            logString.append(coordinatesSeparator);
            logString.append(loc.getLongitude());
            logString.append(separator);
        }
        writeToLogFile(logString.toString(), true);
    }

    public void writeToLogFile(String text, boolean append) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(logsFile, append);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(text.getBytes());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        readLogFile();
    }

    public void readLogFile() {
        String line = "";
        String myData = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(logsFile);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedReader buff = new BufferedReader(new InputStreamReader(dataInputStream));

            while ( (line = buff.readLine())!= null){
                myData = myData + line;
            }
            dataInputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
        Log.d(TAG, "whats on the file :" + myData);
    }

    public void resetLogsArray() {
        logs = new ArrayList<Location>();
    }

    public void clearFile() {
        writeToLogFile("", false);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
