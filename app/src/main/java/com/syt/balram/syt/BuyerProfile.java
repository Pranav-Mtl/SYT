package com.syt.balram.syt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.syt.balram.syt.BE.BuyerProfileBE;
import com.syt.balram.syt.BL.BuyerProfileBL;
import com.syt.balram.syt.BL.DeletePostBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;


public class BuyerProfile extends ActionBarActivity {

    TextView tvTitle,tvDate,tvCategory,tvsubCategory,tvZip,tvDeliveryDate,tvPrice,tvExperience,tvDeliveryMode,tvChargeMode,tvDesc,tvName,tvLocTop;
    String id;
    ProgressDialog progressDialog;
    BuyerProfileBL objBuyerProfileBL;
    BuyerProfileBE objBuyerProfileBE;
    AlertDialog alertDialog;
    ImageButton btnAdd,back;
    Button btnBuyerProfile,btnBuyerDelete;
    TransparentProgressDialog pd;
    DeletePostBL objDeletePostBL;
    int i;
    String userEmail;
    String userPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_profile);

        tvCategory= (TextView) findViewById(R.id.buyer_profile_category);
        tvTitle= (TextView) findViewById(R.id.buyer_profile_title);
        tvDate= (TextView) findViewById(R.id.buyer_profile_posteddate);
        tvsubCategory= (TextView) findViewById(R.id.buyer_profile_subcategory);
        tvZip= (TextView) findViewById(R.id.buyer_profile_location);
        tvDeliveryDate= (TextView) findViewById(R.id.buyer_profile_availability);
        tvPrice= (TextView) findViewById(R.id.buyer_profile_delivery_charges);
        tvExperience= (TextView) findViewById(R.id.buyer_profile_experience);
        tvChargeMode= (TextView) findViewById(R.id.buyer_profile_delivery_mode);
        tvDeliveryMode= (TextView) findViewById(R.id.buyer_profile_delivery_location);
        tvDesc= (TextView) findViewById(R.id.buyer_profile_desc);
        tvName= (TextView) findViewById(R.id.buyer_profile_postedname);
        btnAdd= (ImageButton) findViewById(R.id.buyer_profile_add);
        tvLocTop= (TextView) findViewById(R.id.buyer_profile_posteddlocation);
        btnBuyerProfile= (Button) findViewById(R.id.buyer_profile_btn);
        back= (ImageButton) findViewById(R.id.seller_profile_back);
        progressDialog=new ProgressDialog(BuyerProfile.this,R.style.MyDialogTheme);
        alertDialog=new AlertDialog.Builder(BuyerProfile.this,R.style.MyDialogTheme).create();
        pd=new TransparentProgressDialog(BuyerProfile.this,R.drawable.logo_single);
        btnBuyerDelete= (Button) findViewById(R.id.buyer_profile_delete);
        objDeletePostBL=new DeletePostBL();

        userEmail=Configuration.getSharedPrefrenceValue(BuyerProfile.this,Constant.SHARED_PREFERENCE_UserID);

        MyApp.tracker().setScreenName("Buyer Post Screen");
        MyApp.tracker().send(new HitBuilders.EventBuilder("UI", "Open")
                .setCategory("UI")
                .setAction("Open")
                .setLabel("Open")
                .build());


        objBuyerProfileBE=new BuyerProfileBE();
        objBuyerProfileBL=new BuyerProfileBL();

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        id=bundle.getString("ID");
        userPost=bundle.getString("MyPost");


       /* if(userPost.trim().equals("userPost"))
        {
            btnBuyerDelete.setVisibility(View.VISIBLE);
            btnBuyerProfile.setVisibility(View.GONE);
        }
        else
        {
            btnBuyerDelete.setVisibility(View.GONE);
            btnBuyerProfile.setVisibility(View.VISIBLE);
        }*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0) {
                    tvDesc.setVisibility(View.VISIBLE);
                    btnAdd.setBackgroundResource(R.drawable.subtract);
                    i++;
                }
                else
                {
                    btnAdd.setBackgroundResource(R.drawable.add);
                    tvDesc.setVisibility(View.GONE);
                    i=0;
                }

            }
        });

        try {
            if(Configuration.isInternetConnection(BuyerProfile.this)) {
                new LongRunningGetIO().execute(id);
            }
            else
            {
                alertDialog
                        .setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);
                alertDialog.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);
                alertDialog.setButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                //finish();
                                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        });

                alertDialog.show();
            }
        }
        catch (Exception e)
        {

        }

        btnBuyerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1=new Intent(getApplicationContext(),ContactBuyer.class);
                intent1.putExtra("ID",id);
                startActivity(intent1);
            }
        });

        btnBuyerDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeletePost().execute(id);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buyer_profile, menu);
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

            // String result=objSellerFragmentBL.getsellerData(objSellerFragmentBE);
            String result=objBuyerProfileBL.getBuyerData(objBuyerProfileBE,params[0]);
            return "aa";

        }

        @Override
        protected void onPostExecute(String result) {

            pd.dismiss();

            tvCategory.setText(objBuyerProfileBE.getCategory());
            tvsubCategory.setText(objBuyerProfileBE.getSubcategory());
            tvTitle.setText(objBuyerProfileBE.getDescription());
            tvZip.setText(objBuyerProfileBE.getZip());

            String dd[]=objBuyerProfileBE.getDate().split(" ");
            tvDeliveryDate.setText(objBuyerProfileBE.getDeliveryDate());

            String delLocation[];
                    delLocation=objBuyerProfileBE.getServiceMode().split("/");
            String dLoc="";

            int deliveryLocation=0;
            for(int i=0;i<delLocation.length;i++)
            {
                if(!(delLocation[i].equalsIgnoreCase("undefined"))) {

                    if(delLocation[i].equalsIgnoreCase("I will travel to Client Site")) {
                        dLoc = dLoc + " " + "Client Side";
                       deliveryLocation++;
                    }
                    else if(delLocation[i].equalsIgnoreCase("At My Place")) {
                        if(deliveryLocation==0) {
                            dLoc = dLoc + "" + "My Place";
                            deliveryLocation++;
                        }
                        else
                        {
                            dLoc = dLoc + "," + "My Place";
                            deliveryLocation++;
                        }
                    }
                    else {
                        if(deliveryLocation==0) {
                            dLoc = dLoc + "" + "Online";
                            deliveryLocation++;
                        }
                        else
                        {
                            dLoc = dLoc + "," + "Online";
                            deliveryLocation++;
                        }
                    }
                }

            }
            tvDeliveryMode.setText(dLoc);
            tvChargeMode.setText(objBuyerProfileBE.getChargeMode());
            tvPrice.setText(objBuyerProfileBE.getPrice());
            tvDate.setText("Posted on: "+dd[0]);
            tvName.setText("Posted by: "+objBuyerProfileBE.getFullName());
            tvLocTop.setText("Location: "+objBuyerProfileBE.getZip());
            tvExperience.setText(objBuyerProfileBE.getExperience()+" years");
            tvDesc.setText(objBuyerProfileBE.getTitle());
            try
            {
                String loginID=Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.profileId);

                System.out.println("LOGIN IN"+loginID);
                System.out.println("Progile id"+objBuyerProfileBE.getProfileID());
                if(loginID.trim().equals(objBuyerProfileBE.getProfileID().trim()))
                {
                    btnBuyerDelete.setVisibility(View.VISIBLE);
                    btnBuyerProfile.setVisibility(View.GONE);
                }
                else
                {
                    btnBuyerDelete.setVisibility(View.GONE);
                    btnBuyerProfile.setVisibility(View.VISIBLE);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

                MyApp.tracker().setScreenName("Posted Requirement Screen");
                MyApp.tracker().send(new HitBuilders.EventBuilder("UI", "OPEN")
                        .setLabel("Posted Requirement Screen")
                        .build());


        }
    }

    private class DeletePost extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
           String result= objDeletePostBL.postData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();

            if(s.equals("1"))
            {
                startActivity(new Intent(getApplicationContext(),HomeScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                Toast.makeText(getApplicationContext(),"Post deleted successfully",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Post hasn't deleted. Please try again",Toast.LENGTH_LONG).show();
            }
        }
    }

}
