package com.km1930.dynamicbicycleclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.km1930.dynamicbicycleclient.serialndk.SerialManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SerialManager mSerialManager = SerialManager.getInstance();
        mSerialManager.openSerial();
    }
}
