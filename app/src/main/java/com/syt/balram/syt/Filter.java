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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.efor18.rangeseekbar.RangeSeekBar;
import com.syt.balram.syt.BE.FilterBE;
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
import java.util.List;


public class Filter extends ActionBarActivity implements AdapterView.OnItemClickListener {

    Spinner spinneAvailability;
    Spinner subCategory;
    String txtSubCategory;
    String title;
    ProgressDialog progressDialog;
    Button btnFilted;
    FilterBE objFilterBE;
    EditText etMinPrice,etMaxPrice;
    TextView etMinExp,etMaxExp;
    AlertDialog alertDialog;
    AutoCompleteTextView etZip;
    ImageButton back;
    TransparentProgressDialog pd;

    ArrayList<String> adapterList = new ArrayList<String>();
    private static final String LOG_TAG = "SellYourTime";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBugME8AtB66ogVSb0kZShmnlkSLwGusC4";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        overridePendingTransition(R.animator.anim_bottom, R.animator.anim_top);
        spinneAvailability= (Spinner) findViewById(R.id.filter_availability);
        subCategory= (Spinner) findViewById(R.id.filter_subcategory);
        etMinExp= (TextView) findViewById(R.id.minValue);
        etMaxExp= (TextView) findViewById(R.id.maxValue);
        etMinPrice= (EditText) findViewById(R.id.filter_minprice);
        etMaxPrice= (EditText) findViewById(R.id.filter_maxprice);
        etZip= (AutoCompleteTextView) findViewById(R.id.filter_zip);
        alertDialog = new AlertDialog.Builder(Filter.this).create();
        btnFilted= (Button) findViewById(R.id.filter_apply);
        back= (ImageButton) findViewById(R.id.filter_cross);
        pd=new TransparentProgressDialog(Filter.this,R.drawable.logo_single);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etZip.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.gender_spinner_item));

        //tvLocation.showDropDown();
        etZip.setOnItemClickListener(this);




        final Intent intent=getIntent();
        Bundle bb=intent.getExtras();
        title=bb.get("Title").toString();

        objFilterBE= (FilterBE) intent.getSerializableExtra("FilterBE");

        if(title.equals("Events and Entertainment"))
        {
            title="Events & Entertainment";
        }


        btnFilted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txSubcategory=subCategory.getSelectedItem().toString();
                String txtMinExp=etMinExp.getText().toString();
                String txtMaxExp=etMaxExp.getText().toString();
                String txtMinPrice=etMinPrice.getText().toString();
                String txtMaxPrice=etMaxPrice.getText().toString();
                String txtAvailability=spinneAvailability.getSelectedItem().toString();
                String txtZip=etZip.getText().toString();
                System.out.println("txtZip"+txtZip);

                System.out.println(txSubcategory);
                objFilterBE.setSubCategory(txSubcategory);
                objFilterBE.setMinExperience(txtMinExp);
                objFilterBE.setMaxExperience(txtMaxExp);
                objFilterBE.setMinPrice(txtMinPrice);
                objFilterBE.setMaxPrice(txtMaxPrice);
                objFilterBE.setAvailability(txtAvailability);
                objFilterBE.setLocation(txtZip);

                MyApp.tracker().setScreenName("Filter Screen");
                MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                        .setCategory("UI")
                        .setAction("Click")
                        .setLabel("Filter Button")
                        .build());



                intent.putExtra("FilterBE",objFilterBE);
                setResult(RESULT_OK,intent);
                finish();

            }
        });
        progressDialog=new ProgressDialog(Filter.this);

        System.out.println(title);

        List categories = new ArrayList();
        categories.add("Monday-Friday");
        categories.add("Monday-Saturday");
        categories.add("On Weekends");
        categories.add("All 7 days");

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.gender_spinner_item,categories);
        spinneAvailability.setAdapter(adapter);

        final TextView min = (TextView) findViewById(R.id.minValue);
        final TextView max = (TextView) findViewById(R.id.maxValue);





        // create RangeSeekBar as Integer range between 20 and 75
        RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0,20, this);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values


                min.setText(minValue.toString());
                max.setText(maxValue.toString());

            }
        });

        // add RangeSeekBar to pre-defined layout
        ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
        layout.addView(seekBar);


        if(title.equals("Trainers and Tutors")){
            if(Configuration.isInternetConnection(Filter.this)) {
                new RunForSubTrainerTutor().execute();
            }
            else{

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        Filter.this);

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

                                dialog.cancel();
                                finish();
                            }
                        });

