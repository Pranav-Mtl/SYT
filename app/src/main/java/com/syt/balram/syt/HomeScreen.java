package com.syt.balram.syt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.syt.balram.syt.BE.ProfileBE;
import com.syt.balram.syt.BL.ProfileBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;

import java.util.Locale;

public class HomeScreen extends ActionBarActivity {

    String TITLES[] = {"My Profile","My Posts", "Post Requirement", "Share & Refer", "Notifications", "Feedback", "Signout"};
    int ICONS[] = {R.drawable.sidebar_profile,R.drawable.sidebar_post, R.drawable.sidebar_requirement, R.drawable.sidebar_share,R.drawable.sidebar_notification,R.drawable.sidebar_feedback, R.drawable.sidebar_signout};

    String loginTitle;
    String NAME = "";
    String EMAIL = "";
    String PROFILE ="";

    View _lastColored;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    CustomPagerAdapter mCustomPagerAdapter;
    String uid;

    ViewPager mViewPager;
    ProfileBE objProfileBE;
    ProfileBL objProfileBL;
    ProgressDialog progressDialog;
    TransparentProgressDialog pd;
    String shareMessage;
    String profileStatus;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        uid = Configuration.getSharedPrefrenceValue(HomeScreen.this, Constant.SHARED_PREFERENCE_UserID);
        pd=new TransparentProgressDialog(HomeScreen.this,R.drawable.logo_single);

        if(uid==null)
        {
            startActivity(new Intent(getApplicationContext(), HowItWork.class));
        }

        System.out.println("At home Screen");

        //System.out.println(Configuration.getSharedPrefrenceValue(HomeScreen.this,Constant.CampaignLadoo));

         objProfileBE = new ProfileBE();
         objProfileBL = new ProfileBL();

