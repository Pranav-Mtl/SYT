package com.syt.balram.syt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import com.syt.balram.syt.BL.SearchLeadsBL;
import com.syt.constant.Constant;
import com.syt.util.Configuration;


public class SearchLeads extends ActionBarActivity {

    String title,category,location;

    SearchLeadsAdapter objSearchLeadsAdapter;
    ProgressDialog progressDialog;
    ListView tvSearch;
    ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_leads);
        tvSearch= (ListView) findViewById(R.id.search_leads_tv);
        back= (ImageButton) findViewById(R.id.search_leads_back);
        progressDialog=new ProgressDialog(SearchLeads.this);
        Intent intent=getIntent();
        Bundle bb=intent.getExtras();
        String title=bb.get("Title").toString();
        String category=bb.get("Category").toString();
        String location=bb.get("Location").toString();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(Configuration.isInternetConnection(getApplicationContext()))
        {
            new LongRunningGetIO().execute(category,location);
        }


    }


    public static class SearchLeadsAdapter extends BaseAdapter {

        Context mContext;
        TextView txtName, txtSummary, txtReadMore;
        TextView txtPost;
        public static String[] txtItemList;
        public static String[] txtItemListSummary;
        String category,location;

        SearchLeadsBL objSearchLeadsBL;


        public SearchLeadsAdapter(Context context,String cat,String loc) {
            mContext = context;
            category=cat;
            location=loc;
            objSearchLeadsBL = new SearchLeadsBL();

            try {

                String gg = getBuyerJson(category,location);
            } catch (Exception e) {

            }


        }
        private String getBuyerJson(String category,String location)
        {
            String result=objSearchLeadsBL.getsellerData(category,location);

            return result;

        }

        @Override
        public int getCount() {
            return Constant.SearchNameArray.length;
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

            View gridView = null;
            //TextView tv,tv1;
            if (convertView != null) {

                gridView = convertView;

            } else {
                gridView = new View(mContext);
                gridView = infalInflater.inflate(R.layout.buyer_leads_rawlist, null);

            }
            txtName = (TextView) gridView.findViewById(R.id.buy_leads_name);
            txtSummary = (TextView) gridView.findViewById(R.id.buy_leads_summary);
            txtReadMore = (TextView) gridView.findViewById(R.id.buy_leads_read);
            txtPost = (TextView) gridView.findViewById(R.id.buy_leads_read);

            System.out.println("Name" + Constant.SearchNameArray[position]);
            System.out.println("Sunnary" + Constant.SearchCategoryArray[position]);


            txtName.setText(Constant.SearchNameArray[position]);
            txtSummary.setText("Posted a " + Constant.SearchSubCategoryArray[position] + " requirement at " + Constant.SearchZipArray[position] + " location.");

            txtPost.setText("View post");

            gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
            return gridView;
        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_leads, menu);
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
    private class LongRunningGetIO extends AsyncTask<String, String, String> {

        // ProgressDialog progress;

        @Override
        protected void onPreExecute() {

            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            objSearchLeadsAdapter=new SearchLeadsAdapter(getApplicationContext(),params[0],params[1]);

            return "";
        }

        @Override
        protected void onPostExecute(String result) {


            tvSearch.setAdapter(objSearchLeadsAdapter);
            progressDialog.dismiss();

            tvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getApplicationContext(),BuyerProfile.class);
                    intent.putExtra("ID", Constant.SearchIdArray[position]);
                    intent.putExtra("MyPost","allPost");
                    startActivity(intent);
                }
            });


        }
    }
}
