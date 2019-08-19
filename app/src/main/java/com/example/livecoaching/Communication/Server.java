package com.example.livecoaching.Communication;

import com.example.livecoaching.Model.Trial;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    protected final int PORT = 8080;
    protected Thread serverSocketThread;
    protected ServerSocket serverSocket;
    protected boolean running;
    private Trial trial;

    protected String messageFromClient;
    protected String replyMsg;

    public Server(Trial trial) {
        this.trial = trial;
        serverSocketThread = new Thread(new SocketServerThread());
        running = true;
        serverSocketThread.start();
        System.out.println("Server launched");
    }

    public void setRunning(boolean bool) {
        running = bool;
        if (!bool) {
            // TODO : make watch respond to stop / make server send stop message
            this.serverSocketThread.interrupt();
            this.serverSocketThread = null;
        }
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

                    replyMsg = trial.decodeMessage(messageFromClient);
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
