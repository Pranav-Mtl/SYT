package com.syt.balram.syt;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.syt.constant.Constant;

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
import java.util.List;

/**
 * Created by Balram on 4/17/2015.
 */
public class SubCategoryAdapter extends BaseAdapter {

    TextView categoryText;
    public static String[] txtItemList;
    Context mContext;
    String subChildName;
    String subChildArray;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;

    // if called with the same position multiple lines it works as toggle
    public void setSelection(int position) {
        if (selectedPos == position) {
            selectedPos = NOT_SELECTED;
        } else {
            selectedPos = position;
        }

    }

    public SubCategoryAdapter(Context context ,String mChild){

        subChildName=mChild;

        System.out.println("Before" + subChildName);

        if(subChildName.equals("Sports and Recreation"))
        {
            subChildName="Sports & Recreation";
        }
        else if(subChildName.equals("Occupation"))
        {
            subChildName="Occupational";
        }
        else if(subChildName.equals("Arts and Crafts"))
        {
            subChildName="Arts & Craft";
        }
        System.out.println("After"+subChildName);

        mContext= context;

        try {

            String Result = new RunSubCategoryLogin().execute().get();
            validate(Result);

        }
        catch (Exception e)
        {

        }

        JSONParser parser = new JSONParser();

        //System.out.println("get subcjilhd araraaahayyyyy"+subChildArray);

        List<String> homeUtility = new ArrayList<String>();
        try {
            Object objJsonParserByIndex = parser.parse(subChildArray);
            JSONObject jsonObjectByIndex = (JSONObject) objJsonParserByIndex;

           // int j =1 + jsonObjectByIndex.size();
            txtItemList=new String[jsonObjectByIndex.size()+1];

            for (int i = 1; i <=jsonObjectByIndex.size(); i++) {

               txtItemList[i-1] =String.valueOf(jsonObjectByIndex.get("id_pk" + i));

            }

            txtItemList[jsonObjectByIndex.size()]="Others";
        }
        catch (Exception e)
        {
                e.printStackTrace();
        }




        /*txtItemList[0] ="Non-Alcoholic Sangria";
        txtItemList[1]= "Banta Soda";
        txtItemList[2]="Canned Juices";
        txtItemList[3]="Nimbu Paani";
        txtItemList[4] ="Aereated Drinks";
        txtItemList[5]= "Shakes";
        txtItemList[6]="Oreo Shake";
        txtItemList[7]="Mango Shake";
        txtItemList[8]="Banana Shake";
        txtItemList[9]="Healthy Museli Shake";
        txtItemList[10]="Chocolate Shake";
        txtItemList[11]="Kit Kat Shake";
        txtItemList[12]="Strawberry shake";*/
    }
    @Override
    public int getCount() {

        return txtItemList.length;
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
            gridView= infalInflater.inflate(R.layout.subcategory_list_raw, null);

        }

        if (position == selectedPos) {
            // your color for selected item
            gridView.setBackgroundColor(Color.parseColor("#e4e4e4"));
        } else {
            // your color for non-selected item
            gridView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }




        categoryText = (TextView) gridView .findViewById(R.id.subcategory_tv);

        //String[] mThumbIds=getList();
        categoryText.setText(txtItemList[position]);

        gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,70));

        return gridView;
    }

    private class RunSubCategoryLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute ( )
        {

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            HttpGet httpGet = new HttpGet(Constant.WEBSERVICE_URL+Constant.WEBSERVICE_SubCategoriesList);
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

    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();


        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n>0) {
            byte[] b = new byte[4096];
            n =  in.read(b);


            if (n>0) out.append(new String(b, 0, n));
        }


        return out.toString();
    }

    public String validate(String strValue)
    {
        //System.out.println("Complete json"+strValue);
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
          //      System.out.println("increase order...."+subChildName+"\\\\"+jsonObjectByIndex.get(subChildName).toString());
                subChildArray=jsonObjectByIndex.get(subChildName).toString();

                /*FashionStyle=jsonObjectByIndex.get("Fashion and Lifestyle").toString();
                BeautyWellness=jsonObjectByIndex.get("Beauty and Wellness").toString();
                HomeUtility=jsonObjectByIndex.get("Home and Utility").toString();
                EventEntert=jsonObjectByIndex.get("Events & Entertainment").toString();
                EverthingElse=jsonObjectByIndex.get("Everything Else").toString();
                socialcause=jsonObjectByIndex.get("Social Causes").toString();
*/

                //BusinessCons=new String[jsonObjectByIndex.get("Business Consultants").toString().length()];
                //System.out.println(jsonObjectByIndex.get("Business Consultants").toString());


            }



            //JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());



        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return result;

    }

}
