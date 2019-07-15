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

    protected int count = 0;

    protected String messageFromClient;

    public Server() {
        serverSocketThread = new Thread(new SocketServerThread());
        running = true;
        serverSocketThread.start();
        System.out.println("Server launched");
    }

    private class SocketServerThread extends Thread {
        @Override
        public void run() {
            System.out.println("Socket server running");

            Socket socket = null;
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            try {
                serverSocket = new ServerSocket(PORT);
                while (running) {
                    socket = serverSocket.accept();
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    String replyMsg = "reply message not defined yet";

                    messageFromClient = dataInputStream.readUTF();
                    System.out.println("received message from client : " + messageFromClient);

                    if (messageFromClient.equals("Ready")){
                        replyMsg = "Continue";
                    } else {
                        replyMsg = "Hello from server little bro #" + count;
                    }
                    count ++;
                    dataOutputStream.writeUTF(replyMsg);
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
