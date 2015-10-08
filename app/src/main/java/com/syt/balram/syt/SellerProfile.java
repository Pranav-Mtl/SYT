package com.syt.balram.syt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.syt.balram.syt.BE.SellerProfileBE;
import com.syt.balram.syt.BL.FollowBL;
import com.syt.balram.syt.BL.SellerProfileBL;
import com.syt.balram.syt.BL.SellerRatingBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.imageloader.ImageLoader;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;


public class SellerProfile extends ActionBarActivity {

    ImageButton btnBack,editSummary,editSkills,back;
    ImageView sellerPic;
    TextView sellerName,sellerExp,sellerDeliveryMode,sellerCharge,sellerChargeMode,sellerLocaion,sellerAvailability,summary,tvTitle;
    RatingBar ratingBar;
    EditText etCategory,editCategory,etskills,etSubcategory;
    LinearLayout llTopArea;

    SellerProfileBE objSellerProfileBE;
    SellerProfileBL objSellerProfileBL;
    String id;

    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    Activity activity;
    RatingBar rbSeller;
    RatingBar ratingBarUser;
    int i=0;
    TransparentProgressDialog pd;

    FollowBL objFollowBL ;
    Context context;
    int xx,yy;
    float currentRating=0;
    String emailID;
    String name;

    Button btnConnect,btnShare,btnChat,btnFollow;

    String shareMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Display display = getWindowManager().getDefaultDisplay();

        // Point size = new Point();
        // display.getSize(size);
        int width = display.getWidth();
        int height = display.getHeight();

        System.out.println("width" + width + "height" + height);

        if(width>=600 && height>=1024)
        {
            xx=500;
            yy=500;
        }
        else
        {
            xx=400;
            yy=550;
        }
        setContentView(R.layout.activity_seller_profile);

        btnBack= (ImageButton) findViewById(R.id.seller_profile_back);
        editCategory= (EditText) findViewById(R.id.seller_profile_category);
        editSummary= (ImageButton) findViewById(R.id.seller_profile_edit_summary);
        rbSeller= (RatingBar) findViewById(R.id.seller_profile_rating);
        // editSkills= (ImageButton) findViewById(R.id.seller_profile_edit_skills);
        sellerExp= (TextView) findViewById(R.id.seller_profile_experience);
        sellerChargeMode= (TextView) findViewById(R.id.seller_profile_charge_mode);
        sellerCharge= (TextView) findViewById(R.id.seller_profile_service_charge);
        sellerDeliveryMode= (TextView) findViewById(R.id.seller_profile_service_delivery_location);
        sellerLocaion= (TextView) findViewById(R.id.seller_profile_service_location);
        sellerAvailability= (TextView) findViewById(R.id.seller_profile_service_availability);
        etSubcategory= (EditText) findViewById(R.id.seller_profile_subcategory);
        summary= (TextView) findViewById(R.id.seller_profile_summary);
        back= (ImageButton) findViewById(R.id.seller_profile_back);
        btnConnect= (Button) findViewById(R.id.seller_connect);
        btnShare= (Button) findViewById(R.id.seller_share);
        btnChat= (Button) findViewById(R.id.seller_chat);
        btnFollow= (Button) findViewById(R.id.seller_follow);
        context=getApplicationContext();
        tvTitle= (TextView) findViewById(R.id.tvTitleName);
        pd=new TransparentProgressDialog(this,R.drawable.logo_single);
        objFollowBL = new FollowBL();
        emailID = Configuration.getSharedPrefrenceValue(SellerProfile.this, Constant.SHARED_PREFERENCE_UserID);

        sellerPic= (ImageView) findViewById(R.id.seller_profile_pic);
        sellerName= (TextView) findViewById(R.id.seller_profile_name);
        ratingBar= (RatingBar) findViewById(R.id.seller_profile_rating);
        etCategory= (EditText) findViewById(R.id.seller_profile_category);
        llTopArea= (LinearLayout) findViewById(R.id.seller_profile_two);
        //alertDialog=new AlertDialog.Builder(SellerProfile.this).create();

        MyApp.tracker().setScreenName("Seller Profile");
        MyApp.tracker().send(new HitBuilders.EventBuilder("UI", "Open")
                .setCategory("UI")
                .setAction("Open")
                .setLabel("Seller Profile")
                .build());


