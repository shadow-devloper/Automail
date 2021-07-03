package com.shadowDeveloper.automail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;


public class SplashScreen extends AppCompatActivity {
    public final int SPLASH_DISPLAY_LENGTH = 3000;

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) !=
                        PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WAKE_LOCK,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE},
                    123);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_splash_initial);

        new Handler().postDelayed(new Runnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    checkPermission();
                }
                String lang = "load or determine the system language and set to default if it isn't available.";
                Locale locale = new Locale(lang);
                Configuration config = new Configuration();
                config.locale=locale;
                SplashScreen.this.getResources().updateConfiguration(config,
                        SplashScreen.this.getResources().getDisplayMetrics());

                Intent mainIntent =new Intent(SplashScreen.this,LoginActivity.class);
                SplashScreen.this.startActivity(mainIntent);

                SplashScreen.this.finish();
            }
        },SPLASH_DISPLAY_LENGTH);

    }

    public void onPause() {
        super.onPause();
        finish();
    }


}
