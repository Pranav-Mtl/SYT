package com.syt.balram.syt;

import android.content.Intent;
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
import android.widget.Spinner;

import com.syt.balram.syt.BE.RequirementBE;


public class RequirementThree extends ActionBarActivity {

    Spinner spnr;
    Button button1;
    EditText etDistance;
    String [] year={
            "Select",
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

    RequirementBE objRequirementBE;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_three);
        overridePendingTransition(R.animator.anim_in, R.animator.anim_out);

        spnr = (Spinner)findViewById(R.id.req_three_spinner);
        back= (ImageButton) findViewById(R.id.requirement_three_back);
        etDistance= (EditText) findViewById(R.id.seller_distance);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent=getIntent();
        objRequirementBE= (RequirementBE) intent.getSerializableExtra("RequirementBE");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.gender_spinner_item,year);
        spnr.setAdapter(adapter);

        spnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if(position!=0) {

                        objRequirementBE.setExperience(spnr.getSelectedItem().toString());
                        Intent intent = new Intent(getApplicationContext(), RequirementFour.class);
                        intent.putExtra("RequirementBE",objRequirementBE);
                        startActivity(intent);
                    }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_requirement_three, menu);
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
