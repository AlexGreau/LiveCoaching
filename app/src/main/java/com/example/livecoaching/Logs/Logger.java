package com.example.livecoaching.Logs;

import android.content.Context;
import android.location.Location;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Logger {

    private final String TAG = "Logger";

    protected ArrayList<Location> logs;
    protected File logsFile;
    protected final String fileName = "TrainingLogs.txt";
    protected final String separator = ";";
    protected final String coordinatesSeparator = ",";
    protected Context context;

    public Logger(Context context) {
        this.context = context;
        logs = new ArrayList<Location>();
        initFile();
    }

    protected void initFile() {
        logsFile = new File(context.getFilesDir(), fileName);
        if (logsFile.exists()) {
            Log.d(TAG, "file exists !");
        } else {
            Log.d(TAG, "file doesnt exist... creating it");
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

    public void initNewLog(String ID, String trajectory, String interactionType) {
        String res = "\r\n" + ID + separator + trajectory + separator + interactionType + separator;
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
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
        Log.d(TAG, "whats on the file :" + ret);
    }

    public void resetLogsArray() {
        logs = new ArrayList<Location>();
    }

    public void clearFile() {
        writeToLogFile("", false);
    }
}
