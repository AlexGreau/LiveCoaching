package com.example.livecoaching.Communication;

import android.app.Activity;

import com.example.livecoaching.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CommunicationThread implements Runnable {

    private Socket clientSocket;
    private BufferedReader input;
    private MainActivity activity;

    public CommunicationThread(Socket clientSocket, MainActivity activity){
        this.clientSocket = clientSocket;
        this.activity = activity;
        try {
            this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

        }catch  (IOException e){
            System.out.println("Failed to connect to client...");
            e.printStackTrace();
        }
        System.out.println("connected to client !");
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                String read = input.readLine();
                if (read == null ||"Disconnect".contentEquals(read)){
                    Thread.interrupted();
                    read = "Client Disconnected";
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
