package com.syt.balram.syt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.syt.constant.Constant;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;


public class LandingScreen extends Activity {

    Typeface lattoLight;
    TextView tvLineOne,tvLineTwo;
    String emailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        lattoLight = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");
        tvLineOne= (TextView) findViewById(R.id.landingLineOne);
        tvLineTwo= (TextView) findViewById(R.id.landingLineTwo);
        tvLineOne.setTypeface(lattoLight);
        tvLineTwo.setTypeface(lattoLight);
        emailID= Configuration.getSharedPrefrenceValue(LandingScreen.this, Constant.SHARED_PREFERENCE_UserID);
        try {
            MyApp.tracker().setScreenName("Landing Screen");
            MyApp.tracker().send(new HitBuilders.EventBuilder("UI", "OPEN")
                    .setLabel("Landing Screen")
                    .build());

           // AffleInAppTracker.inAppTrackerViewName(getApplicationContext(), "Landing Screen", "App First Screen", "APP Open", null);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //AffleInAppTracker.inAppTrackerViewName(getApplicationContext(), "Landing Screen", "App First Screen", "APP Open", null);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (Configuration.isInternetConnection(LandingScreen.this)) {

                    if (emailID == null) {
                        Intent i = new Intent(LandingScreen.this,
                                HowItWork.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(LandingScreen.this,
                                HomeScreen.class);


                        startActivity(i);
                        finish();
                    }
                } else {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            LandingScreen.this);

// Setting Dialog Title
                    alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

// Setting Dialog Message
                    alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

// Setting Icon to Dialog
                    alertDialog2.setIcon(R.drawable.no_internet);
// Setting Positive "Yes" Btn
                    alertDialog2.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                                }
                            });
// Setting Negative "NO" Btn
                    alertDialog2.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    Toast.makeText(getApplicationContext(),
                                            "You clicked on NO", Toast.LENGTH_SHORT)
                                            .show();
                                    dialog.cancel();
                                    finish();
                                }
                            });

// Showing Alert Dialog
                    alertDialog2.show();

                }


            }


        }, 4000);

    }



}
