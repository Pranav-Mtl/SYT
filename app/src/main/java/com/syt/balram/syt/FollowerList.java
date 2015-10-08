package com.syt.balram.syt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.syt.balram.syt.BE.FollowerListBE;
import com.syt.balram.syt.BL.FollowerGcmBL;
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
import java.util.ArrayList;
import java.util.List;


public class FollowerList extends ActionBarActivity {

    ListView listView;
    ArrayAdapter<FollowerListBE> adapter;
    List<FollowerListBE> list = new ArrayList<FollowerListBE>();
    StringBuffer responseText = new StringBuffer();

    Button btnSend;

    //ProgressDialog progressDialog;
    TransparentProgressDialog pd;
    EditText etFollowersMSg;
    FollowerGcmBL objFollowerGcmBL;
    ImageButton back;

    String emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_list);

        listView = (ListView) findViewById(R.id.my_list);
        btnSend= (Button) findViewById(R.id.follow_send_btn);
        back= (ImageButton) findViewById(R.id.follower_list_back);
        //progressDialog=new ProgressDialog(FollowerList.this);
        pd=new TransparentProgressDialog(FollowerList.this,R.drawable.logo_single);
        etFollowersMSg= (EditText) findViewById(R.id.follower_editText_message);
        objFollowerGcmBL=new FollowerGcmBL();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emailId= Configuration.getSharedPrefrenceValue(FollowerList.this,Constant.SHARED_PREFERENCE_UserID);
        adapter = new FollowerListAdapter(this,getModel(),this);
        listView.setAdapter(adapter);
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView label = (TextView) v.getTag(R.id.code);
                CheckBox checkbox = (CheckBox) v.getTag(R.id.checkBox1);
                Toast.makeText(v.getContext(), label.getText().toString() + " " + isCheckedOrNot(checkbox), Toast.LENGTH_LONG).show();
            }
        });
*/
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder sb = new StringBuilder();

                String wordList[] = responseText.toString().split("/");
                for (int i = 0; i < wordList.length; i++) {
                    boolean found = false;
                    for (int j = i + 1; j < wordList.length; j++) {
                        if (wordList[j].equals(wordList[i])) {
                            found = true;
                            break;
                        }
                    }
                    // System.out.printf("Checking: [%s]%n", wordList[i]);
                    if (!found) {
                        if (sb.length() > 0)
                            sb.append(',');
                        sb.append(wordList[i]);
                    }
                }
                System.out.println(responseText);
                System.out.println("SVVVV" + sb);

                String msg = etFollowersMSg.getText().toString();

                if (Configuration.isInternetConnection(FollowerList.this)) {

                    if (msg.length() > 0) {
                        if (sb.length() > 0)
                            new SendGCM().execute(emailId, String.valueOf(sb), msg);
                    }
                } else {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            FollowerList.this);

// Setting Dialog Title
                    alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

// Setting Dialog Message
                    alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

// Setting Icon to Dialog


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




    }



    private String isCheckedOrNot(CheckBox checkbox) {
        if(checkbox.isChecked())
            return "is checked";
        else
            return "is not checked";
    }

    private List<FollowerListBE> getModel() {

        try {
            String result = new GetFollowerList().execute(emailId).get();
             String status=validate(result);
        }
        catch (Exception e)
        {

        }
        /*list.add(new FollowerListBE("Linux"));
        list.add(new FollowerListBE("Windows7"));
        list.add(new FollowerListBE("Suse"));
        list.add(new FollowerListBE("Eclipse"));
        list.add(new FollowerListBE("Ubuntu"));
        list.add(new FollowerListBE("Solaris"));
        list.add(new FollowerListBE("Android"));
        list.add(new FollowerListBE("iPhone"));
        list.add(new FollowerListBE("Java"));
        list.add(new FollowerListBE(".Net"));
        list.add(new FollowerListBE("PHP"));*/
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_follower_list, menu);
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

    class GetFollowerList extends AsyncTask<String,String,String>
    {


        @Override
        protected String doInBackground(String... params) {

           String status=getDataStatus(params[0]);
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private String getDataStatus(String email)
    {

        String text = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String url="email="+email;

        try {
            URI uri = new URI("https", "www.sellyourtime.in", "/api/followed.php", url, null);
            String ll = uri.toASCIIString();

            System.out.println("Follower list" + ll);


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

        System.out.println("Folloowww"+strValue);
        String status="";
        String result=null;

        JSONParser jsonP=new JSONParser();

        try {

            Object obj =jsonP.parse(strValue);


            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.followIdArray=new String[jsonArrayObject.size()];
            Constant.followFirstNameArray=new String[jsonArrayObject.size()];
            Constant.followLastNameArray=new String[jsonArrayObject.size()];
            Constant.followCategoryArray=new String[jsonArrayObject.size()];
            Constant.followSubCategoryArray=new String[jsonArrayObject.size()];
            Constant.followImageArray=new String[jsonArrayObject.size()];
            Constant.followLocationArray=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {

                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                status=jsonObject.get("status").toString();
                list.add(new FollowerListBE(jsonObject.get("firstname").toString()));
                Constant.followIdArray[i]=jsonObject.get("id").toString();
                Constant.followFirstNameArray[i]=jsonObject.get("firstname").toString();
                Constant.followLastNameArray[i]=jsonObject.get("lastname").toString();
                Constant.followCategoryArray[i]=jsonObject.get("category").toString();
                Constant.followSubCategoryArray[i]=jsonObject.get("subcategory").toString();




            }



            //System.out.println("names"+objSellerFragmentBE.getName());
            //objSellerFragmentBE.setName(arrName);*/



        } catch (Exception e) {



            e.getLocalizedMessage();
        }

        return status;

    }
    class SendGCM extends AsyncTask<String,String,String>
    {
        TransparentProgressDialog pd=new TransparentProgressDialog(FollowerList.this,R.drawable.logo_single);

        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String status=objFollowerGcmBL.SendFollow(params[0],params[1],params[2]);
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("1"))
            {
                etFollowersMSg.setText("");
                try {
                    MyApp.tracker().setScreenName("PublishAdd Screen");
                    MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                            .setCategory("UI")
                            .setAction("Click")
                            .setLabel("Submit")
                            .build());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                pd.dismiss();
                Toast.makeText(getApplicationContext(),"You successfully posted an update to your followers",Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Problem with MSG SEND",Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        }
    }
}
