package com.example.comkostiuk.android_audio_recorder_server.server;

import android.content.Context;

import com.example.comkostiuk.android_audio_recorder_server.upnp.FileSenderController;

import org.fourthline.cling.model.meta.LocalService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by mkostiuk on 24/04/2017.
 */

public class Server implements Runnable {

    private ServerSocket socketServer;
    private Context context;
    private LocalService<FileSenderController> service;
    private volatile boolean running;

    public Server(LocalService<FileSenderController> s, Context c, boolean r) {
        service = s;
        context = c;
        running = r;
    }



    @Override
    public void run() {
        try {

            System.err.println("Demarrage du serveur");

            socketServer = new ServerSocket(10302);
            Socket socket;

            service.getManager().getImplementation()
                    .setFile(getIpAddress());

            System.err.println("Addresse: "+ getIpAddress());


            Thread.sleep(200);
            service.getManager().getImplementation().setSending(true);
            while (running) {
                System.err.println("En attente de connexion");
                socket = socketServer.accept();
                new Thread(new Sender(socket, service, context)).start();
            }

            service.getManager().getImplementation()
                    .setFile("");


            socketServer.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Server running at : "
                                + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
}
