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
    protected File completeLogsFile;
    protected File simpleLogsFile;
    protected final String completeLogsFileName = "completeTrainingLogs.txt";
    protected final String simpleLogsFileName = "simpleTrainingLogs.txt";
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
        completeLogsFile = new File(context.getExternalFilesDir(filePath), completeLogsFileName);
        if (completeLogsFile.exists()) {
            Log.d(TAG, "file exists !");
        } else {
            Log.e(TAG, "file does not exist... creating it");
            try {
                completeLogsFile.createNewFile();
                writeToLogFile("Structure : \nID;interaction;difficulty;trial;lat,long;partOfRoute;timestamp ", false, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        simpleLogsFile = new File(context.getExternalFilesDir(filePath), simpleLogsFileName);
        if (simpleLogsFile.exists()) {
            Log.d(TAG, "file exists !");
        } else {
            Log.e(TAG, "file does not exist... creating it");
            try {
                simpleLogsFile.createNewFile();
                writeToLogFile("Structure : \nID;interaction;difficulty;trial;theoricDist(m);totalRealDist(m);totalTime(ms);success", false, true);
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

    public void writeCompleteLog(String ID, String interactionType, int difficulty, int trialNumber, int partOfRoute, Location loc, long timestamp) {
        String log = "\r\n" + ID + separator + interactionType + separator + difficulty + separator + trialNumber + separator;
        String coordinates = "" + loc.getLatitude() + coordinatesSeparator + loc.getLongitude();
        log = log + coordinates + separator + partOfRoute + separator + timestamp;
        writeToLogFile(log, true, false);
    }

    public void writeSimpleLog(String ID, String interactionType, int difficulty, int trialNumber, double theoricDistance, double totalRealDistance, long totalTime, boolean success) {
        String log = "\r\n" + ID + separator + interactionType + separator + difficulty + separator + trialNumber + separator;
        log = log + theoricDistance + separator + totalRealDistance + separator + totalTime + separator + success;

        writeToLogFile(log, true, true);
    }

    public void writeToLogFile(String text, boolean append, boolean isSimpleLog) {
        FileOutputStream stream = null;
        File file;
        if (isSimpleLog) {
            file = simpleLogsFile;
        } else {
            file = completeLogsFile;
        }
        try {
            stream = new FileOutputStream(file, append);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(text.getBytes());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readLogFile() {
        String line = "";
        String myData = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(completeLogsFile);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedReader buff = new BufferedReader(new InputStreamReader(dataInputStream));

            while ((line = buff.readLine()) != null) {
                myData = myData + line;
            }
            dataInputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
        Log.d(TAG, "whats on the file :" + myData);
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
