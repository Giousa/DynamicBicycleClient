package com.km1930.dynamicbicycleclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.km1930.dynamicbicycleclient.serialndk.SerialManager;
import com.km1930.dynamicbicycleclient.service.TService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SerialManager mSerialManager = SerialManager.getInstance();
//        mSerialManager.openSerial();

        Intent intent = new Intent(MainActivity.this, TService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startService(intent);
    }

    @Override
    protected void onDestroy() {
        Intent sevice = new Intent(this, TService.class);
        this.startService(sevice);
        super.onDestroy();
    }
}
