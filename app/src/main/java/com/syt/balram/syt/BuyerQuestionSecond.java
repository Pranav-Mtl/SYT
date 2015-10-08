package com.syt.balram.syt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.syt.balram.syt.BE.BuyerQuestionBE;
import com.syt.balram.syt.BL.BuyerQuestionBL;
import com.syt.constant.Constant;
import com.syt.util.Configuration;
import com.syt.util.Mail;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class BuyerQuestionSecond extends ActionBarActivity {

    EditText txtPhone,txtAddress;
    AutoCompleteTextView txtZip;
    Spinner txtCountry;
    Button btnGo;
    String emailID;
    ProgressDialog progressDialog;
    BuyerQuestionBL objBuyerQuestionBL;
    BuyerQuestionBE objBuyerQuestionBE;

    ArrayList<String> adapterList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_question_second);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        txtPhone= (EditText) findViewById(R.id.buyer_phone);
        txtAddress= (EditText) findViewById(R.id.buyer_address);
        txtZip= (AutoCompleteTextView) findViewById(R.id.buyer_zip);
        txtCountry= (Spinner) findViewById(R.id.buyer_country);
        btnGo= (Button) findViewById(R.id.buyer_go);

        progressDialog=new ProgressDialog(BuyerQuestionSecond.this,R.style.MyDialogTheme);

        List categories = new ArrayList();
        categories.add("Select");
        categories.add("India");

        emailID= Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_UserID);

        LinearLayout obj_ll_et2=(LinearLayout)findViewById(R.id.Buyer2HeaderMain);  //keyboard hiding
        obj_ll_et2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });

        txtZip.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>3 && s.length()<=6)
                {
                    // Toast.makeText(getApplicationContext(),"char:"+s,Toast.LENGTH_LONG).show();
                    new RunForGetLocation().execute(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.gender_spinner_item,categories);
        txtCountry.setAdapter(adapter);
             
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        objBuyerQuestionBL=new BuyerQuestionBL();

       objBuyerQuestionBE=(BuyerQuestionBE)intent.getSerializableExtra("BuyerQuestionBE");

       System.out.println("cjfnvngv"+objBuyerQuestionBE.getFirstname());


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone=txtPhone.getText().toString();
                String address=txtAddress.getText().toString();
                String zip=txtZip.getText().toString();
                String country=txtCountry.getSelectedItem().toString();

                if(phone.trim().length()==0)
                {
                    txtPhone.setError("required");
                }
                else if(phone.trim().length()!=10){

                    txtPhone.setError("Invalid number");
                }
                else if(address.trim().length()==0){

                    txtAddress.setError("required");
                }
                else if(zip.trim().length()==0){

                    txtZip.setError("required");
                }
                else if(country.equalsIgnoreCase("select")){


                }
                else
                {
                    objBuyerQuestionBE.setPhoneno(phone);
                    objBuyerQuestionBE.setAddress(address);
                    objBuyerQuestionBE.setZip(zip);
                    objBuyerQuestionBE.setCountry(country);


                    int randomPIN = (int)(Math.random()*9000)+1000;
                    String PINString= String.valueOf(randomPIN);
                    Constant.userOTP=PINString;

                    System.out.println("Con" + Constant.userOTP);
                    sendMail();

                    Intent intent=new Intent(getApplicationContext(),OTPBuyerActivity.class);
                    intent.putExtra("BuyerQuestionBE",objBuyerQuestionBE);
                    startActivity(intent);

                       //new BuyerRegister().execute();
                   // String result=objBuyerQuestionBL.insertBuyer(objBuyerQuestionBE,BuyerQuestionSecond.this);




                }
            }
        });



    }

    private class RunForGetLocation extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

           /* progressDialog.show();
            progressDialog.setMessage("Loading");*/
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            String url = Constant.WEBSERVICE_URL+Constant.WEBSERVICE_Location+"zip=" + params[0];

            HttpGet httpGet = new HttpGet(url);

            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);


                HttpEntity entity = response.getEntity();


                text = getASCIIContentFromEntity(entity);


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }


            return text;

        }

        @Override
        protected void onPostExecute(String result) {    //set adapter here

            validateLocation(result);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.gender_spinner_item, adapterList);
            txtZip.setAdapter(adapter);
            txtZip.showDropDown();
            //progressDialog.dismiss();
        }
    }
    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();


        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n>0) {
            byte[] b = new byte[8192];
            n =  in.read(b);
            if (n>0) out.append(new String(b, 0, n));
        }


        return out.toString();
    }

    public String validateLocation(String strValue) {
        System.out.println("Complete json" + strValue);
        Long status;
        String result = null;

        JSONParser jsonP = new JSONParser();

        try {

            Object obj = jsonP.parse(strValue);


            JSONArray jsonArrayObject = (JSONArray) obj;

            //System.out.println("SIZEEEEE"+jsonArrayObject.size());

            for (int i = 0; i < jsonArrayObject.size(); i++) {

                JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(i).toString());
                JSONObject jsonObjectByIndex =(JSONObject)jsonObject;
                adapterList.add(jsonObjectByIndex.get("location").toString());
            }
        } catch (Exception e) {
        }
        return "";
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buyer_question_second, menu);
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

   /* @Override
    public void onBackPressed() {

        //nothing happen
    }*/

    private void sendMail() {
        // TODO Auto-generated method stub

        Mail m = new Mail("appslurewebsolution@gmail.com", "appslure1990");
        String toArr =emailID;
        m.setTo(toArr);
        m.setFrom("appslurewebsolution@gmail.com");
        m.setSubject("Wellcome to SellYourTime ");
        m.setBody("Hello "+objBuyerQuestionBE.getFirstname()+""+objBuyerQuestionBE.getLastname()+"\n"+"Your OTP is : "+Constant.userOTP);

        try {


            if(m.send()) {
                Toast.makeText(BuyerQuestionSecond.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(BuyerQuestionSecond.this, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            Log.e("MailApp", "Could not send email", e);
        }

    }



}