        try {
            progressDialog=new ProgressDialog(HomeScreen.this);
            //System.out.println("At home Screen1");
            toolbar = (Toolbar) findViewById(R.id.tool_bar);
            //System.out.println("At home Screen2");
            setSupportActionBar(toolbar);
            //System.out.println("At home Screen3");
            ActionBar actionBar = getSupportActionBar();// set the icon
            //System.out.println("At home Screen4");
            actionBar.setIcon(R.drawable.logo_mainscreen);
            //System.out.println("At home Screen5");
            actionBar.setTitle("");
            //System.out.println("At home Screen6");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            
            loginTitle = Configuration.getSharedPrefrenceValue(HomeScreen.this, Constant.SHARED_PREFERENCE_LoginTitle);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println(loginTitle);
        if(loginTitle!=null) {
            if (loginTitle.equalsIgnoreCase("Buyer")) {
                TITLES=new String[6];
                TITLES[0]="My Posts";
                TITLES[1]="Post Requirement";
                TITLES[2]="Share & Refer";
                TITLES[3]="Notifications";
                TITLES[4]="Feedback";
                TITLES[5]="Signout";

                ICONS=new int[6];
                ICONS[0]=R.drawable.sidebar_profile;
                ICONS[1]=R.drawable.sidebar_requirement;
                ICONS[2]=R.drawable.sidebar_share;
                ICONS[3]=R.drawable.sidebar_notification;
                ICONS[4]=R.drawable.sidebar_feedback;
                ICONS[5]=R.drawable.sidebar_signout;
            }
        }

         id=Configuration.getSharedPrefrenceValue(HomeScreen.this,Constant.profileId);



        try {
            MyApp.tracker().setScreenName("Home Screen");
            MyApp.tracker().send(new HitBuilders.EventBuilder("UI", "OPEN")
                    .setLabel("Home Screen")
                    .build());
           /* Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
            hashtable.put("EmailID", uid);
            hashtable.put("LoginAs", loginTitle);*/

         // AffleInAppTracker.inAppTrackerViewName(getApplicationContext(), "Home Screen", "First Screen of APP", "Home Page UI", hashtable);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String emailId = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.SHARED_PREFERENCE_UserID);

        System.out.println("EmailID:" + emailId);

        if (emailId != null) {

            //progressDialog.show();
            try {
                if(Configuration.isInternetConnection(HomeScreen.this)) {
                    profileStatus=new GetProfileInformation().execute(emailId).get();
                }
                else
                {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            HomeScreen.this);

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
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try {
                if (profileStatus.equals("done")) {

                    NAME = objProfileBE.getFirstName() + " " + objProfileBE.getLastName();
                    EMAIL = objProfileBE.getSubCategory();
                    PROFILE = objProfileBE.getPicture();
                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.profileId, objProfileBE.getId());
                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.appURL, objProfileBE.getAppURL());
                    mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
                    mRecyclerView.setHasFixedSize(true);                            // Letting the s0ystem know that the list objects are of fixed size
                    mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE, getApplicationContext());       // Creating the Adapter of com.example.balram.sampleactionbar.MyAdapter class(which we are going to see in a bit)

                    // And passing the titles,icons,header view name, header view email,
                    // and header  view profile picture
                    mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
                    mLayoutManager = new LinearLayoutManager(HomeScreen.this);                 // Creating a layout Manager
                    mRecyclerView.setLayoutManager(mLayoutManager);

                } else {

                    startActivity(new Intent(getApplicationContext(),HomeOops.class));
                }
            }
            catch (NullPointerException e)
            {
                // e.printStackTrace();
                startActivity(new Intent(getApplicationContext(), HomeOops.class));
            }
            finally {
                pd.dismiss();
                shareMessage="Hi!\n" +
                        "How are you?DownloadSellYourTime app to find qualified business leads and trusted service providers.\n" +
                        "APP Link:  "+objProfileBE.getAppURL()+"\n"+
                        "\n"+
                        "I have a SellYourTime profile. Create yours now.\n" +
                        "https://www.sellyourtime.in/categories/profile.php?id="+id+"\n" +
                        "\n" +
                        "Cheers!\n";
            }

            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            if(position!=0)
                            {
                                if (_lastColored != null) {
                                    _lastColored.setBackgroundColor(getResources().getColor(R.color.WhiteColor));
                                    _lastColored.invalidate();

                                }

                                _lastColored=view;
                                _lastColored.setBackgroundColor(getResources().getColor(R.color.GrayColor));

                            }
                            // do whatever
                            if (position == 5) {
                                if(loginTitle.equalsIgnoreCase("Buyer")) {
                                    // view.setBackgroundColor(Color.parseColor("#ffffff"));
                                    Intent intent = new Intent(getApplicationContext(), Feedback.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    //view.setBackgroundColor(Color.parseColor("#ffffff"));
                                    Intent intent = new Intent(getApplicationContext(), Setting.class);
                                    startActivity(intent);
                                }
                            /*Toast.makeText(getApplicationContext(), "posi" + view, Toast.LENGTH_LONG).show();*/
                            }
                            if (position == 3) {
                                if(loginTitle.equalsIgnoreCase("Buyer")) {
                                    // view.setBackgroundColor(Color.parseColor("#ffffff"));
                                    Intent shareIntent = new Intent();
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.setType("text/plain");
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                    startActivity(Intent.createChooser(shareIntent, "Share and Refer"));
                                }
                                else
                                {
                                    pd.show();
                                    //view.setBackgroundColor(Color.parseColor("#ffffff"));
                                    Intent intent = new Intent(getApplicationContext(), RequirementOne.class);
                                    startActivity(intent);

                                    new Handler().postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                        }


                                    }, 4000);
                                }
                            }

                            if (position == 2) {
                                if(loginTitle.equalsIgnoreCase("Buyer")) {

                                    //view.setBackgroundColor(Color.parseColor("#ffffff"));
                                    Intent intent = new Intent(getApplicationContext(), RequirementOne.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    //view.setBackgroundColor(Color.parseColor("#ffffff"));
                                    Intent intent = new Intent(getApplicationContext(), MyPosts.class);
                                    startActivity(intent);
                                }
                            /*Toast.makeText(getApplicationContext(), "posi" + view, Toast.LENGTH_LONG).show();*/
                            }

                            if (position == 4) {
                                if(loginTitle.equalsIgnoreCase("Buyer")) {
                                    view.setBackgroundColor(Color.parseColor("#ffffff"));
                                    Intent intent = new Intent(getApplicationContext(), Setting.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    view.setBackgroundColor(Color.parseColor("#ffffff"));
                                    Intent shareIntent = new Intent();
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.setType("text/plain");
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                    startActivity(Intent.createChooser(shareIntent, "Share and Refer"));
                                }
                            /*Toast.makeText(getApplicationContext(), "posi" + view, Toast.LENGTH_LONG).show();*/
                            }
                            if (position == 6) {
                                if(loginTitle.equalsIgnoreCase("Buyer")) {
                                    view.setBackgroundColor(Color.parseColor("#ffffff"));
                                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_UserID, null);
                                    Intent intent = new Intent(getApplicationContext(), HowItWork.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else
                                {
                                    view.setBackgroundColor(Color.parseColor("#ffffff"));
                                    Intent intent = new Intent(getApplicationContext(), Feedback.class);
                                    startActivity(intent);
                                }
                            /*Toast.makeText(getApplicationContext(), "posi" + view, Toast.LENGTH_LONG).show();*/
                            }
                            if (position == 1) {
                                if (uid != null) {
                                    if(loginTitle.equalsIgnoreCase("Buyer"))
                                    {
                                        view.setBackgroundColor(Color.parseColor("#ffffff"));
                                        Intent intent = new Intent(getApplicationContext(), MyPosts.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        view.setBackgroundColor(Color.parseColor("#ffffff"));
                                        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    view.setBackgroundColor(Color.parseColor("#ffffff"));
                                    Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                                    startActivity(intent);
                                }
                            }
                            if(position==7)
                            {
                                if(!loginTitle.equalsIgnoreCase("Buyer")) {
                                    view.setBackgroundColor(Color.parseColor("#ffffff"));
                                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_UserID, null);
                                    Intent intent = new Intent(getApplicationContext(), HowItWork.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }
                        }
                    })
            );

           // System.out.println("PROFILE"+profileStatus);

            //progressDialog.dismiss();
        }
        // Setting the layout Manager

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State


        mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), getApplicationContext());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mCustomPagerAdapter);

        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColor(Color.parseColor("#1f8e03"));
    }

    class CustomPagerAdapter extends FragmentPagerAdapter {

        Context mContext;

        public CustomPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {

            // Create fragment object
            switch (position) {
                case 0:
                    // Top Rated fragment activity
                    return new CategoriesFragment();
                case 1:
                    // Movies fragment activity
                    return new BuyerFragment();
                case 2:
                    // Movies fragment activity
                    return new SellerFragment();
            }
            return null;
        }


        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section4).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);

            }

            return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            // Toast.makeText(getApplicationContext(),"search",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(HomeScreen.this, Search.class);
            startActivity(intent);
            //showPopupWindow();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class GetProfileInformation extends AsyncTask<String, String, String>{


               @Override
               protected void onPreExecute() {

                   pd.show();
               }

                   @Override
            protected String doInBackground(String... params) {
                String result=objProfileBL.getProfileData(params[0], objProfileBE);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {

               super.onPostExecute(s);
                pd.dismiss();

            }
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).setNegativeButton("No", null).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("RESUME----->");

        pd.dismiss();
        /*mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);*/

    }
}
