package com.syt.balram.syt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.syt.balram.syt.BE.SellerQuestionBE;
import com.syt.balram.syt.BL.SendOtpBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.syt.util.Mail;

import java.util.ArrayList;
import java.util.List;

public class SellerQuestionFive extends ActionBarActivity {

    Spinner sellerCharge,sellerOn;
    LinearLayout lout1,lout2;
    EditText fixedprice,minprice,maxprice;
    RadioGroup groupNotified;
    private Button next;
    RadioButton radioBtn1;
    String emailID;
    SellerQuestionBE objSellerQuestionBE;
    String priceType;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_question_five);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        back= (ImageButton) findViewById(R.id.seller_question_five_back);
        sellerCharge= (Spinner) findViewById(R.id.spinnerSeller_charge);
        sellerOn= (Spinner) findViewById(R.id.spinnerSeller_offerOn);
        fixedprice= (EditText) findViewById(R.id.seller_fixedprice);
        minprice= (EditText) findViewById(R.id.seller_minprice);
        maxprice= (EditText) findViewById(R.id.seller_maxprice);
        next= (Button) findViewById(R.id.seller_btn5);



        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        objSellerQuestionBE=(SellerQuestionBE)intent.getSerializableExtra("SellerQuestionBE");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        groupNotified= (RadioGroup) findViewById(R.id.radio_notified);
        emailID= Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.SHARED_PREFERENCE_UserID);

        List categories = new ArrayList();
        if(objSellerQuestionBE.getCategory().equalsIgnoreCase("Social Causes"))
        {
            categories.add("Volunteer/Not Charge");
        }


        categories.add("Charge per hour");
        categories.add("Charge a fix price");
        categories.add("Charge per day");
        categories.add("Charge per project");




        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.gender_spinner_item,categories);
        sellerCharge.setAdapter(adapter);

        lout1= (LinearLayout) findViewById(R.id.seller_priceone);
        lout2= (LinearLayout) findViewById(R.id.seller_priceTwo);

        List dayOn=new ArrayList();
        dayOn.add("Monday-Friday");
        dayOn.add("Monday-Saturday");
        dayOn.add("On Weekends");
        dayOn.add("All 7 days");

        ArrayAdapter adapter1 = new ArrayAdapter(this, R.layout.gender_spinner_item,dayOn);
        sellerOn.setAdapter(adapter1);

        priceType="Max Min";


    sellerCharge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String chargeSelected=sellerCharge.getSelectedItem().toString();
            if(chargeSelected.equals("Charge a fix price"))
            {
                lout2.setVisibility(View.VISIBLE);
                lout1.setVisibility(View.INVISIBLE);
                priceType="Fix Price";
            }
            else if(chargeSelected.equals("Volunteer/Not Charge"))
            {
                lout2.setVisibility(View.GONE);
                lout1.setVisibility(View.GONE);
                priceType="Volunteer";
            }
            else
            {
                lout1.setVisibility(View.VISIBLE);
                lout2.setVisibility(View.INVISIBLE);
                priceType="Max Min";
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String charge=sellerCharge.getSelectedItem().toString();
                String txtMinPrice=minprice.getText().toString();
                String txtMaxPrice=maxprice.getText().toString();
                String txtFixedPrice=fixedprice.getText().toString();
                String txtSellingDay=sellerOn.getSelectedItem().toString();
                int selected = groupNotified.getCheckedRadioButtonId();

                radioBtn1 = (RadioButton) findViewById(selected);

                if(priceType.equalsIgnoreCase("Max Min"))
                {
                    System.out.println("MAX MIN");



                    if (txtMinPrice.length()==0) {

                            minprice.setError("required");

                    }
                    else if( txtMaxPrice.length() == 0)
                    {
                        maxprice.setError("required");
                    }

                    else
                    {
                        int minInt=Integer.valueOf(txtMinPrice);
                        int maxInt=Integer.valueOf(txtMaxPrice);

                         if(minInt>maxInt)
                         {
                             maxprice.setError("must be greater than min price");
                         }
                        else
                         {
                             try {
                                 objSellerQuestionBE.setServiceCharge(charge);
                                 objSellerQuestionBE.setMaxPrice(txtMaxPrice.trim());
                                 objSellerQuestionBE.setMinPrice(txtMinPrice.trim());
                                 objSellerQuestionBE.setServicesWhen(txtSellingDay);
                                 objSellerQuestionBE.setServiceNotified(radioBtn1.getText().toString().trim());
                                 int randomPIN = (int)(Math.random()*9000)+1000;
                                 String PINString= String.valueOf(randomPIN);
                                 Constant.userOTP=PINString;
                                 System.out.println(Constant.userOTP);
                                 sendOTP(objSellerQuestionBE.getEmail(), objSellerQuestionBE.getPhone(), Constant.userOTP);

                             }
                             catch(Exception e)
                             {
                                 e.printStackTrace();
                             }
                         }

                    }
                }
                else if(priceType.equalsIgnoreCase("Fix Price"))
                {
                    System.out.println("Fix Price");
                    if(txtFixedPrice.length()==0)
                    {
                        fixedprice.setError("required");
                    }
                    else
                    {
                        txtMinPrice=txtFixedPrice;
                        txtMaxPrice="";
                        try {
                            objSellerQuestionBE.setServiceCharge(charge);
                            objSellerQuestionBE.setMaxPrice(txtMaxPrice.trim());
                            objSellerQuestionBE.setMinPrice(txtMinPrice.trim());
                            objSellerQuestionBE.setServicesWhen(txtSellingDay);
                            objSellerQuestionBE.setServiceNotified(radioBtn1.getText().toString().trim());
                            int randomPIN = (int)(Math.random()*9000)+1000;
                            String PINString= String.valueOf(randomPIN);
                            Constant.userOTP=PINString;
                            System.out.println(Constant.userOTP);
                            sendOTP(objSellerQuestionBE.getEmail(), objSellerQuestionBE.getPhone(), Constant.userOTP);

                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else if(priceType.equalsIgnoreCase("Volunteer"))
                {
                    System.out.println("Volunteer");
                    txtMinPrice="";
                    txtMaxPrice="";
                    try {
                        objSellerQuestionBE.setServiceCharge(charge);
                        objSellerQuestionBE.setMaxPrice(txtMaxPrice.trim());
                        objSellerQuestionBE.setMinPrice(txtMinPrice.trim());
                        objSellerQuestionBE.setServicesWhen(txtSellingDay);
                        objSellerQuestionBE.setServiceNotified(radioBtn1.getText().toString().trim());
                        int randomPIN = (int)(Math.random()*9000)+1000;
                        String PINString= String.valueOf(randomPIN);
                        Constant.userOTP=PINString;
                        System.out.println(Constant.userOTP);
                        sendOTP(objSellerQuestionBE.getEmail(), objSellerQuestionBE.getPhone(), Constant.userOTP);

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendMail() {
        // TODO Auto-generated method stub

        Mail m = new Mail("appslurewebsolution@gmail.com", "appslure1990");
        String toArr = objSellerQuestionBE.getEmail();
        m.setTo(toArr);
        m.setFrom("appslurewebsolution@gmail.com");
        m.setSubject("Wellcome to SellYourTime ");
        m.setBody("Hello " + Constant.userOTP);

        try {


            if (m.send()) {
                Toast.makeText(SellerQuestionFive.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(SellerQuestionFive.this, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            Log.e("MailApp", "Could not send email", e);
        }
    }



        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seller_question_five, menu);
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
    private void sendOTP(String email,String phone,String otp)
    {
        new OTPRun().execute(email,phone,otp);

    }

    class OTPRun extends AsyncTask<String,String,String>
    {
        SendOtpBL objSendOtpBL=new SendOtpBL();
        TransparentProgressDialog progressDialog=new TransparentProgressDialog(SellerQuestionFive.this,R.drawable.logo_single);
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result= objSendOtpBL.otpSend(params[0],params[1],params[2]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s.equals("1"))            {
                Intent intent=new Intent(getApplicationContext(),OTPActivity.class);
                // intent.putExtra("BuyerQuestionBE",objBuyerQuestionBE);
                intent.putExtra("SellerQuestionBE",objSellerQuestionBE);
                startActivity(intent);
            }

        }
    }

}
