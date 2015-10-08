 package com.syt.balram.syt;

 import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.syt.balram.syt.BE.BuyerQuestionBE;
import com.syt.balram.syt.BL.BuyerQuestionBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;


 public class OTPBuyerActivity extends AppCompatActivity {

    Button btn;
    EditText etOTP;
    BuyerQuestionBE objBuyerQuestionBE;
    BuyerQuestionBL objBuyerQuestionBL;
    TransparentProgressDialog progressDialog;

    ImageButton back;
    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpbuyer);
        btn = (Button) findViewById(R.id.otp_buyer_btn);
        etOTP = (EditText) findViewById(R.id.otp_buyer_text);
        back= (ImageButton) findViewById(R.id.otp_buyer_back);
        Intent intent = getIntent();
        objBuyerQuestionBE = (BuyerQuestionBE) intent.getSerializableExtra("BuyerQuestionBE");
        objBuyerQuestionBL = new BuyerQuestionBL();
        progressDialog = new TransparentProgressDialog(OTPBuyerActivity.this,R.drawable.logo_single);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerReceiver(receiver,filter);

       /* Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);

                }
                // use
                System.out.println(msgData);
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }*/


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otpText = etOTP.getText().toString();

                if (otpText.equals(Constant.userOTP)) {
                    if (Configuration.isInternetConnection(OTPBuyerActivity.this)) {
                        String ladoo=Configuration.getSharedPrefrenceValue(OTPBuyerActivity.this,Constant.CampaignLadoo);
                        if(ladoo==null) {
                            new BuyerRegister().execute("not ladoo");
                        }
                        else
                        {
                            new BuyerRegister().execute(ladoo);

                        }
                    }
                }
                else
                {
                    etOTP.setError("You enter incorrect OTP");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_otpbuyer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class BuyerRegister extends AsyncTask<String, String, String> {

        // ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            //starting the progress dialogue

            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String result = objBuyerQuestionBL.insertBuyer(objBuyerQuestionBE, OTPBuyerActivity.this,params[0]);
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            try {
                if (result.equalsIgnoreCase("y")) {

                    MyApp.tracker().setScreenName("Buyer Registration");
                    MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                            .setCategory("UI")
                            .setAction("Click")
                            .setLabel("Submit")
                            .build());

                    Configuration.setSharedPrefrenceValue(OTPBuyerActivity.this, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_UserID, objBuyerQuestionBE.getEmail());
                    Configuration.setSharedPrefrenceValue(OTPBuyerActivity.this, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_LoginTitle, "Buyer");
                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"falied",Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }


    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        final SmsManager sms = SmsManager.getDefault();

        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        //etOTP.setText(message.trim());
                        Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);


                        // Show Alert
                        int duration = Toast.LENGTH_LONG;

                        //etOTP.setText(message);
                       /* Toast toast = Toast.makeText(context,
                                "senderNum: " + senderNum + ", message: " + message, duration);
                        toast.show();*/

                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);

            }
        }
    };



    @Override
    protected void onResume() {

        registerReceiver(receiver,filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }
}
