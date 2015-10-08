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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.syt.balram.syt.BE.ManageCategoryBE;
import com.syt.balram.syt.BL.ManageCategoryBL;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ManageCategory extends ActionBarActivity {
    String categoryName,categoryMain;
    Spinner spnsubCategory,spnExp,spnAvailability,spnCharge;
    EditText etDescription,etPrice,etMinPrice;
    CheckBox cbClientSide,cbMyPlace,cbOnline;
    //ProgressDialog progressDialog;



    ManageCategoryBE objManageCategoryBE;
    ManageCategoryBL objManageCategoryBL;

    String txtSubCategory;
    TransparentProgressDialog pd;
    String emailID;

    Button btnUpdate,btnDelete;
    ImageButton btnEdit;

    String [] year={
            "1 year",
            "2 years",
            "3 years",
            "4 years",
            "5 years",
            "6 years",
            "7 years",
            "8 years",
            "9 years",
            "10 years",
            "11 years",
            "12 years",
            "13 years",
            "14 years",
            "15 years",
            "16 years",
            "17 years",
            "18 years",
            "19 years",
            "20+ years"

    };

    List dayOn=new ArrayList();

    List categories = new ArrayList();

    ImageButton back;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        spnsubCategory= (Spinner) findViewById(R.id.manage_subcategory);
        spnExp= (Spinner) findViewById(R.id.manage_experience);
        spnAvailability= (Spinner) findViewById(R.id.manage_availability);

        etDescription= (EditText) findViewById(R.id.manage_details);
        etPrice= (EditText) findViewById(R.id.manage_price);
        etMinPrice= (EditText) findViewById(R.id.manage_minprice);

        cbClientSide= (CheckBox) findViewById(R.id.manage_clientside);
        cbMyPlace= (CheckBox) findViewById(R.id.manage_myplace);
        cbOnline= (CheckBox) findViewById(R.id.manage_online);

        btnEdit= (ImageButton) findViewById(R.id.manage_btn_edit);
        btnUpdate= (Button) findViewById(R.id.manage_btn_update);
        btnDelete= (Button) findViewById(R.id.manage_btn_delete);
        spnCharge= (Spinner) findViewById(R.id.manage_charge_mode);
        back= (ImageButton) findViewById(R.id.manage_back);

        btnUpdate.setEnabled(false);

        spnsubCategory.setEnabled(false);
        spnExp.setEnabled(false);
        spnAvailability.setEnabled(false);
        etDescription.setEnabled(false);
        etPrice.setEnabled(false);
        cbClientSide.setEnabled(false);
        cbMyPlace.setEnabled(false);
        cbOnline.setEnabled(false);
        spnCharge.setEnabled(false);
        etMinPrice.setEnabled(false);

        dayOn.add("Monday-Friday");
        dayOn.add("Monday-Saturday");
        dayOn.add("On Weekends");
        dayOn.add("All 7 days");


        categories.add("Charge a fix price");
        categories.add("Charge per hour");
        categories.add("Charge per day");
        categories.add("Charge per project");


       // progressDialog=new ProgressDialog(ManageCategory.this);

        pd=new TransparentProgressDialog(this,R.drawable.logo_single);

        objManageCategoryBL=new ManageCategoryBL();
        objManageCategoryBE=new ManageCategoryBE();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emailID= Configuration.getSharedPrefrenceValue(ManageCategory.this, Constant.SHARED_PREFERENCE_UserID);

        Intent intent=getIntent();
        Bundle bb=intent.getExtras();
        categoryName=bb.get("CategoryName").toString();
        categoryMain=bb.get("CategoryMain").toString();
        String categoryName2=categoryName.replace(" ","%20");

        System.out.println("CategoryName" + categoryName);

        if(Configuration.isInternetConnection(ManageCategory.this)) {
            if(categoryMain.equals("yes"))
             new RunForUserData().execute(emailID, categoryName2);
            else
            {
                btnDelete.setVisibility(View.VISIBLE);
                new RunForOtherCategory().execute(emailID, categoryName2);
            }

        }
        else{

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    ManageCategory.this);

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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subcategory=spnsubCategory.getSelectedItem().toString();
                String exp=spnExp.getSelectedItem().toString();
                String expDetail=etDescription.getText().toString();

                String availability=spnAvailability.getSelectedItem().toString();
                String chargeMode=spnCharge.getSelectedItem().toString();
                String minPrice=etMinPrice.getText().toString();
                String maxPrice=etPrice.getText().toString();

                StringBuffer result = new StringBuffer();
                if(cbClientSide.isChecked())
                    result.append("I will travel to Client Site/");
                if(cbMyPlace.isChecked())
                    result.append("At My Place/");
                if (cbOnline.isChecked())
                    result.append("Online");

                if(Configuration.isInternetConnection(ManageCategory.this)) {
                    new RunForUpdateData().execute(emailID, subcategory, exp, expDetail, availability, chargeMode, minPrice, maxPrice, String.valueOf(result),categoryName);
                }
                else
                {
                   AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            ManageCategory.this);

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
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subCat=objManageCategoryBE.getSubcategory();
                subCat=subCat.replace(" ","%20");
                new DeleteCategory().execute(emailID,subCat);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnUpdate.setEnabled(true);
                spnsubCategory.setEnabled(true);
                spnAvailability.setEnabled(true);
                spnExp.setEnabled(true);
                etPrice.setEnabled(true);
                etDescription.setEnabled(true);
                spnCharge.setEnabled(true);
                cbClientSide.setEnabled(true);
                cbMyPlace.setEnabled(true);
                cbOnline.setEnabled(true);
                etMinPrice.setEnabled(true);
                btnEdit.setEnabled(false);
                System.out.println("Final Name:" + categoryName);
                if(categoryName.trim().equalsIgnoreCase("Trainers and Tutors"))
                {
                    System.out.println("Under Trainers and Tutors");
                    if(Configuration.isInternetConnection(ManageCategory.this)) {
                        new RunForSubTrainerTutor().execute();
                    }
                    else{

                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                ManageCategory.this);

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
                else if(categoryName.trim().equalsIgnoreCase("IT and Marketing")){
                    System.out.println("Under IT and Marketing");
                    if(Configuration.isInternetConnection(ManageCategory.this)) {
                        new RunForSubITMarketing().execute();
                    }
                    else{

                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                ManageCategory.this);

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
                else
                {
                    if(Configuration.isInternetConnection(ManageCategory.this)) {
                        System.out.println("Under Others");
                        new RunForSubcategory().execute();
                    }
                    else{

                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                ManageCategory.this);

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
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_category, menu);
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

           objManageCategoryBL.getManageCategoryData(params[0],params[1],objManageCategoryBE);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {

            etDescription.setText(objManageCategoryBE.getExpDetails());
            etMinPrice.setText(objManageCategoryBE.getMinPrice());
            etPrice.setText(objManageCategoryBE.getMaxPrice());

            String ss[]=new String[1];
            ss[0]=objManageCategoryBE.getSubcategory();

            String ss1[]=new String[1];
            ss1[0]=objManageCategoryBE.getExp();

            String ss2[]=new String[1];
            ss2[0]=objManageCategoryBE.getAvailability();

            String ss3[]=new String[1];
            ss3[0]=objManageCategoryBE.getChargeMode();


            String serviceMode[]=objManageCategoryBE.getServiceLocation().split("/");

            int i=serviceMode.length;

            System.out.println("Length: "+serviceMode.length);

            for(int j=0;j<i;j++)
            {
                System.out.println("sssssss"+serviceMode[j]);
                if(!(serviceMode[j].equals("undefined"))) {
                    if (serviceMode[j].equals("I will travel to Client Site")) {

                        cbClientSide.setChecked(true);

                    } else if (serviceMode[j].equals("At My Place")) {
                        cbMyPlace.setChecked(true);

                    } else if (serviceMode[j].equals("Online")) {
                        cbOnline.setChecked(true);

                    }
                }
            }


            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,ss);
            spnsubCategory.setAdapter(adapter);

            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,ss1);
            spnExp.setAdapter(adapter1);
            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,ss2);
            spnAvailability.setAdapter(adapter2);

            ArrayAdapter<String> adapter3=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,ss3);
            spnCharge.setAdapter(adapter3);

            pd.dismiss();

        }
    }
    private class RunForOtherCategory extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pd.show();


        }

        @Override
        protected String doInBackground(String... params) {

            objManageCategoryBL.getDataJsonOtherCategory(params[0],params[1],objManageCategoryBE);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {

            etDescription.setText(objManageCategoryBE.getExpDetails());
            etMinPrice.setText(objManageCategoryBE.getMinPrice());
            etPrice.setText(objManageCategoryBE.getMaxPrice());

            String ss[]=new String[1];
            ss[0]=objManageCategoryBE.getSubcategory();

            String ss1[]=new String[1];
            ss1[0]=objManageCategoryBE.getExp();

            String ss2[]=new String[1];
            ss2[0]=objManageCategoryBE.getAvailability();

            String ss3[]=new String[1];
            ss3[0]=objManageCategoryBE.getChargeMode();


            String serviceMode[]=objManageCategoryBE.getServiceLocation().split("/");

            int i=serviceMode.length;

            System.out.println("Length: "+serviceMode.length);

            for(int j=0;j<i;j++)
            {
                System.out.println("sssssss"+serviceMode[j]);
                if(!(serviceMode[j].equals("undefined"))) {
                    if (serviceMode[j].equals("I will travel to Client Site")) {

                        cbClientSide.setChecked(true);

                    } else if (serviceMode[j].equals("At My Place")) {
                        cbMyPlace.setChecked(true);

                    } else if (serviceMode[j].equals("Online")) {
                        cbOnline.setChecked(true);

                    }
                }
            }


            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,ss);
            spnsubCategory.setAdapter(adapter);

            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,ss1);
            spnExp.setAdapter(adapter1);
            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,ss2);
            spnAvailability.setAdapter(adapter2);

            ArrayAdapter<String> adapter3=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,ss3);
            spnCharge.setAdapter(adapter3);

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


            System.out.println(url);

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


            String txtSub=validate(result);
            System.out.println("Category Name" + categoryName);
            System.out.println("SUBCategory" + txtSub);
            JSONParser parser = new JSONParser();

            List homeUtility = new ArrayList();
            try {
                Object objJsonParserByIndex = parser.parse(txtSub);
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

            ArrayAdapter adapter = new ArrayAdapter(ManageCategory.this, R.layout.gender_spinner_item,homeUtility);
            spnsubCategory.setAdapter(adapter);

            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,year);
            spnExp.setAdapter(adapter1);
            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,dayOn);
            spnAvailability.setAdapter(adapter2);

            ArrayAdapter<String> adapter3=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,categories);
            spnCharge.setAdapter(adapter3);


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


            System.out.println(url);

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


            ArrayAdapter adapter = new ArrayAdapter(ManageCategory.this, R.layout.gender_spinner_item,trainer);
            spnsubCategory.setAdapter(adapter);

            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,year);
            spnExp.setAdapter(adapter1);
            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,dayOn);
            spnAvailability.setAdapter(adapter2);

            ArrayAdapter<String> adapter3=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,categories);
            spnCharge.setAdapter(adapter3);


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


            System.out.println(url);

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
            System.out.println("Result IT Marketing:   "+result);

            List trainer=validateTrainerTutor(result);


            ArrayAdapter adapter = new ArrayAdapter(ManageCategory.this, R.layout.gender_spinner_item,trainer);
            spnsubCategory.setAdapter(adapter);

            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,year);
            spnExp.setAdapter(adapter1);
            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,dayOn);
            spnAvailability.setAdapter(adapter2);

            ArrayAdapter<String> adapter3=new ArrayAdapter<String>(getApplicationContext(),R.layout.gender_spinner_item,categories);
            spnCharge.setAdapter(adapter3);


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

            System.out.println("xxx"+jsonArrayObject.size());
            for(int i=0;i<jsonArrayObject.size();i++)
            {
                //System.out.println("LOOOPPPPPPP"+categoryName);
                JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(i).toString());
                JSONObject jsonObjectByIndex =(JSONObject)jsonObject;
                txtSubCategory=jsonObjectByIndex.get(categoryName.trim()).toString();
                //System.out.println("xxx" +txtSubCategory);
            }

        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return txtSubCategory;

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
    private class RunForUpdateData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String result=objManageCategoryBL.updateData(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9]);

            /*String result= objEditProfileBL.updateData(params[0],params[1],params[2],params[3],params[4],params[5],params[6],objEditProfileBE);
            return result;*/

            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            pd.dismiss();

            MyApp.tracker().setScreenName("Manage Category Screen");
            MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                    .setCategory("UI")
                    .setAction("Click")
                    .setLabel("Submit")
                    .build());

            if(s.equals("1"))
            {
                Toast.makeText(getApplicationContext(),"Data Updated Successfully",Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }



        }
    }

   private class DeleteCategory extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            String url= Constant.WEBSERVICE_URL+Constant.WEBSERVICE_DeleteCategory+"email="+params[0]+"&subcategory="+params[1];

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
        protected void onPostExecute(String s) {


            Long status;
            String result="";

            JSONParser jsonP=new JSONParser();

            try {

                Object obj =jsonP.parse(s);

                JSONArray jsonArrayObject = (JSONArray) obj;

                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

                result=jsonObject.get("status").toString();



            } catch (Exception e) {



                e.getLocalizedMessage();
            }

            pd.dismiss();
            if(result.equals("1"))
            {
                Toast.makeText(getApplicationContext(),"You have successfilly delete selected category.",Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }
}
