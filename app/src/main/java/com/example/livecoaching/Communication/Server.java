package com.example.livecoaching.Communication;

import com.example.livecoaching.Model.ApplicationState;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    protected final int PORT = ApplicationState.PORT;
    protected Thread serverSocketThread;
    protected ServerSocket serverSocket;
    protected boolean running;

    protected float[] location;

    protected String messageFromClient;
    protected String replyMsg;

    public Server() {
        serverSocketThread = new Thread(new SocketServerThread());
        running = true;
        location = new float[2];
        serverSocketThread.start();
        System.out.println("Server launched");
    }

    protected void decodeMessage(String msg) {
        // split message
        String[] parts = msg.split(":");
        String senderState = parts[0];
        // interpret results
        if (senderState.equals("Ready")) {
            replyMsg = "Continue";
            if (parts.length >= 2) {
                parseInfos(parts[1]);
            }
        } else if (senderState.equals("Running")) {
            System.out.println("detected " + senderState);
            replyMsg = "";
            if (parts.length >= 2) {
                parseInfos(parts[1]);
            }
        } else if (senderState.equals("Stop")) {
            System.out.println("detected " + senderState);
            if (parts.length >= 2) {
                parseInfos(parts[1]);
            }
            stopLogging();
        } else if (senderState.equals("End")) {
            replyMsg = "reset";
        } else if (senderState.equals("Asking")){
            parseInfos(parts[1]);
            replyMsg = "route:" + location[0] +"-" + location[1] +";";
        }
    }

    private void parseInfos(String str) {
        String[] infos = str.split("-");
        location[0] = Float.parseFloat(infos[0]);
        location[1] = Float.parseFloat(infos[1]);
    }

    private void stopLogging() {

    }

    private class SocketServerThread extends Thread {
        @Override
        public void run() {
            System.out.println("Socket server running");

            Socket socket = null;
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;
            replyMsg = "";

            try {
                serverSocket = new ServerSocket(PORT);
                while (running) {
                    socket = serverSocket.accept();
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    messageFromClient = dataInputStream.readUTF();
                    System.out.println("received message from client : " + messageFromClient);

                    decodeMessage(messageFromClient);
                    if (!replyMsg.isEmpty() || replyMsg != null) {
                        System.out.println("Sent : " + replyMsg);
                        dataOutputStream.writeUTF(replyMsg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
