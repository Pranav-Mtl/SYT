package com.syt.balram.syt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.syt.balram.syt.BE.FilterBE;
import com.syt.balram.syt.BL.CategoryBL;
import com.syt.balram.syt.BL.FilterBL;
import com.syt.constant.Constant;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;
import com.loopj.android.image.SmartImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;


public class CategoryTrainerTutor extends ActionBarActivity {

    ListView tvCategoryList;
    CategoryTrainerTutorAdapter objCategoryTrainerTutorAdapter;
    ImageButton btnFilter,btnArrow,back;
    String title,category,location;
    ProgressDialog progressDialog;
    TextView tvTitle;
    FilterBE objFilterBE;
    AlertDialog alertDialog;
    String resultJson;
    String searchTitle;
    String result;
    AQuery aq;
    Button btnSubcategory;
    LinearLayout llImg;
    String subCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_trainer_tutor);

        btnArrow= (ImageButton) findViewById(R.id.category_arrow_btn);
        back= (ImageButton) findViewById(R.id.category_back);
        btnFilter= (ImageButton) findViewById(R.id.category_filter);
        tvTitle= (TextView) findViewById(R.id.category_title);
        btnSubcategory= (Button) findViewById(R.id.category_subcategory_button);
        llImg= (LinearLayout) findViewById(R.id.category_img);
        alertDialog = new AlertDialog.Builder(CategoryTrainerTutor.this).create();
        aq=new AQuery(this);

        objFilterBE=new FilterBE();

        progressDialog=new ProgressDialog(CategoryTrainerTutor.this);

        searchTitle=Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_SearchTitle);

        MyApp.tracker().setScreenName("Category Screen");
        MyApp.tracker().send(new HitBuilders.EventBuilder("UI", "Open")
                .setCategory("UI")
                .setAction("Open")
                .setLabel("Open")
                .build());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(searchTitle.equals("Category"))
        {
            Intent intent=getIntent();
            Bundle bundle=intent.getExtras();
            title=bundle.get("Title").toString();
            tvTitle.setText(title);

            try
            {
                if(Configuration.isInternetConnection(CategoryTrainerTutor.this)) {
                    new LongRunningGetIO().execute(title);
                }
                else{

                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            CategoryTrainerTutor.this);

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
                                    Toast.makeText(getApplicationContext(),
                                            "You clicked on NO", Toast.LENGTH_SHORT)
                                            .show();
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
            //
        }
        else
        {
            Intent intent=getIntent();
            Bundle bundle=intent.getExtras();
            title=bundle.get("Category").toString();
            String Option=bundle.get("Title").toString();
            category=bundle.get("Category").toString();
            location=bundle.get("Location").toString();
            subCategory=bundle.get("SubCategory").toString();
            tvTitle.setText(category);

            try
            {
                 if(Configuration.isInternetConnection(CategoryTrainerTutor.this)) {
                    new GetSearcjData().execute(Option,category,location,subCategory);
                }
                else{

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
                e.printStackTrace();
            }
        }



        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Filter.class);
                intent.putExtra("Title",title);
                intent.putExtra("FilterBE",objFilterBE);
                startActivityForResult(intent, 1);
                //overridePendingTransition(R.animator.anim_out, R.animator.anim_in );
            }
        });



        tvCategoryList= (ListView) findViewById(R.id.category_trainer_listview);


        btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Filter.class);
                intent.putExtra("Title",title);
                intent.putExtra("FilterBE",objFilterBE);
                startActivityForResult(intent, 1);

            }
        });



        btnSubcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Filter.class);
                intent.putExtra("Title",title);
                intent.putExtra("FilterBE",objFilterBE);
                startActivityForResult(intent, 1);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category_trainer_tutor, menu);
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

            objCategoryTrainerTutorAdapter = new CategoryTrainerTutorAdapter(getApplicationContext(), params[0]);
            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();

            try {
                tvCategoryList.setAdapter(objCategoryTrainerTutorAdapter);
                if(title.equals("Trainers and Tutors"))
                {
                    llImg.setBackgroundResource(R.drawable.trainers);
                }
                else if(title.equals("Business Consultants"))
                {
                    llImg.setBackgroundResource(R.drawable.bussiness);
                }
                else if(title.equals("IT and Marketing"))
                {
                    llImg.setBackgroundResource(R.drawable.it);
                }
                else if(title.equals("Fashion and Lifestyle"))
                {
                    llImg.setBackgroundResource(R.drawable.fashion);
                }
                else if(title.equals("Beauty and Wellness"))
                {
                    llImg.setBackgroundResource(R.drawable.beauty);
                }
                else if(title.equals("Social Causes"))
                {
                    llImg.setBackgroundResource(R.drawable.social);
                }
                else if(title.equals("Home and Utility"))
                {
                    llImg.setBackgroundResource(R.drawable.home);
                }
                else if(title.equals("Events and Entertainment"))
                {
                    llImg.setBackgroundResource(R.drawable.events);
                }
                else if(title.equals("Everything Else"))
                {
                    llImg.setBackgroundResource(R.drawable.everything);
                }

                tvCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //Toast.makeText(CategoryTrainerTutor.this, "clicked on" + position, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), SellerProfile.class);
                        intent.putExtra("ID", Constant.categoryIdArray[position]);
                        startActivity(intent);
                    }
                });
            }
            catch (Exception e)
            {

            }


        }

    }

    private class GetSearcjData extends AsyncTask<String, String, String> {

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

            objCategoryTrainerTutorAdapter=new CategoryTrainerTutorAdapter(getApplicationContext(),params[0],params[1],params[2],params[3]);
            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();

            try {
                tvCategoryList.setAdapter(objCategoryTrainerTutorAdapter);

                if (resultJson.equals("empty")) {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            CategoryTrainerTutor.this);

// Setting Dialog Title
                    alertDialog2.setTitle("Looking to buy services under " + title);

// Setting Dialog Message
                    alertDialog2.setMessage("We will find a service provider matching your exact requirements.\n" +
                            "Please post your interest with us.");

// Setting Icon to Dialog


// Setting Positive "Yes" Btn
                    alertDialog2.setPositiveButton("Register",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    startActivity(new Intent(getApplicationContext(),RequirementOne.class));


                                }
                            });
