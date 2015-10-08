package com.syt.balram.syt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.syt.balram.syt.BE.UserProfileBE;
import com.syt.balram.syt.BL.UserProfileBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;
import com.loopj.android.image.SmartImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;


public class UserProfile extends AppCompatActivity {

    UserProfileBE objUserProfileBE;
    UserProfileBL objUserProfileBL;
    String emailID;
    TextView tvAddress,tvEmail,tvPhone,tvDob,tvDetail,tvSkills;
    EditText tvName;
    SmartImageView imgProfile;
    ListView lvLeads;
    UserProfileAdapter objUserProfileAdapter;
    ActionBar actionBar;
    //ProgressDialog progressDialog;

    TransparentProgressDialog pd;
    String shareMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setIcon(R.drawable.login_logo);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#074286")));
        pd=new TransparentProgressDialog(UserProfile.this,R.drawable.logo_single);
        //progressDialog=new ProgressDialog(UserProfile.this);
        objUserProfileBE=new UserProfileBE();
        objUserProfileBL=new UserProfileBL();

        String id=Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.profileId);
        String url=Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.appURL);

        MyApp.tracker().setScreenName("User Profile");
        MyApp.tracker().send(new HitBuilders.EventBuilder("UI", "Open")
                .setCategory("UI")
                .setAction("Open")
                .setLabel("Open")
                .build());

        shareMessage="Hi!\n" +
                "How are you?\n" +
                "Follow my profile on SellYourTime -An app to find qualified business leads and trusted service providers.\n" +
                "https://www.sellyourtime.in/categories/profile.php?id="+id+"\n" +
                "Follow my profile and look for amazing discounts and deals on my services.\n" +
                "\n" +
                "Download SellYourTime-it makes life easier.\n" +
                url+"\n";

        emailID= Configuration.getSharedPrefrenceValue(UserProfile.this, Constant.SHARED_PREFERENCE_UserID);
        System.out.println("loggedIN"+emailID);
        lvLeads= (ListView) findViewById(R.id.user_listview);
        //setListViewHeightBasedOnChildren(lvLeads);
        lvLeads.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        tvName= (EditText) findViewById(R.id.user_name);
        tvAddress= (TextView) findViewById(R.id.user_address);
        tvEmail= (TextView) findViewById(R.id.user_email);
        tvPhone= (TextView) findViewById(R.id.user_phone);
        tvDob= (TextView) findViewById(R.id.user_dob);
        tvDetail= (TextView) findViewById(R.id.user_details);
        tvSkills= (TextView) findViewById(R.id.user_skills);
        try {
            imgProfile = (SmartImageView) findViewById(R.id.user_image);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        tvName.setEnabled(false);
        tvName.requestFocus();

        if(Configuration.isInternetConnection(UserProfile.this)) {
            new RunForUserData().execute(emailID);
        }
        else
        {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    UserProfile.this);

// Setting Dialog Title
            alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

// Setting Dialog Message
            alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

// Setting Icon to Dialog
           alertDialog2.setIcon(R.drawable.no_internet);

// Setting Positive "Yes" Btn
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                             startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
// Setting Negative "NO" Btn
            alertDialog2.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            dialog.cancel();
                            finish();
                        }
                    });

// Showing Alert Dialog
            alertDialog2.show();



        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.accounSetting) {

            Intent intent=new Intent(UserProfile.this,UserSetting.class);
            startActivity(intent);
            return true;
        }
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }
        if (id == R.id.editProfile){
            Intent intent=new Intent(UserProfile.this,EditProfile.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.addServices){
            Intent intent=new Intent(UserProfile.this,AddServicesOne.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.publishAds){
            Intent intent=new Intent(UserProfile.this,FollowerList.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.buildFollowers){
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share your experience"));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private class RunForUserData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {

            objUserProfileBL.getData(params[0],objUserProfileBE);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {

            tvName.setText(objUserProfileBE.getFirstName()+" "+objUserProfileBE.getLastName());
            tvAddress.setText(objUserProfileBE.getAddress());
            tvDob.setText(objUserProfileBE.getDob());
            tvDetail.setText("Description:  "+objUserProfileBE.getDetails());
            tvSkills.setText(objUserProfileBE.getSubCategory());
            actionBar.setTitle(objUserProfileBE.getFirstName()+" "+objUserProfileBE.getLastName());

            //int loader = R.drawable.default_avatar_man;
            //ImageLoader imgLoader = new ImageLoader(UserProfile.this);
            String image_url =objUserProfileBE.getImage();
            image_url=image_url.replace("\\","");

            String checkFB=image_url.substring(7,12);
            System.out.println("User Profile CHECKFB------->" + checkFB);

            if(checkFB.equalsIgnoreCase("graph"))
            {
                System.out.println("User Profile FB WALI------->");
                try {
                    System.out.println("User Profile FB WALI PIC------->"+image_url);
                   /*URL imageURL = new URL(profilePic);
                    InputStream inputStream = (InputStream) imageURL.getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    System.out.println("PROFILE BITMAP"+bitmap);*/

                    HttpGet httpRequest = new HttpGet(URI.create(image_url) );
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
                    HttpEntity entity = response.getEntity();
                    BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                    Bitmap bmap = BitmapFactory.decodeStream(bufHttpEntity.getContent());
                    httpRequest.abort();
                    System.out.println("PROFILE BITMAP" + bmap);
                    imgProfile.setImageUrl(bmap.toString());

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else {

                imgProfile.setImageUrl(image_url);
                //imgLoader.DisplayImage(image_url, loader, imgProfile);
            }

            objUserProfileAdapter=new UserProfileAdapter(getApplicationContext(),objUserProfileBE);

            lvLeads.setAdapter(objUserProfileAdapter);

            //tvName.setFocusable(true);


            pd.dismiss();

            lvLeads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getApplicationContext(),BuyerProfile.class);
                    intent.putExtra("ID", UserProfileAdapter.txtItemID[position]);
                    startActivity(intent);
                }
            });

        }
    }

    public static class UserProfileAdapter extends BaseAdapter {

        Context mContext;
        TextView txtTitle,txtCharge;
        public static String[] txtItemTitle;
        public static String[] txtItemCharge;
        public static String[] txtItemID;

        public UserProfileAdapter(Context context,UserProfileBE objUserProfileBE)
        {
            mContext=context;

            String leads=objUserProfileBE.getLeads();
            System.out.println("Leads "+leads);

            JSONParser jsonP=new JSONParser();

            try {

                Object obj = jsonP.parse(leads);

                JSONArray jsonArrayObject = (JSONArray) obj;
                txtItemTitle=new String[jsonArrayObject.size()];
                txtItemCharge=new String[jsonArrayObject.size()];
                txtItemID=new String[jsonArrayObject.size()];

                for(int i=0;i<jsonArrayObject.size();i++)
                {
                    JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                    txtItemTitle[i]=jsonObject.get("syt_title").toString();
                    txtItemCharge[i]=jsonObject.get("price").toString();
                    txtItemID[i]=jsonObject.get("id").toString();
                }

            }
            catch (Exception e)
            {

            }



            }
        @Override
        public int getCount() {
            return txtItemCharge.length;
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
                gridView= infalInflater.inflate(R.layout.user_profile_raw_list, null);
            }
            txtTitle = (TextView) gridView .findViewById(R.id.user_title);
            txtCharge= (TextView) gridView.findViewById(R.id.user_charge);

            txtTitle.setText(txtItemTitle[position]);
            txtCharge.setText(txtItemCharge[position]);

            gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,80));
            return gridView;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
