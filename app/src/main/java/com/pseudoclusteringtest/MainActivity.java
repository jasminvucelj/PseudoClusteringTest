package com.pseudoclusteringtest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private static final double DISTANCE_THRESHOLD = 2000;

    private Button b;
    private TextView t;
    private TextView t2;
    private LocationManager locationManager;
    private LocationListener listener;

    PseudoCluster pc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        t = (TextView) findViewById(R.id.textView);
        t2 = (TextView) findViewById(R.id.textView2);
        b = (Button) findViewById(R.id.button);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        pc = new PseudoCluster(DISTANCE_THRESHOLD);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                t.append("\n " + location.getLongitude() + " " + location.getLatitude());
                pc.add(location);
                PseudoClusterElement largestCluster = pc.getLargestCluster();

                t2.setText(String.valueOf(largestCluster.size()));

                // t2.setText(String.valueOf(pc.getTotalElementCount()));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }


    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
            }
        });
    }


    String locationListToString(List<Location> locationList) {
        int size = locationList.size();
        if (size == 0) return "";
        else {
            StringBuilder sb = new StringBuilder();
            for(Location location : locationList) {
                sb.append(location.getLatitude())
                        .append(" ")
                        .append(location.getLongitude())
                        .append("\n");
            }
            return sb.toString();
        }
    }
}