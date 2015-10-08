package com.syt.balram.syt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.syt.balram.syt.BL.LoginBL;
import com.syt.balram.syt.BL.SignUpValidity;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;


public class LoginScreen extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 0;
    private UiLifecycleHelper uiHelper;

    GoogleCloudMessaging gcmObj;
    Context applicationContext;
    String regId = "";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private LinkedInOAuthService oAuthService;
    private LinkedInApiClientFactory factory;
    private LinkedInRequestToken liToken;
    private LinkedInApiClient client;

    String email;
    private SessionTracker sessionTracker;

    SignUpValidity objSignUpValidity;

    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;



    private LoginButton loginBtn;       //facebook button
    ImageButton btnLinkedIN,btnGooglePlus;           // linkedIn button

    Button btnLogin,btnRegister,btnForgot;
    EditText userName,password;
    LoginBL objLoginBL;
    ProgressDialog progressDialog;
    String userId,pswd;
    AlertDialog alertDialog;
    String localTime;
    String savedRegID;
    TransparentProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        String loginEmail=Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.SHARED_PREFERENCE_UserID);
        if(loginEmail!=null)
        {
            startActivity(new Intent(LoginScreen.this,HomeScreen.class));
        }

        applicationContext = getApplicationContext();

        String url="https://mywebsite/docs/english/site/mybook.do&request_type";



        CookieSyncManager.createInstance(this);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();


