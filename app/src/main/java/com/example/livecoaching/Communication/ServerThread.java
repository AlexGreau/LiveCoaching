package com.example.livecoaching.Communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sql.CommonDataSource;

public class ServerThread implements Runnable {

    ServerSocket serverSocket;
    final int PORT;

    public ServerThread(ServerSocket serverSocket, int PORT) {
        this.serverSocket = serverSocket;
        this.PORT = PORT;
    }

    @Override
    public void run() {
        Socket socket;
        try {
            serverSocket = new ServerSocket(this.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (serverSocket != null) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    socket = serverSocket.accept();
                    CommunicationThread communicationThread = new CommunicationThread(socket);
                    new Thread(communicationThread).start();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
