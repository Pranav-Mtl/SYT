package com.syt.balram.syt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.syt.balram.syt.BL.MyPostsBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;


public class MyPosts extends ActionBarActivity {

    ListView lvPosts;
    MyPostsAdapter objMyPostsAdapter;
    String emailID;
    static String ss;
    ImageButton back;
    TransparentProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        lvPosts= (ListView) findViewById(R.id.my_posts);
        back= (ImageButton) findViewById(R.id.my_post_back);
        pd=new TransparentProgressDialog(MyPosts.this,R.drawable.logo_single);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emailID= Configuration.getSharedPrefrenceValue(MyPosts.this,Constant.SHARED_PREFERENCE_UserID);

        if(emailID!=null) {
            try {
                new LongRunningGetIO().execute(emailID);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    private static class MyPostsAdapter extends BaseAdapter {

        Context mContext;
        TextView txtName, txtSummary, txtDate;

        String emailID;

        MyPostsBL objMyPostsBL=new MyPostsBL();

        public MyPostsAdapter(Context context,String email) {
            mContext = context;
            emailID=email;


            try {

                ss = getBuyerJson(emailID);

            } catch (Exception e) {

            }


        }
        private String getBuyerJson(String email)
        {
            String result=objMyPostsBL.getsellerData(email);

            return result;

        }


        @Override
        public int getCount() {
            return Constant.myPostId.length;
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
                gridView = infalInflater.inflate(R.layout.my_post_raw_list, null);

            }
            txtName = (TextView) gridView.findViewById(R.id.my_post_title);
            txtSummary = (TextView) gridView.findViewById(R.id.my_post_category);
            txtDate= (TextView) gridView.findViewById(R.id.my_post_date);


            txtName.setText("Title: "+Constant.myPostTitle[position]);
            txtSummary.setText("Category: "+Constant.myPostCategory[position] + " / " + Constant.myPostSubCategory[position]);
            txtDate.setText("Posted on: "+Constant.myPostDate[position]);


            gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
            return gridView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_posts, menu);
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


        pd.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            //objBuyerFragmentAdapter=new BuyerFragmentAdapter(getActivity());

            objMyPostsAdapter=new MyPostsAdapter(getApplicationContext(),params[0]);

            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            //progressDialog.dismiss();
            pd.dismiss();
            if(ss.equals("NO"))
            {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        MyPosts.this);

// Setting Dialog Title
                alertDialog2.setTitle("No Post Found");

// Setting Dialog Message
                alertDialog2.setMessage("You haven't posted any requirement. \n Please post your 1st requirement");

// Setting Icon to Dialog


// Setting Positive "Yes" Btn
                alertDialog2.setPositiveButton("Post Requirement",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                startActivity(new Intent(MyPosts.this, RequirementOne.class));
                            }
                        });
// Setting Negative "NO" Btn
                alertDialog2.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                finish();
                            }
                        });
//
// Showing Alert Dialog
                alertDialog2.show();

            }
            else {

                lvPosts.setAdapter(objMyPostsAdapter);
            }

            lvPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getApplicationContext(),BuyerProfile.class);
                    intent.putExtra("ID", Constant.myPostId[position]);
                    intent.putExtra("MyPost","userPost");
                    startActivity(intent);
                }
            });


        }
    }

    @Override
    public void onBackPressed() {
      finish();
    }
}
