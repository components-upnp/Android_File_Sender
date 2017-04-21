package com.example.comkostiuk.android_audio_recorder_server;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.comkostiuk.android_audio_recorder_server.upnp.Service;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class App extends AppCompatActivity {

    private Service service;
    private ServiceConnection serviceConnection;
    private boolean hasWait = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        service = new Service();
        serviceConnection = service.getService();

        getApplicationContext().bindService(new Intent(this, AndroidUpnpServiceImpl.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE);


    }

    public void sendAudio(View view) throws IOException, InterruptedException {

        final Timer time = new Timer();



        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                service.getRecorderLocalService().getManager().getImplementation()
                        .setSending(true);

                InputStream lol = null;
                try {
                    lol = getAssets().open("test.mp3");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.err.println("DEBUT ENVOIE");
                int i = 0;
                int eof = 0;

                byte[] buffer = new byte[16192];
                try {
                    eof = lol.read(buffer);
                    while (eof != -1) {
                        if (hasWait) {
                           // hasWait = false;
                            System.err.println("ENVOIE" + i);
                            i++;
                            service.getRecorderLocalService().getManager().getImplementation()
                                    .setFile(buffer);

                           /* time.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    hasWait = true;
                                }
                            }, 1);*/
                            eof = lol.read(buffer);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.err.println("FIN ENVOIE");

                service.getRecorderLocalService().getManager().getImplementation()
                        .setSending(false);
            }
        });

        t.run();
    }
}
