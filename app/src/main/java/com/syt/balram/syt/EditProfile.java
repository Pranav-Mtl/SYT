package com.syt.balram.syt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.syt.balram.syt.BE.EditProfileBE;
import com.syt.balram.syt.BL.EditProfileBL;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EditProfile extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ImageButton imgArrow;
    EditProfileBE objEditProfileBE;
    EditProfileBL objEditProfileBL;
    //ProgressDialog progressDialog;


    EditText etFirstName,etLastName,etAddress,etPhone;
    Button etDob;
    AutoCompleteTextView etZip;
    String emailID;
    ImageButton btnPersonal,back;
    Button btnUpdate;
    TextView tvcategoryMain,tvCategoryOne,tvcategoryTwo;
    ImageButton btnCategoryOne,btnCategoryTwo;
    LinearLayout llCategoryOne,llCategoryTwo;
    private TextView datePickerShowDialogButton;

    private static final String LOG_TAG = "SellYourTime";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    TransparentProgressDialog pd;


    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBugME8AtB66ogVSb0kZShmnlkSLwGusC4";

    ArrayList<String> adapterList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        imgArrow= (ImageButton) findViewById(R.id.edit_profile_arrow);
       // progressDialog=new ProgressDialog(EditProfile.this);
      pd=new TransparentProgressDialog(EditProfile.this,R.drawable.logo_single);
        back= (ImageButton) findViewById(R.id.edit_profile_back);
        llCategoryOne= (LinearLayout) findViewById(R.id.edit_Layout_category_one);
        llCategoryTwo= (LinearLayout) findViewById(R.id.edit_Layout_category_two);

        etFirstName= (EditText) findViewById(R.id.edit_firstname);
        etLastName= (EditText) findViewById(R.id.edit_lastname);
        //etAddress= (EditText) findViewById(R.id.edit_address);
        etZip= (AutoCompleteTextView) findViewById(R.id.edit_zip);
        etDob= (Button) findViewById(R.id.edit_dob);
        etPhone= (EditText) findViewById(R.id.edit_phone);
        btnPersonal= (ImageButton) findViewById(R.id.edit_personal_information);
        btnUpdate= (Button) findViewById(R.id.edit_update);
        tvcategoryMain= (TextView) findViewById(R.id.edit_categoryMain);
        tvCategoryOne= (TextView) findViewById(R.id.edit_category);
        tvcategoryTwo= (TextView) findViewById(R.id.edit_categoryTwo);
        btnCategoryOne= (ImageButton) findViewById(R.id.edit_profile_category);
        btnCategoryTwo= (ImageButton) findViewById(R.id.edit_profile_Two);

        btnUpdate.setClickable(false);
        btnUpdate.setEnabled(false);
        etFirstName.setEnabled(false);
        etLastName.setEnabled(false);
        //etAddress.setEnabled(false);
        etZip.setEnabled(false);
        etDob.setEnabled(false);
        etPhone.setEnabled(false);

        objEditProfileBE=new EditProfileBE();
        objEditProfileBL=new EditProfileBL();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName[]=tvcategoryMain.getText().toString().split("/");
                Intent intent=new Intent(EditProfile.this,ManageCategory.class);
                intent.putExtra("CategoryName",categoryName[0]);
                intent.putExtra("CategoryMain","yes");
                startActivity(intent);
            }
        });

        btnPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirstName.setEnabled(true);
                etLastName.setEnabled(true);
                //etAddress.setEnabled(true);
                etZip.setEnabled(true);
                etDob.setEnabled(true);
                etPhone.setEnabled(true);
                btnUpdate.setClickable(true);
                btnUpdate.setEnabled(true);

            }
        });
        emailID= Configuration.getSharedPrefrenceValue(EditProfile.this, Constant.SHARED_PREFERENCE_UserID);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname=etFirstName.getText().toString();
                String lname=etLastName.getText().toString();
                //String address=etAddress.getText().toString();
                String zip=etZip.getText().toString();
                String dob=etDob.getText().toString();
                String phone=etPhone.getText().toString();

                if(fname.trim().length()==0)
                {
                    etFirstName.setError("required");
                }
                else if(lname.trim().length()==0)
                {
                    etLastName.setError("required");
                }

                else if(zip.trim().length()==0)
                {
                    etZip.setError("required");
                }
                else if(dob.trim().length()==0)
                {
                    etDob.setError("required");
                }
                else if(phone.trim().length()==0)
                {
                    etPhone.setError("required");
                }
                else
                {
                   new RunForUpdateData().execute(emailID,fname,lname,zip,dob,phone);
                }
            }
        });


        if(Configuration.isInternetConnection(EditProfile.this)) {
            new RunForUserData().execute(emailID);
        }
        else
        {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    EditProfile.this);

