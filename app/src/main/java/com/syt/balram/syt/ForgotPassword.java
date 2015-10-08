package com.syt.balram.syt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.syt.balram.syt.BL.ForgotPasswordBL;
import com.syt.balram.syt.BL.SignUpValidity;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.syt.util.Mail;
import com.google.android.gms.analytics.HitBuilders;


public class ForgotPassword extends ActionBarActivity {

    Button btnGo;
    EditText txtEmail;
    String email;
    ProgressDialog progressDialog;
    SignUpValidity objSignUpValidity;
    AlertDialog alertDialog;
    ImageButton back;
    ForgotPasswordBL objForgotPasswordBL;
    TransparentProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        alertDialog = new AlertDialog.Builder(ForgotPassword.this,R.style.MyDialogTheme).create();
        btnGo= (Button) findViewById(R.id.forgot_btn);
        txtEmail= (EditText) findViewById(R.id.forgot_txt);
        back= (ImageButton) findViewById(R.id.forget_back);
        progressDialog=new ProgressDialog(ForgotPassword.this);
        pd=new TransparentProgressDialog(ForgotPassword.this,R.drawable.logo_single);
        objForgotPasswordBL=new ForgotPasswordBL();
        objSignUpValidity=new SignUpValidity();
        enableStrictMode();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=txtEmail.getText().toString();
                if(email.trim().length()==0)
                {
                    txtEmail.setError("required");
                }
                else
                {
                    if(Configuration.isEmailValid(email)) {
                        if(Configuration.isInternetConnection(ForgotPassword.this)) {
                            int randomPIN = (int) (Math.random() * 9000) + 1000;
                            String PINString = String.valueOf(randomPIN);
                            Constant.userOTP = PINString;
                            new VeriFicationCodeSent().execute(email,PINString);
                            //new RegisterValidityForgot().execute(email);
                        }
                        else
                        {
                            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                    ForgotPassword.this);

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
                    else
                    {
                        txtEmail.setError("Invalid email-id");
                    }
                }
            }
        });
    }
    private class RegisterValidityForgot extends AsyncTask<String, String, String> {

        // ProgressDialog progress;

        @Override
        protected void onPreExecute ( )
        {
            //starting the progress dialogue

            progressDialog.setMessage("Loading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String result = objSignUpValidity.isEmailAvailable(params[0]);


            return result;

        }

        @Override
        protected void onPostExecute (String result)
        {
            if (result.equalsIgnoreCase("y")) {

                txtEmail.setError("Email-id not exists");
                progressDialog.dismiss();

            } else {

                                    int randomPIN = (int) (Math.random() * 9000) + 1000;
                                    String PINString = String.valueOf(randomPIN);
                                    Constant.userOTP = PINString;
                                    try {

                                        sendMail(email);
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    finally {
                                        progressDialog.dismiss();
                                        Intent intent=new Intent(getApplicationContext(),NewPassword.class);
                                        intent.putExtra("UserName",email);
                                        startActivity(intent);

                                    }

            }
        }


    }

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }


    private void sendMail(String emailid) {
        // TODO Auto-generated method stub

        Mail m = new Mail("appslurewebsolution@gmail.com", "appslure1990");
        String toArr = emailid;
        m.setTo(toArr);
        m.setFrom("appslurewebsolution@gmail.com");
        m.setSubject("Wellcome to SellYourTime ");
        m.setBody("Hello Your Verification Code is" + Constant.userOTP);

        try {


            if (m.send()) {
                Toast.makeText(ForgotPassword.this, "verification code sent to your email-id", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(ForgotPassword.this, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            Log.e("MailApp", "Could not send email", e);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
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

    class VeriFicationCodeSent extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result=objForgotPasswordBL.sendVerification(params[0],params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            if(s.equalsIgnoreCase("y"))
            {
                MyApp.tracker().setScreenName("Forgot Password Screen");
                MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                        .setCategory("UI")
                        .setAction("Click")
                        .setLabel("Submit")
                        .build());

                Intent intent=new Intent(getApplicationContext(),NewPassword.class);
                intent.putExtra("UserName",email);
                startActivity(intent);
            }
            else {
                txtEmail.setError("Email Id does not exist");
            }
        }
    }
}
