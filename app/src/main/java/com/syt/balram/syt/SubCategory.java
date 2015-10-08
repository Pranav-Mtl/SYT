package com.syt.balram.syt;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


public class SubCategory extends ActionBarActivity {

    SubCategoryAapterClass objSubCategoryAdapter;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        back= (ImageButton) findViewById(R.id.category_back);
        ListView tvSunCategory= (ListView) findViewById(R.id.subcategory_listview);

        objSubCategoryAdapter=new SubCategoryAapterClass(SubCategory.this);

        tvSunCategory.setAdapter(objSubCategoryAdapter);

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

    public class SubCategoryAapterClass extends BaseAdapter
    {
        Context context;

        public SubCategoryAapterClass(Context mContext)
        {
            context=mContext;
        }

        @Override
        public int getCount() {
            return 10;
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
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);

            View gridView=null;
            //TextView tv,tv1;

            TextView tvName,tvCount;
            if (convertView != null){

                gridView=convertView;

            }else{
                gridView = new View(context);
                gridView= infalInflater.inflate(R.layout.sub_category_list_row, null);

            }


            tvName= (TextView) gridView.findViewById(R.id.subcategory_title);
            tvCount= (TextView) gridView.findViewById(R.id.subcategory_count);
            tvName.setText("dddd");
            tvCount.setText("446");


            gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,80));
            return gridView;
        }
    }
}
