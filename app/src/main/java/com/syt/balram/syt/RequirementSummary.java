package com.syt.balram.syt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.syt.balram.syt.BE.RequirementBE;
import com.syt.balram.syt.BL.RequirementBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;


public class RequirementSummary extends ActionBarActivity {

    EditText etSubcategory,etExp,etdays,etLocation,etMinPrice,etMaxPrice,etTitle,etSummary,etFixPrice;
    Button btnSummary,btnNotNow;

    RequirementBE objRequirementBE;
    ProgressDialog progressDialog;
    RequirementBL objRequirementBL;
    String titleSummary;
    AlertDialog alertDialog;
    TransparentProgressDialog pd;
    ImageButton back;
    LinearLayout llout;
    String priceType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_summary);
        overridePendingTransition(R.animator.anim_in, R.animator.anim_out);
        back= (ImageButton) findViewById(R.id.summary_back);
        btnNotNow= (Button) findViewById(R.id.summary_not_now);
        progressDialog=new ProgressDialog(RequirementSummary.this,R.style.MyDialogTheme);
        pd=new TransparentProgressDialog(RequirementSummary.this,R.drawable.logo_single);
        Intent intent=getIntent();
        objRequirementBE= (RequirementBE) intent.getSerializableExtra("RequirementBE");
        objRequirementBL=new RequirementBL();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llout= (LinearLayout) findViewById(R.id.summary_price_layout);
        alertDialog = new AlertDialog.Builder(RequirementSummary.this,R.style.MyDialogTheme).create();
        etFixPrice= (EditText) findViewById(R.id.summary_fixPrice);
        etSubcategory= (EditText) findViewById(R.id.summary_subcategory);
        etExp= (EditText) findViewById(R.id.summary_exp);
        etdays= (EditText) findViewById(R.id.summary_days);
        etLocation= (EditText) findViewById(R.id.summary_location);
        btnSummary= (Button) findViewById(R.id.summary_btn);
        etMaxPrice= (EditText) findViewById(R.id.summary_maxprice);
        etMinPrice= (EditText) findViewById(R.id.summary_minprice);
        etTitle= (EditText) findViewById(R.id.summary_title);
        etSummary= (EditText) findViewById(R.id.summary_desciption);

        etSubcategory.setText(objRequirementBE.getSubCategory());
        etExp.setText(objRequirementBE.getExperience());
        etdays.setText(objRequirementBE.getServiceDays());
        etLocation.setText(objRequirementBE.getServiceLocation());

        if(objRequirementBE.getServiceCharge().equalsIgnoreCase("Pay a fixed price"))
        {
            etFixPrice.setVisibility(View.VISIBLE);
            llout.setVisibility(View.GONE);
            priceType="Fixed";
        }
        else {
            etFixPrice.setVisibility(View.GONE);
            llout.setVisibility(View.VISIBLE);
            priceType="Not Fixed";
        }

        titleSummary="Hi, I am looking for "+objRequirementBE.getSubCategory()+", required on "+objRequirementBE.getServiceDays()+". Experience required is "+objRequirementBE.getExperience()+".";
        etSummary.setText(titleSummary);
        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subCategory = etSubcategory.getText().toString();
                String exp = etExp.getText().toString();
                String days = etdays.getText().toString();
                String location = etLocation.getText().toString();
                String maxPrice = etMaxPrice.getText().toString();
                String minPrice = etMinPrice.getText().toString();
                String title = etTitle.getText().toString();

                if (subCategory.trim().length() == 0) {

                } else if (exp.length() == 0) {

                } else if (days.length() == 0) {

                } else if (location.length() == 0) {

                } else if (title.length() == 0) {
                    etTitle.setError("required");
                } else {
                    if (priceType.equals("Fixed")) {

                        if (etFixPrice.getText().toString().length() == 0) {
                            etFixPrice.setError("required");
                        } else {

                            minPrice = etFixPrice.getText().toString();
                            maxPrice = "";
                            objRequirementBE.setSubCategory(subCategory);
                            objRequirementBE.setExperience(exp);
                            objRequirementBE.setServiceDays(days);
                            objRequirementBE.setServiceLocation(location);
                            objRequirementBE.setMaxPrice(maxPrice);
                            objRequirementBE.setMinPrice(minPrice);
                            objRequirementBE.setTitle(title);
                            objRequirementBE.setDesc(titleSummary);

                            if (Configuration.isInternetConnection(RequirementSummary.this)) {
                                new LongRunningGetIO().execute();
                            } else {

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
                    } else {
                        int valueMin = Integer.valueOf(minPrice);
                        int valueMax = Integer.valueOf(maxPrice);
                        if (minPrice.length() == 0 && maxPrice.length() == 0) {
                            etMaxPrice.setError("required");
                            etMinPrice.setError("required");
                        } else if (valueMin > valueMax) {
                            etMaxPrice.setError("should be greater than min price");
                        } else {
                            objRequirementBE.setSubCategory(subCategory);
                            objRequirementBE.setExperience(exp);
                            objRequirementBE.setServiceDays(days);
                            objRequirementBE.setServiceLocation(location);
                            objRequirementBE.setMaxPrice(maxPrice);
                            objRequirementBE.setMinPrice(minPrice);
                            objRequirementBE.setTitle(title);
                            objRequirementBE.setDesc(titleSummary);

                            if (Configuration.isInternetConnection(RequirementSummary.this)) {
                                new LongRunningGetIO().execute();
                            } else {

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
                    }

                }

            }
        });

        btnNotNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(),HomeScreen.class);
                startActivity(intent1);
            }
        });
    }

    private class LongRunningGetIO extends AsyncTask<String, String, String> {

        // ProgressDialog progress;

        @Override
        protected void onPreExecute ( )
        {
           pd.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

           String result=objRequirementBL.registerRequirement(objRequirementBE,getApplicationContext());

            return result;

        }

        @Override
        protected void onPostExecute (String result)
        {    //set adapter here
            // progress.dismiss();
            //userName.setText(result);
           pd.dismiss();
            if(result.equals("y")) {
                try {
                    MyApp.tracker().setScreenName("New Post");
                    MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                            .setCategory("UI")
                            .setAction("Click")
                            .setLabel("Submit")
                            .build());

                   /* Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
                    hashtable.put("EmailID", objRequirementBE.getEmail());
                    hashtable.put("Category", objRequirementBE.getCategory());
                    hashtable.put("SubCategory", objRequirementBE.getSubCategory());
                    hashtable.put("Location", objRequirementBE.getServiceLocation());
                    hashtable.put("Availability", objRequirementBE.getServiceDays());
                    AffleInAppTracker.inAppTrackerViewName(getApplicationContext(), "Post Requirement", "New Requirement successfully posted by user", "Click", hashtable);*/
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(),"Your Requirement has been successfully posted",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                startActivity(intent);
            }
            else
            {

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
        getMenuInflater().inflate(R.menu.menu_requirement_summary, menu);
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
