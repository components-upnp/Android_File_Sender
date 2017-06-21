package com.example.comkostiuk.android_audio_recorder_server.server;

import android.content.Context;

import com.example.comkostiuk.android_audio_recorder_server.upnp.FileSenderController;
import com.example.comkostiuk.android_audio_recorder_server.xml.GenerateurXml;

import org.fourthline.cling.model.meta.LocalService;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import xdroid.toaster.Toaster;

/**
 * Created by mkostiuk on 24/04/2017.
 */

public class Server extends Thread {

    private ServerSocket socketServer;
    private Context context;
    private LocalService<FileSenderController> service;
    private volatile boolean running;
    private String pathFile;
    private String udn;

    public Server(String u, LocalService<FileSenderController> s, Context c, boolean r, String path) {
        service = s;
        context = c;
        running = r;
        pathFile = path;
        udn = u;
    }



    @Override
    public void run() {
        try {

            System.err.println("Demarrage du serveur");

            socketServer = new ServerSocket(10302);
            Socket socket = null;
            socketServer.setSoTimeout(50);

            service.getManager().getImplementation()
                    .setFile(new GenerateurXml().getDocXml(udn, getIpAddress(), new File(pathFile).getName()));

            System.err.println("Addresse: "+ getIpAddress());


            Thread.sleep(200);
            service.getManager().getImplementation().setSending(true);
            while (!Thread.interrupted()) {
                System.err.println("En attente de connexion");
                try {
                    socket = socketServer.accept();
                    new Thread(new Sender(socket, service, context, pathFile)).start();
                } catch (SocketTimeoutException e) {
                    System.err.println("Server socket time out!!!");
                }

            }
            if (socket != null)
                socket.close();
            socketServer.close();
            Thread.currentThread().interrupt();

        }  catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
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
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }

    public void setPathFile(String p) {
        pathFile = p;
    }
}
