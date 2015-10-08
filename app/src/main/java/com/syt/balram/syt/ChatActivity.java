package com.syt.balram.syt;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.syt.balram.syt.BE.ChatPeopleBE;
import com.syt.balram.syt.DB.DBOperation;
import com.syt.constant.Constant;
import com.syt.imageloader.ImageLoader;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class ChatActivity extends ActionBarActivity {

    DBOperation dbOperation;

    ArrayList<ChatPeopleBE> ChatPeoples;

    EditText edtMessage;
    ListView chatLV;

    ChatListAdapter chatAdapater;

    String localTime;
    String id,name,pic,mssg;

    ImageView imgProfile;
    TextView tvName;
    String dt="";
    String email;
    ImageButton back;
    LinearLayout llHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ChatPeoples = new ArrayList<ChatPeopleBE>();

        chatLV = (ListView) findViewById(R.id.chat_listView);
        edtMessage = (EditText) findViewById(R.id.chat_editText_message);
        imgProfile= (ImageView) findViewById(R.id.chat_image);
        tvName= (TextView) findViewById(R.id.chat_name);
        back= (ImageButton) findViewById(R.id.chat_back);
        llHeader= (LinearLayout) findViewById(R.id.chat_header);
        email= Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.SHARED_PREFERENCE_UserID);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MyApp.tracker().setScreenName("Chat Screen");
        MyApp.tracker().send(new HitBuilders.EventBuilder("UI", "OPEN")
                .setCategory("Start")
                .setAction("Open")
                .setLabel("Chat Screen")
                .build());

        name="";

        Intent intent=getIntent();
        Bundle bnd=intent.getExtras();
        try {
            if(bnd!=null) {
                id = bnd.get("ID").toString();
                name = bnd.get("Name").toString();
                pic = bnd.get("Pic").toString();
                mssg=bnd.get("Message").toString();
                System.out.println("MESSAGESSSSSS"+bnd.get("Message").toString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
           System.out.println(id+name+pic+mssg);
             }



        int loader = R.drawable.default_avatar_man;

        String image_url = "https://www.sellyourtime.in/dashboard/images/"+pic;

        ImageLoader imgLoader = new ImageLoader(ChatActivity.this);

        imgLoader.DisplayImage(image_url, loader, imgProfile);
        tvName.setText(name);



        registerReceiver(broadcastReceiver, new IntentFilter(
                "CHAT_MESSAGE_RECEIVED"));
       /* NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
*/

        dbOperation = new DBOperation(this);
        dbOperation.createAndInitializeTables();

        if(!mssg.isEmpty())
        {
            dt=getCurrentDateTime();
            // this demo this is the same device
            System.out.println("From top Notification"+"insert"+id+ mssg+"Received"+dt);
            ChatPeopleBE curChatObj = addToChat(id, mssg,
                    "Received",dt);
            addToDB(curChatObj); // adding to db
           populateChatMessages("new Msg------------------>");
        }


        populateChatMessages("onCreate--------------->");

        chatLV.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chatLV.setStackFromBottom(true);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            System.out.println("Broadcast RECIEVED-------->>>>>>>");

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(GCMNotificationIntentService.NOTIFICATION_ID);



            Bundle b = intent.getExtras();

            String mm="";
           // JSONObject jsonObj = new JSONObject(msg);
            String message = b.getString("message");
            try {
                JSONObject jsonObj = new JSONObject(message);
                id=jsonObj.getString("userid");
                mm=jsonObj.getString("greetMsg");
            }
            catch (Exception e)
            {

            }


            //System.out.println(" Chat Activity BroadCast reciever" +message);

            dt=getCurrentDateTime();
            // this demo this is the same device
            //System.out.println( "insert"+id+ mm+"Received"+dt);
            ChatPeopleBE curChatObj = addToChat(id, mm,
                    "Received",dt);
            addToDB(curChatObj); // adding to db

            populateChatMessages("Broadcast---------->");

        }
    };

    private void populateChatMessages(String ss) {

        System.out.println(ss);

        getData();
        if (ChatPeoples.size() > 0) {
            chatAdapater = new ChatListAdapter(this, ChatPeoples);
            chatLV.setAdapter(chatAdapater);
        }

    }

    void getData() {

        ChatPeoples.clear();

        Cursor cursor = dbOperation.getDataFromTable(id);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                ChatPeopleBE people = addToChat(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2),cursor.getString(3));
                ChatPeoples.add(people);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }
    ChatPeopleBE addToChat(String personID, String chatMessage, String toOrFrom,String chatDate) {

        /*Log.i(TAG, "inserting : " + personName + ", " + chatMessage + ", "
                + toOrFrom + " , " + chattingToDeviceID);*/
        ChatPeopleBE curChatObj = new ChatPeopleBE();
        curChatObj.setPERSON_ID(personID);
        curChatObj.setPERSON_CHAT_MESSAGE(chatMessage);
        curChatObj.setPERSON_CHAT_TO_FROM(toOrFrom);
        curChatObj.setCHAT_DATE(chatDate);
        return curChatObj;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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

    public String getCurrentDateTime()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = cal.getTime();
        SimpleDateFormat date = new SimpleDateFormat("dd:MM:yyyy hh:mm a");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        localTime = date.format(currentLocalTime);

        return localTime;
    }

    void clearMessageTextBox() {

        edtMessage.setText("");

        //hideKeyBoard(edtMessage);

    }

    private void hideKeyBoard(EditText edt) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }


    public void onClick(final View view) {

        if (view == findViewById(R.id.chat_send)) {


            dt = getCurrentDateTime();

            if (edtMessage.getText().toString().trim().length() > 0) {

                String textMessage=edtMessage.getText().toString();
                String textMessageCaps = textMessage.substring(0, 1).toUpperCase() + textMessage.substring(1);

                System.out.println("insertbutton" + id + edtMessage.getText().toString() + "sent" + dt);

                ChatPeopleBE curChatObj = addToChat(id, textMessageCaps, "Sent", dt);
                addToDB(curChatObj); // adding to db
                populateChatMessages("SEND------->");

                clearMessageTextBox();

                try {

                     new SendChatData().execute(id,textMessageCaps,email);
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        }

    }
    void addToDB(ChatPeopleBE curChatObj) {

        ChatPeopleBE people = new ChatPeopleBE();
        ContentValues values = new ContentValues();
        values.put(people.getPERSON_ID(), curChatObj.getPERSON_ID());
        values.put(people.getPERSON_CHAT_MESSAGE(),
                curChatObj.getPERSON_CHAT_MESSAGE());
        values.put(people.getPERSON_CHAT_TO_FROM(),
                curChatObj.getPERSON_CHAT_TO_FROM());
        values.put(people.getCHAT_DATE(), dt);
        dbOperation.open();
        long id = dbOperation.insertTableData(people.getTableName(), values);
        dbOperation.close();
        if (id != -1) {
          //  Log.i(TAG, "Succesfully Inserted");
        }

        //populateChatMessages("ADD to DB");
    }


    private class SendChatData extends AsyncTask<String,String,String>
    {


        @Override
        protected String doInBackground(String... params) {

            String status=getLoginStatus(params[0],params[1],params[2]);
            return status;
        }

        @Override
        protected void onPostExecute(String s) {

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

    private String getLoginStatus(String id,String msg,String userID)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

       // Constant.WEBSERVICE_URL+Constant.WEBSERVICE_Login+"email="+email+"&password="+password);
        String text = null;
        try {
            String url="id="+id+"&message="+msg+"&email="+userID;
            URI uri = new URI("https", "www.sellyourtime.in", "/api/chat.php",url, null);
            String ll=uri.toASCIIString();
            HttpGet httpGet = new HttpGet(ll);
            HttpResponse response = httpClient.execute(httpGet, localContext);


            HttpEntity entity = response.getEntity();


            text = getASCIIContentFromEntity(entity);


        } catch (Exception e) {
            return e.getLocalizedMessage();
        }


        return text;

    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


}