// Showing Alert Dialog
                alertDialog2.show();


            }
        }
        else if(title.equals("IT and Marketing")){
            if(Configuration.isInternetConnection(Filter.this)) {
                new RunForSubITMarketing().execute();
            }
            else{

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        Filter.this);

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

                                dialog.cancel();
                            }
                        });

// Showing Alert Dialog
                alertDialog2.show();


            }
        }
        else
        {
            if(Configuration.isInternetConnection(Filter.this)) {

                new RunForSubcategory().execute();
            }
            else{

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        Filter.this);

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
        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter, menu);
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
       // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    private class RunForSubcategory extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute ( )
        {
          pd.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            String url= Constant.WEBSERVICE_URL+Constant.WEBSERVICE_CategoriesList;

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
        protected void onPostExecute (String result)
        {    //set adapter here
            System.out.println("Result"+result);

            validate(result);
            JSONParser parser = new JSONParser();

            List homeUtility = new ArrayList();
            try {
                Object objJsonParserByIndex = parser.parse(txtSubCategory);
                JSONObject jsonObjectByIndex = (JSONObject) objJsonParserByIndex;

                int j =1 + jsonObjectByIndex.size();

                for (int i = 1; i < j; i++) {
                    System.out.println("aksmmsmsms" + jsonObjectByIndex.get("id_pk" + i));

                    homeUtility.add(String.valueOf(jsonObjectByIndex.get("id_pk" + i)));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            ArrayAdapter adapter = new ArrayAdapter(Filter.this, R.layout.gender_spinner_item,homeUtility);
            subCategory.setAdapter(adapter);

            pd.dismiss();



        }





    }

    private class RunForSubTrainerTutor extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute ( )
        {
           pd.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            String url= Constant.WEBSERVICE_URL+Constant.WEBSERVICE_SubTrainerTutorList;

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
        protected void onPostExecute (String result)
        {    //set adapter here
            System.out.println("Result Trainer tutor :   "+result);

            List trainer=validateTrainerTutor(result);


            ArrayAdapter adapter = new ArrayAdapter(Filter.this, R.layout.gender_spinner_item,trainer);
            subCategory.setAdapter(adapter);

           pd.dismiss();

        }

    }

    private class RunForSubITMarketing extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute ( )
        {
           pd.show();


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            String url= Constant.WEBSERVICE_URL+Constant.WEBSERVICE_SubITMarketing;

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
        protected void onPostExecute (String result)
        {    //set adapter here
            System.out.println("Result Trainer tutor :   "+result);

            List trainer=validateTrainerTutor(result);


            ArrayAdapter adapter = new ArrayAdapter(Filter.this, R.layout.gender_spinner_item,trainer);
            subCategory.setAdapter(adapter);

           pd.dismiss();

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



    public String validate(String strValue)
    {
        System.out.println("Complete json"+strValue);
        Long status;
        String result=null;

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);


            JSONArray jsonArrayObject = (JSONArray) obj;


            System.out.println("Filter Size+" + jsonArrayObject.size());
            for(int i=0;i<jsonArrayObject.size();i++)
            {
                System.out.println("hkppkc"+title);
                JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(i).toString());
                JSONObject jsonObjectByIndex =(JSONObject)jsonObject;
                txtSubCategory=jsonObjectByIndex.get(title).toString();
           }

        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return result;

    }

    public List validateTrainerTutor(String strValue)
    {
        System.out.println("Complete json"+strValue);
        Long status;
        String result=null;
        List TrainerTutor=new ArrayList();
        TrainerTutor.add("Select");

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);


            JSONArray jsonArrayObject = (JSONArray) obj;



            for(int i=0;i<jsonArrayObject.size();i++)
            {
                JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(i).toString());
                JSONObject jsonObjectByIndex =(JSONObject)jsonObject;

                System.out.println(jsonObjectByIndex.get("category").toString());
                TrainerTutor.add(jsonObjectByIndex.get("category").toString());

            }

        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return TrainerTutor;

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
