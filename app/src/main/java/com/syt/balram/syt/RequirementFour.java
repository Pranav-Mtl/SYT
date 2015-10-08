package com.syt.balram.syt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;

import com.syt.balram.syt.BE.RequirementBE;


public class RequirementFour extends ActionBarActivity {

    CheckBox cb1,cb2,cb3;
    RequirementBE objRequirementBE;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_four);
        overridePendingTransition(R.animator.anim_in, R.animator.anim_out);
        back= (ImageButton) findViewById(R.id.requirement_four_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent=getIntent();
        objRequirementBE= (RequirementBE) intent.getSerializableExtra("RequirementBE");
        cb1= (CheckBox) findViewById(R.id.requirementfour_firstCheckbox);
        cb2= (CheckBox) findViewById(R.id.requirementfour_secondCheckbox);
        cb3= (CheckBox) findViewById(R.id.requirementfour_thirdCheckbox);

        cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked)
                {
                    objRequirementBE.setServiceLocation("At service provider");
                    Intent intent=new Intent(getApplicationContext(),RequirementFive.class);
                    intent.putExtra("RequirementBE",objRequirementBE);
                    startActivity(intent);
                }
                else
                {

                }

            }
        });

        cb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                                           @Override
                                           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {


                                               if(isChecked)
                                               {
                                                   objRequirementBE.setServiceLocation("At my place");
                                                   Intent intent=new Intent(getApplicationContext(),RequirementFive.class);
                                                   intent.putExtra("RequirementBE",objRequirementBE);
                                                   startActivity(intent);
                                               }
                                               else
                                               {

                                               }

                                           }
                                       }
        );
        cb3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                                           @Override
                                           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {


                                               if(isChecked)
                                               {
                                                   objRequirementBE.setServiceLocation("Online");
                                                   Intent intent=new Intent(getApplicationContext(),RequirementFive.class);
                                                   intent.putExtra("RequirementBE",objRequirementBE);
                                                   startActivity(intent);
                                               }
                                               else
                                               {

                                               }

                                           }
                                       }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_requirement_four, menu);
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
