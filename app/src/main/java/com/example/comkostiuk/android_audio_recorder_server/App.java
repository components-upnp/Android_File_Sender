package com.example.comkostiuk.android_audio_recorder_server;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.comkostiuk.android_audio_recorder_server.server.Server;
import com.example.comkostiuk.android_audio_recorder_server.upnp.Service;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import xdroid.toaster.Toaster;

public class App extends AppCompatActivity {

    private Service service;
    private ServiceConnection serviceConnection;
    private Thread server;
    private volatile boolean running;
    private Button endRecord, stop, record, send;


    public void init() {
        endRecord = (Button) findViewById(R.id.endRecord);
        stop = (Button) findViewById(R.id.stop);
        record = (Button) findViewById(R.id.record);
        send = (Button) findViewById(R.id.sendButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app);

        init();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        File dir;
        System.err.println(Build.BRAND);
        if (Build.BRAND.toString().equals("htc_europe"))
                dir = new File("/mnt/emmc/FileSender/");
            else
                dir = new File(Environment.getExternalStorageDirectory().getPath() + "/FileSender/");

        while (!dir.exists()) {
            dir.mkdir();
            dir.setReadable(true);
            dir.setExecutable(true);
            dir.setWritable(true);
        }



        service = new Service();
        serviceConnection = service.getService();



        getApplicationContext().bindService(new Intent(this, AndroidUpnpServiceImpl.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE);


    }

    public void sendAudio(View view) throws IOException, InterruptedException {

        running = true;
        Server s = new Server(service.getRecorderLocalService(),this, running);

        server = new Thread(s);

        server.start();


    }

    public void stop(View view) {
        running = false;
    }

    public void record(View view) {
        activate(endRecord);
        deactivate(send,stop);
    }

    public void activate(Button ... buttons) {
        for (Button b : buttons)
            b.setClickable(true);
    }

    public void deactivate(Button ... buttons) {
        for (Button b : buttons)
            b.setClickable(false);
    }

}
