package com.syt.balram.syt;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;

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
import java.net.URI;


public class UserSetting extends ActionBarActivity {

    ListView list;
    ImageButton back;
    UserSettingAdapter objUserSettingAdapter;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        back= (ImageButton) findViewById(R.id.user_settings_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list=(ListView)findViewById(R.id.accountList);

        objUserSettingAdapter=new UserSettingAdapter(getApplicationContext());
        list.setAdapter(objUserSettingAdapter);

        email=Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_UserID);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if(position==0)
                {
                    Intent intent=new Intent(getApplicationContext(),UploadPhoto.class);
                    startActivity(intent);
                }
                 else if(position==1)
                    {
                        Intent intent=new Intent(getApplicationContext(),ChangePassword.class);
                        startActivity(intent);
                    }
                else if(position==2)
                    {
                        Intent intent=new Intent(getApplicationContext(),PrivacySetting.class);
                        startActivity(intent);
                    }
                else if(position==3)
                {
                    new DeactivateAccount().execute(email);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_setting, menu);
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

    public class UserSettingAdapter extends BaseAdapter
    {
        public  String[] txtItemList;
        public Integer[] txtImage;
        Context context;
        UserSettingAdapter(Context mcontext)
        {
            context=mcontext;
            txtItemList=new String[4];
            txtImage=new Integer[4];
            txtItemList[0]="Upload photo";
            txtItemList[1]="Change Password";
            txtItemList[2]="Privacy Settings";
            txtItemList[3]="Deactivate your Account";

            txtImage[0]=R.drawable.upload_photo;
            txtImage[1]=R.drawable.change_password;
            txtImage[2]=R.drawable.privacy_setting;
            txtImage[3]=R.drawable.deactivate_account;
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
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);

            View gridView=null;
            //TextView tv,tv1;

            TextView tvName,tvCount;
            ImageView img;
            if (convertView != null){

                gridView=convertView;

            }else{
                gridView = new View(context);
                gridView= infalInflater.inflate(R.layout.user_setting_raw_list, null);

            }

            img= (ImageView) gridView.findViewById(R.id.account_img);
            tvName= (TextView) gridView.findViewById(R.id.accoutItem);
            tvName.setText(txtItemList[position]);
            img.setBackgroundResource(txtImage[position]);


            gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,100));
            return gridView;
        }
    }

    class DeactivateAccount extends AsyncTask<String,String,String>
    {
        TransparentProgressDialog pd=new TransparentProgressDialog(UserSetting.this,R.drawable.logo_single);
        @Override
        protected void onPreExecute() {
                pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            String text = null;
            String url="email="+params[0];

            try {
                URI uri = new URI("https", "www.sellyourtime.in", "/api/deactivate.php", url, null);
                String ll = uri.toASCIIString();


                HttpGet httpGet = new HttpGet(ll);



                try {
                    HttpResponse response = httpClient.execute(httpGet, localContext);


                    HttpEntity entity = response.getEntity();


                    text = getASCIIContentFromEntity(entity);


                } catch (Exception e) {
                    return e.getLocalizedMessage();
                }
            }
            catch (Exception e)
            {

            }


            return text;


        }

        @Override
        protected void onPostExecute(String s) {
            JSONParser jsonP=new JSONParser();
            String status="";

            try {

                Object obj = jsonP.parse(s);

                JSONArray jsonArrayObject = (JSONArray) obj;

                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

                status=jsonObject.get("status").toString();

                if(status.equals("1"))
                {
                    MyApp.tracker().setScreenName("Account Deactivate");
                    MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                            .setCategory("UI")
                            .setAction("Click")
                            .setLabel("Submit")
                            .build());

                    Toast.makeText(getApplicationContext(),"Your account have been successfully deactivated",Toast.LENGTH_LONG).show();
                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_UserID, null);
                    Intent intent = new Intent(getApplicationContext(), HowItWork.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                pd.dismiss();
            }
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

}
