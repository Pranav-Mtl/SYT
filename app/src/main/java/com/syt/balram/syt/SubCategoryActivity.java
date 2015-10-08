package com.syt.balram.syt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.syt.balram.syt.BE.RequirementBE;
import com.syt.dialog.TransparentProgressDialog;


public class SubCategoryActivity extends ActionBarActivity {

    Context context;
    String SubChild;
    RequirementBE objRequirementBE;
    TextView tvTitle;
     SubCategoryAdapter objSubCategoryAdapter;
    TransparentProgressDialog pd;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category2);
        context=getApplicationContext();

        tvTitle= (TextView) findViewById(R.id.subcategoryTitlenew);
        back= (ImageButton) findViewById(R.id.subcategory2_back);

        SubChild=getIntent().getExtras().get("SubChild").toString();
        pd=new TransparentProgressDialog(SubCategoryActivity.this,R.drawable.logo_single);

        Intent intent1=getIntent();
        objRequirementBE= (RequirementBE) intent1.getSerializableExtra("RequirementBE");
        pd.show();

        final SubCategoryAdapter objSubCategoryAdapter = new SubCategoryAdapter(context,SubChild);
        ListView listview = (ListView) findViewById(R.id.subcategory_listview);

        tvTitle.setText(SubChild);
        listview.setAdapter(objSubCategoryAdapter);

        pd.dismiss();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                objSubCategoryAdapter.setSelection(position);

                objRequirementBE.setSubCategory(SubCategoryAdapter.txtItemList[position]);

                Intent intent = new Intent(SubCategoryActivity.this, RequirementTwo.class);
                intent.putExtra("RequirementBE", objRequirementBE);
                startActivity(intent);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub_category, menu);
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
