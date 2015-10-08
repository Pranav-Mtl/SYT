package com.syt.balram.syt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.syt.balram.syt.BE.SellerQuestionBE;
import com.syt.constant.Constant;

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


public class SellerQuestionFour extends ActionBarActivity {

    EditText phone,adress;
    AutoCompleteTextView zip;
    Spinner spinnerCountry;
    Button next;
    SellerQuestionBE objSellerQuestionBE;
    ArrayList<String> adapterList = new ArrayList<String>();
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_question_four);
        back= (ImageButton) findViewById(R.id.seller_question_four_back);
        phone= (EditText) findViewById(R.id.seller_phone);
        adress= (EditText) findViewById(R.id.seller_address);
        zip= (AutoCompleteTextView) findViewById(R.id.seller_zip);
        spinnerCountry= (Spinner) findViewById(R.id.seller_country);

        next= (Button) findViewById(R.id.seller_btn4);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        objSellerQuestionBE=(SellerQuestionBE)intent.getSerializableExtra("SellerQuestionBE");


        List categories = new ArrayList();
        categories.add("Select");
        categories.add("India");
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.gender_spinner_item,categories);
        spinnerCountry.setAdapter(adapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtPhone=phone.getText().toString();
                String txtAddress=adress.getText().toString();
                String txtZip=zip.getText().toString();

                String txtCountry=spinnerCountry.getSelectedItem().toString();

                    if(txtPhone.trim().length()==0)
                    {
                        phone.setError("required");
                    }
                    else if(txtPhone.trim().length()!=10){
                        phone.setError("Invalid number");
                    }
                else if(txtAddress.trim().length()==0)
                    {
                        adress.setError("required");
                    }
                else if(txtZip.trim().length()==0)
                    {
                        zip.setError("required");
                    }

                else
                    {

                        objSellerQuestionBE.setPhone(txtPhone.trim());
                        objSellerQuestionBE.setAddress(txtAddress);
                        objSellerQuestionBE.setZip(txtZip.trim());
                        objSellerQuestionBE.setCountry(txtCountry.trim());

                        Intent intent=new Intent(getApplicationContext(),SellerQuestionFive.class);
                        intent.putExtra("SellerQuestionBE",objSellerQuestionBE);
                        startActivity(intent);
                    }
            }
        });

        zip.addTextChangedListener(new TextWatcher() {

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
            zip.setAdapter(adapter);
            zip.showDropDown();
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
        getMenuInflater().inflate(R.menu.menu_seller_question_four, menu);
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
