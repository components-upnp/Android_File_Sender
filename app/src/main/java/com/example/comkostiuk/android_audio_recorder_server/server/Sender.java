package com.example.comkostiuk.android_audio_recorder_server.server;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.example.comkostiuk.android_audio_recorder_server.upnp.FileSenderController;

import org.fourthline.cling.model.meta.LocalService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by mkostiuk on 24/04/2017.
 */

public class Sender implements Runnable {

    private Context context;
    private Socket socket;
    private OutputStream out;
    private LocalService<FileSenderController> service;
    private InputStream in;
    private byte[] buffer;

    public Sender(Socket s, LocalService<FileSenderController> ser, Context c, String path) throws IOException {
        socket = s;
        context = c;
        service = ser;
        out = socket.getOutputStream();
        in = new FileInputStream(path);
        buffer = new byte[1024];

    }

    @Override
    public void run() {

        System.err.println("Connexion etablie");
        int eof = 0;

        //service.getManager().getImplementation().setSending(true);
        while (eof != -1) {
            try {
                out.write(buffer,0,eof);
                eof = in.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        service.getManager().getImplementation().setFile("fin");
        System.err.println("Fin envoie");

       try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
