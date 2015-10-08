package com.syt.balram.syt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Toast;

import com.syt.balram.syt.BE.UserProfileBE;
import com.syt.balram.syt.BL.ContactBL;
import com.syt.balram.syt.BL.ContactBuyerBL;
import com.syt.balram.syt.BL.UserProfileBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class ContactBuyer extends ActionBarActivity implements AdapterView.OnItemClickListener{

    AutoCompleteTextView tvLocation;

    ArrayList<String> adapterList = new ArrayList<String>();

    String userEmail;

    UserProfileBE objUserProfileBE;
    UserProfileBL objUserProfileBL;

    EditText etName,etEmail,etPhone,etDetail;

    Button btnSend;

    ContactBL objContactBL;

    ProgressDialog progressDialog;
    String id;

    ContactBuyerBL objContactBuyerBL;
    ImageButton back;
    TransparentProgressDialog pd;

    private static final String LOG_TAG = "SellYourTime";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBugME8AtB66ogVSb0kZShmnlkSLwGusC4";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_buyer);

        tvLocation= (AutoCompleteTextView) findViewById(R.id.contact_buyer_location);
        etName= (EditText) findViewById(R.id.contact_buyer_name);
        etEmail= (EditText) findViewById(R.id.contact_buyer_email);
        etPhone= (EditText) findViewById(R.id.contact_buyer_phone);
        etDetail= (EditText) findViewById(R.id.contact_buyer_detail);
        back= (ImageButton) findViewById(R.id.contact_buyer_back);

        btnSend= (Button) findViewById(R.id.contact_buyer_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        objContactBL=new ContactBL();

        Intent intent=getIntent();
        Bundle bb=intent.getExtras();
        id=bb.get("ID").toString();

        progressDialog=new ProgressDialog(ContactBuyer.this);
        pd=new TransparentProgressDialog(ContactBuyer.this,R.drawable.logo_single);

        userEmail= Configuration.getSharedPrefrenceValue(ContactBuyer.this, Constant.SHARED_PREFERENCE_UserID);

        System.out.println("USEREMAIL" + userEmail);

        tvLocation.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.gender_spinner_item));

        //tvLocation.showDropDown();
        tvLocation.setOnItemClickListener(this);


        objUserProfileBL=new UserProfileBL();
        objUserProfileBE=new UserProfileBE();
        objContactBuyerBL=new ContactBuyerBL();

        try {

            if (!(userEmail==null)){
                if (Configuration.isInternetConnection(ContactBuyer.this)) {
                    try {
                        new RunForUserData().execute(userEmail);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            ContactBuyer.this);

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
            } else {

            }
        }
        catch (Exception e)
        {

        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String location = tvLocation.getText().toString();
                String descrip = etDetail.getText().toString();


                if (name.trim().length() == 0) {
                    etName.setError("required");
                } else if (email.length() == 0) {
                    etEmail.setError("required");
                } else if (!Configuration.isEmailValid(email)) {
                    etEmail.setError("Invalid Email-ID");
                } else if (phone.length() == 0) {
                    etPhone.setError("required");
                } else if (phone.length() != 10) {
                    etPhone.setError("Invalid Phone Number");
                } else if (location.length() == 0) {
                    tvLocation.setError("required");
                } else if (descrip.length() == 0) {
                    etDetail.setError("required");
                } else {
                    new RunForInsertData().execute(id, name, email, phone, location, descrip);
                }


            }
        });




    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
       // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private class RunForInsertData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

           pd.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String result=objContactBuyerBL.insertData(params[0],params[1],params[2],params[3],params[4],params[5]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("Contact Buyer SSsssss---->"+s);
            pd.dismiss();
            try
            {
                if(s.equals("1"))
                {
                    MyApp.tracker().setScreenName("Contact Buyer");
                    MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                            .setCategory("UI")
                            .setAction("Click")
                            .setLabel("Submit")
                            .build());

                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            ContactBuyer.this);

// Setting Dialog Title


// Setting Dialog Message
                    alertDialog2.setMessage("Your contact details have been shared with the buyer.");

// Setting Icon to Dialog


// Setting Positive "Yes" Btn
                    alertDialog2.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    finish();
                                }
                            });
// Setting Negative "NO" Btn

// Showing Alert Dialog
                    alertDialog2.show();
                }
            }
            catch (NullPointerException e)
            {

            }


        }

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
            tvLocation.setAdapter(adapter);
            tvLocation.showDropDown();
            //progressDialog.dismiss();
        }
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
        }        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_buyer, menu);
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

    private class RunForUserData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

           pd.show();

        }

        @Override
        protected String doInBackground(String... params) {

            objUserProfileBL.getData(params[0],objUserProfileBE);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                etName.setText(objUserProfileBE.getFirstName() + " " + objUserProfileBE.getLastName());
                etEmail.setText(userEmail);
                etPhone.setText(objUserProfileBE.getPhone());
                tvLocation.setText(objUserProfileBE.getZip());

            }
            catch (Exception e)
            {


            }
            finally {
                pd.dismiss();
            }
        }

    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=geocode");
            sb.append("&sensor=false");
            sb.append("&language=en");
            sb.append("components=country:IN");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);

            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "mmmmmmmm", e);
            return resultList;
        }

        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            org.json.JSONObject jsonObj = new org.json.JSONObject(jsonResults.toString());
            org.json.JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public android.widget.Filter getFilter() {
            android.widget.Filter filter = new android.widget.Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    android.widget.Filter.FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        System.out.println(resultList);

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, android.widget.Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

}
