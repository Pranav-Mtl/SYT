package com.syt.balram.syt;

/**
 * Created by Balram on 5/14/2015.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.syt.constant.Constant;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

public class GCMNotificationIntentService extends IntentService {
    // Sets an ID for the notification, so it can be updated

    //private NotificationManager mNotificationManager;

    public static final int notifyID = 9001;
    public static final int NOTIFICATION_ID = 1;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {


            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                    sendNotification("Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                try {

                    System.out.println("GCM MESSAGE" + "" + extras.get(Constant.MSG_KEY));

                    String msgArray = extras.get(Constant.MSG_KEY) + "";
                    try {
                        JSONObject jsonObj = new JSONObject(msgArray);
                        String notification_type = jsonObj.getString("notification_type");

                        if (notification_type.equals("Seller")) {
                            sendNotificationSeller(msgArray);
                        } else if (notification_type.equals("Buyer")) {
                            sendNotification("" + extras.get(Constant.MSG_KEY)); //When Message is received normally from GCM Cloud Server
                        } else if (notification_type.equals("Follower")) {
                            sendNotificationFollower("" + extras.get(Constant.MSG_KEY));
                        } else if (notification_type.equals("Chat")) {
                            sendNotificationChat("" + extras.get(Constant.MSG_KEY));
                        }
                        else if (notification_type.equals("followed")) {
                            sendNotificationFollowed("" + extras.get(Constant.MSG_KEY));
                        }
                        else if (notification_type.equals("admin_message")) {
                            sendNotificationAdmin("" + extras.get(Constant.MSG_KEY));
                        }
                        else if (notification_type.equals("service_provider")) {
                            sendNotificationServiceProvider("" + extras.get(Constant.MSG_KEY));
                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }


            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String greetMsg) {

        String id="";
        String messagees="";

        try {
            JSONObject jsonObj = new JSONObject(greetMsg);

            id=jsonObj.getString("last_id");
            messagees=jsonObj.getString("greetMsg");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, BuyerProfile.class);
        resultIntent.putExtra("ID", id);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("SellYourTime")
                .setContentText(messagees)
                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(messagees);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }

    private void sendNotificationFollower(String greetMsg) {

        String id="";
        String messagees="";

        try {
            JSONObject jsonObj = new JSONObject(greetMsg);


            messagees=jsonObj.getString("fullname");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, FollowerMessage.class);
        resultIntent.putExtra("JSON", greetMsg);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("SellYourTime")
                .setContentText("You have new message from " + messagees)
                .setSmallIcon(R.mipmap.ic_launcher);                ;
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText("You have new message from " + messagees);

        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }

    private void sendNotificationSeller(String greetMsg) {

        String id="";
        String messagees="";

        try {
            JSONObject jsonObj = new JSONObject(greetMsg);

            id=jsonObj.getString("userid");
            messagees=jsonObj.getString("greetMsg");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, SellerProfile.class);
        resultIntent.putExtra("ID", id);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Sell Your Time")
                .setContentText(messagees)
                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(messagees);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        mNotifyBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(messagees));
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }

    private void sendNotificationChat(String msg) {
        String id="";
        String messagees="";
        String name="";
        String image="";
        try {
            JSONObject jsonObj = new JSONObject(msg);

            messagees=jsonObj.getString("greetMsg");
            id=jsonObj.getString("userid");
            name=jsonObj.getString("fullname");
            image=jsonObj.getString("image");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
         Intent resultIntent = new Intent(this, ChatActivity.class);
         resultIntent.putExtra("ID",id);
         resultIntent.putExtra("Name",name);
         resultIntent.putExtra("Pic", image);
         resultIntent.putExtra("Message", messagees);
       // resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //............................................
        System.out.println("Notification Intent Message" + messagees);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("SellYourTime")
                .setContentText(messagees)
                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(contentIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(messagees);

        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification


        mNotificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());
    }

    private void sendNotificationFollowed(String greetMsg) {

        String id="";
        String messagees="";

        try {
            JSONObject jsonObj = new JSONObject(greetMsg);

            messagees=jsonObj.getString("greetMsg");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, FollowerGcmMessage.class);
        resultIntent.putExtra("MESSAGE", messagees);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("SellYourTime")
                .setContentText(messagees)
                .setSmallIcon(R.mipmap.ic_launcher).setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messagees))

        ;

        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(messagees);
        mNotifyBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(messagees));
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }
    private void sendNotificationAdmin(String greetMsg) {

        String URL="";
        String messagees="";

        try {
            JSONObject jsonObj = new JSONObject(greetMsg);

            messagees=jsonObj.getString("greetMsg");
            URL=jsonObj.getString("http_link");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, NotificationGeneralMessage.class);
        resultIntent.putExtra("Message", messagees);
        resultIntent.putExtra("URL", URL);

        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("SellYourTime")
                .setContentText(messagees)
                .setSmallIcon(R.mipmap.ic_launcher).setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messagees))

        ;

        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(messagees);
        mNotifyBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(messagees));
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }

    private void sendNotificationServiceProvider(String greetMsg) {

        String id="";
        String messagees="";

        try {
            JSONObject jsonObj = new JSONObject(greetMsg);

            messagees=jsonObj.getString("greetMsg");
            id=jsonObj.getString("seller_id");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, ServiceProvider.class);
        resultIntent.putExtra("Message", messagees);
        resultIntent.putExtra("Id", id);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("SellYourTime")
                .setContentText(messagees)
                .setSmallIcon(R.mipmap.ic_launcher).setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messagees))

        ;

        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(messagees);
        mNotifyBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(messagees));
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }

}

