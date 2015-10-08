package com.syt.balram.syt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import com.syt.balram.syt.BE.AddServicesBE;
import com.syt.balram.syt.BL.AddServicesBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;

import java.util.ArrayList;
import java.util.List;


public class AddServicesThree extends ActionBarActivity {

    Spinner sellerCharge, sellerOn;
    LinearLayout lout1, lout2;
    EditText fixedprice, minprice, maxprice;
    RadioGroup groupNotified;
    private Button next;
    RadioButton radioBtn1;
    String emailID;
    AddServicesBE objAddServicesBE;
    TransparentProgressDialog pd;
    ImageButton back;
    String priceType;
    AddServicesBL objAddServicesBL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services_three);

        sellerCharge = (Spinner) findViewById(R.id.addSeller_charge);
        sellerOn = (Spinner) findViewById(R.id.spinneraddSeller_offerOn);
        fixedprice = (EditText) findViewById(R.id.addseller_fixedprice);
        minprice = (EditText) findViewById(R.id.addseller_minprice);
        maxprice = (EditText) findViewById(R.id.addseller_maxprice);
        next = (Button) findViewById(R.id.addseller_btn);
        back= (ImageButton) findViewById(R.id.addseller_question_five_back);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        objAddServicesBE = (AddServicesBE) intent.getSerializableExtra("AddServicesBE");
        objAddServicesBL = new AddServicesBL();

        groupNotified = (RadioGroup) findViewById(R.id.addradio_notified);

        emailID = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.SHARED_PREFERENCE_UserID);

        List categories = new ArrayList();

        if(objAddServicesBE.getCategory().equalsIgnoreCase("Social Causes"))
        {
            categories.add("Volunteer/Not Charge");
        }


        categories.add("Charge per hour");
        categories.add("Charge a fix price");
        categories.add("Charge per day");
        categories.add("Charge per project");

        priceType="Max Min";


        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.gender_spinner_item, categories);
        sellerCharge.setAdapter(adapter);

        lout1 = (LinearLayout) findViewById(R.id.addseller_priceone);
        lout2 = (LinearLayout) findViewById(R.id.addseller_priceTwo);

        List dayOn = new ArrayList();
        dayOn.add("Monday-Friday");
        dayOn.add("Monday-Saturday");
        dayOn.add("On Weekends");
        dayOn.add("All 7 days");

        ArrayAdapter adapter1 = new ArrayAdapter(this, R.layout.gender_spinner_item, dayOn);
        sellerOn.setAdapter(adapter1);


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
                String charge = sellerCharge.getSelectedItem().toString();
                String txtMinPrice = minprice.getText().toString();
                String txtMaxPrice = maxprice.getText().toString();
                String txtFixedPrice = fixedprice.getText().toString();
                String txtSellingDay = sellerOn.getSelectedItem().toString();
                int selected = groupNotified.getCheckedRadioButtonId();
                radioBtn1 = (RadioButton) findViewById(selected);

                if(priceType.equalsIgnoreCase("Max Min")) {
                    System.out.println("MAX MIN");


                    if (txtMinPrice.length() == 0) {

                        minprice.setError("required");

                    } else if (txtMaxPrice.length() == 0) {
                        maxprice.setError("required");
                    }

                    else {
                        int minInt=Integer.valueOf(txtMinPrice);
                        int maxInt=Integer.valueOf(txtMaxPrice);
                         if(minInt>maxInt)
                        {
                            maxprice.setError("must be greater than min price");
                        }
                        else {
                             objAddServicesBE.setServiceCharge(charge);
                             objAddServicesBE.setMaxPrice(txtMaxPrice.trim());
                             objAddServicesBE.setMinPrice(txtMinPrice.trim());
                             objAddServicesBE.setServiceDays(txtSellingDay);
                             objAddServicesBE.setServiceNotified(radioBtn1.getText().toString().trim());


                             new InsertAddService().execute(emailID);
                         }

                    }
                }
                else if(priceType.equalsIgnoreCase("Fix Price")) {
                    System.out.println("Fix Price");
                    if (txtFixedPrice.length() == 0) {
                        fixedprice.setError("required");
                    } else {
                        txtMinPrice = txtFixedPrice;
                        txtMaxPrice = "";
                        objAddServicesBE.setServiceCharge(charge);
                        objAddServicesBE.setMaxPrice(txtMaxPrice.trim());
                        objAddServicesBE.setMinPrice(txtMinPrice.trim());
                        objAddServicesBE.setServiceDays(txtSellingDay);
                        objAddServicesBE.setServiceNotified(radioBtn1.getText().toString().trim());


                        new InsertAddService().execute(emailID);
                    }
                }
                else if(priceType.equalsIgnoreCase("Volunteer")) {
                    System.out.println("Volunteer");
                    txtMinPrice = "";
                    txtMaxPrice = "";
                    objAddServicesBE.setServiceCharge(charge);
                    objAddServicesBE.setMaxPrice(txtMaxPrice.trim());
                    objAddServicesBE.setMinPrice(txtMinPrice.trim());
                    objAddServicesBE.setServiceDays(txtSellingDay);
                    objAddServicesBE.setServiceNotified(radioBtn1.getText().toString().trim());


                    new InsertAddService().execute(emailID);


                }


               /* if (txtMinPrice.trim().length() == 0 || txtMaxPrice.trim().length() == 0) {
                    minprice.setError("required");
                    maxprice.setError("required");
                } else {

                        objAddServicesBE.setServiceCharge(charge);
                        objAddServicesBE.setMaxPrice(txtMaxPrice.trim());
                        objAddServicesBE.setMinPrice(txtMinPrice.trim());
                        objAddServicesBE.setServiceDays(txtSellingDay);
                        objAddServicesBE.setServiceNotified(radioBtn1.getText().toString().trim());


                        new InsertAddService().execute(emailID);


                }*/


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_services_three, menu);
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

    class InsertAddService extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String status=objAddServicesBL.insertService(objAddServicesBE, params[0]);
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("1"))
            {
                MyApp.tracker().setScreenName("Add Service Screen");
                MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                        .setCategory("UI")
                        .setAction("Click")
                        .setLabel("Submit")
                        .build());

                Toast.makeText(getApplicationContext(),"New service have been added",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), UserProfile.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
            else if(s.equals("2"))
            {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        AddServicesThree.this);

// Setting Dialog Title
                alertDialog2.setTitle("You already registered into MAX categories");

// Setting Dialog Message
                alertDialog2.setMessage("SYT allows you to register and sell your services in 3 categories.");

// Setting Icon to Dialog


// Setting Positive "Yes" Btn
                alertDialog2.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                startActivity(new Intent(getApplicationContext(), UserProfile.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        });
// Setting Negative "NO" Btn

// Showing Alert Dialog
                alertDialog2.show();


            }

            else if(s.equals("3"))
            {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        AddServicesThree.this);

// Setting Dialog Title
                alertDialog2.setTitle("You already added in this subcategory");

// Setting Dialog Message
                alertDialog2.setMessage("You can register into this subcategory only once.");

// Setting Icon to Dialog

                alertDialog2.setIcon(R.drawable.no_internet);
// Setting Positive "Yes" Btn
                alertDialog2.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                startActivity(new Intent(getApplicationContext(), UserProfile.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        });
// Setting Negative "NO" Btn


// Showing Alert Dialog
                alertDialog2.show();
            }
            else
            {

            }

        }
    }
}