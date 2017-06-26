package com.example.comkostiuk.android_audio_recorder_server.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.comkostiuk.android_audio_recorder_server.R;



public class App extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app);
        startService(new Intent(this, AppService.class));
        finish();
    }





}
