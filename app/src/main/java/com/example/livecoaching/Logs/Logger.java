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
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Logger {

    private final String TAG = "Logger";

    protected ArrayList logs;
    protected File logsFile;
    protected final String fileName = "TrainingLogs.txt";
    protected final String separator = ",";
    protected Context context;

    public Logger(Context context) {
        this.context = context;
        logs = new ArrayList<Location>();
        initFile();
    }

    protected void initFile() {
        logsFile = new File(context.getFilesDir(), fileName);
        if (logsFile.exists()){
            try
            {
                FileOutputStream fOut = new FileOutputStream(logsFile);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append("test");
                myOutWriter.close();
                fOut.close();
            } catch(Exception e)
            {

            }
        } else {
            Log.d(TAG,"exists");
            try {
                logsFile.createNewFile();
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
        // new id Subject, trajectory, interaction technique

        String res = ID + separator + trajectory + separator + interactionType + separator;
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(logsFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(res.getBytes());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " written : " + res);
        readLogFile();
    }

    public void readLogFile(){
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
        Log.d(TAG,"whats on the file :" + ret);
    }

}
