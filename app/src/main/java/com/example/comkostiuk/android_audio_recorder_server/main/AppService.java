package com.example.comkostiuk.android_audio_recorder_server.main;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.view.View;

import com.example.comkostiuk.android_audio_recorder_server.R;
import com.example.comkostiuk.android_audio_recorder_server.server.Server;
import com.example.comkostiuk.android_audio_recorder_server.upnp.FileSenderController;
import com.example.comkostiuk.android_audio_recorder_server.upnp.FileToSendService;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.meta.LocalService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import xdroid.toaster.Toaster;

/**
 * Created by mkostiuk on 21/06/2017.
 */

public class AppService extends Service {

    private com.example.comkostiuk.android_audio_recorder_server.upnp.Service service;
    private ServiceConnection serviceConnection;
    private LocalService<FileToSendService> fileToSendService;
    private LocalService<FileSenderController> fileSenderService;
    private boolean running;
    private String pathFile;
    private Server s = null;
    private Context context;

    public static final int NOTIFICATION_ID = 1;

    public static final String ACTION_1 = "action_1";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread().start();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        context = this;

        File dir;
        System.err.println(Build.BRAND);
        if (Build.BRAND.toString().equals("htc_europe")) {
            dir = new File("/mnt/emmc/FileSender/");
            pathFile = "/mnt/emmc/FileSender/enregistrement.mp3";
        }
        else {
            dir = new File(Environment.getExternalStorageDirectory().getPath() + "/FileSender/");
            pathFile = Environment.getExternalStorageDirectory().getPath() + "/FileSender/enregistrement.mp3";
        }

        while (!dir.exists()) {
            dir.mkdir();
            dir.setReadable(true);
            dir.setExecutable(true);
            dir.setWritable(true);
        }

        service = new com.example.comkostiuk.android_audio_recorder_server.upnp.Service();
        serviceConnection = service.getService();



        getApplicationContext().bindService(new Intent(this, AndroidUpnpServiceImpl.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE);


        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        fileToSendService = service.getFileToSendLocalService();
                        fileSenderService = service.getRecorderLocalService();

                        fileToSendService.getManager().getImplementation().getPropertyChangeSupport()
                                .addPropertyChangeListener(
                                        new PropertyChangeListener() {
                                            @Override
                                            public void propertyChange(PropertyChangeEvent evt) {
                                                if (evt.getPropertyName().equals("path")) {
                                                    if (s == null) {
                                                        running = true;
                                                        s = new Server(fileSenderService, context, running, pathFile);
                                                        s.start();
                                                    }
                                                    else {
                                                        s.setPathFile(pathFile);
                                                    }
                                                }
                                            }
                                        }
                                );
                    }
                },
                3000
        );

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //fonction permettant d'afficher une notification test à l'utilisateur lors d'un évènement
    public static void displayNotification(Context context) {

        Intent action1Intent = new Intent(context, NotificationActionService.class)
                .setAction(ACTION_1);

        PendingIntent action1PendingIntent = PendingIntent.getService(context, 0,
                action1Intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);

        android.support.v4.app.NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Accelerometer")
                        .setContentText("Cliquer ici pour fermer l'application")
                        .addAction(new NotificationCompat.Action(R.mipmap.ic_launcher_round,
                                "Fermer", action1PendingIntent))
                        .setContentIntent(action1PendingIntent)
                        .setAutoCancel(true)
                ;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }


    //Classe interne permettant de créer un service gérant les actions faites sur la notification
    //L'utilisateur peut passer au fichier audio suivant ou précédent
    public static class NotificationActionService extends IntentService {
        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String action = intent.getAction();
            Toaster.toast("Received notification action: " + action);
            if (ACTION_1.equals(action)) {
                // TODO: handle action 1.
                System.out.println("Action 1 notification");

                Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                getApplicationContext().sendBroadcast(it);

                System.exit(0);
            }
        }
    }

}
