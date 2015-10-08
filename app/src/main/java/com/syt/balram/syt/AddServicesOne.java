package com.syt.balram.syt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.syt.balram.syt.BE.AddServicesBE;
import com.syt.constant.Constant;
import com.syt.util.Configuration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddServicesOne extends ActionBarActivity {

    ExpandableListView expListView;
    SellerQuestionExpandableAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Context context;
    AddServicesBE objAddServicesBE;
    AlertDialog alertDialog;
    ImageButton back;

    String BusinessCons;
    String FashionStyle;
    String BeautyWellness;
    String HomeUtility;
    String EventEntert;
    String EverthingElse;
    String socialcause;
    String Others;
    String txtheader,txtChild;
    int xx;
    int yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services_one);
        Display display = getWindowManager().getDefaultDisplay();

        // Point size = new Point();
        // display.getSize(size);
        int width = display.getWidth();
        int height = display.getHeight();

        System.out.println("width"+width+"height"+height);

        if(width>=600 && height>=1024)
        {
            xx=width;
            yy=1000;
        }
        else
        {
             xx=480;
             yy=685;
        }

        expListView= (ExpandableListView) findViewById(R.id.expandable_list);
        back= (ImageButton) findViewById(R.id.addServices_one_back);

        objAddServicesBE=new AddServicesBE();

        /*Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        =(SellerQuestionBE)intent.getSerializableExtra("SellerQuestionBE");*/
        alertDialog = new AlertDialog.Builder(AddServicesOne.this,R.style.MyDialogTheme).create();

        prepareListData();
        context=getApplicationContext();

        listAdapter = new SellerQuestionExpandableAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
          /*      Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();*/

                txtheader=listDataHeader.get(groupPosition);

               objAddServicesBE.setCategory(txtheader);

                if(listDataHeader.get(groupPosition).equals("Trainers and Tutors") || listDataHeader.get(groupPosition).equals("IT and Marketing")) {
                    //showPopupWindow(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                    startActivity(new Intent(getApplicationContext(), AddServicesSubCategory.class).putExtra("SubChild", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)).putExtra("AddServicesBE", objAddServicesBE));
                }
                else
                {
                    txtChild=listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                    if(txtChild.trim().equalsIgnoreCase("Others"))
                    {
                        //Toast.makeText(SellerQuestionExpandable.this,"Others CLicked",Toast.LENGTH_LONG).show();
                        initiatePopupWindow(txtheader);
                        //showDialog(getApplicationContext());
                    }
                    else {
                        objAddServicesBE.setSubCategory(txtChild);
                        Intent intent = new Intent(AddServicesOne.this, AddServicesTwo.class);
                        intent.putExtra("AddServicesBE", objAddServicesBE);
                        startActivity(intent);
                    }
                }

                return false;
            }
        });


    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Trainers and Tutors");
        listDataHeader.add("Home and Utility");
        listDataHeader.add("Business Consultants");
        listDataHeader.add("Beauty and Wellness");
        listDataHeader.add("IT and Marketing");
        listDataHeader.add("Events and Entertainment");
        listDataHeader.add("Fashion and Lifestyle");
        listDataHeader.add("Social Causes");
        listDataHeader.add("Others");


        // Adding child data
        List<String> TrainerTutor = new ArrayList<String>();
        TrainerTutor.add("Academic");
        TrainerTutor.add("Language Experts");
        TrainerTutor.add("Performance Arts");
        TrainerTutor.add("Sports and Recreation");
        TrainerTutor.add("Occupation");
        TrainerTutor.add("Arts and Crafts");



        String result;


        try {
            if(Configuration.isInternetConnection(AddServicesOne.this)) {
                result = new RunForSubcategory().execute().get();
                validate(result);
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

        JSONParser parser = new JSONParser();

        List<String> homeUtility = new ArrayList<String>();
        try {
            Object objJsonParserByIndex = parser.parse(HomeUtility);
            JSONObject jsonObjectByIndex = (JSONObject) objJsonParserByIndex;

            int j =1 + jsonObjectByIndex.size();

            for (int i = 1; i < j; i++) {

                homeUtility.add(String.valueOf(jsonObjectByIndex.get("id_pk" + i)));
            }
            homeUtility.add("Others");
        }
        catch (Exception e)
        {

        }


        List<String> bussinessConsultant = new ArrayList<String>();




        try {
            Object objJsonParserByIndex = parser.parse(BusinessCons);
            JSONObject jsonObjectByIndex = (JSONObject) objJsonParserByIndex;

            int j = 1 + jsonObjectByIndex.size();

            for (int i =1; i < j; i++) {
                //System.out.println("aksmmsmsms" + jsonObjectByIndex.get("id_pk" + i));

                bussinessConsultant.add(String.valueOf(jsonObjectByIndex.get("id_pk" + i)));
            }
            bussinessConsultant.add("Others");
        }
        catch (Exception e)
        {

        }




        List<String> beautywellness = new ArrayList<String>();
        try {
            Object objJsonParserByIndex = parser.parse(BeautyWellness);
            JSONObject jsonObjectByIndex = (JSONObject) objJsonParserByIndex;

            int j = 1 + jsonObjectByIndex.size();

            for (int i = 1; i < j; i++) {
                //System.out.println("aksmmsmsms" + jsonObjectByIndex.get("id_pk" + i));

                beautywellness.add(String.valueOf(jsonObjectByIndex.get("id_pk" + i)));
            }
            beautywellness.add("Others");
        }
        catch (Exception e)
        {

        }

        List<String> eventsEntertainment = new ArrayList<String>();
        try {
            Object objJsonParserByIndex = parser.parse(EventEntert);
            JSONObject jsonObjectByIndex = (JSONObject) objJsonParserByIndex;

            int j = 1 + jsonObjectByIndex.size();

            for (int i =1; i < j; i++) {
                //System.out.println("aksmmsmsms" + jsonObjectByIndex.get("id_pk" + i));

                eventsEntertainment.add(String.valueOf(jsonObjectByIndex.get("id_pk" + i)));
            }

            eventsEntertainment.add("Others");
        }
        catch (Exception e)
        {

        }

        List<String> fashionLifeStyle = new ArrayList<String>();
        try {
            Object objJsonParserByIndex = parser.parse(FashionStyle);
            JSONObject jsonObjectByIndex = (JSONObject) objJsonParserByIndex;

            // int j = 1+ jsonObjectByIndex.size();

            for (int i =1; i <=jsonObjectByIndex.size(); i++) {
                //System.out.println("aksmmsmsms" + jsonObjectByIndex.get("id_pk" + i));

                fashionLifeStyle.add(String.valueOf(jsonObjectByIndex.get("id_pk" + i)));


            }

            fashionLifeStyle.add("Others");
        }
        catch (Exception e)
        {

        }

        List<String> socialcauseses = new ArrayList<String>();
        try {
            Object objJsonParserByIndex = parser.parse(socialcause);
            JSONObject jsonObjectByIndex = (JSONObject) objJsonParserByIndex;

            int j = 1+ jsonObjectByIndex.size();

            for (int i =1; i < j; i++) {
                //System.out.println("aksmmsmsms" + jsonObjectByIndex.get("id_pk" + i));

                socialcauseses.add(String.valueOf(jsonObjectByIndex.get("id_pk" + i)));
            }


        }
        catch (Exception e)
        {

        }
        List<String> others = new ArrayList<String>();
        try {
            Object objJsonParserByIndex = parser.parse(EverthingElse);
            JSONObject jsonObjectByIndex = (JSONObject) objJsonParserByIndex;

            int j = 1+ jsonObjectByIndex.size();

            for (int i=1; i < j; i++) {
                //System.out.println("aksmmsmsms" + jsonObjectByIndex.get("id_pk" + i));

                others.add(String.valueOf(jsonObjectByIndex.get("id_pk" + i)));
            }
            others.add("Others");
        }
        catch (Exception e)
        {

        }



        List<String> itmarketing = new ArrayList<String>();
        itmarketing.add("IT");
        itmarketing.add("Marketing");



        listDataChild.put(listDataHeader.get(0), TrainerTutor); // Header, Child data
        listDataChild.put(listDataHeader.get(1), homeUtility);
        listDataChild.put(listDataHeader.get(2), bussinessConsultant);
        listDataChild.put(listDataHeader.get(3), beautywellness);
        listDataChild.put(listDataHeader.get(4), itmarketing);
        listDataChild.put(listDataHeader.get(5), eventsEntertainment);
        listDataChild.put(listDataHeader.get(6), fashionLifeStyle);
        listDataChild.put(listDataHeader.get(7), socialcauseses);
        listDataChild.put(listDataHeader.get(8), others);


    }

    private PopupWindow myPopupWindow;

    void showPopupWindow(String SubChild) {
        // inflate your layout
        myPopupWindow=new PopupWindow(context);
        try {
            LayoutInflater inflater = (LayoutInflater) AddServicesOne.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View myPopupView = inflater.inflate(R.layout.sub_category_popup, (ViewGroup) findViewById(R.id.subcategory_popup));

            // Create the popup window; decide on the layout parameters
            myPopupWindow = new PopupWindow(myPopupView,xx,yy, true);
            myPopupWindow.showAtLocation(myPopupView, Gravity.BOTTOM, 0, 0);
            myPopupWindow.setFocusable(true);
            myPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            myPopupWindow.setOutsideTouchable(true);

            SubCategoryAdapter objSubCategoryAdapter=new SubCategoryAdapter(context,SubChild);
            ListView listview= (ListView) myPopupView.findViewById(R.id.subcategory_listview);
            ImageButton back= (ImageButton) myPopupView.findViewById(R.id.subcategory_popup_back);
            TextView tv= (TextView) myPopupView.findViewById(R.id.subcategoryTitle);

            tv.setText(SubChild);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myPopupWindow.dismiss();

                }
            });

            listview.setAdapter(objSubCategoryAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub

                    txtChild=SubCategoryAdapter.txtItemList[position];
                    objAddServicesBE.setSubCategory(txtChild);




                    Intent intent=new Intent(AddServicesOne.this, AddServicesTwo .class);
                    intent.putExtra("AddServicesBE",objAddServicesBE);
                    startActivity(intent);
                }
            });



            // find and initialize your TextView(s), EditText(s) and Button(s); setup their behavior

            // display your popup window
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_services_one, menu);
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

    private class RunForSubcategory extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute ( )
        {


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            String url= Constant.WEBSERVICE_URL+Constant.WEBSERVICE_CategoriesList;

            HttpGet httpGet = new HttpGet(url);




            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);


                HttpEntity entity = response.getEntity();


                text = getASCIIContentFromEntity(entity);


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }


            return text;

        }

        @Override
        protected void onPostExecute (String result)
        {    //set adapter here

            super.onPostExecute(result);


        }



    }


    private String getLoginStatus()
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        String url=Constant.WEBSERVICE_URL+Constant.WEBSERVICE_CategoriesList;

        HttpGet httpGet = new HttpGet(url);

        System.out.println("url"+url);


        String text = null;
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);


            HttpEntity entity = response.getEntity();


            text = getASCIIContentFromEntity(entity);


        } catch (Exception e) {
            return e.getLocalizedMessage();
        }


        return text;

    }
    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();


        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n>0) {
            byte[] b = new byte[8192];
            n =  in.read(b);
            if (n>0) out.append(new String(b, 0, n));
        }


        return out.toString();
    }

    public String validate(String strValue)
    {
        System.out.println("Complete json"+strValue);
        Long status;
        String result=null;

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);


            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++)
            {
                JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(i).toString());
                JSONObject jsonObjectByIndex =(JSONObject)jsonObject;
                BusinessCons=jsonObjectByIndex.get("Business Consultants").toString();
                FashionStyle=jsonObjectByIndex.get("Fashion and Lifestyle").toString();
                BeautyWellness=jsonObjectByIndex.get("Beauty and Wellness").toString();
                HomeUtility=jsonObjectByIndex.get("Home and Utility").toString();
                EventEntert=jsonObjectByIndex.get("Events & Entertainment").toString();
                EverthingElse=jsonObjectByIndex.get("Everything Else").toString();
                socialcause=jsonObjectByIndex.get("Social Causes").toString();
                //BusinessCons=new String[jsonObjectByIndex.get("Business Consultants").toString().length()];
                //System.out.println(jsonObjectByIndex.get("Business Consultants").toString());


            }



            //JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());



        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return result;

    }

    private PopupWindow pwindo;

    private void initiatePopupWindow(String title) {
        final EditText edittext;
        TextView tvTitle;
        Button btnClosePopup,btnsave;
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) AddServicesOne.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.seller_other_layout,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 500, 550, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            edittext= (EditText) layout.findViewById(R.id.seller_other_category);
            btnClosePopup = (Button) layout.findViewById(R.id.seller_other_cancel);
            btnsave= (Button) layout.findViewById(R.id.seller_other_button);
            tvTitle= (TextView) layout.findViewById(R.id.seller_other_title);

            tvTitle.setText(title);

            btnClosePopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                    pwindo.dismiss();
                }
            });

            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subcategory=edittext.getText().toString();
                    if(subcategory.trim().length()>0) {
                        objAddServicesBE.setSubCategory(subcategory);
                        Intent intent=new Intent(AddServicesOne.this, AddServicesTwo.class);
                        intent.putExtra("AddServicesBE",objAddServicesBE);
                        startActivity(intent);
                    }
                    pwindo.dismiss();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