// Setting Negative "NO" Btn
                    alertDialog2.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    /*Toast.makeText(getApplicationContext(),
                                            "You clicked on NO", Toast.LENGTH_SHORT)
                                            .show();*/
                                    dialog.cancel();
                                }
                            });

// Showing Alert Dialog
                    alertDialog2.show();

                }
                else
                {

                }
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }


            if(title.equals("Trainers and Tutors"))
            {
                llImg.setBackgroundResource(R.drawable.trainers);
            }
            else if(title.equals("Business Consultants"))
            {
                llImg.setBackgroundResource(R.drawable.bussiness);
            }
            else if(title.equals("IT and Marketing"))
            {
                llImg.setBackgroundResource(R.drawable.it);
            }
            else if(title.equals("Fashion and Lifestyle"))
            {
                llImg.setBackgroundResource(R.drawable.fashion);
            }
            else if(title.equals("Beauty and Wellness"))
            {
                llImg.setBackgroundResource(R.drawable.beauty);
            }
            else if(title.equals("Social Causes"))
            {
                llImg.setBackgroundResource(R.drawable.social);
            }
            else if(title.equals("Home and Utility"))
            {
                llImg.setBackgroundResource(R.drawable.home);
            }
            else if(title.equals("Events and Entertainment"))
            {
                llImg.setBackgroundResource(R.drawable.events);
            }
            else if(title.equals("Everything Else"))
            {
                llImg.setBackgroundResource(R.drawable.everything);
            }



            tvCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Toast.makeText(CategoryTrainerTutor.this, "clicked on" + position, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), SellerProfile.class);
                    intent.putExtra("ID", Constant.categoryIdArray[position]);
                    startActivity(intent);
                }
            });


        }
    }








    public class CategoryTrainerTutorAdapter extends BaseAdapter
    {
        Context context;
        String categoryTitle;
        CategoryBL objCategoryBL;

        public CategoryTrainerTutorAdapter(Context mContext,String mtitle)
        {
            context=mContext;
            categoryTitle=mtitle;
            System.out.println("Title"+categoryTitle);
            objCategoryBL=new CategoryBL();
            objCategoryBL.getCategoryData(categoryTitle);
        }
        public CategoryTrainerTutorAdapter(Context mContext)
        {
            context=mContext;
            FilterBL objFilterBL = new FilterBL();
            resultJson=objFilterBL.getFilteredData(objFilterBE);
            System.out.println("result"+resultJson);


        }
        public CategoryTrainerTutorAdapter(Context mContext,String title,String category,String location,String subCategory)
        {
            context=mContext;
            objCategoryBL=new CategoryBL();

            resultJson=objCategoryBL.getSearchData(title,category,location,subCategory);

        }

        @Override
        public int getCount() {
            return Constant.categoryCategoryArray.length;
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
            if (convertView != null){

                gridView=convertView;

            }else{
                gridView = new View(context);
                gridView= infalInflater.inflate(R.layout.category_raw_list, null);

            }

            TextView tvName= (TextView) gridView.findViewById(R.id.main_category_name);
            TextView tvCategory= (TextView) gridView.findViewById(R.id.main_category_category);
            TextView tvSubCategory= (TextView) gridView.findViewById(R.id.main_category_subcategory);
            TextView tvPrice= (TextView) gridView.findViewById(R.id.main_category_price);
            TextView tvAvailability= (TextView) gridView.findViewById(R.id.main_category_availability);
            TextView tvExperience= (TextView) gridView.findViewById(R.id.main_category_exp);
            TextView tvLocation= (TextView) gridView.findViewById(R.id.main_category_local);
            SmartImageView imgPic= (SmartImageView) gridView.findViewById(R.id.main_category_pic);

            tvName.setText(Constant.categoryFirstNameArray[position]+" "+Constant.categoryLastNameArray[position]);
            tvCategory.setText(Constant.categoryCategoryArray[position]);
            tvSubCategory.setText(Constant.categorySubCategoryArray[position]);
            tvPrice.setText(Constant.categoryPriceMinArray[position]+"-"+Constant.categoryPriceMaxArray[position]);
            tvAvailability.setText(Constant.categoryAvailabilityArray[position]);
            tvExperience.setText("Experience "+Constant.categoryExperienceArray[position]+" years");
            tvLocation.setText("");


          //  int loader = R.drawable.default_avatar_man;

           // System.out.println("https://www.sellyourtime.in/dashboard/images/"+Constant.categoryImageArray[position]);
            String image_url;

                 image_url =Constant.categoryImageArray[position];
            System.out.println("ImageURL"+image_url);

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
                    imgPic.setImageBitmap(bmap);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else {

                imgPic.setImageUrl(image_url);
            }



            /* ImageLoader imgLoader = new ImageLoader(CategoryTrainerTutor.this);
            imgLoader.DisplayImage(image_url, loader, imgPic);*/


           /* aq.id(R.id.main_category_pic)
                    .image(image_url, true, true,
                            0, 0, null, AQuery.FADE_IN);

*/


            gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,AbsListView.LayoutParams.WRAP_CONTENT));
            return gridView;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK)
        {

            objFilterBE= (FilterBE) data.getSerializableExtra("FilterBE");
            /*FilterBL objFilterBL = new FilterBL();
            objFilterBL.getFilteredData(objFilterBE);*/
            new GetFilter().execute();

           // Toast.makeText(CategoryTrainerTutor.this,"Result code 1",Toast.LENGTH_LONG).show();
        }
        else
        {
            //Toast.makeText(CategoryTrainerTutor.this,"Result code not 1",Toast.LENGTH_LONG).show();
        }
    }


    private class GetFilter extends AsyncTask<String, String, String> {

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
            objCategoryTrainerTutorAdapter=new CategoryTrainerTutorAdapter(getApplicationContext());

            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();
            tvCategoryList.setAdapter(objCategoryTrainerTutorAdapter);

            if(resultJson.equals("empty"))
            {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        CategoryTrainerTutor.this);

// Setting Dialog Title
                alertDialog2.setTitle("Looking to buy services under "+ title);

// Setting Dialog Message
                alertDialog2.setMessage("We will find a service provider matching your exact requirements.\n" +
                        "Please post your interest with us.");

// Setting Icon to Dialog


// Setting Positive "Yes" Btn
                alertDialog2.setPositiveButton("Register",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                //startActivity(new Intent(Settings.ACTION_SETTINGS));
                                startActivity(new Intent(getApplicationContext(), RequirementOne.class));
                            }
                        });
// Setting Negative "NO" Btn
                alertDialog2.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                dialog.cancel();
                            }
                        });

// Showing Alert Dialog
                alertDialog2.show();

            }

            tvCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Toast.makeText(CategoryTrainerTutor.this, "clicked on" + position, Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),SellerProfile.class);
                    intent.putExtra("ID",Constant.categoryIdArray[position]);
                    startActivity(intent);

                    System.out.println("result"+resultJson);


                }
            });

        }
    }
}
