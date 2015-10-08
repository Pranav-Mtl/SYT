package com.syt.balram.syt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.syt.balram.syt.BE.SellerQuestionBE;

import java.util.ArrayList;
import java.util.List;


public class SellerQuestionThree extends ActionBarActivity {
    Spinner spinnerExperience;

    RadioGroup groupIam;
    CheckBox client,myplace,online;
    private RadioButton radioBtn1;
    EditText etDescription,etDistance;

    SellerQuestionBE objSellerQuestionBE;

    Button next;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_question_three);

        spinnerExperience= (Spinner) findViewById(R.id.spinnerExperience);
        back= (ImageButton) findViewById(R.id.seller_question_three_back);
        // groupIam= (RadioGroup) findViewById(R.id.radio_iam);
        next= (Button) findViewById(R.id.seller_btn3);
        etDescription= (EditText) findViewById(R.id.seller_description);
        client= (CheckBox) findViewById(R.id.checkbox_clientside);
        myplace= (CheckBox) findViewById(R.id.checkbox_myplace);
        online= (CheckBox) findViewById(R.id.checkbox_online);
        etDistance= (EditText) findViewById(R.id.seller_distance);
        client.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked)
                {
                        etDistance.setVisibility(View.VISIBLE);
                }
                else
                {
                    etDistance.setVisibility(View.INVISIBLE);
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent=getIntent();

        objSellerQuestionBE=(SellerQuestionBE)intent.getSerializableExtra("SellerQuestionBE");

        List categories = new ArrayList();
        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        categories.add("6");
        categories.add("7");
        categories.add("8");
        categories.add("9");
        categories.add("10");
        categories.add("11");
        categories.add("12");
        categories.add("13");
        categories.add("14");
        categories.add("15");
        categories.add("16");
        categories.add("17");
        categories.add("18");
        categories.add("19");
        categories.add("20+");


        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.gender_spinner_item,categories);
        spinnerExperience.setAdapter(adapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String exp=spinnerExperience.getSelectedItem().toString();
                String descrip=etDescription.getText().toString();

                descrip = descrip.substring(0, 1).toUpperCase() + descrip.substring(1);



                // Toast.makeText(getApplicationContext(),radioBtn1.getText().toString(),Toast.LENGTH_LONG).show();

                StringBuffer result = new StringBuffer();
                if(client.isChecked())
                result.append("I will travel to Client Site/");
                if(myplace.isChecked())
                result.append("At My Place/");
                if (online.isChecked())
                result.append("Online");

                //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

                if(client.isChecked())
                {

                    String distance=etDistance.getText().toString();
                    if(distance.trim().length()==0)
                    {
                        etDistance.setError("required");
                    }
                    else if(descrip.trim().length()==0)
                    {
                        etDescription.setError("required");
                    }
                    else {
                        String travel=String.valueOf(result);

                        // objSellerQuestionBE.setServicesWhom(radioBtn1.getText().toString().trim());
                        objSellerQuestionBE.setDistance(distance);
                        objSellerQuestionBE.setExperience(exp.trim());
                        objSellerQuestionBE.setDescription(descrip);
                        objSellerQuestionBE.setServicesWhere(travel);
                        Intent intent=new Intent(getApplicationContext(),SellerQuestionFive.class);
                        intent.putExtra("SellerQuestionBE",objSellerQuestionBE);
                        startActivity(intent);
                    }

                }
                else {

                    if(descrip.trim().length()==0)
                    {
                        etDescription.setError("required");
                    }
                    else {
                        String travel = String.valueOf(result);

                        // objSellerQuestionBE.setServicesWhom(radioBtn1.getText().toString().trim());
                        objSellerQuestionBE.setDistance("0");
                        objSellerQuestionBE.setExperience(exp.trim());
                        objSellerQuestionBE.setDescription(descrip);
                        objSellerQuestionBE.setServicesWhere(travel);
                        Intent intent = new Intent(getApplicationContext(), SellerQuestionFive.class);
                        intent.putExtra("SellerQuestionBE", objSellerQuestionBE);
                        startActivity(intent);
                    }
                }


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seller_question_three, menu);
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
