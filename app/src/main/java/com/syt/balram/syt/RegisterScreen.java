package com.syt.balram.syt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.syt.balram.syt.BE.BuyerQuestionBE;
import com.syt.balram.syt.BE.SellerQuestionBE;
import com.syt.balram.syt.BL.SignUpValidity;
import com.syt.balram.syt.LinkedInDialog.OnVerifyListener;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.internal.SessionTracker;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.EnumSet;

import static com.syt.balram.syt.R.id.register_loginbtn;


public class RegisterScreen extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    RequestParams params = new RequestParams();
    GoogleCloudMessaging gcmObj;
    Context applicationContext;
    String regId = "";
    static int PROFILE_PIC_SIZE=100;


    private UiLifecycleHelper uiHelper;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LinkedInOAuthService oAuthService;
    private LinkedInApiClientFactory factory;
    private LinkedInRequestToken liToken;
    private LinkedInApiClient client;

    String lastName,email,imageURL;
    // String birth = user.getBirthday();
    String firstName;
    String encode;
    ImageView img;



    private LoginButton loginBtn;    //facebook button
    ImageButton btnLinkedIN,btnGooglePlus;           // linkedIn button
    TextView userNameFB;
    private SessionTracker sessionTracker;

    EditText userName,password;
    Button btnRegister,btnSell,btnBuy,btnLoginPage;

    SignUpValidity objSignUpValidity;

    String selectionSell="sell";
    String selectionBuy="buy";
    ProgressDialog progressDialog;
    SellerQuestionBE objSellerQuestionBE;
    BuyerQuestionBE objBuyerQuestionBE;

    TransparentProgressDialog pd;

    String pswd;
    String uName;
    Boolean selected;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        CookieSyncManager.createInstance(this);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        setContentView(R.layout.activity_register_screen);

        String loginEmail=Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_UserID);
        if(loginEmail!=null)
        {
            startActivity(new Intent(RegisterScreen.this,HomeScreen.class));
        }

        startActivity(new Intent(getApplicationContext(),SignUpOverLay.class));

        ImageView imageView = (ImageView) findViewById(R.id.register_logo);
        img= (ImageView) findViewById(R.id.img_register);

        selected=false;

        progressDialog=new ProgressDialog(RegisterScreen.this,R.style.MyDialogTheme);
        objSellerQuestionBE=new SellerQuestionBE();
        CookieSyncManager.createInstance(this);

        pd=new TransparentProgressDialog(RegisterScreen.this,R.drawable.logo_single);

        applicationContext = getApplicationContext();

        Constant.RegisterTitle="Nothing";

        showHashKey(getApplicationContext());



        try {
            //final Session openSession = sessionTracker.getOpenSession();
            Session ss=Session.getActiveSession();
            ss.closeAndClearTokenInformation();
           /* if (openSession != null) {
                openSession.closeAndClearTokenInformation();
            }*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            mGoogleApiClient = new GoogleApiClient.Builder(RegisterScreen.this)
                    .addConnectionCallbacks(RegisterScreen.this)
                    .addOnConnectionFailedListener(RegisterScreen.this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .build();


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {


            String savedRegID = Configuration.getSharedPrefrenceValue(applicationContext, Constant.SHARED_PREFERENCE_RegistrationID);

            if (savedRegID == null) {
                if (checkPlayServices()) {
                    registerInBackground();
                }
            }
        }
        catch (Exception e)
        {

        }

        userName= (EditText) findViewById(R.id.register_email);
        password= (EditText) findViewById(R.id.register_password );
        btnRegister= (Button) findViewById(R.id.register_btn);
        userNameFB= (TextView) findViewById(R.id.tv_fb);
        btnSell= (Button) findViewById(R.id.register_sell);
        btnBuy= (Button) findViewById(R.id.register_buy);

        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        btnLinkedIN= (ImageButton) findViewById(R.id.btn_linked_in);
        btnGooglePlus= (ImageButton) findViewById(R.id.btn_google_plus);
        btnLoginPage= (Button) findViewById(register_loginbtn);

        objSignUpValidity=new SignUpValidity();
        objBuyerQuestionBE=new BuyerQuestionBE();

        /* btnRegister.setEnabled(false);
        btnLinkedIN.setEnabled(false);*/
        loginBtn.setEnabled(true);



        // I Want to sell btn Listener


   btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Configuration.setSharedPrefrenceValue(RegisterScreen.this,Constant.PREFS_NAME,Constant.REGISTER_SELECTION,selectionSell);
               // String selection=Configuration.getSharedPrefrenceValue(RegisterScreen.this,Constant.REGISTER_SELECTION);
                selected=true;
                Constant.RegisterTitle=selectionSell;
                loginBtn.setEnabled(true);
               // System.out.println(selection);
                btnSell.setBackgroundResource(R.drawable.register_sell_selected);
                btnBuy.setBackgroundResource(R.drawable.register_buy_default);
            }
        });

       /* final Session openSession = Session.getOpenSession();

        if (openSession != null) {
            openSession.closeAndClearTokenInformation();
        }*/


    // I want to buy btn Listener

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected=true;
                Constant.RegisterTitle = selectionBuy;
                // Configuration.setSharedPrefrenceValue(RegisterScreen.this,Constant.PREFS_NAME,Constant.REGISTER_SELECTION,selectionBuy);
                //String selection=Configuration.getSharedPrefrenceValue(RegisterScreen.this,Constant.REGISTER_SELECTION);
                // System.out.println(selection);
                loginBtn.setEnabled(true);
                btnSell.setBackgroundResource(R.drawable.register_sell);
                btnBuy.setBackgroundResource(R.drawable.register_buy_pressed);

            }
        });

        btnGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Constant.RegisterTitle=="Nothing")
                {
                    Intent intent=new Intent(RegisterScreen.this,SignUpOverLay.class);
                    startActivity(intent);
                }
                else {
                    mGoogleApiClient.connect();
                }
            }
        });




        loginBtn.setReadPermissions(Arrays.asList("email"));
       loginBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!selected) {
                   Intent intent = new Intent(RegisterScreen.this, SignUpOverLay.class);
                   startActivity(intent);
                   try {
                       //final Session openSession = sessionTracker.getOpenSession();
                       Session ss = Session.getActiveSession();
                       ss.closeAndClearTokenInformation();
           /* if (openSession != null) {
                openSession.closeAndClearTokenInformation();
            }*/
                   } catch (Exception e) {
                       e.printStackTrace();
                   }


               } else if (selected) {
                   loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {


                       @Override
                       public void onUserInfoFetched(GraphUser user) {


                           if (user != null) {
                               //  userNameFB.setText("Hello, " + user.getName());
                               lastName = user.getLastName();
                               // String birth = user.getBirthday();
                               firstName = user.getFirstName();

                               String id = user.getId();
                               try {
                                   //String email = (String) response.getGraphObject().getProperty("email");
                                   email = user.asMap().get("email").toString();
                                   String safeEmail = user.asMap().get("email").toString();
                                   URL image_value = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
/*
                                    URL  imageURL = new URL("https://graph.facebook.com/" + id + "/picture");
                                    InputStream inputStream = (InputStream) imageURL.getContent();
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    img.setImageBitmap(bitmap);

                                    System.out.println("IMAGE________BITMAP->>"+String.valueOf(bitmap));
                                    String ss=BitMapToString(bitmap);
                                    System.out.println("IMAGE________->>"+ss);*/


                                    /*InputStream in = (InputStream) new URL("http://graph.facebook.com/" + id + "/picture").getContent();
                                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                                    String ss=BitMapToString(bitmap);

                                    System.out.println("IMAGE________->>"+ss);
*/
                                   //imageURL = image_value.toString();


                                   //System.out.println("Facebook Imahe"+String.valueOf(bb));

                                   try {
                                       byte[] data = String.valueOf(image_value).getBytes("UTF-8");
                                       encode = Base64.encodeToString(data, Base64.DEFAULT);

                                   } catch (Exception e) {

                                   }

                                   //System.out.println("FaceBOOK" + image_value);

                                   if (email != null) {

                                       new RegisterValiditySocialMedia().execute(email);


                                       // userNameFB.setText(user.getProperty("email").toString());
                                   }
                               } catch (Exception e) {
                                   e.printStackTrace();
                                   // userNameFB.setText("Hello, " + user.getName());
                               }

                           } else {

                           }

                       }

                   });
               }
           }
       });




        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                registerValues();
            }
        });

        btnLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginScreen.class);
                startActivity(intent);
            }
        });

    try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.balram.syt",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        btnLinkedIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linkedInLogin();
            }
        });



    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                //buttonsEnabled(true);
                Log.d("FacebookSampleActivity", "Facebook session opened");
            } else if (state.isClosed()) {
                //buttonsEnabled(false);
                Log.d("FacebookSampleActivity", "Facebook session closed");
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        AppEventsLogger.activateApp(this);

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
           // mGoogleApiClient.connect();
        }

           }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        AppEventsLogger.deactivateApp(this);

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            // mGoogleApiClient.connect();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }

    }


    private void linkedInLogin()
    {
        if(Constant.RegisterTitle=="Nothing")
        {
            Intent intent=new Intent(RegisterScreen.this,SignUpOverLay.class);
            startActivity(intent);
        }
        else {
            ProgressDialog progressDialog = new ProgressDialog(RegisterScreen.this);//.show(LinkedInSampleActivity.this, null, "Loadong...");

            LinkedInDialog d = new LinkedInDialog(RegisterScreen.this, progressDialog);
            d.show();

            //set call back listener to get oauth_verifier value
            d.setVerifierListener(new OnVerifyListener() {
                @Override
                public void onVerify(String verifier) {
                    try {
                        Log.i("LinkedinSample", "verifier: " + verifier);

                        LinkedInAccessToken accessToken = LinkedInDialog.oAuthService.getOAuthAccessToken(LinkedInDialog.liToken, verifier);
                        //LinkedInAccessToken accessToken = oAuthService.getOAuthAccessToken(liToken, verifier);
                        client = LinkedInDialog.factory.createLinkedInApiClient(accessToken);

                        Log.i("LinkedinSample", "ln_access_token: " + accessToken.getToken());
                        Log.i("LinkedinSample", "ln_access_token: " + accessToken.getTokenSecret());
                        //Person p = client.getProfileForCurrentUser();
                        com.google.code.linkedinapi.schema.Person profile = client.getProfileForCurrentUser(EnumSet.of(
                                        ProfileField.ID, ProfileField.FIRST_NAME,
                                        ProfileField.LAST_NAME, ProfileField.HEADLINE,
                                        ProfileField.PHONE_NUMBERS, ProfileField.EMAIL_ADDRESS,
                                        ProfileField.DATE_OF_BIRTH, ProfileField.LOCATION_NAME,
                                        ProfileField.MAIN_ADDRESS, ProfileField.LOCATION_COUNTRY,
                                        ProfileField.PICTURE_URL)
                        );
                        //tv1.setText("Welcome " +

                        email = profile.getEmailAddress();

                        try {
                            imageURL = profile.getPictureUrl().toString();
                            System.out.println("Image" + profile.getPictureUrl().toString());
                            byte[] data = imageURL.getBytes("UTF-8");
                             encode = Base64.encodeToString(data, Base64.DEFAULT);
                           // encode = java.net.URLEncoder.encode(imageURL, "UTF-8");

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                        firstName = profile.getFirstName();
                        lastName = profile.getLastName();

                        new RegisterValiditySocialMedia().execute(email);


                        //tv4.setText(profile.getLastName());
                    } catch (Exception e) {
                        Log.i("LinkedinSample", "error to get verifier");
                        e.printStackTrace();
                    }
                }
            });

            //set progress dialog
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
            //d.dismiss();
        }
    }


    void registerValues()
    {
        if(Constant.RegisterTitle=="Nothing")
        {
            Intent intent=new Intent(RegisterScreen.this,SignUpOverLay.class);
            startActivity(intent);
        }
        else {
            uName = userName.getText().toString();
            pswd = password.getText().toString();

            if (uName.trim().length() == 0) {
                userName.setError("required");
            } else if (pswd.trim().length() == 0) {
                password.setError("required");
            } else {
                if (Configuration.isEmailValid(uName)) {
                    //String result = objSignUpValidity.isEmailAvailable(userName.getText().toString());

                    new RegisterValidity().execute(uName);
                } else {
                    userName.setError("Invalid Email-ID");
                }
            }
        }


    }

    @Override
    public void onConnected(Bundle bundle) {
        getProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(result.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private class RegisterValidity extends AsyncTask<String, String, String> {

        // ProgressDialog progress;

        @Override
        protected void onPreExecute ( )
        {
            //starting the progress dialogue


            pd.show();


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String result = objSignUpValidity.isEmailAvailable(params[0]);

            return result;

        }

        @Override
        protected void onPostExecute (String result)
        {
            System.out.println();
            pd.dismiss();

            if (result.equalsIgnoreCase("y")) {
               // Configuration.setSharedPrefrenceValue(RegisterScreen.this, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_UserID, uName);
                Constant.LoginMedium="syt";
               // String userTitle=Configuration.getSharedPrefrenceValue(RegisterScreen.this,Constant.REGISTER_SELECTION);

                        if(Constant.RegisterTitle.equalsIgnoreCase(selectionBuy)) {

                            objBuyerQuestionBE.setEmail(uName);
                            objBuyerQuestionBE.setPassword(pswd);
                            Intent intent=new Intent(RegisterScreen.this,BuyerQuestionFirst.class);

                            intent.putExtra("First Name","");
                            intent.putExtra("Last Name","");
                            intent.putExtra("DOB","");
                            intent.putExtra("BuyerQuestionBE",objBuyerQuestionBE);
                            startActivity(intent);
                        }
                        else
                        {
                            objSellerQuestionBE.setEmail(uName);
                            objSellerQuestionBE.setPassword(pswd);
                            Intent intent=new Intent(RegisterScreen.this,SellerQuestionOne.class);
                            intent.putExtra("First Name","");
                            intent.putExtra("Last Name","");
                            intent.putExtra("DOB","");
                            intent.putExtra("SellerQuestionBE",objSellerQuestionBE);
                            startActivity(intent);
                        }

            } else {
                userName.setError("Email-ID already exist");
            }
        }


    }

    private class RegisterValiditySocialMedia extends AsyncTask<String, String, String> {

        // ProgressDialog progress;

        @Override
        protected void onPreExecute ( )
        {
            //starting the progress dialogue
            pd.show();


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String result = objSignUpValidity.isEmailAvailable(params[0]);

            return result;

        }

        @Override
        protected void onPostExecute (String result)
        {
            pd.dismiss();

            try {
                //final Session openSession = sessionTracker.getOpenSession();
                Session ss=Session.getActiveSession();
                ss.closeAndClearTokenInformation();
           /* if (openSession != null) {
                openSession.closeAndClearTokenInformation();
            }*/
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (result.equalsIgnoreCase("y")) {
                //Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_UserID, email);
                Constant.LoginMedium = "facebook";
                if(Constant.RegisterTitle.equalsIgnoreCase(selectionBuy)) {

                    objBuyerQuestionBE.setEmail(email);
                    objBuyerQuestionBE.setImageURL(encode);

                    Intent intent=new Intent(RegisterScreen.this,BuyerQuestionFirst.class);
                    intent.putExtra("First Name",firstName);
                    intent.putExtra("Last Name",lastName);
                    intent.putExtra("BuyerQuestionBE",objBuyerQuestionBE);
                    startActivity(intent);
                }
                else
                {
                    objSellerQuestionBE.setEmail(email);
                    objSellerQuestionBE.setImageURL(encode);
                    Intent intent=new Intent(RegisterScreen.this,SellerQuestionOne.class);
                    intent.putExtra("First Name",firstName);
                    intent.putExtra("Last Name",lastName);
                    intent.putExtra("SellerQuestionBE",objSellerQuestionBE);
                    startActivity(intent);
                }
            } else {

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        RegisterScreen.this);

// Setting Dialog Title
                alertDialog2.setTitle("Email-ID already exists");

// Setting Dialog Message
                alertDialog2.setMessage("You have already signed up with this email-id.");

// Setting Icon to Dialog

                alertDialog2.setIcon(R.drawable.no_internet);
// Setting Positive "Yes" Btn
                alertDialog2.setPositiveButton("Login",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                startActivity(new Intent(RegisterScreen.this, LoginScreen.class));
                            }
                        });
// Setting Negative "NO" Btn
                alertDialog2.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
/*                                        Toast.makeText(getApplicationContext(),
                                                "You clicked on NO", Toast.LENGTH_SHORT)
                                                .show();*/
                                dialog.cancel();

                            }
                        });

// Showing Alert Dialog
                alertDialog2.show();

            }
        }


    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    regId = gcmObj
                            .register(Constant.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    //storeRegIdinSharedPref(applicationContext, regId, emailID);
                    Configuration.setSharedPrefrenceValue(applicationContext, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_RegistrationID, regId);
                   /* Toast.makeText(
                            applicationContext,
                            "Registered with GCM Server successfully.\n\n"
                                    + msg, Toast.LENGTH_SHORT).show();*/
                } else {
                  /*  Toast.makeText(
                            applicationContext,
                            "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();*/
                }
            }
        }.execute(null, null, null);
    }



    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
               /* Toast.makeText(
                        getApplicationContext(),
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();*/
                finish();
            }
            return false;
        } else {
            /*Toast.makeText(
                    getApplicationContext(),
                    "This device supports Play services, App will work normally",
                    Toast.LENGTH_LONG).show();*/
        }
        return true;
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                System.out.println("Person method is called");
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName[] = currentPerson.getDisplayName().split(" ");
                firstName=personName[0];
                lastName=personName[1];
                imageURL = currentPerson.getImage().getUrl();




                email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                imageURL = imageURL.substring(0,
                        imageURL.length() - 2)
                        + PROFILE_PIC_SIZE;

                System.out.println("GOOGLE IMAGE URL"+imageURL);

                byte[] data = imageURL.getBytes("UTF-8");
                encode = Base64.encodeToString(data, Base64.DEFAULT);

                if (email != null) {

                    new RegisterValiditySocialMedia().execute(email);


                    // userNameFB.setText(user.getProperty("email").toString());
                }

/*
                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);
*/

              /*  name.setText(personName);
                email.setText(email1);*/
               /* signOutFromGplus();*/

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X


             /*   Toast.makeText(getApplicationContext(),
                        firstName+"/"+lastName+"/"+email+"/"+encode, Toast.LENGTH_LONG).show();
*/
                //new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {

            resolveSignInError();
        }
    }*/
   /* private void resolveSignInError() {
        try {
            if (mConnectionResult.hasResolution()) {

                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            }
        }catch (Exception e) {
            mIntentInProgress = false;
            mGoogleApiClient.connect();
        }
    }
*/

    public  void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.syt.balram.syt",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String sign=Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", sign);
                //  Toast.makeText(getApplicationContext(),sign,     Toast.LENGTH_LONG).show();
            }
            Log.d("KeyHash:", "****------------***");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
