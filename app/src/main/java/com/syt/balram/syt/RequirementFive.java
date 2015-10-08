package com.syt.balram.syt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.syt.balram.syt.BE.RequirementBE;


public class RequirementFive extends ActionBarActivity {

    ListView tvCharge;
    ImageButton back;

    RequirementFiveAdapter objRequirementFiveAdapter;

    RequirementBE objRequirementBE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_five);

        overridePendingTransition(R.animator.anim_in,R.animator.anim_out);
        back= (ImageButton) findViewById(R.id.requirement_five_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent=getIntent();
        objRequirementBE= (RequirementBE) intent.getSerializableExtra("RequirementBE");
        tvCharge= (ListView) findViewById(R.id.requirement_five_listview);
        objRequirementFiveAdapter=new RequirementFiveAdapter(getApplicationContext());


        tvCharge.setAdapter(objRequirementFiveAdapter);

        tvCharge.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                objRequirementBE.setServiceCharge(RequirementFiveAdapter.txtItemList[position]);

                Intent intent=new Intent(RequirementFive.this, RequirementSix.class);
                intent.putExtra("RequirementBE",objRequirementBE);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_requirement_five, menu);
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

    public static class RequirementFiveAdapter extends BaseAdapter {

        Context mContext;
        TextView txtItem;
        public static String[] txtItemList;

        public RequirementFiveAdapter(Context context)
        {
            mContext=context;

            txtItemList=new String[4];
            txtItemList[0]="Pay a fixed price";
            txtItemList[1]="Pay per project";
            txtItemList[2]="Pay per hour";
            txtItemList[3]="Pay per day";


        }
        @Override
        public int getCount() {
            return txtItemList.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

            View gridView=null;
            //TextView tv,tv1;
            if (convertView != null){

                gridView=convertView;

            }else{
                gridView = new View(mContext);
                gridView= infalInflater.inflate(R.layout.requirement_raw_list, null);

            }
            txtItem = (TextView) gridView .findViewById(R.id.requirement_list_text);

            txtItem.setText(txtItemList[position]);

            gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,80));
            return gridView;
        }
    }
}