// Setting Dialog Title
            alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

// Setting Dialog Message
            alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

// Setting Icon to Dialog


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
                        }
                    });

// Showing Alert Dialog
            alertDialog2.show();


        }


        btnCategoryOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName[]=tvCategoryOne.getText().toString().split("/");
                Intent intent=new Intent(EditProfile.this,ManageCategory.class);
                intent.putExtra("CategoryName",categoryName[0]);
                intent.putExtra("CategoryMain","no");
                startActivity(intent);

            }
        });

        btnCategoryTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName[]=tvcategoryTwo.getText().toString().split("/");
                Intent intent=new Intent(EditProfile.this,ManageCategory.class);
                intent.putExtra("CategoryName",categoryName[0]);
                intent.putExtra("CategoryMain","no");
                startActivity(intent);
            }
        });

        etZip.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.gender_spinner_item));

        //tvLocation.showDropDown();
        etZip.setOnItemClickListener(this);

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

    }
    public void showDatePicker() {
        // Initializiation
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        final AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(this);
        View customView = inflater.inflate(R.layout.custom_datepicker, null);
        dialogBuilder.setView(customView);
        final Calendar now = Calendar.getInstance();
        final DatePicker datePicker =
                (DatePicker) customView.findViewById(R.id.dialog_datepicker);
        final TextView dateTextView =
                (TextView) customView.findViewById(R.id.dialog_dateview);
        final SimpleDateFormat dateViewFormatter =
                new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        final SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        // Minimum date
        Calendar minDate = Calendar.getInstance();
        try {
            minDate.setTime(formatter.parse("1958-12-12"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePicker.setMinDate(minDate.getTimeInMillis());
        // View settings
        dialogBuilder.setTitle("Choose a date");
        dialogBuilder.setIcon(R.drawable.logo_watch);
        Calendar choosenDate = Calendar.getInstance();
        int year = choosenDate.get(Calendar.YEAR);
        int month = choosenDate.get(Calendar.MONTH);
        int day = choosenDate.get(Calendar.DAY_OF_MONTH);
        try {
             Date choosenDateFromUI = formatter.parse(
                    datePickerShowDialogButton.getText().toString()
            );
            choosenDate.setTime(choosenDateFromUI);
            year = choosenDate.get(Calendar.YEAR);
            month = choosenDate.get(Calendar.MONTH);
            day = choosenDate.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar dateToDisplay = Calendar.getInstance();
        dateToDisplay.set(year, month, day);
        dateTextView.setText(
                dateViewFormatter.format(dateToDisplay.getTime())
        );
        // Buttons
        dialogBuilder.setNegativeButton(
                "Go to today",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etDob.setText(
                                formatter.format(now.getTime())
                        );
                        dialog.dismiss();
                    }
                }
        );

        dialogBuilder.setPositiveButton(
                "Choose",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar choosen = Calendar.getInstance();
                        choosen.set(
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth()
                        );
                        etDob.setText(
                                dateViewFormatter.format(choosen.getTime())
                        );
                        dialog.dismiss();
                    }
                }
        );
        final AlertDialog dialog = dialogBuilder.create();
        // Initialize datepicker in dialog atepicker
        datePicker.init(
                year,
                month,
                day,
                new DatePicker.OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        Calendar choosenDate = Calendar.getInstance();
                        choosenDate.set(year, monthOfYear, dayOfMonth);
                        dateTextView.setText(
                                dateViewFormatter.format(choosenDate.getTime())
                        );
                        if (choosenDate.get(Calendar.DAY_OF_WEEK) ==
                                Calendar.SUNDAY ||
                                now.compareTo(choosenDate) < 0) {
                            dateTextView.setTextColor(
                                    Color.parseColor("#000000")
                            );
                            ((Button) dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE))
                                    .setEnabled(true);
                        } else {
                            dateTextView.setTextColor(
                                    Color.parseColor("#000000")
                            );
                            ((Button) dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE))
                                     .setEnabled(true);
                        }
                    }
                }
        );
        // Finish
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    }

    private class RunForUserData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pd.show();


        }

        @Override
        protected String doInBackground(String... params) {

            objEditProfileBL.getData(params[0], objEditProfileBE);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {


                etFirstName.setText(objEditProfileBE.getFirstName());
                etLastName.setText(objEditProfileBE.getLastname());
                //etAddress.setText(objEditProfileBE.getAddress());
                etZip.setText(objEditProfileBE.getZip());
                etPhone.setText(objEditProfileBE.getPhone());
                etDob.setText(objEditProfileBE.getDob());

                JSONParser jsonP = new JSONParser();

                System.out.println("Category Main" + objEditProfileBE.getCategoryMain());

                System.out.println("Category" + objEditProfileBE.getCategory());

                try {

                    Object obj = jsonP.parse(objEditProfileBE.getCategoryMain());

                    JSONObject jsonObjectByIndex = (JSONObject) obj;


                    System.out.println(jsonObjectByIndex.get("cate").toString());
                    tvcategoryMain.setText(jsonObjectByIndex.get("cate").toString() + " / " + jsonObjectByIndex.get("subcate").toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {

                    if (!objEditProfileBE.getCategory().equals(null)) {

                        try {
                            Object obj = jsonP.parse(objEditProfileBE.getCategory());
                            JSONArray jsonArrayObject = (JSONArray) obj;
                            System.out.println("size" + jsonArrayObject.size());
                            for (int i = 0; i < jsonArrayObject.size(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                                if (i == 0) {

                                    llCategoryOne.setVisibility(View.VISIBLE);
                                    tvCategoryOne.setVisibility(View.VISIBLE);
                                    tvCategoryOne.setText(jsonObject.get("cate").toString() + " / " + jsonObject.get("subcate").toString());
                                    btnCategoryOne.setVisibility(View.VISIBLE);

                                } else if (i == 1) {
                                    llCategoryTwo.setVisibility(View.VISIBLE);
                                    tvcategoryTwo.setVisibility(View.VISIBLE);
                                    tvcategoryTwo.setText(jsonObject.get("cate").toString() + " / " + jsonObject.get("subcate").toString());
                                    btnCategoryTwo.setVisibility(View.VISIBLE);
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }



            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }
            finally {
                pd.dismiss();
            }
        }

    }
    private class RunForUpdateData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pd.show();


        }

        @Override
        protected String doInBackground(String... params) {

           String result= objEditProfileBL.updateData(params[0],params[1],params[2],params[3],params[4],params[5],objEditProfileBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            pd.dismiss();

            try {
                if (s.equals("1")) {
                    MyApp.tracker().setScreenName("EditProfile Screen");
                    MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                            .setCategory("UI")
                            .setAction("Click")
                            .setLabel("Submit")
                            .build());
                    Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                }
            } catch (NullPointerException e)
            {
                e.printStackTrace();
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

            String url =Constant.WEBSERVICE_URL+Constant.WEBSERVICE_Location+"zip=" + params[0];

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
            etZip.setAdapter(adapter);
            etZip.showDropDown();
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
