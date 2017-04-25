package com.example.comkostiuk.android_audio_recorder_server;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.comkostiuk.android_audio_recorder_server.server.Server;
import com.example.comkostiuk.android_audio_recorder_server.upnp.Service;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;

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

public class App extends AppCompatActivity {

    private Service service;
    private ServiceConnection serviceConnection;
    private Thread server;
    private volatile boolean running;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

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

}
