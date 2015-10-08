package com.syt.balram.syt;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.syt.balram.syt.BE.RequirementBE;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
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


public class RequirementFragment extends Fragment {

    ExpandableListView expListView;
    SellerQuestionExpandableAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Context context;
    TransparentProgressDialog pd;


    String BusinessCons;
    String FashionStyle;
    String BeautyWellness;
    String HomeUtility;
    String EventEntert;
    String EverthingElse;
    String socialcause;


    ProgressDialog progressDialog;

    RequirementBE objRequirementBE;

    int xx,yy;


    public RequirementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_requirement, container, false);

        progressDialog=new ProgressDialog(getActivity());
        pd=new TransparentProgressDialog(getActivity(),R.drawable.logo_single);

        WindowManager wm = (WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

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

        objRequirementBE=new RequirementBE();

        expListView= (ExpandableListView) view.findViewById(R.id.requirement_expandable_list);

        if(Configuration.isInternetConnection(getActivity())) {
            try {
                progressDialog.show();
                progressDialog.setMessage("Loading");
                prepareListData();
                progressDialog.hide();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    getActivity());

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
                            Toast.makeText(getActivity(),
                                    "You clicked on NO", Toast.LENGTH_SHORT)
                                    .show();
                            dialog.cancel();

                        }
                    });

// Showing Alert Dialog
            alertDialog2.show();

        }
        context=getActivity();

        progressDialog.show();

        listAdapter = new SellerQuestionExpandableAdapter(getActivity(), listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        progressDialog.hide();

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
             /*   Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();*/

                objRequirementBE.setCategory( listDataHeader.get(groupPosition));

                if(listDataHeader.get(groupPosition).equals("Trainers and Tutors") || listDataHeader.get(groupPosition).equals("IT and Marketing")) {
                    startActivity(new Intent(getActivity(), SubCategoryActivity.class).putExtra("SubChild", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)).putExtra("RequirementBE", objRequirementBE));
                }
                else
                {
                    objRequirementBE.setSubCategory(listDataChild.get(
                            listDataHeader.get(groupPosition)).get(
                            childPosition));
                    Intent intent=new Intent(getActivity(), RequirementTwo.class);
                    intent.putExtra("RequirementBE",objRequirementBE);
                    startActivity(intent);
                }
                // showPopupWindow();
                return false;
            }
        });




        return view;
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
            result = new RunForSubcategory().execute().get();
            validate(result);
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
                //System.out.println("aksmmsmsms" + jsonObjectByIndex.get("id_pk" + i));

                homeUtility.add(String.valueOf(jsonObjectByIndex.get("id_pk" + i)));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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

    private class RunForSubcategory extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            String url = Constant.WEBSERVICE_URL + Constant.WEBSERVICE_CategoriesList;

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
        protected void onPostExecute(String result) {    //set adapter here
            progressDialog.dismiss();
            super.onPostExecute(result);


        }
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

            }


        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return result;

    }


        @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private PopupWindow myPopupWindow;

    void showPopupWindow(String SubChild,View view) {
        // inflate your layout
        myPopupWindow = new PopupWindow(context);


        try {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myPopupView = inflater.inflate(R.layout.sub_category_popup, (ViewGroup) view.findViewById(R.id.subcategory_popup));

            // Create the popup window; decide on the layout parameters
            myPopupWindow = new PopupWindow(myPopupView, xx, yy, true);
            myPopupWindow.showAtLocation(myPopupView, Gravity.BOTTOM, 0, 0);
            myPopupWindow.setFocusable(true);
            myPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            myPopupWindow.setOutsideTouchable(true);

            ListView listview = (ListView) myPopupView.findViewById(R.id.subcategory_listview);
            ImageButton back = (ImageButton) myPopupView.findViewById(R.id.subcategory_popup_back);
            TextView title= (TextView) myPopupView.findViewById(R.id.subcategoryTitle);
            title.setText(SubChild);
            try {
                SubCategoryAdapter objSubCategoryAdapter = new SubCategoryAdapter(context,SubChild);
                listview.setAdapter(objSubCategoryAdapter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                pd.dismiss();
            }




            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myPopupWindow.dismiss();
                }
            });
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub

                    objRequirementBE.setSubCategory(SubCategoryAdapter.txtItemList[position]);

                    Intent intent = new Intent(getActivity(), RequirementTwo.class);
                    intent.putExtra("RequirementBE",objRequirementBE);
                    startActivity(intent);
                    myPopupWindow.dismiss();

                }
            });


            // find and initialize your TextView(s), EditText(s) and Button(s); setup their behavior

            // display your popup window
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
