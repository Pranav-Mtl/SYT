package com.syt.balram.syt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.syt.balram.syt.BE.RequirementBE;


public class RequirementSix extends ActionBarActivity {
    RadioGroup rg;
    RequirementBE objRequirementBE;
    RadioButton rb;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_six);
        overridePendingTransition(R.animator.anim_in, R.animator.anim_out);
        back= (ImageButton) findViewById(R.id.requirement_six_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent=getIntent();
        objRequirementBE= (RequirementBE) intent.getSerializableExtra("RequirementBE");

        rg= (RadioGroup) findViewById(R.id.reqsix_group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton
                rb = (RadioButton) findViewById(checkedId);
                objRequirementBE.setServiceContacted(rb.getText().toString());

                Intent intent1=new Intent(getApplicationContext(),RequirementSummary.class);
                intent1.putExtra("RequirementBE",objRequirementBE);
                startActivity(intent1);
                System.out.println(objRequirementBE.getCategory());
                System.out.println(objRequirementBE.getSubCategory());
                System.out.println(objRequirementBE.getServiceDays());
                System.out.println(objRequirementBE.getExperience());
                System.out.println(objRequirementBE.getServiceLocation());
                System.out.println(objRequirementBE.getServiceContacted());
                System.out.println(objRequirementBE.getServiceCharge());


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_requirement_six, menu);
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
