package com.example.animate;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PhoneData extends AppCompatActivity {
    private TextView txt;
    private Button checkPermissionBtn;
    private View mLayout;
    private boolean wifiAvailable;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonedata);

        mLayout = findViewById(R.id.mLayout);
        txt = (TextView) findViewById(R.id.textView);
        txt.setText("You can't kick me outta freestyle club just cause I'm gay");

        checkPermissionBtn = (Button) findViewById(R.id.clickMe);
        checkPermissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermission();
                String wifiName = "";
                if(wifiAvailable){
                    wifiName = getWifiName();
                } else {
                    wifiName = "Try Again";
                }
                float battery = batteryLevel();
                txt.setText(wifiName +
                        "\n\n Oh, by the way " +
                        "\nYa mum's gay and ye battery " +
                        "is at " + battery + "%.");
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "Location granted",
                        Snackbar.LENGTH_SHORT)
                        .show();
                wifiAvailable = true;
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Location denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    public String getWifiName(){
        WifiManager wifiManager = (WifiManager) this.getSystemService(this.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        return ssid;
    }

    public float batteryLevel(){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = PhoneData.this.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;
        return batteryPct;
    }

    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.e("Location permission", "Located permission granted, darling.");
            Snackbar.make(mLayout,
                    "Location available",
                    Snackbar.LENGTH_SHORT).show();
            wifiAvailable = true;
        } else {
            Log.e("Location permission", "Requesting Location permission..");
            requestLocationPermission();
        }
    }

    public void requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)){
            Snackbar.make(mLayout, "Location Access Requested.",
                    Snackbar.LENGTH_INDEFINITE).setAction("Ok", view -> {
                        // Request the permission
                        ActivityCompat.requestPermissions(PhoneData.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    Log.e("Location permission", "Requested location from Snackbar.");
                    }).show();
        } else {
            Snackbar.make(mLayout, "Location unavailable", Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }
}
