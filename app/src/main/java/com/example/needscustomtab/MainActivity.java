package com.example.needscustomtab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.opn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTab(getString(R.string.website_url));
            }
        });
        findViewById(R.id.opnChrm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInChrome(getString(R.string.website_url));
            }
        });

        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.setUpdateFrom(UpdateFrom.JSON)
                .setDisplay(Display.DIALOG)
                .setUpdateJSON(getString(R.string.auto_update_file))
                .start();

        /*Handler handler = new Handler();
        handler.postDelayed(new MyRunnable(appUpdater) {
        }, 600000);*/
    }

    private void openCustomTab(String urlBase) {
        boolean failed = false;
        String mobileEndPoint = "mapp";
        try {
            String url = "http://" + urlBase + mobileEndPoint;
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            builder.setToolbarColor(Color.BLUE);
            customTabsIntent.launchUrl(this, Uri.parse(url));
        } catch (Exception e) {
            // If chrome custom tabs got error, launch app with Chrome
            openInChrome(urlBase);
        }
    }
    private void openInChrome(String urlBase) {
        try {
            String mobileWebEndPoint = "mweb";
            String url = "http://" + urlBase + mobileWebEndPoint;
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
/*class MyRunnable implements Runnable {
    private AppUpdater appUpdater;
    public MyRunnable(AppUpdater _appUpdater) {
        this.appUpdater = _appUpdater;
    }
    @Override
    public void run() {
        this.appUpdater.stop();
    }
}*/