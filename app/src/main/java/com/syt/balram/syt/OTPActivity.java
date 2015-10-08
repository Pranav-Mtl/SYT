package com.syt.balram.syt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.syt.balram.syt.BE.SellerQuestionBE;
import com.syt.balram.syt.BL.SellerQuestionBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;


public class OTPActivity extends ActionBarActivity {

    Button btnOTP;
    EditText txtOTP;
    SellerQuestionBE objSellerQuestionBE;
    SellerQuestionBL objSellerQuestionBL;
    AlertDialog alertDialog;
    ImageButton back;

    TransparentProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        btnOTP= (Button) findViewById(R.id.otp_btn);
        txtOTP= (EditText) findViewById(R.id.otp_text);
        back= (ImageButton) findViewById(R.id.otp_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        alertDialog = new AlertDialog.Builder(OTPActivity.this,R.style.MyDialogTheme).create();
        objSellerQuestionBE=(SellerQuestionBE)intent.getSerializableExtra("SellerQuestionBE");
        objSellerQuestionBL=new SellerQuestionBL();

        progressDialog=new TransparentProgressDialog(OTPActivity.this,R.drawable.logo_single);

       /* System.out.println(objSellerQuestionBE.getCategory());
        System.out.println(objSellerQuestionBE.getSubCategory());
        System.out.println(objSellerQuestionBE.getPhone());
        System.out.println(objSellerQuestionBE.getAddress());
        System.out.println(objSellerQuestionBE.getGender());
        System.out.println(objSellerQuestionBE.getCountry());
        System.out.println(objSellerQuestionBE.getDob());
        System.out.println(objSellerQuestionBE.getExperience());
        System.out.println(objSellerQuestionBE.getFirstName());
        System.out.println(objSellerQuestionBE.getMinPrice());
        System.out.println(objSellerQuestionBE.getMaxPrice());
        System.out.println(objSellerQuestionBE.getServiceCharge());
        System.out.println(objSellerQuestionBE.getDistance());
*/

        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=txtOTP.getText().toString();
                if(otp.trim().length()==0)
                {
                    txtOTP.setError("required");
                }
                else
                {

                    //String result=objSellerQuestionBL.insertSeller(objSellerQuestionBE);

                    if(otp.equals(Constant.userOTP)){
                        if(Configuration.isInternetConnection(OTPActivity.this)) {

                            String ladoo=Configuration.getSharedPrefrenceValue(OTPActivity.this,Constant.CampaignLadoo);
                            if(ladoo==null) {
                                new LongRunningGetIO().execute("not ladoo");
                            }
                            else
                            {
                                new LongRunningGetIO().execute(ladoo);
                            }
                        }
                        else{

                            alertDialog
                                    .setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);
                            alertDialog.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);
                            alertDialog.setButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            //finish();
                                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                                        }
                                    });

                            alertDialog.show();

                        }

                    }
                    else
                    {
                        txtOTP.setError("Incorrect OTP");
                    }
                }
            }
        });

    }


    private class LongRunningGetIO extends AsyncTask<String, String, String> {

        // ProgressDialog progress;

        @Override
        protected void onPreExecute ( )
        {
                progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String result=objSellerQuestionBL.insertSeller(objSellerQuestionBE,getApplicationContext(),params[0]);

            return result;

        }

        @Override
        protected void onPostExecute (String result)
        {    //set adapter here
            // progress.dismiss();
            //userName.setText(result);
            try {

                if (result.trim().equals("1")) {

                    MyApp.tracker().setScreenName("Seller Registration");
                    MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                            .setCategory("UI")
                            .setAction("Click")
                            .setLabel("Submit")
                            .build());

                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_UserID, objSellerQuestionBE.getEmail());
                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_LoginTitle, "Seller");
                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    progressDialog.dismiss();
                } else {


                    progressDialog.dismiss();

                }
            }
            catch (NullPointerException e)
            {
               e.printStackTrace();

            }
            finally {
                progressDialog.dismiss();
            }

            //super.onPostExecute(result);

        }

          /*  if(result!=null)
            {

                String emailId=validate(result);

                //Toast.makeText(getApplicationContext(), emailId, Toast.LENGTH_LONG).show();

            }
        }
*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ot, menu);
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
}