        objSellerProfileBE=new SellerProfileBE();
        objSellerProfileBL=new SellerProfileBL();

        final Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        id=bundle.getString("ID");


        objSellerProfileBE.setFollowType("followed");
        //rbSeller.setNumStars(5);

        context=getApplicationContext();

        progressDialog=new ProgressDialog(SellerProfile.this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0) {
                    summary.setVisibility(View.VISIBLE);
                    editSummary.setBackgroundResource(R.drawable.subtract);
                    i++;
                }
                else
                {
                    editSummary.setBackgroundResource(R.drawable.add);
                    summary.setVisibility(View.GONE);
                    i=0;
                }

            }
        });

        try {

            if(Configuration.isInternetConnection(SellerProfile.this))
            {
                new LongRunningGetIO().execute(id,emailID);
            }else
                {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            SellerProfile.this);

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

                                }
                            });

// Showing Alert Dialog
                    alertDialog2.show();
                }

        }
        catch (Exception e)
        {

        }

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), Contact.class);
                intent1.putExtra("ID", id);
                startActivity(intent1);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share " + objSellerProfileBE.getSubCategoryName()));
            }
        });

        System.out.println("Name" + name);
        System.out.println("SubCategory" + objSellerProfileBE.getSubCategoryName());



        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Configuration.isInternetConnection(SellerProfile.this)) {

                        new FollowData().execute(emailID, id);

                } else {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            SellerProfile.this);

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

                                }
                            });