//        System.out.println(localTime);

        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        btnLinkedIN= (ImageButton) findViewById(R.id.btn_linked);
        btnGooglePlus= (ImageButton) findViewById(R.id.btn_plus);

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
            mGoogleApiClient = new GoogleApiClient.Builder(LoginScreen.this)
                    .addConnectionCallbacks(LoginScreen.this)
                    .addOnConnectionFailedListener(LoginScreen.this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .build();

            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
         pd=new TransparentProgressDialog(LoginScreen.this,R.drawable.logo_single);

        try {

            savedRegID = Configuration.getSharedPrefrenceValue(applicationContext, Constant.SHARED_PREFERENCE_RegistrationID);

            System.out.println("ffffff" + savedRegID);

            if (savedRegID==null)  {

                if (checkPlayServices()) {
                    registerInBackground();

                    System.out.println("REGIGGG"+regId);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        progressDialog=new ProgressDialog(LoginScreen.this);
        alertDialog = new AlertDialog.Builder(LoginScreen.this).create();
        objSignUpValidity=new SignUpValidity();

        objLoginBL=new LoginBL();
        userName= (EditText) findViewById(R.id.login_email);
        password= (EditText) findViewById(R.id.login_password);


        btnLogin= (Button) findViewById(R.id.login_btn);
        btnRegister= (Button) findViewById(R.id.login_registernow);
        btnForgot= (Button) findViewById(R.id.login_forgotPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 userId=userName.getText().toString();
                pswd=password.getText().toString();
                if(userId.trim().length()==0)
                {
                    userName.setError("required");
                }
                else if(pswd.trim().length()==0)
                {
                    password.setError("required");
                }
                else {
                        if (Configuration.isEmailValid(userId)) {
                            if(Configuration.isInternetConnection(LoginScreen.this)) {
                                new RunForLogin().execute(userName.getText().toString(), password.getText().toString(),savedRegID);
                            }
                            else{

                                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                        LoginScreen.this);

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
                        else {
                            userName.setError("Invalid Email-id");
                        }
                }


            }
        });

        btnGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleApiClient.connect();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),RegisterScreen.class);
                startActivity(intent);
            }
        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(intent);
            }
        });

        loginBtn.setReadPermissions(Arrays.asList("email", "user_birthday"));
        loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                pd.show();

                if (user != null) {


                    String id = user.getId();
                    try {
                        //String email = (String) response.getGraphObject().getProperty("email");
                         email = user.asMap().get("email").toString();
                        //String safeEmail = user.asMap().get("email").toString();

                        if (email != null) {

                             new RunForSocialLogin().execute(email,savedRegID);




                            // userNameFB.setText(user.getProperty("email").toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // userNameFB.setText("Hello, " + user.getName());
                    }

                } else {

                }
                pd.dismiss();
            }
        });


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
        checkPlayServices();
        //buttonsEnabled(Session.getActiveSession().isOpened());
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        AppEventsLogger.deactivateApp(this);
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
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
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
        ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);//.show(LinkedInSampleActivity.this, null, "Loadong...");

        LinkedInDialog d = new LinkedInDialog(LoginScreen.this, progressDialog);
        d.show();

        //set call back listener to get oauth_verifier value
        d.setVerifierListener(new LinkedInDialog.OnVerifyListener()
        {
            @Override
            public void onVerify(String verifier)
            {
                try
                {
                    Log.i("LinkedinSample", "verifier: " + verifier);

                    LinkedInAccessToken accessToken = LinkedInDialog.oAuthService.getOAuthAccessToken(LinkedInDialog.liToken, verifier);
                    client=LinkedInDialog.factory.createLinkedInApiClient(accessToken);

                    Log.i("LinkedinSample", "ln_access_token: " + accessToken.getToken());
                    Log.i("LinkedinSample", "ln_access_token: " + accessToken.getTokenSecret());
                    //Person p = client.getProfileForCurrentUser();
                    com.google.code.linkedinapi.schema.Person profile = client.getProfileForCurrentUser(EnumSet.of(
                            ProfileField.ID, ProfileField.FIRST_NAME,
                            ProfileField.LAST_NAME, ProfileField.HEADLINE,
                            ProfileField.PHONE_NUMBERS, ProfileField.EMAIL_ADDRESS,
                            ProfileField.DATE_OF_BIRTH, ProfileField.LOCATION_NAME,
                            ProfileField.MAIN_ADDRESS, ProfileField.LOCATION_COUNTRY));
                    //tv1.setText("Welcome " +

                   email=profile.getEmailAddress();

                   new RunForSocialLogin().execute(email,savedRegID);



                    //tv4.setText(profile.getLastName());
                }
                catch (Exception e)
                {
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


    private class RunForLogin extends AsyncTask<String, String, String> {
        TransparentProgressDialog pd=new TransparentProgressDialog(LoginScreen.this,R.drawable.logo_single);

        @Override
        protected void onPreExecute ( )
        {
            //starting the progress dialogue
            pd.show();
          /*  progressDialog.show();
            progressDialog.setMessage("Loading..");
            progressDialog.setCancelable(false);*/

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String status=objLoginBL.isUserValid(params[0], params[1],params[2],getApplicationContext());
            return status;

        }

        @Override
        protected void onPostExecute (String result)
        {    //set adapter here


            try {
                if (result.equalsIgnoreCase("y")) {
                    System.out.println("status" + result);
                    Configuration.setSharedPrefrenceValue(LoginScreen.this, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_UserID, userId);
                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    pd.dismiss();
                    userName.setError("Wrong email-id or password");
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

    private class RunForSocialLogin extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute ( )
        {
            //starting the progress dialogue
            progressDialog.show();
            progressDialog.setMessage("Authenticating your account");
            progressDialog.setCancelable(false);


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String status=objSignUpValidity.isSocialEmailAvailable(params[0],params[1]);

            return status;

        }

        @Override
        protected void onPostExecute (String result) {    //set adapter here
            try {
                if (!result.equalsIgnoreCase("y")) {
                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_LoginTitle, Constant.SocialMediaLoginTitle);
                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_UserID, email);
                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                   progressDialog.dismiss();
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            LoginScreen.this);

// Setting Dialog Title
                    alertDialog2.setTitle("Email-ID not exists");

// Setting Dialog Message
                    alertDialog2.setMessage("You have not signed up with this email-id.");

// Setting Icon to Dialog


// Setting Positive "Yes" Btn
                    alertDialog2.setPositiveButton("Signup",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    startActivity(new Intent(LoginScreen.this, RegisterScreen.class));
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
                                    finish();
                                }
                            });

// Showing Alert Dialog
                    alertDialog2.show();

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                if(progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
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

                System.out.println(msg);
                if (!TextUtils.isEmpty(regId)) {
                    //storeRegIdinSharedPref(applicationContext, regId, emailID);
                    savedRegID=regId;
                    Configuration.setSharedPrefrenceValue(applicationContext, Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_RegistrationID, regId);
                   /* Toast.makeText(
                            applicationContext,
                            "Registered with GCM Server successfully.\n\n"
                                    + msg, Toast.LENGTH_SHORT).show();*/
                } else {
                    /*Toast.makeText(
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
                /*Toast.makeText(
                        getApplicationContext(),
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();*/
                finish();
            }
            return false;
        } else {
           /* Toast.makeText(
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


                email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                if(email!=null) {
                    pd.show();
                   new RunForSocialLogin().execute(email,savedRegID);

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



                //new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
