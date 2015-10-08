package com.syt.balram.syt;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.syt.balram.syt.BE.Age;
import com.syt.balram.syt.BE.BuyerQuestionBE;
import com.syt.balram.syt.BL.SendOtpBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Mail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class BuyerQuestionFirst extends ActionBarActivity implements AdapterView.OnItemClickListener {

    Calendar dateandtime;
    EditText txtFirstName,txtLastName,txtPhone;
    AutoCompleteTextView txtAddress;
    Button btnNext;
    Button txtDate;
    private TextView datePickerShowDialogButton;
    Spinner spinner;

    BuyerQuestionBE objBuyerQuestionBE;
    private static final String LOG_TAG = "SellYourTime";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBugME8AtB66ogVSb0kZShmnlkSLwGusC4";
    RadioGroup groupGender;
    RadioButton Gender;
    ImageButton back;
    static final int DATE_PICKER_ID = 1111;
    int currentYY,currentMM,currentDD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_question_first);
       /* ActionBar actionBar=getSupportActionBar();
        actionBar.hide();*/
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        txtDate= (Button) findViewById(R.id.buyer_date);
        btnNext= (Button) findViewById(R.id.buyer_next);
       // spinner= (Spinner) findViewById(R.id.spinner);
        txtFirstName= (EditText) findViewById(R.id.buyer_firstname);
        txtLastName= (EditText) findViewById(R.id.buyer_lastname);
        txtPhone= (EditText) findViewById(R.id.buyer_phone);
        txtAddress= (AutoCompleteTextView) findViewById(R.id.buyer_address);
        groupGender= (RadioGroup) findViewById(R.id.buyer_radio_gender);
        back= (ImageButton) findViewById(R.id.buyer_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent1=getIntent();
        objBuyerQuestionBE= (BuyerQuestionBE) intent1.getSerializableExtra("BuyerQuestionBE");
        LinearLayout obj_ll_et2=(LinearLayout)findViewById(R.id.Buyer1HeaderMain);  //keyboard hiding
        obj_ll_et2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));

        currentYY = cal.get(Calendar.YEAR);
        currentMM = cal.get(Calendar.MONTH);
        currentDD = cal.get(Calendar.DAY_OF_MONTH);
       // currentDate=currentDD+"/"+currentMM+"/"+currentYY;

        if(Constant.LoginMedium=="linkedin" || Constant.LoginMedium=="facebook") {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            String fname = bundle.get("First Name").toString();
            String lname = bundle.get("Last Name").toString();
            txtFirstName.setText(fname);
            txtLastName.setText(lname);
        }
        else
        {

        }
       // String userID= Configuration.getSharedPrefrenceValue(BuyerQuestionFirst.this, Constant.SHARED_PREFERENCE_UserID);
       // String selection=Configuration.getSharedPrefrenceValue(BuyerQuestionFirst.this,Constant.REGISTER_SELECTION);
       // System.out.println(selection);

        //Toast.makeText(getApplicationContext(),select,Toast.LENGTH_LONG).show();



       /* txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*Intent intent=new Intent(getApplicationContext(),BuyerQuestionSecond.class);
                startActivity(intent);*//*
                showDatePicker();
            }
        });*/

        txtAddress.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.gender_spinner_item));

        //tvLocation.showDropDown();
        txtAddress.setOnItemClickListener(this);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

       /* txtDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(DATE_PICKER_ID);
            }
        });
*/
     btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {


                String firstName=txtFirstName.getText().toString();
                String lastName=txtLastName.getText().toString();
                String date=txtDate.getText().toString();
                String phone=txtPhone.getText().toString();
                String address=txtAddress.getText().toString();
                int selectedGender=groupGender.getCheckedRadioButtonId();
                int years=0;



                System.out.print("FIRST NAME"+firstName);

                try {
                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = format2.parse(date);
                    System.out.println("new date"+format1.format(date1));
                    String newDate=format1.format(date1);

                    Date birthDate = format1.parse(newDate); //Yeh !! It's my date of birth :-)


                    Age age = AgeCalculater.calculateAge(birthDate);
                    //My age is

                   System.out.println("AGGGEGEGGEGGEGE"+age);
                    years=age.getYears();
                   System.out.println("YEArs" + String.valueOf(age.getYears()));

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

               // String formarDate=new SimpleDateFormat("yyyy-MM-dd").format(date);
               Gender= (RadioButton) findViewById(selectedGender);

                if(firstName.trim().length()==0)
                {
                    txtFirstName.setError("required");
                    
                }
                else if(lastName.trim().length()==0)
                {
                    txtLastName.setError("required");
                }

                else if(date.trim().length()==0){

                    txtDate.setError("required");
                }
                else if(phone.trim().length()==0)
                {
                    txtPhone.setError("required");
                }
                else if(phone.length()!=10)
                {
                    txtPhone.setError("Invalid phone number");
                }
                else if(address.length()==0)
                {
                    txtAddress.setError("required");
                }
                else if(years<18)
                {
                    final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            BuyerQuestionFirst.this);
// Setting Dialog Title


// Setting Dialog Message
                    alertDialog2.setMessage("You must be atleast 18 years old to register");

// Setting Icon to Dialog


// Setting Positive "Yes" Btn
                    alertDialog2.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    dialog.cancel();
                                }
                            });
