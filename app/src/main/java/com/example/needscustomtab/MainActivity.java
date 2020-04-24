package com.example.needscustomtab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected LocationManager locationManager;
    TextView txtLocLon;
    TextView txtLocLat;
    TextView locReadWaitMsg;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            try {
                if (location != null) {
                    txtLocLon = (TextView) findViewById(R.id.locLonTxt);
                    txtLocLat = (TextView) findViewById(R.id.locLatTxt);
                    locReadWaitMsg = (TextView) findViewById(R.id.locReadWaitMsg);
                    txtLocLon.setText(String.valueOf(location.getLongitude()));
                    txtLocLat.setText(String.valueOf(location.getLatitude()));
                    locReadWaitMsg.setVisibility(View.INVISIBLE);
                }
            }catch (Exception e)
            {
                locReadWaitMsg = (TextView) findViewById(R.id.locReadWaitMsg);
                locReadWaitMsg.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            locReadWaitMsg = (TextView) findViewById(R.id.locReadWaitMsg);
            locReadWaitMsg.setVisibility(View.VISIBLE);
            // ----------- just hide message after certain time
            new Handler().postDelayed(new Runnable(){
                public  void run() {
                    locReadWaitMsg = (TextView) findViewById(R.id.locReadWaitMsg);
                    locReadWaitMsg.setVisibility(View.INVISIBLE);
                }
            }, 7000);
            // --------------------
            this.requestLocationUpdate();
        }catch (Exception e){
            locReadWaitMsg = (TextView) findViewById(R.id.locReadWaitMsg);
            locReadWaitMsg.setVisibility(View.INVISIBLE);
        }

        findViewById(R.id.opn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLocLon = (TextView) findViewById(R.id.locLonTxt);
                txtLocLat = (TextView) findViewById(R.id.locLatTxt);
                openCustomTab(getString(R.string.website_url), txtLocLon.getText().toString(), txtLocLat.getText().toString());
            }
        });
        findViewById(R.id.opnChrm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLocLon = (TextView) findViewById(R.id.locLonTxt);
                txtLocLat = (TextView) findViewById(R.id.locLatTxt);
                openInChrome(getString(R.string.website_url), txtLocLon.getText().toString(), txtLocLat.getText().toString());
            }
        });
    }
    private void setLocatoinByLastKnown(Location location){ // This method is to use set location by calling LocationManager.getLastKnownLocation('Network location provider')
        try {
            if (location != null) {
                txtLocLon = (TextView) findViewById(R.id.locLonTxt);
                txtLocLat = (TextView) findViewById(R.id.locLatTxt);
                locReadWaitMsg = (TextView) findViewById(R.id.locReadWaitMsg);
                txtLocLon.setText(String.valueOf(location.getLongitude()));
                txtLocLat.setText(String.valueOf(location.getLatitude()));
                locReadWaitMsg.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){

        }

    }

    private void requestLocationUpdate() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(this)
                            .setTitle("Location access permission")
                            .setMessage("Kath app needs permission to access your location for advanced functionality")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Prompt the user once explanation has been shown
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            MY_PERMISSIONS_REQUEST_LOCATION);
                                }
                            })
                            .create()
                            .show();


                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return;
            } else {
                // Try getting location by 'Network provider first (this is quicker and less failure)
                Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                setLocatoinByLastKnown(loc);
                // Also try getting location by GPS
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                        0, mLocationListener);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        try {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_LOCATION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // location-related task you need to do.
                        if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                            // Try getting location by 'Network provider first (this is quicker and less failure)
                            Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            setLocatoinByLastKnown(loc);
                            // Also try getting location by GPS
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                                    0, mLocationListener);
                        }

                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.

                    }
                    return;
                }

            }
        }catch (Exception e){

        }
    }
    private void openCustomTab(String urlBase, String longi, String lati) {
        boolean failed = false;
        String mobileEndPoint = "mapp";
        try {
            String url = "http://" + urlBase + mobileEndPoint + "?lon=" + longi + "&lat=" + lati;
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            builder.setToolbarColor(Color.BLUE);
            customTabsIntent.launchUrl(this, Uri.parse(url));
        } catch (Exception e) {
            // If chrome custom tabs got error, launch app with Chrome
            txtLocLon = (TextView) findViewById(R.id.locLonTxt);
            txtLocLat = (TextView) findViewById(R.id.locLatTxt);
            openInChrome(urlBase, txtLocLon.getText().toString(), txtLocLat.getText().toString());
        }
    }
    private void openInChrome(String urlBase, String longi, String lati) {
        try {
            String mobileWebEndPoint = "mweb";
            String url = "http://" + urlBase + mobileWebEndPoint + "?lon=" + longi + "&lat=" + lati;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please check your internet connection. Contact administrator.")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}
