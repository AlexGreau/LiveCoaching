package com.example.livecoaching.Communication;

import android.location.Location;
import android.location.LocationManager;

import com.example.livecoaching.Logs.Logger;
import com.example.livecoaching.Model.ApplicationState;
import com.example.livecoaching.Model.RouteCalculator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Decoder {

    protected final int PORT = ApplicationState.PORT;
    protected Thread serverSocketThread;
    protected ServerSocket serverSocket;
    protected boolean running;

    protected Location actualLocation;
    protected Logger logger;

    protected String messageFromClient;
    protected String replyMsg;

    protected RouteCalculator routeCalculator;

    public Server(Logger logger) {
        this.logger = logger;
        serverSocketThread = new Thread(new SocketServerThread());
        running = true;
        actualLocation = new Location(LocationManager.GPS_PROVIDER);
        serverSocketThread.start();
        System.out.println("Server launched");
    }

    @Override
    public void decodeMessage(String msg) {
        // split message
        String[] parts = msg.split(":");
        String senderState = parts[0];
        // interpret results
        if (senderState.equals("Ready")) {
            replyMsg = "Continue";
            if (parts.length >= 2) {
                logger.getLogsArray().clear();
                parseInfos(parts[1]);
                initRouteCalculator(logger.getLogsArray().get(0));
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
        } else if (senderState.equals("Asking")) {
            parseInfos(parts[1]);
            replyMsg = "route:" + format(routeCalculator.getActualRoute());
        }
    }

    private void parseInfos(String str) {
        String[] infos = str.split("-");
        actualLocation.setLatitude(Float.parseFloat(infos[0]));
        actualLocation.setLongitude(Float.parseFloat(infos[1]));
        logger.getLogsArray().add(actualLocation);
        // System.out.println("added location to log : " + actualLocation);
    }

    private void stopLogging() {
        System.out.println("stopping the logging");
        System.out.println(logger.getLogsArray().size());
        logger.flushLogArray();
        logger.readLogFile();
    }

    private void initRouteCalculator(Location loc) {
        routeCalculator = new RouteCalculator(loc);
        routeCalculator.getRouteI();
        System.out.println("route : " + routeCalculator.getActualRoute());
    }

    private String format(ArrayList<Location> locs) {
        // formats the array of location into a sendable message
        String res = "";
        for (Location loc : locs) {
            res += loc.getLatitude() + "-" + loc.getLongitude() + ";";
        }
        return res;
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