// Setting Negative "NO" Btn


// Showing Alert Dialog
                    alertDialog2.show();


                }
                else
                {
                    String firstNameCaps = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
                    String lastNameCaps = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);

                    objBuyerQuestionBE.setFirstname(firstNameCaps);
                    objBuyerQuestionBE.setLastname(lastNameCaps);
                    objBuyerQuestionBE.setGender(Gender.getText().toString());
                    objBuyerQuestionBE.setBdate(date);
                    objBuyerQuestionBE.setPhoneno(phone);
                    objBuyerQuestionBE.setZip(address);


                    int randomPIN = (int)(Math.random()*9000)+1000;
                    String PINString= String.valueOf(randomPIN);
                    Constant.userOTP=PINString;

                    System.out.println("Con" + Constant.userOTP);
                    sendOTP(objBuyerQuestionBE.getEmail(), objBuyerQuestionBE.getPhoneno(), Constant.userOTP);


                }

            }
        });
    }

    public void showDatePicker() {
        // Initializiation
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        final AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(this);
        View customView = inflater.inflate(R.layout.custom_datepicker, null);
        dialogBuilder.setView(customView);
        final Calendar now = Calendar.getInstance();
        final DatePicker datePicker =
                (DatePicker) customView.findViewById(R.id.dialog_datepicker);
        final TextView dateTextView =
                (TextView) customView.findViewById(R.id.dialog_dateview);
        final SimpleDateFormat dateViewFormatter =
                new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        final SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        // Minimum date
        Calendar minDate = Calendar.getInstance();
        try {
            minDate.setTime(formatter.parse("1930-12-12"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePicker.setMinDate(minDate.getTimeInMillis());
        // View settings
        dialogBuilder.setTitle("Choose a date");
        dialogBuilder.setIcon(R.drawable.logo_watch);
        Calendar choosenDate = Calendar.getInstance();
        int year = choosenDate.get(Calendar.YEAR);
        int month = choosenDate.get(Calendar.MONTH);
        int day = choosenDate.get(Calendar.DAY_OF_MONTH);
        try {
            Date choosenDateFromUI = formatter.parse(
                    datePickerShowDialogButton.getText().toString()
            );
            choosenDate.setTime(choosenDateFromUI);
            year = choosenDate.get(Calendar.YEAR);
            month = choosenDate.get(Calendar.MONTH);
            day = choosenDate.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar dateToDisplay = Calendar.getInstance();
        dateToDisplay.set(year, month, day);
        dateTextView.setText(
                dateViewFormatter.format(dateToDisplay.getTime())
        );
        // Buttons
        dialogBuilder.setNegativeButton(
                "Go to today",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtDate.setText(
                                formatter.format(now.getTime())
                        );
                        dialog.dismiss();
                    }
                }
        );

        dialogBuilder.setPositiveButton(
                "Choose",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar choosen = Calendar.getInstance();
                        choosen.set(
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth()
                        );
                        txtDate.setText(
                                dateViewFormatter.format(choosen.getTime())
                        );
                        dialog.dismiss();
                    }
                }
        );
        final AlertDialog dialog = dialogBuilder.create();
        // Initialize datepicker in dialog atepicker
        datePicker.init(
                year,
                month,
                day,
                new DatePicker.OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        Calendar choosenDate = Calendar.getInstance();
                        choosenDate.set(year, monthOfYear, dayOfMonth);
                        dateTextView.setText(
                                dateViewFormatter.format(choosenDate.getTime())
                        );
                        if (choosenDate.get(Calendar.DAY_OF_WEEK) ==
                                Calendar.SUNDAY ||
                                now.compareTo(choosenDate) < 0) {
                            dateTextView.setTextColor(
                                    Color.parseColor("#000000")
                            );
                            ((Button) dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE))
                                    .setEnabled(true);
                        } else {
                            dateTextView.setTextColor(
                                    Color.parseColor("#000000")
                            );
                            ((Button) dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE))
                                    .setEnabled(true);
                        }
                    }
                }
        );
        // Finish
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buyer_question_first, menu);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public android.widget.Filter getFilter() {
            android.widget.Filter filter = new android.widget.Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    android.widget.Filter.FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        System.out.println(resultList);

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, android.widget.Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=geocode");
            sb.append("&sensor=false");
            sb.append("&language=en");
            sb.append("components=country:IN");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);

            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "mmmmmmmm", e);
            return resultList;
        }

        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    private void sendMail() {
        // TODO Auto-generated method stub

        Mail m = new Mail("appslurewebsolution@gmail.com", "appslure1990");
        String toArr =objBuyerQuestionBE.getEmail();
        m.setTo(toArr);
        m.setFrom("appslurewebsolution@gmail.com");
        m.setSubject("Wellcome to SellYourTime ");
        m.setBody("Hello "+objBuyerQuestionBE.getFirstname()+""+objBuyerQuestionBE.getLastname()+"\n"+"Your OTP is : "+Constant.userOTP);

        try {


            if(m.send()) {
                Toast.makeText(BuyerQuestionFirst.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(BuyerQuestionFirst.this, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            Log.e("MailApp", "Could not send email", e);
        }

    }

    private void sendOTP(String email,String phone,String otp)
    {
        new OTPRun().execute(email,phone,otp);

    }

    class OTPRun extends AsyncTask<String,String,String>
    {
        SendOtpBL objSendOtpBL=new SendOtpBL();
        TransparentProgressDialog progressDialog=new TransparentProgressDialog(BuyerQuestionFirst.this,R.drawable.logo_single);
        @Override
        protected void onPreExecute() {
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
           String result= objSendOtpBL.otpSend(params[0],params[1],params[2]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s.equals("1"))
            {
                Intent intent=new Intent(getApplicationContext(),OTPBuyerActivity.class);
                // intent.putExtra("BuyerQuestionBE",objBuyerQuestionBE);
                intent.putExtra("BuyerQuestionBE",objBuyerQuestionBE);
                startActivity(intent);
            }

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, currentYY, currentMM,currentDD);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

          /*  year  = selectedYear;
            enterYY=selectedYear;
            enterMM=selectedMonth;
            enterDD= selectedDay;;
            month = selectedMonth;
            String months[]={"Jan","Feb","Mar","April","May","June","July","Aug","Sept","oct","Nov","Dec"};
            String mm=months[month];
            date   = selectedDay;
*/
            // Show selected date
            txtDate.setText(new StringBuilder().append(selectedYear)
                    .append("-").append(selectedMonth).append("-").append(selectedDay));
           // enterDate=enterDD+"/"+enterMM+"/"+enterYY;

        }
    };
}
