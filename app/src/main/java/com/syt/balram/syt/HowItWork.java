package com.syt.balram.syt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.analytics.HitBuilders;

import java.util.Locale;


public class HowItWork extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    Button btnSignup,btnLogin,btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_work);

       /* ActionBar actionBar=getSupportActionBar();
        actionBar.hide();*/
        btnSignup= (Button) findViewById(R.id.htw_signup_btn);
        btnLogin= (Button) findViewById(R.id.htw_login_btn);
       // btnSkip= (Button) findViewById(R.id.htw_skip_btn);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.tracker().setScreenName("SIGNUP Screen");
                MyApp.tracker().send(new HitBuilders.EventBuilder("ui", "open").setLabel("SIGNUP")
                        .build());
                //AffleInAppTracker.inAppTrackerViewName(getApplicationContext(), "SignUP Screen", "", "SignUp UI", null);
                Intent intent=new Intent(getApplicationContext(),RegisterScreen.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.tracker().setScreenName("Login Screen");
                MyApp.tracker().send(new HitBuilders.EventBuilder("ui", "open")
                        .setLabel("LOGIN")
                        .build());
                //AffleInAppTracker.inAppTrackerViewName(getApplicationContext(), "LogIn Screen", "", "LogIn UI", null);
                Intent intent=new Intent(getApplicationContext(),LoginScreen.class);
                startActivity(intent);
            }
        });

       /* btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.tracker().setScreenName("Home Screen");
                MyApp.tracker().send(new HitBuilders.EventBuilder("ui", "open")
                        .setLabel("Home Screen")
                        .build());

                Intent intent=new Intent(getApplicationContext(),HomeScreen.class);
                startActivity(intent);
            }
        });*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_how_it_work, menu);
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            try {
                switch (position) {
                    case 0:
                        // Top Rated fragment activity
                        return new HTWone();
                    case 1:
                        // Games fragment activity
                        return new HTWtwo();
                    case 2:
                        // Movies fragment activity
                        return new HTWthree();
                    case 3:
                        // Movies fragment activity
                        return new HTWfour();
                    case 4:
                        // Movies fragment activity
                        return new HTWfive();
                    case 5:
                        // Movies fragment activity
                        return new HTWsix();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 5:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_how_it_work, container, false);
            return rootView;
        }
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).setNegativeButton("NO", null).show();

    }
}
