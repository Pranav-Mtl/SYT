package com.syt.balram.syt;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.syt.balram.syt.BL.FeedbacKBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;


public class Feedback extends ActionBarActivity {

    Button btnSend;
    EditText etMessage;
    String emailID;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        emailID= Configuration.getSharedPrefrenceValue(Feedback.this, Constant.SHARED_PREFERENCE_UserID);

        btnSend= (Button) findViewById(R.id.feedback_btn);
        etMessage= (EditText) findViewById(R.id.feedback_message);
        back= (ImageButton) findViewById(R.id.feedback_back);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=etMessage.getText().toString();

                if(message.trim().length()>0)
                {
                    //sendMail(message);
                    new SendFeedback().execute(emailID,message);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
/*
    private void sendMail(String msg) {
        // TODO Auto-generated method stub

        Mail m = new Mail("appslurewebsolution@gmail.com", "appslure1990");
        String toArr ="31.akshay@gmail.com";
        m.setTo(toArr);
        m.setFrom("appslurewebsolution@gmail.com");
        m.setSubject("Sell Your Time Feedback ");
        m.setBody(emailID+" send you feedback from SYT APP "+"/n"+msg);
        try {


            if(m.send()) {
                Toast.makeText(Feedback.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(Feedback.this, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            Log.e("MailApp", "Could not send email", e);
        }

    }
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
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

    class SendFeedback extends AsyncTask<String,String,String>{
        FeedbacKBL objFeedbacKBL=new FeedbacKBL();
        TransparentProgressDialog pd=new TransparentProgressDialog(Feedback.this,R.drawable.logo_single);

        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
           String ss= objFeedbacKBL.isUserValid(params[0],params[1]);
            return ss;
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            try
            {
                if(s.equalsIgnoreCase("y"))
                {
                    MyApp.tracker().setScreenName("Feedback Screen");
                    MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                            .setCategory("UI")
                            .setAction("Click")
                            .setLabel("Submit")
                            .build());

                    finish();
                    Toast.makeText(getApplicationContext(),"Your message has been sent to SYT customer support team.",Toast.LENGTH_LONG).show();
                }
                else
                {
                        finish();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {

            }

        }
    }
}