// Showing Alert Dialog
                    alertDialog2.show();
                }
            }


        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), ChatActivity.class);
                intent1.putExtra("ID", id);
                intent1.putExtra("Name", objSellerProfileBE.getFirstName() + " " + objSellerProfileBE.getLastName());
                intent1.putExtra("Pic", objSellerProfileBE.getPicURL());
                intent1.putExtra("Message", "");
                startActivity(intent1);
            }
        });
       // ratingBar.setFocusable(false);
        rbSeller.setFocusable(false);
       /* rbSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Rating Bar Clicked", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(getApplicationContext(), RatingBarActivity.class);
                intent1.putExtra("Rating", currentRating);
                intent1.putExtra("Id", id);
                startActivityForResult(intent1, 1);
            }
        });

*/

        llTopArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindow();
            }
        });

        rbSeller.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                //initiatePopupWindow();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // TODO perform your action here

                    /*Intent intent1 = new Intent(getApplicationContext(), RatingBarActivity.class);
                    intent1.putExtra("Rating", currentRating);
                    intent1.putExtra("Id", id);
                    startActivityForResult(intent1, 1);*/

                    initiatePopupWindow();
                }

               // startActivity(new Intent(SellerProfile.this,RatingBarActivity.class).putExtra("Rating",currentRating).putExtra("Id",id));*//*
                //Toast.makeText(getApplicationContext(),"Rating Bar Clicked",Toast.LENGTH_LONG).show();
                return true;
            }
        });

       /* rbSeller.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                //Toast.makeText(getApplicationContext(),"Rating Bar Clicked: "+rating,Toast.LENGTH_LONG).show();

            }

        });
*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seller_profile, menu);
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

     class LongRunningGetIO extends AsyncTask<String,String,String> {

         TransparentProgressDialog pd=new TransparentProgressDialog(SellerProfile.this,R.drawable.logo_single);

         @Override
         protected void onPreExecute() {
            pd.show();

         }

         @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

           // String result=objSellerFragmentBL.getsellerData(objSellerFragmentBE);
            String result= objSellerProfileBL.getsellerData(objSellerProfileBE,params[0],params[1]);
            Log.d("Result", result);
            return result;

        }


         @Override
        protected void onPostExecute(String result) {
            Log.d("ResultPost",String.valueOf(result));
            System.out.println("Under kakaakkakakaak" + result);

             name=objSellerProfileBE.getFirstName();
            name=name+" "+objSellerProfileBE.getLastName();
            String delLocation[];
            sellerName.setText(name);
            editCategory.setText(objSellerProfileBE.getCategoryName());
            etSubcategory.setText(objSellerProfileBE.getSubCategoryName());
            sellerExp.setText(objSellerProfileBE.getExperience()+" years");
            sellerChargeMode.setText(objSellerProfileBE.getServiceMode());
            sellerCharge.setText(objSellerProfileBE.getServiceCharge());

            currentRating=Float.valueOf(objSellerProfileBE.getRating());
            rbSeller.setRating(Float.valueOf(objSellerProfileBE.getRating()));
            rbSeller.setStepSize((float) 1);

             tvTitle.setText(name);

             if(objSellerProfileBE.getFollowType().equalsIgnoreCase("Following"))
             {
                 btnFollow.setBackgroundResource(R.drawable.seller_following);
             }


            summary.setText(objSellerProfileBE.getExpDetails());

            System.out.println(objSellerProfileBE.getExpDetails());

            delLocation=objSellerProfileBE.getDeliveryLocation().split("/");
            String dLoc="";
             int serviceLoc=0;
            for(int i=0;i<delLocation.length;i++)
            {
                if(!(delLocation[i].equalsIgnoreCase("undefined"))) {

                    if(delLocation[i].equalsIgnoreCase("I will travel to Client Site")) {

                        dLoc = dLoc + "" + "Client Side";
                        serviceLoc++;
                    }
                    else if(delLocation[i].equalsIgnoreCase("At My Place")) {
                        if(serviceLoc==0) {
                            dLoc = dLoc + "" + "My Place";
                            serviceLoc++;
                        }
                        else
                        {
                            dLoc = dLoc + "," + "My Place";
                            serviceLoc++;
                        }
                    }
                    else {
                        if(serviceLoc==0) {
                            dLoc = dLoc + "" + "Online";
                            serviceLoc++;

                        }
                        else
                        {
                            dLoc = dLoc + "," + "Online";
                            serviceLoc++;
                        }
                    }
                }

            }
            sellerDeliveryMode.setText(dLoc);
            sellerLocaion.setText(objSellerProfileBE.getLocation());
            sellerAvailability.setText(objSellerProfileBE.getAvailabity());

            int loader = R.drawable.default_avatar_man;

            String image_url = objSellerProfileBE.getPicURL();
            ImageLoader imgLoader = new ImageLoader(SellerProfile.this);

             image_url=image_url.replace("\\","");

             String checkFB=image_url.substring(7,12);
             System.out.println("User Profile CHECKFB------->" + checkFB);

             if(checkFB.equalsIgnoreCase("graph"))
             {
                 System.out.println("User Profile FB WALI------->");
                 try {
                     System.out.println("User Profile FB WALI PIC------->"+image_url);
                   /* URL imageURL = new URL(profilePic);
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
                     sellerPic.setImageBitmap(bmap);

                 }
                 catch (Exception e)
                 {
                     e.printStackTrace();
                 }
             }
             else {

                 imgLoader.DisplayImage(image_url, loader, sellerPic);
             }

             String url=Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.appURL);

             shareMessage="Hi!\n" +
                     "I just found "+name+" under "+objSellerProfileBE.getSubCategoryName()+" on SellYourTime-An app to find qualified business leads and trusted service providers.\n" +
                     "This service provider might serve your purpose : https://www.sellyourtime.in/categories/profile.php?id="+id+"\n" +
                     "Download SellYourTime-it makes life easier.\n" +
                      url+"\n";
             pd.dismiss();
        }
    }

    class FollowData extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String status=objFollowBL.addFollow(params[0], params[1]);
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            if(s.equals("1"))
            {
                btnFollow.setBackgroundResource(R.drawable.seller_following);
                Toast.makeText(getApplicationContext(),"You have successfully followed this seller.You will receive offers and updates from this seller.",Toast.LENGTH_LONG).show();
            }
            else if(s.equals("2"))
            {
                btnFollow.setBackgroundResource(R.drawable.seller_follow);
                Toast.makeText(getApplicationContext(),"You have  unfollow this seller.",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Try again",Toast.LENGTH_LONG).show();
            }

        }
    }

    class FollowedData extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String status=objFollowBL.addFollow(params[0], params[1]);
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("1"))
            {
                btnFollow.setBackgroundResource(R.drawable.seller_following);
                Toast.makeText(getApplicationContext(),"You have successfully followed this seller.You will receive offers and updates from this seller",Toast.LENGTH_LONG).show();
            }
            else if(s.equals("2")){
                Toast.makeText(getApplicationContext(),"You are already following this seller",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Try again",Toast.LENGTH_LONG).show();
            }

        }
    }


    // Rating bar Popup

    PopupWindow myPopupWindow;
    void showPopupWindow() {
        // inflate your layout
         //myPopupWindow = new PopupWindow(context);
        try {
            LayoutInflater inflater = (LayoutInflater) SellerProfile.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myPopupView = inflater.inflate(R.layout.rating_bar_popup, (ViewGroup) findViewById(R.id.rating_bar_pop));
            // Create the popup window; decide on the layout parameters
            myPopupWindow = new PopupWindow(myPopupView,xx,yy, true);
            myPopupWindow.showAtLocation(myPopupView, Gravity.CENTER, 0, 0);
            myPopupWindow.setFocusable(true);
            myPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            myPopupWindow.setOutsideTouchable(true);

            final RatingBar ratingBarCurrent= (RatingBar) myPopupView.findViewById(R.id.rating_popup_current_rating);
            final RatingBar ratingBarUser= (RatingBar) myPopupView.findViewById(R.id.rating_popup_user_rating);
            Button btn= (Button) myPopupView.findViewById(R.id.rating_popup_button_ok);

            //ratingBarCurrent.setFocusable(false);
            ratingBarCurrent.setIsIndicator(true);
            ratingBarCurrent.setRating(currentRating);


            ratingBarUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {

                    Toast.makeText(getApplicationContext(),ratingBarUser.getRating()+"", Toast.LENGTH_SHORT).show();
                }

            });
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int rrr=(int)ratingBarUser.getRating();
                    Toast.makeText(getApplicationContext(),rrr+"", Toast.LENGTH_SHORT).show();
                    System.out.print(myPopupWindow.isShowing());
                    if(myPopupWindow.isShowing()) {
                        myPopupWindow.dismiss();
                    }

                   new InsertRating().execute(emailID, id, String.valueOf(rrr));

                }
            });
        } catch (Exception e) {
        }
    }

    class InsertRating extends AsyncTask<String,String,String>
    {
        SellerRatingBL objSellerRatingBL=new SellerRatingBL();

        @Override
        protected void onPreExecute() {
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String status=objSellerRatingBL.insertRating(params[0], params[1], params[2]);
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                pd.dismiss();

                if(s.equals("1"))
                {

                    Toast.makeText(getApplicationContext(),"You have successfully rated this seller",Toast.LENGTH_SHORT).show();
                    pwindo.dismiss();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"You have already rated this seller",Toast.LENGTH_SHORT).show();
                    pwindo.dismiss();
                }
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }
        }
    }

    private PopupWindow pwindo;

    private void initiatePopupWindow() {

        try {

          // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) SellerProfile.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.rating_bar_popup,
                    (ViewGroup) findViewById(R.id.rating_bar_pop));
            pwindo = new PopupWindow(layout, xx, yy, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
            pwindo.setOutsideTouchable(true);
            final RatingBar ratingBarCurrent= (RatingBar) layout.findViewById(R.id.rating_popup_current_rating);
            ratingBarUser= (RatingBar) layout.findViewById(R.id.rating_popup_user_rating);
            Button btn= (Button) layout.findViewById(R.id.rating_popup_button_ok);
            Button close= (Button) layout.findViewById(R.id.rating_popup_button_later);



            //ratingBarCurrent.setFocusable(false);

            ratingBarCurrent.setIsIndicator(true);
            ratingBarCurrent.setRating(currentRating);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Window" + pwindo.isShowing());
                    pwindo.dismiss();
                    int rrr = (int) ratingBarUser.getRating();
                    //Toast.makeText(getApplicationContext(),rrr+"", Toast.LENGTH_SHORT).show();
                    new InsertRating().execute(emailID, id, String.valueOf(rrr));


                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pwindo.dismiss();

                }
            });

            ratingBarUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {

                   // Toast.makeText(getApplicationContext(), ratingBarUser.getRating() + "", Toast.LENGTH_SHORT).show();
                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {

        }
    };

*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }




}
