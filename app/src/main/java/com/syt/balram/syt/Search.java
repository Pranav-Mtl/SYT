package com.syt.balram.syt;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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


public class Search extends ActionBarActivity implements OnItemClickListener {

    Spinner spnLooking,spnCategory,spnSubCategory;

    AutoCompleteTextView tvLocation;

    ArrayList<String> adapterList = new ArrayList<String>();

    Button btnSearch;
    private static final String LOG_TAG = "SellYourTime";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBugME8AtB66ogVSb0kZShmnlkSLwGusC4";

    ImageButton back;
    LinearLayout llSearch;
    String title;
    TransparentProgressDialog pd;
    String txtSubCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        back= (ImageButton) findViewById(R.id.search_back);
       // spnLooking= (Spinner) findViewById(R.id.search_looking_spn);

        spnCategory= (Spinner) findViewById(R.id.search_category_spn);
        spnSubCategory= (Spinner) findViewById(R.id.search_sub_category_spn);

        tvLocation= (AutoCompleteTextView) findViewById(R.id.search_location);
        llSearch= (LinearLayout) findViewById(R.id.ll_search);
        pd=new TransparentProgressDialog(Search.this,R.drawable.logo_single);

        btnSearch= (Button) findViewById(R.id.search_btn);

        ArrayList listDataHeader=new ArrayList();

        listDataHeader.add("Trainers and Tutors");
        listDataHeader.add("Home and Utility");
        listDataHeader.add("Business Consultants");
        listDataHeader.add("Beauty and Wellness");
        listDataHeader.add("IT and Marketing");
        listDataHeader.add("Events and Entertainment");
        listDataHeader.add("Fashion and Lifestyle");
        listDataHeader.add("Social Causes");
        listDataHeader.add("Others");

        String [] selectOption={"Sell a service","Buy a service"};

        /*ArrayAdapter adapterOption=new ArrayAdapter(getApplicationContext(),R.layout.gender_spinner_item,selectOption);

        spnLooking.setAdapter(adapterOption);
*/
        ArrayAdapter adapterCategory=new ArrayAdapter(getApplicationContext(),R.layout.gender_spinner_item,listDataHeader);

        spnCategory.setAdapter(adapterCategory);

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                llSearch.setVisibility(View.VISIBLE);

                if (position != -1) {

                    title=spnCategory.getSelectedItem().toString();
                    if(title.equals("Events and Entertainment"))
                    {
                        title="Events & Entertainment";
                    }
                    if(title.equals("Trainers and Tutors")) {
                        if (Configuration.isInternetConnection(Search.this)) {
                            new RunForSubTrainerTutor().execute();
                        }
                    }
                    else if(title.equals("IT and Marketing")) {
                        if (Configuration.isInternetConnection(Search.this)) {
                            new RunForSubITMarketing().execute();
                        }
                    }
                    else {
                        if (Configuration.isInternetConnection(Search.this)) {

                            new RunForSubcategory().execute();
                        }
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        tvLocation.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.gender_spinner_item));

        //tvLocation.showDropDown();
        tvLocation.setOnItemClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



/*
        tvLocation.addTextChangedListener(new TextWatcher() {

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
*/

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String title=spnLooking.getSelectedItem().toString();
                String category=spnCategory.getSelectedItem().toString();
                String Subcategory=spnSubCategory.getSelectedItem().toString();
                String location=tvLocation.getText().toString();

                MyApp.tracker().setScreenName("Search Screen");
                MyApp.tracker().send(new HitBuilders.EventBuilder("UI", "OPEN")
                        .setCategory("UI")
                        .setAction("Click")
                        .setLabel("Search Button")
                        .build());


                    Configuration.setSharedPrefrenceValue(Search.this, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_SearchTitle, "Search");
                    Intent intent=new Intent(getApplicationContext(),CategoryTrainerTutor.class);
                    intent.putExtra("Title",title);
                    intent.putExtra("Category",category);
                    intent.putExtra("Location",location);
                    intent.putExtra("SubCategory",Subcategory);
                    startActivity(intent);




            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
       // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
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

            adapterList.clear();
           // validateLocation(result);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.gender_spinner_item, adapterList);
            tvLocation.setAdapter(adapter);
            tvLocation.showDropDown();
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

   /* public String validateLocation(String strValue) {
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
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

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
        public Filter getFilter() {
            Filter filter = new Filter() {
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


            ArrayAdapter adapter = new ArrayAdapter(Search.this, R.layout.gender_spinner_item,trainer);
            spnSubCategory.setAdapter(adapter);

            pd.dismiss();

        }

    }

    public List validateTrainerTutor(String strValue)
    {
        System.out.println("Complete json"+strValue);
        Long status;
        String result=null;
        List TrainerTutor=new ArrayList();


        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);


            org.json.simple.JSONArray jsonArrayObject = (org.json.simple.JSONArray) obj;



            for(int i=0;i<jsonArrayObject.size();i++)
            {
                org.json.simple.JSONObject jsonObject=(org.json.simple.JSONObject)jsonP.parse(jsonArrayObject.get(i).toString());
                org.json.simple.JSONObject jsonObjectByIndex =(org.json.simple.JSONObject)jsonObject;

                System.out.println(jsonObjectByIndex.get("category").toString());
                TrainerTutor.add(jsonObjectByIndex.get("category").toString());

            }

        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return TrainerTutor;

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


            ArrayAdapter adapter = new ArrayAdapter(Search.this, R.layout.gender_spinner_item,trainer);
            spnSubCategory.setAdapter(adapter);

            pd.dismiss();

        }

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
                org.json.simple.JSONObject jsonObjectByIndex = (org.json.simple.JSONObject) objJsonParserByIndex;

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

            ArrayAdapter adapter = new ArrayAdapter(Search.this, R.layout.gender_spinner_item,homeUtility);
            spnSubCategory.setAdapter(adapter);

            pd.dismiss();



        }





    }

    public String validate(String strValue)
    {
        System.out.println("Complete json"+strValue);
        Long status;
        String result=null;

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);


            org.json.simple.JSONArray jsonArrayObject = (org.json.simple.JSONArray) obj;


            System.out.println("Filter Size+" + jsonArrayObject.size());
            for(int i=0;i<jsonArrayObject.size();i++)
            {
                System.out.println("hkppkc"+title);
                org.json.simple.JSONObject jsonObject=(org.json.simple.JSONObject)jsonP.parse(jsonArrayObject.get(i).toString());
                org.json.simple.JSONObject jsonObjectByIndex =(org.json.simple.JSONObject)jsonObject;
                txtSubCategory=jsonObjectByIndex.get(title).toString();
            }

        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return result;

    }



}
